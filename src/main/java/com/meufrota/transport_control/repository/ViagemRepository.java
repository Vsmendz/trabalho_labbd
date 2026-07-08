package com.meufrota.transport_control.repository;

import com.meufrota.transport_control.entity.Viagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
//import java.util.Optional;

@Repository
public interface ViagemRepository extends JpaRepository<Viagem, Integer> {

    // Buscar viagens por status (ex: "EM_ANDAMENTO", "FINALIZADA")
    List<Viagem> findByStatus(String status);

    // Buscar viagens em andamento de um veículo específico
    List<Viagem> findByVeiculoPlacaAndStatus(String placa, String status);

    // Buscar viagens de um motorista
    List<Viagem> findByMotoristaCpf(String cpf);

    // Buscar viagens por período (data de saída entre duas datas)
    List<Viagem> findByDataSaidaBetween(LocalDateTime inicio, LocalDateTime fim);

    // Método que chama a stored procedure definida na entidade Viagem (@NamedStoredProcedureQuery)
    @Procedure(name = "encerrarViagem")
    @Modifying
    @Transactional
    void encerrarViagem(@Param("P_ID_VIAGEM") Integer idViagem,
                        @Param("P_KM_FINAL") Double kmFinal,
                        @Param("P_OBSERVACAO") String observacao);

    // Alternativa: usar @Query com native query (se preferir não usar @NamedStoredProcedureQuery)
    // @Modifying
    // @Query(value = "CALL ENCERRAR_VIAGEM(:id, :km, :obs)", nativeQuery = true)
    // void encerrarViagemNative(@Param("id") Integer id, @Param("km") Double km, @Param("obs") String obs);
}