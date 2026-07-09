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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.meufrota.transport_control.dto.ViagemDTO;
import com.meufrota.transport_control.entity.Viagem;
import com.meufrota.transport_control.service.ViagemService;

@RestController
@RequestMapping("/api/viagens")
@CrossOrigin(origins = "http://localhost:5173", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS})

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
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirViagem(@PathVariable Integer id) {
        // Presumindo que o método no seu service se chama "excluir" ou "deletar".
        // Se o nome do método no seu ViagemService for diferente, mude a linha abaixo:
        viagemService.excluir(id); 
        
        return ResponseEntity.noContent().build();
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