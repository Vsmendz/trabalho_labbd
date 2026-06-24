package lab_bd;

import java.sql.Timestamp;

public class Viagem {
    private Timestamp data_inicial;
    private Timestamp data_final;
    private double km_inicial;
    private double km_final;
    private String placa_veiculo;
    private String cpf_motorista;

    public Viagem(Timestamp data_inicial, Timestamp data_final, double km_inicial, double km_final, String placa_veiculo, String cpf_motorista) {
        this.data_inicial = data_inicial;
        this.data_final = data_final;
        this.km_inicial = km_inicial;
        this.km_final = km_final;
        this.placa_veiculo = placa_veiculo;
        this.cpf_motorista = cpf_motorista;
    }
    public Timestamp getData_inicial() {
        return data_inicial;
    }

    public double getKm_final() {
        return km_final;
    }

    public double getKm_inicial() {
        return km_inicial;
    }

    public String getCpf_motorista() {
        return cpf_motorista;
    }

    public String getPlaca_veiculo() {
        return placa_veiculo;
    }

    public Timestamp getData_final() {
        return data_final;
    }
}
