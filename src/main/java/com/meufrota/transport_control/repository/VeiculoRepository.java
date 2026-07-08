package com.meufrota.transport_control.repository;

import com.meufrota.transport_control.entity.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, String> {

    // Método derivado: busca veículos pelo status (ex: "DISPONIVEL", "EM_ROTA", "MANUTENCAO")
    List<Veiculo> findByStatusVeiculo(String statusVeiculo);

    // Você pode adicionar outros métodos derivados, ex:
    // List<Veiculo> findByTipo(String tipo);
    // List<Veiculo> findByAnoGreaterThan(Integer ano);
    // Optional<Veiculo> findByPlacaAndStatusVeiculo(String placa, String status);
}