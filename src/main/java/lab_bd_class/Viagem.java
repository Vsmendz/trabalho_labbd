package lab_bd_class;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Viagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime dataSaida;

    private LocalDateTime dataChegada;

    private Double kmInicial;

    private Double kmFinal;

    @ManyToOne
    private Veiculo veiculo;

    @ManyToOne
    private Motorista motorista;

    public Viagem() {
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public LocalDateTime getDataSaida() { return dataSaida; }
    public void setDataSaida(LocalDateTime dataSaida) { this.dataSaida = dataSaida; }

    public LocalDateTime getDataChegada() { return dataChegada; }
    public void setDataChegada(LocalDateTime dataChegada) { this.dataChegada = dataChegada; }

    public Double getKmInicial() { return kmInicial; }
    public void setKmInicial(Double kmInicial) { this.kmInicial = kmInicial; }

    public Double getKmFinal() { return kmFinal; }
    public void setKmFinal(Double kmFinal) { this.kmFinal = kmFinal; }

    public Veiculo getVeiculo() { return veiculo; }
    public void setVeiculo(Veiculo veiculo) { this.veiculo = veiculo; }

    public Motorista getMotorista() { return motorista; }
    public void setMotorista(Motorista motorista) { this.motorista = motorista; }
}