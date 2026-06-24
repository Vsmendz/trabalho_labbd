package lab_bd;

public class Veiculos {
    private String placa;
    private String modelo;
    private int ano;
    private double quilometragem;
    private String tipo;
    private String status;

    public Veiculos(String placa, String modelo, int ano, double quilometragem, String tipo, String status) {
        this.placa = placa;
        this.modelo = modelo;
        this.ano = ano;
        this.quilometragem = quilometragem;
        this.tipo = tipo;
        this.status = status;
    }
    public String getPlaca() {
        return placa;
    }

    public int getAno() {
        return ano;
    }

    public double getQuilometragem() {
        return quilometragem;
    }

    public String getModelo() {
        return modelo;
    }

    public String getStatus() {
        return status;
    }

    public String getTipo() {
        return tipo;
    }
}
