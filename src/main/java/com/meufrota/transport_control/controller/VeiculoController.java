package com.meufrota.transport_control.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.meufrota.transport_control.dto.VeiculoDTO;
import com.meufrota.transport_control.entity.Veiculo;
import com.meufrota.transport_control.service.VeiculoService;

@CrossOrigin(origins = "http://localhost:5173", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RestController
@RequestMapping("/api/veiculos")
public class VeiculoController {

    private final VeiculoService veiculoService;

    public VeiculoController(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }

    // Listar todos os veículos
    @GetMapping
    public ResponseEntity<List<VeiculoDTO>> listarTodos() {
        List<Veiculo> veiculos = veiculoService.listarTodos();
        List<VeiculoDTO> dtos = veiculos.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Listar veículos disponíveis
    @GetMapping("/disponiveis")
    public ResponseEntity<List<VeiculoDTO>> listarDisponiveis() {
        List<Veiculo> veiculos = veiculoService.listarDisponiveis();
        List<VeiculoDTO> dtos = veiculos.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Buscar veículo por placa
    @GetMapping("/{placa}")
    public ResponseEntity<VeiculoDTO> buscarPorPlaca(@PathVariable String placa) {
        Veiculo veiculo = veiculoService.buscarPorPlaca(placa);
        return ResponseEntity.ok(toDTO(veiculo));
    }

    // Criar novo veículo
    @PostMapping
    public ResponseEntity<VeiculoDTO> criar(@RequestBody VeiculoDTO dto) {
        Veiculo veiculo = toEntity(dto);
        Veiculo salvo = veiculoService.salvar(veiculo);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(salvo));
    }

    // Atualizar veículo inteiro
    @PutMapping("/{placa}")
    public ResponseEntity<VeiculoDTO> atualizar(@PathVariable String placa, @RequestBody VeiculoDTO dto) {
        Veiculo veiculo = toEntity(dto);
        veiculo.setPlaca(placa); // garante que a placa é a da URL
        Veiculo atualizado = veiculoService.atualizar(veiculo);
        return ResponseEntity.ok(toDTO(atualizado));
    }

// Atualizar APENAS o status do veículo (Usado para enviar/despachar da manutenção)
    @PatchMapping("/{placa}")
    public ResponseEntity<VeiculoDTO> atualizarStatus(@PathVariable String placa, @RequestBody java.util.Map<String, String> payload) {
        Veiculo veiculo = veiculoService.buscarPorPlaca(placa);
        
        // Tenta buscar o status das duas formas mais comuns que o frontend pode ter enviado
        String novoStatus = payload.get("statusVeiculo");
        if (novoStatus == null) {
            novoStatus = payload.get("status");
        }
        
        // Se achou o status, atualiza. Senão, retorna erro 400 (Bad Request)
        if (novoStatus != null) {
            veiculo.setStatusVeiculo(novoStatus);
            Veiculo atualizado = veiculoService.atualizar(veiculo);
            return ResponseEntity.ok(toDTO(atualizado));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // Excluir veículo
    @DeleteMapping("/{placa}")
    public ResponseEntity<Void> excluir(@PathVariable String placa) {
        veiculoService.excluir(placa);
        return ResponseEntity.noContent().build();
    }

    // Métodos de conversão
    private VeiculoDTO toDTO(Veiculo v) {
        return new VeiculoDTO(
                v.getPlaca(),
                v.getModelo(),
                v.getAno(),
                v.getQuilometragemAtual(),
                v.getStatusVeiculo(),
                v.getTipo()
        );
    }

    private Veiculo toEntity(VeiculoDTO dto) {
        Veiculo v = new Veiculo();
        v.setPlaca(dto.getPlaca());
        v.setModelo(dto.getModelo());
        v.setAno(dto.getAno());
        v.setQuilometragemAtual(dto.getQuilometragemAtual());
        v.setStatusVeiculo(dto.getStatusVeiculo());
        v.setTipo(dto.getTipo());
        return v;
    }
}