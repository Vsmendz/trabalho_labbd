package com.meufrota.transport_control.controller;

import com.meufrota.transport_control.dto.ViagemDTO;
import com.meufrota.transport_control.entity.Viagem;
import com.meufrota.transport_control.service.ViagemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/viagens")
@CrossOrigin(origins = "http://localhost:5173")
public class ViagemController {

    private final ViagemService viagemService;

    public ViagemController(ViagemService viagemService) {
        this.viagemService = viagemService;
    }

    // Iniciar viagem: POST /api/viagens/iniciar?placa=ABC1234&cpf=12345678900
    @PostMapping("/iniciar")
    public ResponseEntity<ViagemDTO> iniciarViagem(@RequestParam String placa,
                                                   @RequestParam String cpf) {
        Viagem viagem = viagemService.iniciarViagem(placa, cpf);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(viagem));
    }

    // Encerrar viagem: POST /api/viagens/encerrar/{id}?kmFinal=150.5&observacao=Chegou bem
    @PostMapping("/encerrar/{id}")
    public ResponseEntity<Void> encerrarViagem(@PathVariable Integer id,
                                               @RequestParam Double kmFinal,
                                               @RequestParam(required = false) String observacao) {
        viagemService.encerrarViagem(id, kmFinal, observacao);
        return ResponseEntity.ok().build();
    }

    // Listar todas as viagens
    @GetMapping
    public ResponseEntity<List<ViagemDTO>> listarTodas() {
        List<Viagem> viagens = viagemService.listarTodas();
        List<ViagemDTO> dtos = viagens.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Listar viagens em andamento
    @GetMapping("/em-andamento")
    public ResponseEntity<List<ViagemDTO>> listarEmAndamento() {
        List<Viagem> viagens = viagemService.listarEmAndamento();
        List<ViagemDTO> dtos = viagens.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Buscar viagem por ID
    @GetMapping("/{id}")
    public ResponseEntity<ViagemDTO> buscarPorId(@PathVariable Integer id) {
        Viagem viagem = viagemService.buscarPorId(id);
        return ResponseEntity.ok(toDTO(viagem));
    }

    // Método de conversão
    private ViagemDTO toDTO(Viagem v) {
        ViagemDTO dto = new ViagemDTO();
        dto.setId(v.getId());
        dto.setDataSaida(v.getDataSaida());
        dto.setDataChegada(v.getDataChegada());
        dto.setKmInicial(v.getKmInicial());
        dto.setKmFinal(v.getKmFinal());
        dto.setStatus(v.getStatus());
        dto.setObservacao(v.getObservacao());
        if (v.getVeiculo() != null) {
            dto.setPlacaVeiculo(v.getVeiculo().getPlaca());
            dto.setModeloVeiculo(v.getVeiculo().getModelo());
        }
        if (v.getMotorista() != null) {
            dto.setCpfMotorista(v.getMotorista().getCpf());
            dto.setNomeMotorista(v.getMotorista().getNome());
        }
        return dto;
    }
}