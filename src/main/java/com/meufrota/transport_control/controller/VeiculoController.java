package com.meufrota.transport_control.controller;

import com.meufrota.transport_control.dto.VeiculoDTO;
import com.meufrota.transport_control.entity.Veiculo;
import com.meufrota.transport_control.service.VeiculoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/veiculos")
@CrossOrigin(origins = "http://localhost:5173")
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

    // Atualizar veículo
    @PutMapping("/{placa}")
    public ResponseEntity<VeiculoDTO> atualizar(@PathVariable String placa, @RequestBody VeiculoDTO dto) {
        Veiculo veiculo = toEntity(dto);
        veiculo.setPlaca(placa); // garante que a placa é a da URL
        Veiculo atualizado = veiculoService.atualizar(veiculo);
        return ResponseEntity.ok(toDTO(atualizado));
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