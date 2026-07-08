package com.meufrota.transport_control.service;

import com.meufrota.transport_control.entity.Veiculo;
import com.meufrota.transport_control.repository.VeiculoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VeiculoService {

    private final VeiculoRepository veiculoRepository;

    public VeiculoService(VeiculoRepository veiculoRepository) {
        this.veiculoRepository = veiculoRepository;
    }

    // Listar todos os veículos
    public List<Veiculo> listarTodos() {
        return veiculoRepository.findAll();
    }

    // Listar veículos disponíveis (usando o método customizado do repository)
    public List<Veiculo> listarDisponiveis() {
        return veiculoRepository.findByStatusVeiculo("DISPONIVEL");
    }

    // Buscar por placa (retorna Veiculo, não Optional)
    public Veiculo buscarPorPlaca(String placa) {
        Optional<Veiculo> optional = veiculoRepository.findById(placa);
        return optional.orElseThrow(() -> new RuntimeException("Veículo não encontrado com placa: " + placa));
    }

    // Salvar novo veículo
    public Veiculo salvar(Veiculo veiculo) {
        return veiculoRepository.save(veiculo);
    }

    // Atualizar veículo existente
    public Veiculo atualizar(Veiculo veiculo) {
        if (!veiculoRepository.existsById(veiculo.getPlaca())) {
            throw new RuntimeException("Veículo não encontrado para atualizar");
        }
        return veiculoRepository.save(veiculo);
    }

    // Excluir veículo por placa
    public void excluir(String placa) {
        if (!veiculoRepository.existsById(placa)) {
            throw new RuntimeException("Veículo não encontrado para excluir");
        }
        veiculoRepository.deleteById(placa);
    }
}