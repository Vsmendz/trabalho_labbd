package lab_bd_class;

import jakarta.persistence.*;

@Entity
public class Motorista {

    @Id
    @Column(name = "CPF", length = 11)
    private String cpf;

    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;

    @Column(name = "CATEGORIA_CNH", nullable = false, length = 1)
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