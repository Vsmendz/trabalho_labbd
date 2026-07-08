package com.meufrota.transport_control.dto;

public class ManutencaoDTO {
    private Integer id;
    private String descricao;
    private Double kmReferencia;

    public ManutencaoDTO() {
    }

    public ManutencaoDTO(Integer id, String descricao, Double kmReferencia) {
        this.id = id;
        this.descricao = descricao;
        this.kmReferencia = kmReferencia;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getKmReferencia() {
        return kmReferencia;
    }

    public void setKmReferencia(Double kmReferencia) {
        this.kmReferencia = kmReferencia;
    }
}