package com.meufrota.transport_control.service;

import com.meufrota.transport_control.entity.Manutencao;
import com.meufrota.transport_control.repository.ManutencaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ManutencaoService {

    private final ManutencaoRepository manutencaoRepository;

    public ManutencaoService(ManutencaoRepository manutencaoRepository) {
        this.manutencaoRepository = manutencaoRepository;
    }

    public List<Manutencao> listarTodas() {
        return manutencaoRepository.findAll();
    }

    public Manutencao buscarPorId(Integer id) {
        return manutencaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Manutenção não encontrada com ID: " + id));
    }

    @Transactional
    public Manutencao salvar(Manutencao manutencao) {
        return manutencaoRepository.save(manutencao);
    }

    @Transactional
    public Manutencao atualizar(Manutencao manutencao) {
        // Verifica se existe
        buscarPorId(manutencao.getId());
        return manutencaoRepository.save(manutencao);
    }

    @Transactional
    public void excluir(Integer id) {
        buscarPorId(id); // verifica se existe
        manutencaoRepository.deleteById(id);
    }
}