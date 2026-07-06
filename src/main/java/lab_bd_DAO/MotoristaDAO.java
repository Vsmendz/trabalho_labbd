package lab_bd_DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import lab_bd_class.Motorista;

import java.util.List;

/**
 * DAO responsável pela entidade Motorista.
 *
 * Esta classe realiza operações de acesso ao banco de dados
 * relacionadas à entidade Motorista, como buscas, listagens,
 * atualizações e remoções.
 *
 * Ela estende GenericDAO para reutilizar operações básicas CRUD.
 */
public class MotoristaDAO extends GenericDAO<Motorista> {

    /**
     * Construtor do DAO.
     *
     * Recebe o EntityManager responsável pela comunicação com o banco
     * e informa ao GenericDAO qual entidade será manipulada.
     *
     * @param em Gerenciador de entidades JPA
     */
    public MotoristaDAO(EntityManager em) {
        super(em, Motorista.class);
    }

    /**
     * Busca um motorista pelo CPF.
     *
     * Utiliza uma consulta JPQL para localizar o motorista
     * com base no campo CPF.
     *
     * Caso não exista resultado, a exceção NoResultException
     * é tratada e o método retorna null.
     *
     * @param cpf CPF do motorista a ser buscado
     * @return Motorista encontrado ou null se não existir
     */
    public Motorista buscarPorCpf(String cpf) {
        try {
            return em.createQuery(
                            "SELECT m FROM Motorista m WHERE m.cpf = :cpf",
                            Motorista.class
                    )
                    .setParameter("cpf", cpf)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Lista todos os motoristas cadastrados no banco.
     *
     * Utiliza uma consulta JPQL simples para retornar todos os registros
     * da entidade Motorista.
     *
     * @return Lista com todos os motoristas
     */
    public List<Motorista> listarMotoristas() {
        return em.createQuery(
                "SELECT m FROM Motorista m",
                Motorista.class
        ).getResultList();
    }

    /**
     * Atualiza os dados de um motorista no banco.
     *
     * Inicia uma transação, utiliza merge para sincronizar
     * o objeto com o banco de dados e confirma a operação.
     *
     * O merge pode atualizar um registro existente ou inserir
     * caso ele não esteja persistido.
     *
     * @param motorista Objeto com os dados atualizados
     */
    @Override
    public void atualizar(Motorista motorista) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        em.merge(motorista);

        tx.commit();
    }

    /**
     * Remove um motorista a partir do CPF.
     *
     * Primeiro inicia uma transação, depois busca o motorista
     * no banco utilizando o CPF como chave.
     *
     * Se o motorista existir, ele é removido do banco de dados
     * e a transação é confirmada.
     *
     * @param cpf CPF do motorista a ser removido
     */
    public void excluirPorCpf(String cpf) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Motorista motorista = em.find(Motorista.class, cpf);

        if (motorista != null) {
            em.remove(motorista);
        }

        tx.commit();
    }
}