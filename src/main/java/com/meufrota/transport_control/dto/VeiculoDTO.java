package com.meufrota.transport_control.dto;

public class VeiculoDTO {
    private String placa;
    private String modelo;
    private Integer ano;
    private Double quilometragemAtual;
    private String statusVeiculo;
    private String tipo;

    // Construtor vazio (obrigatório para serialização)
    public VeiculoDTO() {
    }

    // Construtor com todos os campos
    public VeiculoDTO(String placa, String modelo, Integer ano, Double quilometragemAtual,
                      String statusVeiculo, String tipo) {
        this.placa = placa;
        this.modelo = modelo;
        this.ano = ano;
        this.quilometragemAtual = quilometragemAtual;
        this.statusVeiculo = statusVeiculo;
        this.tipo = tipo;
    }

    // Getters e Setters
    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Double getQuilometragemAtual() {
        return quilometragemAtual;
    }

    public void setQuilometragemAtual(Double quilometragemAtual) {
        this.quilometragemAtual = quilometragemAtual;
    }

    public String getStatusVeiculo() {
        return statusVeiculo;
    }

    public void setStatusVeiculo(String statusVeiculo) {
        this.statusVeiculo = statusVeiculo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}