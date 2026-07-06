package lab_bd_class;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidade que representa a tabela MANUTENCAO_VEICULO no banco de dados.
 *
 * Essa classe modela o relacionamento entre Veiculo e Manutencao,
 * registrando quando uma manutenção foi gerada, realizada e seu status.
 *
 * Funciona como uma entidade de associação com informações adicionais
 * além do simples relacionamento N:N.
 */
@Entity
@Table(name = "MANUTENCAO_VEICULO")
public class ManutencaoVeiculo {

    /**
     * Identificador único do registro.
     *
     * Chave primária gerada automaticamente pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    /**
     * Data em que o registro de manutenção foi gerado no sistema.
     *
     * Representa o momento em que a manutenção foi criada/registrada.
     */
    @Column(name = "DATA_GERACAO")
    private LocalDateTime dataGeracao;

    /**
     * Data prevista ou realizada da manutenção.
     *
     * Representa a data específica em que a manutenção ocorre.
     */
    @Column(name = "DATA_MANUTENCAO")
    private LocalDate dataManutencao;

    /**
     * Quilometragem registrada no momento da manutenção.
     *
     * Usada para controle e histórico de uso do veículo.
     */
    @Column(name = "KM_FEITO")
    private Double kmFeito;

    /**
     * Status da manutenção.
     *
     * Ex: PENDENTE, REALIZADA, CANCELADA.
     */
    @Column(name = "STATUS")
    private String status;

    /**
     * Observações adicionais sobre a manutenção.
     *
     * Campo livre para detalhes extras.
     */
    @Column(name = "OBSERVACAO")
    private String observacao;

    /**
     * Veículo associado à manutenção.
     *
     * Define o relacionamento Many-to-One com a entidade Veiculo,
     * onde várias manutenções podem estar ligadas a um mesmo veículo.
     *
     * A chave estrangeira é armazenada na coluna PLACA_VEICULO.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLACA_VEICULO", nullable = false)
    private Veiculo veiculo;

    /**
     * Tipo de manutenção associada ao veículo.
     *
     * Define o relacionamento Many-to-One com a entidade Manutencao,
     * indicando qual tipo de manutenção foi realizada ou programada.
     *
     * A chave estrangeira é armazenada na coluna ID_MANUTENCAO.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_MANUTENCAO", nullable = false)
    private Manutencao manutencao;

    /**
     * Construtor vazio obrigatório para o JPA.
     *
     * O JPA utiliza esse construtor para instanciar a entidade
     * ao recuperar dados do banco de forma automática.
     */
    public ManutencaoVeiculo() {
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

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public Manutencao getManutencao() {
        return manutencao;
    }

    public void setManutencao(Manutencao manutencao) {
        this.manutencao = manutencao;
    }
}