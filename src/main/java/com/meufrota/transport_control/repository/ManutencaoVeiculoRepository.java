package com.meufrota.transport_control.repository;

import com.meufrota.transport_control.entity.ManutencaoVeiculo;
import com.meufrota.transport_control.entity.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManutencaoVeiculoRepository extends JpaRepository<ManutencaoVeiculo, Integer> {

    // Busca por veículo (todos os registros)
    List<ManutencaoVeiculo> findByVeiculo(Veiculo veiculo);
    
    // Outros métodos úteis:
    List<ManutencaoVeiculo> findByStatus(String status);
}