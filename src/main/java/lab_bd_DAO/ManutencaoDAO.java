package lab_bd_DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lab_bd_class.Manutencao;

import java.util.List;

/**
 * DAO específico da entidade Manutencao.
 *
 * Responsável por operações de acesso ao banco relacionadas
 * exclusivamente à tabela/entidade Manutencao.
 *
 * Reutiliza as operações genéricas do GenericDAO e adiciona
 * consultas específicas do domínio.
 */
public class ManutencaoDAO extends GenericDAO<Manutencao> {

    /**
     * Construtor do DAO.
     *
     * Recebe o EntityManager responsável pela comunicação com o banco
     * e informa ao GenericDAO qual entidade será gerenciada (Manutencao).
     *
     * @param em Gerenciador de entidades JPA
     */
    public ManutencaoDAO(EntityManager em) {
        super(em, Manutencao.class);
    }

    /**
     * Busca uma manutenção pelo seu ID.
     *
     * Utiliza o EntityManager diretamente para localizar a entidade
     * no banco de dados através da chave primária.
     *
     * @param id Identificador da manutenção
     * @return Objeto Manutencao encontrado ou null se não existir
     */
    public Manutencao buscarPorId(Integer id) {
        return em.find(Manutencao.class, id);
    }

    /**
     * Busca uma manutenção pela descrição exata.
     *
     * Utiliza uma consulta JPQL filtrando pelo campo descricao.
     * Caso não exista resultado, a exceção NoResultException é tratada
     * e o método retorna null.
     *
     * @param descricao Texto exato da descrição da manutenção
     * @return Manutencao encontrada ou null se não existir
     */
    public List<Manutencao> buscarPorDescricao(String descricao) {

        return em.createQuery(
                        "SELECT m FROM Manutencao m WHERE m.descricao = :descricao",
                        Manutencao.class)
                .setParameter("descricao", descricao)
                .getResultList();
    }

    /**
     * Lista todas as manutenções cadastradas.
     *
     * Este método apenas reaproveita o método listar()
     * já implementado no GenericDAO.
     *
     * @return Lista de todas as manutenções
     */
    public List<Manutencao> listarManutencoes() {
        return listar();
    }

    /**
     * Busca manutenções com quilometragem de referência menor ou igual ao valor informado.
     *
     * Utiliza JPQL para filtrar registros com base no campo kmReferencia,
     * retornando todas as manutenções que atendem à condição.
     *
     * @param km Quilometragem máxima de referência
     * @return Lista de manutenções filtradas pela quilometragem
     */
    public List<Manutencao> buscarPorKmReferencia(Integer km) {
        return em.createQuery(
                        "SELECT m FROM Manutencao m WHERE m.kmReferencia <= :km",
                        Manutencao.class)
                .setParameter("km", km)
                .getResultList();
    }

    /**
     * Remove uma manutenção pelo ID.
     *
     * Primeiro busca a entidade no banco.
     * Se ela existir, chama o método excluir do GenericDAO,
     * que faz a remoção corretamente dentro de uma transação.
     *
     * @param id Identificador da manutenção a ser removida
     */
    public void excluir(Integer id) {
        Manutencao manutencao = buscarPorId(id);
        if (manutencao != null) {
            super.excluir(manutencao);
        }
    }
}