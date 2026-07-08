package com.meufrota.transport_control.entity;

import jakarta.persistence.*;

/**
 * Entidade que representa a tabela MANUTENCAO no banco de dados.
 *
 * Essa classe é mapeada via JPA e representa os tipos de manutenção
 * disponíveis no sistema, como revisões, trocas de peças, etc.
 */
@Entity
@Table(name = "MANUTENCAO")
public class Manutencao {

    /**
     * Identificador único da manutenção.
     *
     * É a chave primária da tabela e é gerada automaticamente
     * pelo banco de dados (autoincremento).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    /**
     * Descrição da manutenção.
     *
     * Campo obrigatório que define o tipo ou nome da manutenção.
     * Ex: "Troca de óleo", "Alinhamento", etc.
     */
    @Column(name = "DESCRICAO", nullable = false, length = 100)
    private String descricao;

    /**
     * Quilometragem de referência para a manutenção.
     *
     * Indica em que quilometragem a manutenção deve ser realizada
     * ou recomendada.
     */
    @Column(name = "KM_REFERENCIA", nullable = false)
    private Double kmReferencia;

    /**
     * Construtor vazio obrigatório para o JPA.
     *
     * O JPA utiliza esse construtor para instanciar objetos
     * automaticamente ao recuperar dados do banco.
     */
    public Manutencao() {
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