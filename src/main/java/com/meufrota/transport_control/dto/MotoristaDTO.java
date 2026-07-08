package com.meufrota.transport_control.dto;

public class MotoristaDTO {
    private String cpf;
    private String nome;
    private String categoriaCnh;

    public MotoristaDTO() {
    }

    public MotoristaDTO(String cpf, String nome, String categoriaCnh) {
        this.cpf = cpf;
        this.nome = nome;
        this.categoriaCnh = categoriaCnh;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoriaCnh() {
        return categoriaCnh;
    }

    public void setCategoriaCnh(String categoriaCnh) {
        this.categoriaCnh = categoriaCnh;
    }
}