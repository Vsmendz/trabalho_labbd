package lab_bd_class;

import jakarta.persistence.*;

@Entity
public class Manutencao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "DESCRICAO", nullable = false, length = 100)
    private String descricao;

    @Column(name = "KM_REFERENCIA", nullable = false)
    private Integer kmReferencia;

    public Manutencao() {
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Integer getKmReferencia() { return kmReferencia; }
    public void setKmReferencia(Integer kmReferencia) { this.kmReferencia = kmReferencia; }
}