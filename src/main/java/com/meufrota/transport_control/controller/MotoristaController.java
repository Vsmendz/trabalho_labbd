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

import com.meufrota.transport_control.dto.MotoristaDTO;
import com.meufrota.transport_control.entity.Motorista;
import com.meufrota.transport_control.service.MotoristaService;

@RestController
@RequestMapping("/api/motoristas")
@CrossOrigin(origins = "http://localhost:5173", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS})

public class MotoristaController {

    private final MotoristaService motoristaService;

    public MotoristaController(MotoristaService motoristaService) {
        this.motoristaService = motoristaService;
    }

    @GetMapping
    public ResponseEntity<List<MotoristaDTO>> listarTodos() {
        List<Motorista> motoristas = motoristaService.listarTodos();
        List<MotoristaDTO> dtos = motoristas.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<MotoristaDTO> buscarPorCpf(@PathVariable String cpf) {
        Motorista motorista = motoristaService.buscarPorCpf(cpf);
        return ResponseEntity.ok(toDTO(motorista));
    }

    @PostMapping
    public ResponseEntity<MotoristaDTO> criar(@RequestBody MotoristaDTO dto) {
        Motorista motorista = toEntity(dto);
        Motorista salvo = motoristaService.salvar(motorista);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(salvo));
    }

    @PutMapping("/{cpf}")
    public ResponseEntity<MotoristaDTO> atualizar(@PathVariable String cpf, @RequestBody MotoristaDTO dto) {
        Motorista motorista = toEntity(dto);
        motorista.setCpf(cpf);
        Motorista atualizado = motoristaService.atualizar(motorista);
        return ResponseEntity.ok(toDTO(atualizado));
    }

    @DeleteMapping("/{cpf}")
    public ResponseEntity<Void> excluir(@PathVariable String cpf) {
        motoristaService.excluir(cpf);
        return ResponseEntity.noContent().build();
    }

    private MotoristaDTO toDTO(Motorista m) {
        MotoristaDTO dto = new MotoristaDTO();
        dto.setCpf(m.getCpf());
        dto.setNome(m.getNome());
        dto.setCategoriaCnh(m.getCategoriaCnh());
        return dto;
    }

    private Motorista toEntity(MotoristaDTO dto) {
        Motorista m = new Motorista();
        m.setCpf(dto.getCpf());
        m.setNome(dto.getNome());
        m.setCategoriaCnh(dto.getCategoriaCnh());
        return m;
    }
}