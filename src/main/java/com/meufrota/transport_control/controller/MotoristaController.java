package com.meufrota.transport_control.controller;

import com.meufrota.transport_control.dto.MotoristaDTO;
import com.meufrota.transport_control.entity.Motorista;
import com.meufrota.transport_control.service.MotoristaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/motoristas")
@CrossOrigin(origins = "http://localhost:5173")
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