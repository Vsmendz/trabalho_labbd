package lab_bd_class;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

import java.time.LocalDateTime;
import java.util.List;

public class TesteBackend {

    private static EntityManager em;

    public static void main(String[] args) {
        // Inicializa o EntityManager
        em = Persistence
                .createEntityManagerFactory("LabBD")
                .createEntityManager();

        System.out.println("=".repeat(60));
        System.out.println("🧪 TESTANDO O BACKEND JPA");
        System.out.println("=".repeat(60));

        // Teste 1: Listar Veículos
        testarListarVeiculos();

        // Teste 2: Listar Motoristas
        testarListarMotoristas();

        // Teste 3: Listar Manutenções
        testarListarManutencoes();

        // Teste 4: Criar uma Viagem
        testarCriarViagem();

        // Teste 5: Listar Viagens
        testarListarViagens();

        // Teste 6: Encerrar uma Viagem
        testarEncerrarViagem();

        // Teste 7: Verificar manutenções pendentes
        testarManutencoesPendentes();

        // Fecha o EntityManager
        em.close();

        System.out.println("\n✅ TESTES FINALIZADOS!");
    }

    // ========================================
    // TESTE 1: Listar Veículos
    // ========================================
    private static void testarListarVeiculos() {
        System.out.println("\n📋 TESTE 1: Listando Veículos...");
        System.out.println("-".repeat(40));

        try {
            List<Veiculo> veiculos = em.createQuery("SELECT v FROM Veiculo v", Veiculo.class)
                    .getResultList();

            if (veiculos.isEmpty()) {
                System.out.println("❌ Nenhum veículo encontrado!");
                System.out.println("⚠️ Execute insert.sql primeiro!");
            } else {
                System.out.println("✅ Encontrados " + veiculos.size() + " veículos:");
                for (Veiculo v : veiculos) {
                    System.out.printf("  🚗 %s - %s (%s) - %.1f km - %s%n",
                            v.getPlaca(),
                            v.getModelo(),
                            v.getTipo(),
                            v.getQuilometragemAtual(),
                            v.getStatusVeiculo()
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Erro ao listar veículos: " + e.getMessage());
        }
    }

    // ========================================
    // TESTE 2: Listar Motoristas
    // ========================================
    private static void testarListarMotoristas() {
        System.out.println("\n📋 TESTE 2: Listando Motoristas...");
        System.out.println("-".repeat(40));

        try {
            List<Motorista> motoristas = em.createQuery("SELECT m FROM Motorista m", Motorista.class)
                    .getResultList();

            if (motoristas.isEmpty()) {
                System.out.println("❌ Nenhum motorista encontrado!");
            } else {
                System.out.println("✅ Encontrados " + motoristas.size() + " motoristas:");
                for (Motorista m : motoristas) {
                    System.out.printf("  👤 %s - %s (CNH: %s)%n",
                            m.getCpf(),
                            m.getNome(),
                            m.getCategoriaCnh()
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Erro ao listar motoristas: " + e.getMessage());
        }
    }

    // ========================================
    // TESTE 3: Listar Manutenções (CORRIGIDO)
    // ========================================
    private static void testarListarManutencoes() {
        System.out.println("\n📋 TESTE 3: Listando Manutenções...");
        System.out.println("-".repeat(40));

        try {
            List<Manutencao> manutencoes = em.createQuery("SELECT m FROM Manutencao m", Manutencao.class)
                    .getResultList();

            if (manutencoes.isEmpty()) {
                System.out.println("❌ Nenhuma manutenção encontrada!");
            } else {
                System.out.println("✅ Encontradas " + manutencoes.size() + " manutenções:");
                for (Manutencao m : manutencoes) {
                    // kmReferencia é Integer, mas se for Double use %f
                    System.out.printf("  🔧 %d - %s (KM: %.0f)%n",
                            m.getId(),
                            m.getDescricao(),
                            m.getKmReferencia()  // agora é Integer, %d é correto
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Erro ao listar manutenções: " + e.getMessage());
        }
    }

    // ========================================
    // TESTE 4: Criar uma Viagem
    // ========================================
    private static void testarCriarViagem() {
        System.out.println("\n📋 TESTE 4: Criando uma nova viagem...");
        System.out.println("-".repeat(40));

        try {
            // Buscar um veículo disponível
            Veiculo veiculo = em.createQuery(
                            "SELECT v FROM Veiculo v WHERE v.statusVeiculo = 'DISPONIVEL'",
                            Veiculo.class)
                    .setMaxResults(1)
                    .getSingleResult();

            // Buscar um motorista
            Motorista motorista = em.createQuery(
                            "SELECT m FROM Motorista m",
                            Motorista.class)
                    .setMaxResults(1)
                    .getSingleResult();

            // Criar a viagem
            Viagem viagem = new Viagem();
            viagem.setVeiculo(veiculo);
            viagem.setMotorista(motorista);
            viagem.setDataSaida(LocalDateTime.now());
            viagem.setKmInicial(veiculo.getQuilometragemAtual());

            // Salvar
            em.getTransaction().begin();
            em.persist(viagem);

            // Atualizar status do veículo
            veiculo.setStatusVeiculo("EM_ROTA");
            em.merge(veiculo);

            em.getTransaction().commit();

            System.out.println("✅ Viagem criada com sucesso!");
            System.out.printf("  📝 ID: %d%n", viagem.getId());
            System.out.printf("  🚗 Veículo: %s%n", veiculo.getPlaca());
            System.out.printf("  👤 Motorista: %s%n", motorista.getNome());
            System.out.printf("  📅 Data: %s%n", viagem.getDataSaida());

        } catch (Exception e) {
            System.out.println("❌ Erro ao criar viagem: " + e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }

    // ========================================
    // TESTE 5: Listar Viagens
    // ========================================
    private static void testarListarViagens() {
        System.out.println("\n📋 TESTE 5: Listando Viagens...");
        System.out.println("-".repeat(40));

        try {
            List<Viagem> viagens = em.createQuery("SELECT v FROM Viagem v", Viagem.class)
                    .getResultList();

            if (viagens.isEmpty()) {
                System.out.println("❌ Nenhuma viagem encontrada!");
            } else {
                System.out.println("✅ Encontradas " + viagens.size() + " viagens:");
                for (Viagem v : viagens) {
                    String status = v.getDataChegada() == null ? "🟡 EM ANDAMENTO" : "✅ FINALIZADA";
                    System.out.printf("  🛣️ ID: %d - %s - %s%n",
                            v.getId(),
                            v.getVeiculo().getPlaca(),
                            status
                    );
                    System.out.printf("     KM: %.1f -> %s%n",
                            v.getKmInicial(),
                            v.getKmFinal() != null ? String.format("%.1f", v.getKmFinal()) : "-"
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Erro ao listar viagens: " + e.getMessage());
        }
    }

    // ========================================
    // TESTE 6: Encerrar uma Viagem
    // ========================================
    private static void testarEncerrarViagem() {
        System.out.println("\n📋 TESTE 6: Encerrando uma viagem...");
        System.out.println("-".repeat(40));

        try {
            // Buscar uma viagem em andamento
            Viagem viagem = em.createQuery(
                            "SELECT v FROM Viagem v WHERE v.dataChegada IS NULL",
                            Viagem.class)
                    .setMaxResults(1)
                    .getSingleResult();

            if (viagem == null) {
                System.out.println("⚠️ Nenhuma viagem em andamento para encerrar");
                return;
            }

            Double kmFinal = viagem.getKmInicial() + 50.0; // +50 km

            // Encerrar a viagem
            em.getTransaction().begin();

            viagem.setKmFinal(kmFinal);
            viagem.setDataChegada(LocalDateTime.now());

            // Atualizar veículo
            Veiculo veiculo = viagem.getVeiculo();
            veiculo.setQuilometragemAtual(kmFinal);
            veiculo.setStatusVeiculo("DISPONIVEL");

            em.merge(viagem);
            em.merge(veiculo);

            em.getTransaction().commit();

            System.out.println("✅ Viagem encerrada com sucesso!");
            System.out.printf("  📝 ID: %d%n", viagem.getId());
            System.out.printf("  📍 KM Final: %.1f%n", kmFinal);
            System.out.printf("  🚗 Veículo: %s agora está DISPONIVEL%n", veiculo.getPlaca());

        } catch (jakarta.persistence.NoResultException e) {
            System.out.println("⚠️ Nenhuma viagem em andamento para encerrar");
        } catch (Exception e) {
            System.out.println("❌ Erro ao encerrar viagem: " + e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }

    // ========================================
    // TESTE 7: Verificar manutenções pendentes
    // ========================================
    private static void testarManutencoesPendentes() {
        System.out.println("\n📋 TESTE 7: Verificando manutenções pendentes...");
        System.out.println("-".repeat(40));

        try {
            // Tenta encontrar a tabela MANUTENCAO_VEICULO
            List<?> pendentes = em.createNativeQuery(
                            "SELECT * FROM MANUTENCAO_VEICULO WHERE STATUS = 'PENDENTE'")
                    .getResultList();

            if (pendentes.isEmpty()) {
                System.out.println("✅ Nenhuma manutenção pendente");
            } else {
                System.out.println("⚠️ Encontradas " + pendentes.size() + " manutenções pendentes");
                for (Object obj : pendentes) {
                    System.out.println("  " + obj);
                }
            }
        } catch (Exception e) {
            System.out.println("⚠️ Tabela MANUTENCAO_VEICULO ainda não existe ou está vazia");
        }
    }
}