package com.meufrota.transport_control.service;

import com.meufrota.transport_control.entity.Manutencao;
import com.meufrota.transport_control.entity.ManutencaoVeiculo;
import com.meufrota.transport_control.entity.Veiculo;
import com.meufrota.transport_control.repository.ManutencaoRepository;
import com.meufrota.transport_control.repository.ManutencaoVeiculoRepository;
import com.meufrota.transport_control.repository.VeiculoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManutencaoVeiculoService {

    private final ManutencaoVeiculoRepository manutencaoVeiculoRepository;
    private final VeiculoRepository veiculoRepository;
    private final ManutencaoRepository manutencaoRepository;

    public ManutencaoVeiculoService(ManutencaoVeiculoRepository manutencaoVeiculoRepository,
                                    VeiculoRepository veiculoRepository,
                                    ManutencaoRepository manutencaoRepository) {
        this.manutencaoVeiculoRepository = manutencaoVeiculoRepository;
        this.veiculoRepository = veiculoRepository;
        this.manutencaoRepository = manutencaoRepository;
    }

    public List<ManutencaoVeiculo> listarTodos() {
        return manutencaoVeiculoRepository.findAll();
    }

    public ManutencaoVeiculo buscarPorId(Integer id) {
        Optional<ManutencaoVeiculo> optional = manutencaoVeiculoRepository.findById(id);
        return optional.orElseThrow(() -> new RuntimeException("Registro de manutenção de veículo não encontrado com ID: " + id));
    }

    // Busca por placa (String)
    public List<ManutencaoVeiculo> buscarPorVeiculo(String placa) {
        // Primeiro busca o veículo
        Veiculo veiculo = veiculoRepository.findById(placa)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado com placa: " + placa));
        return manutencaoVeiculoRepository.findByVeiculo(veiculo);
    }

    // Busca por status
    public List<ManutencaoVeiculo> buscarPorStatus(String status) {
        return manutencaoVeiculoRepository.findByStatus(status);
    }

    // Salvar (criar) - você precisa associar Veiculo e Manutencao
    public ManutencaoVeiculo salvar(ManutencaoVeiculo registro) {
        // Se o registro vier com IDs, você pode buscar as entidades e associar
        // Mas aqui vamos assumir que o registro já tem as entidades carregadas (veiculo e manutencao)
        // Ou você pode receber DTO e montar no service.
        return manutencaoVeiculoRepository.save(registro);
    }

    // Método para criar a partir de IDs (opcional)
    public ManutencaoVeiculo criarComIds(String placaVeiculo, Integer idManutencao, ManutencaoVeiculo dados) {
        Veiculo veiculo = veiculoRepository.findById(placaVeiculo)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));
        Manutencao manutencao = manutencaoRepository.findById(idManutencao)
                .orElseThrow(() -> new RuntimeException("Manutenção não encontrada"));
        dados.setVeiculo(veiculo);
        dados.setManutencao(manutencao);
        // Se dataGeracao for nula, define como now()
        if (dados.getDataGeracao() == null) {
            dados.setDataGeracao(java.time.LocalDateTime.now());
        }
        return manutencaoVeiculoRepository.save(dados);
    }

    public ManutencaoVeiculo atualizar(ManutencaoVeiculo registro) {
        if (!manutencaoVeiculoRepository.existsById(registro.getId())) {
            throw new RuntimeException("Registro de manutenção de veículo não encontrado para atualizar");
        }
        return manutencaoVeiculoRepository.save(registro);
    }

    public void excluir(Integer id) {
        if (!manutencaoVeiculoRepository.existsById(id)) {
            throw new RuntimeException("Registro de manutenção de veículo não encontrado para excluir");
        }
        manutencaoVeiculoRepository.deleteById(id);
    }
}