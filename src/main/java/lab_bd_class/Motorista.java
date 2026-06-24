package lab_bd_class;

import jakarta.persistence.*;

@Entity
public class Motorista {

    @Id
    private String cpf;

    private String nome;

    private String categoriaCnh;

    public Motorista() {
    }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCategoriaCnh() { return categoriaCnh; }
    public void setCategoriaCnh(String categoriaCnh) { this.categoriaCnh = categoriaCnh; }
}