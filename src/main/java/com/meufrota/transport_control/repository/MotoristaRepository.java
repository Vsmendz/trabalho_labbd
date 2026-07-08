package com.meufrota.transport_control.repository;

import com.meufrota.transport_control.entity.Motorista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MotoristaRepository extends JpaRepository<Motorista, String> {

    // Buscar motoristas por nome (parcial ou exato)
    List<Motorista> findByNomeContainingIgnoreCase(String nome);

    // Buscar por categoria da CNH
    List<Motorista> findByCategoriaCnh(String categoriaCnh);

    // Você pode adicionar outros métodos conforme necessidade
}