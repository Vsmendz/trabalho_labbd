package com.meufrota.transport_control.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.meufrota.transport_control.dto.AbastecimentoDTO;
import com.meufrota.transport_control.entity.Abastecimento;
import com.meufrota.transport_control.service.AbastecimentoService;

@RestController
@RequestMapping("/api/abastecimentos")
@CrossOrigin(origins = "http://localhost:5173", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS})

public class AbastecimentoController {

    private final AbastecimentoService service;

    public AbastecimentoController(AbastecimentoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AbastecimentoDTO>> listarTodos() {
        List<Abastecimento> list = service.listarTodos();
        List<AbastecimentoDTO> dtos = list.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AbastecimentoDTO> buscarPorId(@PathVariable Integer id) {
        Abastecimento entity = service.buscarPorId(id);
        return ResponseEntity.ok(toDTO(entity));
    }

    @PostMapping
    public ResponseEntity<AbastecimentoDTO> criar(@RequestBody AbastecimentoDTO dto) {
        Abastecimento entity = toEntity(dto);
        Abastecimento salvo = service.salvar(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(salvo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // Conversões
    private AbastecimentoDTO toDTO(Abastecimento entity) {
        AbastecimentoDTO dto = new AbastecimentoDTO();
        dto.setId(entity.getId());
        dto.setPlacaVeiculo(entity.getPlacaVeiculo());
        dto.setData(entity.getData());
        dto.setTipo(entity.getTipo());
        // Converte BigDecimal para Double no DTO
        dto.setLitro(entity.getLitro() != null ? entity.getLitro().doubleValue() : null);
        dto.setPreco(entity.getPreco() != null ? entity.getPreco().doubleValue() : null);
        dto.setObs(entity.getObs());
        return dto;
    }

    private Abastecimento toEntity(AbastecimentoDTO dto) {
        Abastecimento entity = new Abastecimento();
        entity.setId(dto.getId());
        entity.setPlacaVeiculo(dto.getPlacaVeiculo());
        entity.setData(dto.getData());
        entity.setTipo(dto.getTipo());
        // Converte Double para BigDecimal
        entity.setLitro(dto.getLitro() != null ? BigDecimal.valueOf(dto.getLitro()) : null);
        entity.setPreco(dto.getPreco() != null ? BigDecimal.valueOf(dto.getPreco()) : null);
        entity.setObs(dto.getObs());
        return entity;
    }
}