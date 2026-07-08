package com.meufrota.transport_control.controller;

import com.meufrota.transport_control.dto.ManutencaoDTO;
import com.meufrota.transport_control.entity.Manutencao;
import com.meufrota.transport_control.service.ManutencaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/manutencoes")
@CrossOrigin(origins = "http://localhost:5173")
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