package com.meufrota.transport_control.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidade que representa a tabela VIAGEM no banco de dados.
 *
 * Essa classe registra as viagens realizadas no sistema,
 * contendo informações como datas, quilometragem e os
 * relacionamentos com Veiculo e Motorista.
 */
@NamedStoredProcedureQuery(
    name = "encerrarViagem",
    procedureName = "ENCERRAR_VIAGEM",
    parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "P_ID_VIAGEM", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "P_KM_FINAL", type = Double.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "P_OBSERVACAO", type = String.class)
    }
)
@Entity
@Table(name = "VIAGEM")
public class Viagem {

    /**
     * Identificador único da viagem.
     *
     * É a chave primária da tabela e é gerada automaticamente
     * pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Data e hora de saída da viagem.
     *
     * Representa o momento em que a viagem foi iniciada.
     */
    @Column(name = "DATA_SAIDA", nullable = false)
    private LocalDateTime dataSaida;

    /**
     * Data e hora de chegada da viagem.
     *
     * Representa o momento em que a viagem foi finalizada.
     * Pode ser nulo enquanto a viagem estiver em andamento.
     */
    @Column(name = "DATA_CHEGADA")
    private LocalDateTime dataChegada;

    /**
     * Quilometragem inicial do veículo na viagem.
     *
     * Registra o odômetro no início da viagem.
     */
    @Column(name = "KM_INICIAL", nullable = false)
    private Double kmInicial;

    /**
     * Quilometragem final do veículo na viagem.
     *
     * Registra o odômetro ao final da viagem.
     * Pode ser nulo enquanto a viagem não for finalizada.
     */
    @Column(name = "KM_FINAL")
    private Double kmFinal;

    /**
     * Status da viagem.
     *
     * Valores possíveis: EM_ANDAMENTO, FINALIZADA, CANCELADA.
     */
    @Column(name = "STATUS", nullable = false, length = 20)
    private String status;

    /**
     * Observação sobre a viagem.
     *
     * Campo livre para anotações adicionais.
     */
    @Column(name = "OBSERVACAO", length = 255)
    private String observacao;

    /**
     * Veículo utilizado na viagem.
     *
     * Define o relacionamento Many-to-One com Veiculo,
     * indicando qual veículo realizou a viagem.
     *
     * A chave estrangeira é armazenada na coluna PLACA_VEICULO.
     */
    @ManyToOne
    @JoinColumn(name = "PLACA_VEICULO", nullable = false)
    private Veiculo veiculo;

    /**
     * Motorista responsável pela viagem.
     *
     * Define o relacionamento Many-to-One com Motorista,
     * indicando quem realizou a viagem.
     *
     * A chave estrangeira é armazenada na coluna CPF_MOTORISTA.
     */
    @ManyToOne
    @JoinColumn(name = "CPF_MOTORISTA", nullable = false, columnDefinition = "CHAR(11)")
    private Motorista motorista;

    /**
     * Construtor vazio obrigatório para o JPA.
     *
     * Utilizado pelo framework para instanciar a entidade
     * ao recuperar dados do banco automaticamente.
     */
    public Viagem() {
        // Define o status padrão como EM_ANDAMENTO
        this.status = "EM_ANDAMENTO";
    }

    // =========================
    // GETTERS E SETTERS
    // =========================

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(LocalDateTime dataSaida) {
        this.dataSaida = dataSaida;
    }

    public LocalDateTime getDataChegada() {
        return dataChegada;
    }

    public void setDataChegada(LocalDateTime dataChegada) {
        this.dataChegada = dataChegada;
    }

    public Double getKmInicial() {
        return kmInicial;
    }

    public void setKmInicial(Double kmInicial) {
        this.kmInicial = kmInicial;
    }

    public Double getKmFinal() {
        return kmFinal;
    }

    public void setKmFinal(Double kmFinal) {
        this.kmFinal = kmFinal;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public Motorista getMotorista() {
        return motorista;
    }

    public void setMotorista(Motorista motorista) {
        this.motorista = motorista;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    // Método auxiliar para verificar se a viagem está em andamento
    public boolean isEmAndamento() {
        return "EM_ANDAMENTO".equals(this.status);
    }

    // Método auxiliar para verificar se a viagem está finalizada
    public boolean isFinalizada() {
        return "FINALIZADA".equals(this.status);
    }
}