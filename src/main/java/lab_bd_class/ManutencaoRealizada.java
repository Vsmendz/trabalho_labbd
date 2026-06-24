package lab_bd_class;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class ManutencaoRealizada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate dataManutencao;

    private Integer kmFeito;

    @ManyToOne
    private Veiculo veiculo;

    @ManyToOne
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