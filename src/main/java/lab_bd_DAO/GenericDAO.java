package lab_bd_DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

/**
 * Classe genérica para operações CRUD (Create, Read, Update, Delete)
 * utilizando JPA (Java Persistence API).
 *
 * @param <T> Tipo da entidade que será gerenciada
 */
public class GenericDAO<T> {

    // Gerenciador de entidades JPA - responsável pelas operações de persistência
    protected EntityManager em;

    // Classe da entidade - usada para consultas e operações reflexivas
    private Class<T> classe;

    /**
     * Construtor que recebe o EntityManager e a classe da entidade.
     *
     * @param em EntityManager para gerenciar as operações de banco de dados
     * @param classe Classe da entidade (ex: Produto.class, Cliente.class)
     */
    public GenericDAO(EntityManager em, Class<T> classe){
        this.em = em;
        this.classe = classe;
    }

    /**
     * Persiste (insere) um novo objeto no banco de dados.
     *
     * @param obj Objeto a ser salvo
     */
    public void salvar(T obj) {
        // Obtém a transação atual do EntityManager
        EntityTransaction tx = em.getTransaction();
        // Inicia a transação
        tx.begin();
        // Persiste o objeto (transforma de transient para persistent)
        em.persist(obj);
        // Confirma (commit) a transação
        tx.commit();
    }

    /**
     * Atualiza um objeto existente no banco de dados.
     *
     * @param obj Objeto a ser atualizado (deve ter o ID preenchido)
     */
    public void atualizar(T obj) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        // Merge: sincroniza o objeto com o banco de dados
        // Se o objeto já existe, atualiza; se não existe, insere
        em.merge(obj);
        tx.commit();
    }

    /**
     * Exclui um objeto do banco de dados.
     *
     * @param obj Objeto a ser excluído
     */
    public void excluir(T obj) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        // Verifica se o objeto está no contexto de persistência
        // Se não estiver, faz merge primeiro para associá-lo
        // Remove permanentemente do banco de dados
        em.remove(em.contains(obj) ? obj : em.merge(obj));
        tx.commit();
    }

    /**
     * Busca uma entidade pelo seu ID.
     *
     * @param id Identificador da entidade (chave primária)
     * @return Objeto da entidade encontrado ou null se não existir
     */
    public T buscarPorId(Object id) {
        // find: busca diretamente pelo ID na base de dados
        return em.find(classe, id);
    }

    /**
     * Lista todas as entidades do tipo T.
     *
     * @return Lista com todas as entidades encontradas
     */
    public List<T> listar() {
        // JPQL (Java Persistence Query Language)
        // "FROM " + classe.getSimpleName() - consulta que seleciona todos os registros
        // Exemplo: para Cliente, a query seria "FROM Cliente"
        return em.createQuery("FROM " + classe.getSimpleName(), classe).getResultList();
    }
}