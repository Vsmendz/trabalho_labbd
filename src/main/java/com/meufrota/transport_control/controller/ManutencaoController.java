package com.meufrota.transport_control.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.meufrota.transport_control.dto.ManutencaoDTO;
import com.meufrota.transport_control.entity.Manutencao;
import com.meufrota.transport_control.service.ManutencaoService;

@RestController
@RequestMapping("/api/manutencoes")
@CrossOrigin(origins = "http://localhost:5173", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS})

public class ManutencaoController {

    private final ManutencaoService manutencaoService;

    public ManutencaoController(ManutencaoService manutencaoService) {
        this.manutencaoService = manutencaoService;
    }

    @GetMapping
    public ResponseEntity<List<ManutencaoDTO>> listarTodas() {
        List<Manutencao> manutencoes = manutencaoService.listarTodas();
        List<ManutencaoDTO> dtos = manutencoes.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManutencaoDTO> buscarPorId(@PathVariable Integer id) {
        Manutencao manutencao = manutencaoService.buscarPorId(id);
        return ResponseEntity.ok(toDTO(manutencao));
    }

    @PostMapping
    public ResponseEntity<ManutencaoDTO> criar(@RequestBody ManutencaoDTO dto) {
        Manutencao manutencao = toEntity(dto);
        Manutencao salva = manutencaoService.salvar(manutencao);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(salva));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ManutencaoDTO> atualizar(@PathVariable Integer id, @RequestBody ManutencaoDTO dto) {
        Manutencao manutencao = toEntity(dto);
        manutencao.setId(id);
        Manutencao atualizada = manutencaoService.atualizar(manutencao);
        return ResponseEntity.ok(toDTO(atualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        manutencaoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    private ManutencaoDTO toDTO(Manutencao m) {
        ManutencaoDTO dto = new ManutencaoDTO();
        dto.setId(m.getId());
        dto.setDescricao(m.getDescricao());
        dto.setKmReferencia(m.getKmReferencia());
        return dto;
    }

    private Manutencao toEntity(ManutencaoDTO dto) {
        Manutencao m = new Manutencao();
        m.setId(dto.getId());
        m.setDescricao(dto.getDescricao());
        m.setKmReferencia(dto.getKmReferencia());
        return m;
    }
}