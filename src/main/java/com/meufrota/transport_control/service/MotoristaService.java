package com.meufrota.transport_control.service;

import com.meufrota.transport_control.entity.Motorista;
import com.meufrota.transport_control.repository.MotoristaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MotoristaService {

    private final MotoristaRepository motoristaRepository;

    public MotoristaService(MotoristaRepository motoristaRepository) {
        this.motoristaRepository = motoristaRepository;
    }

    public List<Motorista> listarTodos() {
        return motoristaRepository.findAll();
    }

    public Motorista buscarPorCpf(String cpf) {
        Optional<Motorista> optional = motoristaRepository.findById(cpf);
        return optional.orElseThrow(() -> new RuntimeException("Motorista não encontrado com CPF: " + cpf));
    }

    public Motorista salvar(Motorista motorista) {
        return motoristaRepository.save(motorista);
    }

    public Motorista atualizar(Motorista motorista) {
        if (!motoristaRepository.existsById(motorista.getCpf())) {
            throw new RuntimeException("Motorista não encontrado para atualizar");
        }
        return motoristaRepository.save(motorista);
    }

    public void excluir(String cpf) {
        if (!motoristaRepository.existsById(cpf)) {
            throw new RuntimeException("Motorista não encontrado para excluir");
        }
        motoristaRepository.deleteById(cpf);
    }
}