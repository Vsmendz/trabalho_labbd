package com.meufrota.transport_control.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ManutencaoVeiculoDTO {
    private Integer id;
    private LocalDateTime dataGeracao;
    private LocalDate dataManutencao;
    private Double kmFeito;
    private String status;
    private String observacao;

    // Referências para as entidades relacionadas
    private String placaVeiculo;    // para identificar o veículo
    private Integer idManutencao;   // para identificar o tipo de manutenção

    // Campos adicionais para exibição (opcionais)
    private String modeloVeiculo;   // se quiser mostrar no front
    private String descricaoManutencao;

    public ManutencaoVeiculoDTO() {
    }

    public ManutencaoVeiculoDTO(Integer id, LocalDateTime dataGeracao, LocalDate dataManutencao,
                                Double kmFeito, String status, String observacao,
                                String placaVeiculo, Integer idManutencao,
                                String modeloVeiculo, String descricaoManutencao) {
        this.id = id;
        this.dataGeracao = dataGeracao;
        this.dataManutencao = dataManutencao;
        this.kmFeito = kmFeito;
        this.status = status;
        this.observacao = observacao;
        this.placaVeiculo = placaVeiculo;
        this.idManutencao = idManutencao;
        this.modeloVeiculo = modeloVeiculo;
        this.descricaoManutencao = descricaoManutencao;
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDataGeracao() {
        return dataGeracao;
    }

    public void setDataGeracao(LocalDateTime dataGeracao) {
        this.dataGeracao = dataGeracao;
    }

    public LocalDate getDataManutencao() {
        return dataManutencao;
    }

    public void setDataManutencao(LocalDate dataManutencao) {
        this.dataManutencao = dataManutencao;
    }

    public Double getKmFeito() {
        return kmFeito;
    }

    public void setKmFeito(Double kmFeito) {
        this.kmFeito = kmFeito;
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

    public Integer getIdManutencao() {
        return idManutencao;
    }

    public void setIdManutencao(Integer idManutencao) {
        this.idManutencao = idManutencao;
    }

    public String getModeloVeiculo() {
        return modeloVeiculo;
    }

    public void setModeloVeiculo(String modeloVeiculo) {
        this.modeloVeiculo = modeloVeiculo;
    }

    public String getDescricaoManutencao() {
        return descricaoManutencao;
    }

    public void setDescricaoManutencao(String descricaoManutencao) {
        this.descricaoManutencao = descricaoManutencao;
    }
}