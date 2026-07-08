package com.meufrota.transport_control.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "ABASTECIMENTO")
public class Abastecimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "placa_veiculo", length = 10, nullable = false)
    private String placaVeiculo;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "tipo", length = 50, nullable = false)
    private String tipo;

    @Column(name = "litro", nullable = false, columnDefinition = "DECIMAL(10,3)")
    private BigDecimal litro;

    @Column(name = "preco", nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;  // usar BigDecimal para DECIMAL

    @Column(name = "obs")
    private String obs;

    // Construtor vazio obrigatório
    public Abastecimento() {
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlacaVeiculo() {
        return placaVeiculo;
    }

    public void setPlacaVeiculo(String placaVeiculo) {
        this.placaVeiculo = placaVeiculo;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getLitro() {
        return litro;
    }

    public void setLitro(BigDecimal litro) {
        this.litro = litro;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }
}