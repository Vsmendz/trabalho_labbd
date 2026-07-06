package lab_bd_DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import lab_bd_class.*;

import java.util.List;

public class TesteDAO {

    public static void main(String[] args) {

        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("LabBD");

        EntityManager em = emf.createEntityManager();

        // Instanciando os DAOs
        MotoristaDAO motoristaDAO = new MotoristaDAO(em);
        ManutencaoDAO manutencaoDAO = new ManutencaoDAO(em);
        VeiculoDAO veiculoDAO = new VeiculoDAO(em);

        try {

            /*
             ==========================================================
                            TESTE MOTORISTA
             ==========================================================
             */

            System.out.println("======================================");
            System.out.println("TESTE MOTORISTA");
            System.out.println("======================================");

            Motorista motorista = motoristaDAO.buscarPorCpf("12345678900");

            if (motorista == null) {

                motorista = new Motorista();
                motorista.setCpf("12345678900");
                motorista.setNome("Pedro");
                motorista.setCategoriaCnh("D");

                motoristaDAO.salvar(motorista);

                System.out.println("Motorista cadastrado!");

            } else {

                System.out.println("Motorista já existe.");

            }

            motorista = motoristaDAO.buscarPorCpf("12345678900");

            if (motorista != null) {

                System.out.println("\nBusca realizada com sucesso:");

                System.out.println("CPF: " + motorista.getCpf());
                System.out.println("Nome: " + motorista.getNome());
                System.out.println("Categoria: " + motorista.getCategoriaCnh());

                motorista.setNome("Pedro João");

                motoristaDAO.atualizar(motorista);

                System.out.println("\nMotorista atualizado!");

            }

            System.out.println("\nLista de motoristas:");

            motoristaDAO.listarMotoristas().forEach(m -> {

                System.out.println("-------------------------");
                System.out.println("CPF: " + m.getCpf());
                System.out.println("Nome: " + m.getNome());
                System.out.println("Categoria: " + m.getCategoriaCnh());

            });


            /*
             ==========================================================
                            TESTE MANUTENÇÃO
             ==========================================================
             */

            System.out.println("\n======================================");
            System.out.println("TESTE MANUTENÇÃO");
            System.out.println("======================================");

            String descricao = "Troca de óleo Teste";

            List<Manutencao> lista =
                    manutencaoDAO.buscarPorDescricao(descricao);

            if (lista.isEmpty()) {

                Manutencao manutencao = new Manutencao();

                manutencao.setDescricao(descricao);
                manutencao.setKmReferencia(10000.0);

                manutencaoDAO.salvar(manutencao);

                System.out.println("Manutenção cadastrada!");

            } else {

                System.out.println("Essa manutenção já existe.");

            }

            lista = manutencaoDAO.buscarPorDescricao(descricao);

            System.out.println("\nResultado da busca:");

            for (Manutencao m : lista) {

                System.out.println("-------------------------");
                System.out.println("ID: " + m.getId());
                System.out.println("Descrição: " + m.getDescricao());
                System.out.println("KM Referência: " + m.getKmReferencia());

            }

            System.out.println("\nLista completa:");

            manutencaoDAO.listarManutencoes().forEach(m -> {

                System.out.println("-------------------------");
                System.out.println("ID: " + m.getId());
                System.out.println("Descrição: " + m.getDescricao());
                System.out.println("KM: " + m.getKmReferencia());

            });


            /*
             ==========================================================
                            TESTE VEÍCULO
             ==========================================================
             */

            System.out.println("\n======================================");
            System.out.println("TESTE VEÍCULO");
            System.out.println("======================================");

            Veiculo veiculo = veiculoDAO.buscarPorPlaca("ABC1234");

            if (veiculo == null) {

                veiculo = new Veiculo();

                veiculo.setPlaca("ABC1234");
                veiculo.setModelo("Fiat Uno");
                veiculo.setAno(2022);
                veiculo.setQuilometragemAtual(25000.0);
                veiculo.setStatusVeiculo("DISPONIVEL");
                veiculo.setTipo("CARRO");

                veiculoDAO.salvar(veiculo);

                System.out.println("Veículo cadastrado!");

            } else {

                System.out.println("Veículo já existe.");

            }

            veiculo = veiculoDAO.buscarPorPlaca("ABC1234");

            if (veiculo != null) {

                System.out.println("\nVeículo encontrado:");

                System.out.println("Placa: " + veiculo.getPlaca());
                System.out.println("Modelo: " + veiculo.getModelo());
                System.out.println("Ano: " + veiculo.getAno());
                System.out.println("Quilometragem: " + veiculo.getQuilometragemAtual());
                System.out.println("Status: " + veiculo.getStatusVeiculo());
                System.out.println("Tipo: " + veiculo.getTipo());

                veiculo.setQuilometragemAtual(30000.0);

                veiculoDAO.atualizar(veiculo);

                System.out.println("\nQuilometragem atualizada!");

            }

            System.out.println("\nLista de veículos:");

            veiculoDAO.listarVeiculos().forEach(v -> {

                System.out.println("-------------------------");
                System.out.println("Placa: " + v.getPlaca());
                System.out.println("Modelo: " + v.getModelo());
                System.out.println("Ano: " + v.getAno());
                System.out.println("Quilometragem: " + v.getQuilometragemAtual());
                System.out.println("Status: " + v.getStatusVeiculo());
                System.out.println("Tipo: " + v.getTipo());

            });

            System.out.println("\nVeículos disponíveis:");

            veiculoDAO.buscarPorStatus("DISPONIVEL").forEach(v ->
                    System.out.println(v.getPlaca() + " - " + v.getModelo())
            );

            System.out.println("\nVeículos do tipo CARRO:");

            veiculoDAO.buscarPorTipo("CARRO").forEach(v ->
                    System.out.println(v.getPlaca() + " - " + v.getModelo())
            );

        } finally {

            em.close();
            emf.close();

        }
    }
}