package com.meufrota.transport_control.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class AbastecimentoDTO {

    private Integer id;

    @NotBlank(message = "A placa do veículo é obrigatória")
    @Size(max = 10, message = "A placa deve ter no máximo 10 caracteres")
    private String placaVeiculo;

    @NotNull(message = "A data é obrigatória")
    private LocalDate data;

    @NotBlank(message = "O tipo de combustível é obrigatório")
    @Size(max = 50, message = "O tipo deve ter no máximo 50 caracteres")
    private String tipo; // Diesel, Gasolina, Etanol, GNV

    @NotNull(message = "A quantidade de litros é obrigatória")
    @Positive(message = "A quantidade de litros deve ser positiva")
    private Double litro;

    @NotNull(message = "O preço é obrigatório")
    @Positive(message = "O preço deve ser positivo")
    private Double preco;

    @Size(max = 255, message = "A observação deve ter no máximo 255 caracteres")
    private String obs;

    // Construtor vazio (obrigatório para frameworks)
    public AbastecimentoDTO() {
    }

    // Construtor com todos os campos (opcional)
    public AbastecimentoDTO(Integer id, String placaVeiculo, LocalDate data, String tipo,
                            Double litro, Double preco, String obs) {
        this.id = id;
        this.placaVeiculo = placaVeiculo;
        this.data = data;
        this.tipo = tipo;
        this.litro = litro;
        this.preco = preco;
        this.obs = obs;
    }

    // ── Getters e Setters ──

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

    public Double getLitro() {
        return litro;
    }

    public void setLitro(Double litro) {
        this.litro = litro;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    // ── toString (opcional, útil para logs) ──
    @Override
    public String toString() {
        return "AbastecimentoDTO{" +
                "id=" + id +
                ", placaVeiculo='" + placaVeiculo + '\'' +
                ", data=" + data +
                ", tipo='" + tipo + '\'' +
                ", litro=" + litro +
                ", preco=" + preco +
                ", obs='" + obs + '\'' +
                '}';
    }
}