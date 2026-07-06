package lab_bd_DAO;

import jakarta.persistence.EntityManager;
import lab_bd_class.Manutencao;
import lab_bd_class.ManutencaoVeiculo;
import lab_bd_class.Veiculo;

import java.util.List;

/**
 * DAO responsável pela entidade ManutencaoVeiculo.
 *
 * Essa classe representa o relacionamento entre Manutencao e Veiculo,
 * geralmente um relacionamento N:N (muitos para muitos), onde um veículo
 * pode ter várias manutenções e uma manutenção pode estar associada a vários veículos.
 *
 * Aqui são implementadas consultas específicas desse relacionamento.
 */
public class ManutencaoVeiculoDAO extends GenericDAO<ManutencaoVeiculo> {

    /**
     * Construtor do DAO.
     *
     * Recebe o EntityManager responsável pela comunicação com o banco
     * e informa ao GenericDAO qual entidade será gerenciada.
     *
     * @param em Gerenciador de entidades JPA
     */
    public ManutencaoVeiculoDAO(EntityManager em) {
        super(em, ManutencaoVeiculo.class);
    }

    /**
     * Busca um registro pelo ID.
     *
     * Utiliza o EntityManager para localizar diretamente a entidade
     * no banco de dados pela chave primária.
     *
     * @param id Identificador do registro de manutenção-veículo
     * @return Objeto encontrado ou null se não existir
     */
    public ManutencaoVeiculo buscarPorId(Integer id) {
        return em.find(ManutencaoVeiculo.class, id);
    }

    /**
     * Lista todos os registros de manutenção-veículo.
     *
     * Reutiliza o método listar() do GenericDAO, retornando
     * todos os relacionamentos cadastrados.
     *
     * @return Lista de todas as associações manutenção-veículo
     */
    public List<ManutencaoVeiculo> listarManutencoesVeiculo() {
        return listar();
    }

    /**
     * Busca todas as manutenções associadas a um veículo específico.
     *
     * Utiliza JPQL para filtrar os registros com base no veículo informado.
     *
     * @param veiculo Veículo que será usado como filtro
     * @return Lista de manutenções associadas ao veículo
     */
    public List<ManutencaoVeiculo> buscarPorVeiculo(Veiculo veiculo) {
        return em.createQuery(
                        "SELECT mv FROM ManutencaoVeiculo mv WHERE mv.veiculo = :veiculo",
                        ManutencaoVeiculo.class)
                .setParameter("veiculo", veiculo)
                .getResultList();
    }

    /**
     * Busca todas as associações de um tipo específico de manutenção.
     *
     * Filtra os registros com base na entidade Manutencao informada.
     *
     * @param manutencao Tipo de manutenção que será filtrado
     * @return Lista de associações relacionadas à manutenção
     */
    public List<ManutencaoVeiculo> buscarPorManutencao(Manutencao manutencao) {
        return em.createQuery(
                        "SELECT mv FROM ManutencaoVeiculo mv WHERE mv.manutencao = :manutencao",
                        ManutencaoVeiculo.class)
                .setParameter("manutencao", manutencao)
                .getResultList();
    }

    /**
     * Busca todas as manutenções com status PENDENTE.
     *
     * Retorna registros que ainda não foram concluídos.
     *
     * @return Lista de manutenções pendentes
     */
    public List<ManutencaoVeiculo> buscarPendentes() {
        return em.createQuery(
                        "SELECT mv FROM ManutencaoVeiculo mv WHERE mv.status = 'PENDENTE'",
                        ManutencaoVeiculo.class)
                .getResultList();
    }

    /**
     * Busca todas as manutenções com status REALIZADA.
     *
     * Retorna registros que já foram concluídos.
     *
     * @return Lista de manutenções realizadas
     */
    public List<ManutencaoVeiculo> buscarRealizadas() {
        return em.createQuery(
                        "SELECT mv FROM ManutencaoVeiculo mv WHERE mv.status = 'REALIZADA'",
                        ManutencaoVeiculo.class)
                .getResultList();
    }

    /**
     * Busca manutenções filtrando dinamicamente pelo status.
     *
     * Permite informar qualquer status válido, tornando a consulta mais flexível.
     *
     * @param status Status da manutenção (ex: PENDENTE, REALIZADA)
     * @return Lista de registros filtrados pelo status
     */
    public List<ManutencaoVeiculo> buscarPorStatus(String status) {
        return em.createQuery(
                        "SELECT mv FROM ManutencaoVeiculo mv WHERE mv.status = :status",
                        ManutencaoVeiculo.class)
                .setParameter("status", status)
                .getResultList();
    }

    /**
     * Remove um registro de manutenção-veículo pelo ID.
     *
     * Primeiro busca a entidade no banco.
     * Se existir, delega a remoção para o método excluir do GenericDAO,
     * que trata a transação corretamente.
     *
     * @param id Identificador do registro a ser removido
     */
    public void excluir(Integer id) {
        ManutencaoVeiculo manutencaoVeiculo = buscarPorId(id);

        if (manutencaoVeiculo != null) {
            super.excluir(manutencaoVeiculo);
        }
    }
}