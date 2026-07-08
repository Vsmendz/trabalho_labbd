package com.meufrota.transport_control.entity;

import jakarta.persistence.*;

/**
 * Entidade que representa a tabela MOTORISTA no banco de dados.
 *
 * Essa classe é mapeada via JPA e representa os motoristas cadastrados
 * no sistema, contendo dados pessoais e informações da CNH.
 */
@Entity
@Table(name = "MOTORISTA")
public class Motorista {

    /**
     * CPF do motorista.
     *
     * Representa a chave primária da tabela.
     * É um identificador único que não se repete no sistema.
     */
   @Id
    @Column(name = "CPF", nullable = false, length = 11, columnDefinition = "CHAR(11)")
    private String cpf;

    /**
     * Nome do motorista.
     *
     * Campo obrigatório que armazena o nome completo do motorista.
     */
    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;

    /**
     * Categoria da CNH do motorista.
     *
     * Representa a categoria da carteira de habilitação (ex: A, B, C, D, E).
     */
    @Column(name = "CATEGORIA_CNH", nullable = false, length = 1)
    private String categoriaCnh;

    /**
     * Construtor vazio obrigatório para o JPA.
     *
     * O JPA utiliza esse construtor para instanciar objetos automaticamente
     * ao recuperar dados do banco.
     */
    public Motorista() {
    }

    // =========================
    // GETTERS E SETTERS
    // =========================

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