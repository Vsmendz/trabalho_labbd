package com.meufrota.transport_control.service;

import com.meufrota.transport_control.entity.Abastecimento;
import com.meufrota.transport_control.repository.AbastecimentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AbastecimentoService {

    private final AbastecimentoRepository repository;

    public AbastecimentoService(AbastecimentoRepository repository) {
        this.repository = repository;
    }

    public List<Abastecimento> listarTodos() {
        return repository.findAll();
    }

    public Abastecimento buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Abastecimento não encontrado com ID: " + id));
    }

    public Abastecimento salvar(Abastecimento abastecimento) {
        return repository.save(abastecimento);
    }

    public void excluir(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Abastecimento não encontrado para exclusão");
        }
        repository.deleteById(id);
    }
}