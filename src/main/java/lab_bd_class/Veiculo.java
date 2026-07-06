package lab_bd_class;

import jakarta.persistence.*;

/**
 * Entidade que representa a tabela VEICULO no banco de dados.
 *
 * Essa classe é mapeada via JPA e representa os veículos cadastrados
 * no sistema, contendo informações como placa, modelo, ano e status.
 *
 * Também utiliza validações Bean Validation para garantir integridade
 * dos dados antes de persistir no banco.
 */
@Entity
@Table(name = "VEICULO")
public class Veiculo {

    /**
     * Placa do veículo.
     *
     * Representa a chave primária da tabela.
     * Deve ser única e não pode ser nula ou vazia.
     */
    @Id
    @Column(length = 10, nullable = false)
    private String placa;

    /**
     * Modelo do veículo.
     *
     * Campo obrigatório que identifica o modelo do veículo.
     */
    @Column(length = 50, nullable = false)
    private String modelo;

    /**
     * Ano de fabricação do veículo.
     *
     * Campo obrigatório que indica o ano do veículo.
     * Deve ser maior ou igual a 1950.
     */
    @Column(nullable = false)
    private Integer ano;

    /**
     * Quilometragem atual do veículo.
     *
     * Representa a distância total percorrida pelo veículo.
     * Não pode ser negativa.
     */
    @Column(name = "QUILOMETRAGEM_ATUAL", nullable = false)
    private Double quilometragemAtual;

    /**
     * Status atual do veículo.
     *
     * Indica a situação do veículo no sistema (ex: DISPONIVEL, EM_USO, MANUTENCAO).
     */
    @Column(name = "STATUS_VEICULO", nullable = false, length = 20)
    private String statusVeiculo;

    /**
     * Tipo do veículo.
     *
     * Exemplo: CARRO, CAMINHAO, MOTO, etc.
     */
    @Column(name = "TIPO", nullable = false, length = 20)
    private String tipo;

    /**
     * Construtor vazio obrigatório para o JPA.
     *
     * Utilizado pelo framework para instanciar a entidade
     * ao recuperar dados do banco de forma automática.
     */
    public Veiculo() {}

    // =========================
    // GETTERS E SETTERS
    // =========================

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