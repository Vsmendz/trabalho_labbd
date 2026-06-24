package lab_bd_class;
import jakarta.persistence.*;

@Entity
public class Veiculo {

    @Id
    @Column(name = "PLACA", length = 10)
    private String placa;

    @Column(name = "MODELO", nullable = false, length = 50)
    private String modelo;

    @Column(name = "ANO", nullable = false)
    private Integer ano;

    @Column(name = "QUILOMETRAGEM_ATUAL", nullable = false)
    private Double quilometragemAtual;

    @Column(name = "STATUS_VEICULO", nullable = false, length = 20)
    private String statusVeiculo;

    @Column(name = "TIPO", nullable = false, length = 20)
    private String tipo;

    public Veiculo() {
    }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public Integer getAno() { return ano; }
    public void setAno(Integer ano) { this.ano = ano; }

    public Double getQuilometragemAtual() { return quilometragemAtual; }
    public void setQuilometragemAtual(Double quilometragemAtual) { this.quilometragemAtual = quilometragemAtual; }

    public String getStatusVeiculo() { return statusVeiculo; }
    public void setStatusVeiculo(String statusVeiculo) { this.statusVeiculo = statusVeiculo; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}