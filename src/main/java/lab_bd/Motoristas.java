package lab_bd;

public class Motoristas {
    private String cpf;
    private String nome;
    private String categoria;

    public Motoristas(String cpf, String nome, String categoria) {
        this.cpf = cpf;
        this.nome = nome;
        this.categoria = categoria;
    }
    public String getCpf() {
        return cpf;
    }

    public String getNome() {
        return nome;
    }

    public String getCategoria() {
        return categoria;
    }
}
