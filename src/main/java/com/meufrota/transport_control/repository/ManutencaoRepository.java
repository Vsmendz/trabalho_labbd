package com.meufrota.transport_control.repository;

import com.meufrota.transport_control.entity.Manutencao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManutencaoRepository extends JpaRepository<Manutencao, Integer> {

    // Buscar manutenções por descrição (ex: "Troca de óleo")
    List<Manutencao> findByDescricaoContainingIgnoreCase(String descricao);

    // Buscar por quilometragem de referência (ex: menor que um valor)
    List<Manutencao> findByKmReferenciaLessThan(Double km);
}