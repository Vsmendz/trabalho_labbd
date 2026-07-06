package lab_bd_DAO;

import jakarta.persistence.EntityManager;
import lab_bd_class.Motorista;
import lab_bd_class.Veiculo;
import lab_bd_class.Viagem;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DAO responsável pela entidade Viagem.
 *
 * Essa classe realiza operações de acesso ao banco de dados
 * relacionadas às viagens realizadas no sistema.
 *
 * Aqui são implementadas consultas específicas envolvendo
 * veículo, motorista e período de tempo.
 */
public class ViagemDAO extends GenericDAO<Viagem> {

    /**
     * Construtor do DAO.
     *
     * Recebe o EntityManager responsável pela comunicação com o banco
     * e define a entidade Viagem como alvo das operações.
     *
     * @param em Gerenciador de entidades JPA
     */
    public ViagemDAO(EntityManager em) {
        super(em, Viagem.class);
    }

    /**
     * Busca uma viagem pelo ID.
     *
     * Utiliza o EntityManager para localizar a viagem diretamente
     * pela chave primária no banco de dados.
     *
     * @param id Identificador da viagem
     * @return Objeto Viagem encontrado ou null se não existir
     */
    public Viagem buscarPorId(Integer id) {
        return em.find(Viagem.class, id);
    }

    /**
     * Lista todas as viagens cadastradas.
     *
     * Reutiliza o método listar() do GenericDAO para retornar
     * todos os registros da entidade Viagem.
     *
     * @return Lista de todas as viagens
     */
    public List<Viagem> listarViagens() {
        return listar();
    }

    /**
     * Busca viagens associadas a um veículo específico.
     *
     * Utiliza uma consulta JPQL filtrando pelo veículo informado.
     *
     * @param veiculo Veículo utilizado como filtro
     * @return Lista de viagens do veículo informado
     */
    public List<Viagem> buscarPorVeiculo(Veiculo veiculo) {
        return em.createQuery(
                        "SELECT v FROM Viagem v WHERE v.veiculo = :veiculo",
                        Viagem.class)
                .setParameter("veiculo", veiculo)
                .getResultList();
    }

    /**
     * Busca viagens associadas a um motorista específico.
     *
     * Utiliza JPQL para filtrar as viagens pelo motorista informado.
     *
     * @param motorista Motorista utilizado como filtro
     * @return Lista de viagens realizadas pelo motorista
     */
    public List<Viagem> buscarPorMotorista(Motorista motorista) {
        return em.createQuery(
                        "SELECT v FROM Viagem v WHERE v.motorista = :motorista",
                        Viagem.class)
                .setParameter("motorista", motorista)
                .getResultList();
    }

    /**
     * Busca viagens realizadas em um intervalo de datas.
     *
     * Utiliza o campo dataSaida para filtrar viagens entre
     * a data inicial e final informadas.
     *
     * @param inicio Data/hora inicial do período
     * @param fim Data/hora final do período
     * @return Lista de viagens dentro do período informado
     */
    public List<Viagem> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return em.createQuery(
                        "SELECT v FROM Viagem v " +
                                "WHERE v.dataSaida BETWEEN :inicio AND :fim",
                        Viagem.class)
                .setParameter("inicio", inicio)
                .setParameter("fim", fim)
                .getResultList();
    }

    /**
     * Busca viagens finalizadas.
     *
     * Considera como finalizadas as viagens que possuem
     * dataChegada preenchida (não nula).
     *
     * @return Lista de viagens concluídas
     */
    public List<Viagem> buscarViagensFinalizadas() {
        return em.createQuery(
                        "SELECT v FROM Viagem v WHERE v.dataChegada IS NOT NULL",
                        Viagem.class)
                .getResultList();
    }

    /**
     * Busca viagens que ainda estão em andamento.
     *
     * Considera como em andamento as viagens que ainda não possuem
     * dataChegada registrada.
     *
     * @return Lista de viagens em andamento
     */
    public List<Viagem> buscarViagensEmAndamento() {
        return em.createQuery(
                        "SELECT v FROM Viagem v WHERE v.dataChegada IS NULL",
                        Viagem.class)
                .getResultList();
    }

    /**
     * Remove uma viagem pelo ID.
     *
     * Primeiro busca a entidade no banco.
     * Se ela existir, delega a remoção para o método excluir do GenericDAO,
     * que gerencia a transação corretamente.
     *
     * @param id Identificador da viagem a ser removida
     */
    public void excluir(Integer id) {
        Viagem viagem = buscarPorId(id);

        if (viagem != null) {
            super.excluir(viagem);
        }
    }
}