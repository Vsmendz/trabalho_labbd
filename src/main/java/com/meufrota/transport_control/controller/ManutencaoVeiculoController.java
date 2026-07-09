package com.meufrota.transport_control.controller;
import java.util.List;
import java.util.Map;
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

import com.meufrota.transport_control.dto.ManutencaoVeiculoDTO;
import com.meufrota.transport_control.entity.ManutencaoVeiculo;
import com.meufrota.transport_control.service.ManutencaoVeiculoService;

@RestController
@RequestMapping("/api/manutencoes-veiculos")
@CrossOrigin(origins = "http://localhost:5173", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS})

public class ManutencaoVeiculoController {

    private final ManutencaoVeiculoService manutencaoVeiculoService;

    public ManutencaoVeiculoController(ManutencaoVeiculoService manutencaoVeiculoService) {
        this.manutencaoVeiculoService = manutencaoVeiculoService;
    }

    // Listar todos os registros
    @GetMapping
    public ResponseEntity<List<ManutencaoVeiculoDTO>> listarTodos() {
        List<ManutencaoVeiculo> registros = manutencaoVeiculoService.listarTodos();
        List<ManutencaoVeiculoDTO> dtos = registros.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<ManutencaoVeiculoDTO> buscarPorId(@PathVariable Integer id) {
        ManutencaoVeiculo registro = manutencaoVeiculoService.buscarPorId(id);
        return ResponseEntity.ok(toDTO(registro));
    }

    // Buscar por veículo (placa)
    @GetMapping("/veiculo/{placa}")
    public ResponseEntity<List<ManutencaoVeiculoDTO>> buscarPorVeiculo(@PathVariable String placa) {
        List<ManutencaoVeiculo> registros = manutencaoVeiculoService.buscarPorVeiculo(placa);
        List<ManutencaoVeiculoDTO> dtos = registros.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Buscar por status (ex: PENDENTE)
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ManutencaoVeiculoDTO>> buscarPorStatus(@PathVariable String status) {
        List<ManutencaoVeiculo> registros = manutencaoVeiculoService.buscarPorStatus(status);
        List<ManutencaoVeiculoDTO> dtos = registros.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Criar um novo registro (associar manutenção a veículo)
    @PostMapping
    public ResponseEntity<ManutencaoVeiculoDTO> criar(@RequestBody ManutencaoVeiculoDTO dto) {
        ManutencaoVeiculo registro = toEntity(dto);
        ManutencaoVeiculo salvo = manutencaoVeiculoService.salvar(registro);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(salvo));
    }


    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> atualizarStatus(@PathVariable Integer id, 
                                            @RequestBody Map<String, String> body) {
    try {
        String novoStatus = body.get("status");
        String observacao = body.get("observacao");
        
        // Busca o registro existente
        ManutencaoVeiculo existente = manutencaoVeiculoService.buscarPorId(id);
        
        // Atualiza apenas o status
        existente.setStatus(novoStatus);
        
        if (observacao != null && !observacao.isEmpty()) {
            existente.setObservacao(observacao);
        }
        
        // Salva
        manutencaoVeiculoService.salvar(existente);
        
        return ResponseEntity.ok().build();
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

    // Atualizar um registro (ex: mudar status, observação, etc.)
    @PutMapping("/{id}")
    public ResponseEntity<ManutencaoVeiculoDTO> atualizar(@PathVariable Integer id,
                                                          @RequestBody ManutencaoVeiculoDTO dto) {
        // Busca o registro EXISTENTE no banco (com veiculo e manutencao já preenchidos)
        ManutencaoVeiculo existente = manutencaoVeiculoService.buscarPorId(id);
        
        // Atualiza apenas os campos que podem ser alterados
        if (dto.getStatus() != null) {
            existente.setStatus(dto.getStatus());
        }
        
        if (dto.getObservacao() != null) {
            existente.setObservacao(dto.getObservacao());
        }
        
        if (dto.getDataManutencao() != null) {
            existente.setDataManutencao(dto.getDataManutencao());
        }
        
        if (dto.getKmFeito() != null) {
            existente.setKmFeito(dto.getKmFeito());
        }
        
        // Salva o registro atualizado (mantém veiculo e manutencao originais)
        ManutencaoVeiculo atualizado = manutencaoVeiculoService.salvar(existente);
        return ResponseEntity.ok(toDTO(atualizado));
    }

    // Excluir registro
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        manutencaoVeiculoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // Métodos de conversão
    private ManutencaoVeiculoDTO toDTO(ManutencaoVeiculo mv) {
        ManutencaoVeiculoDTO dto = new ManutencaoVeiculoDTO();
        dto.setId(mv.getId());
        dto.setDataGeracao(mv.getDataGeracao());
        dto.setDataManutencao(mv.getDataManutencao());
        dto.setKmFeito(mv.getKmFeito());
        dto.setStatus(mv.getStatus());
        dto.setObservacao(mv.getObservacao());
        if (mv.getVeiculo() != null) {
            dto.setPlacaVeiculo(mv.getVeiculo().getPlaca());
        }
        if (mv.getManutencao() != null) {
            dto.setDescricaoManutencao(mv.getManutencao().getDescricao());
        }
        return dto;
    }

    private ManutencaoVeiculo toEntity(ManutencaoVeiculoDTO dto) {
        ManutencaoVeiculo mv = new ManutencaoVeiculo();
        mv.setId(dto.getId());
        mv.setDataGeracao(dto.getDataGeracao());
        mv.setDataManutencao(dto.getDataManutencao());
        mv.setKmFeito(dto.getKmFeito());
        mv.setStatus(dto.getStatus());
        mv.setObservacao(dto.getObservacao());
        // Para criação, você precisa buscar Veiculo e Manutencao
        // Isso deve ser feito no Service
        return mv;
    }
}

