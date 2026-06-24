package lab_bd_class;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class ManutencaoRealizada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "DATA_MANUTENCAO", nullable = false)
    private LocalDate dataManutencao;

    @Column(name = "KM_FEITO", nullable = false)
    private Integer kmFeito;

    @ManyToOne
    @JoinColumn(name = "PLACA_VEICULO", nullable = false)
    private Veiculo veiculo;

    @ManyToOne
    @JoinColumn(name = "ID_MANUTENCAO", nullable = false)
    private Manutencao manutencao;

    public ManutencaoRealizada() {
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public LocalDate getDataManutencao() { return dataManutencao; }
    public void setDataManutencao(LocalDate dataManutencao) { this.dataManutencao = dataManutencao; }

    public Integer getKmFeito() { return kmFeito; }
    public void setKmFeito(Integer kmFeito) { this.kmFeito = kmFeito; }

    public Veiculo getVeiculo() { return veiculo; }
    public void setVeiculo(Veiculo veiculo) { this.veiculo = veiculo; }

    public Manutencao getManutencao() { return manutencao; }
    public void setManutencao(Manutencao manutencao) { this.manutencao = manutencao; }
}