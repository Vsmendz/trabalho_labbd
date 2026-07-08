package com.meufrota.transport_control.dto;

import java.time.LocalDateTime;

public class ViagemDTO {
    private Integer id;
    private LocalDateTime dataSaida;
    private LocalDateTime dataChegada;
    private Double kmInicial;
    private Double kmFinal;
    private String status;
    private String observacao;

    // Dados do veículo (para exibição)
    private String placaVeiculo;
    private String modeloVeiculo;

    // Dados do motorista (para exibição)
    private String cpfMotorista;
    private String nomeMotorista;

    public ViagemDTO() {
    }

    public ViagemDTO(Integer id, LocalDateTime dataSaida, LocalDateTime dataChegada,
                     Double kmInicial, Double kmFinal, String status, String observacao,
                     String placaVeiculo, String modeloVeiculo,
                     String cpfMotorista, String nomeMotorista) {
        this.id = id;
        this.dataSaida = dataSaida;
        this.dataChegada = dataChegada;
        this.kmInicial = kmInicial;
        this.kmFinal = kmFinal;
        this.status = status;
        this.observacao = observacao;
        this.placaVeiculo = placaVeiculo;
        this.modeloVeiculo = modeloVeiculo;
        this.cpfMotorista = cpfMotorista;
        this.nomeMotorista = nomeMotorista;
    }

    // Getters e Setters
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

    public String getPlacaVeiculo() {
        return placaVeiculo;
    }

    public void setPlacaVeiculo(String placaVeiculo) {
        this.placaVeiculo = placaVeiculo;
    }

    public String getModeloVeiculo() {
        return modeloVeiculo;
    }

    public void setModeloVeiculo(String modeloVeiculo) {
        this.modeloVeiculo = modeloVeiculo;
    }

    public String getCpfMotorista() {
        return cpfMotorista;
    }

    public void setCpfMotorista(String cpfMotorista) {
        this.cpfMotorista = cpfMotorista;
    }

    public String getNomeMotorista() {
        return nomeMotorista;
    }

    public void setNomeMotorista(String nomeMotorista) {
        this.nomeMotorista = nomeMotorista;
    }
}