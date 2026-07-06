package lab_bd_DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lab_bd_class.Veiculo;

import java.util.List;

/**
 * DAO responsável pela entidade Veiculo.
 *
 * Reutiliza as operações básicas do GenericDAO e implementa
 * consultas específicas relacionadas aos veículos.
 */
public class VeiculoDAO extends GenericDAO<Veiculo> {

    /**
     * Construtor.
     *
     * @param em EntityManager utilizado pelo JPA.
     */
    public VeiculoDAO(EntityManager em) {
        super(em, Veiculo.class);
    }

    /**
     * Busca um veículo pela placa.
     *
     * @param placa Placa do veículo.
     * @return Veículo encontrado ou null caso não exista.
     */
    public Veiculo buscarPorPlaca(String placa) {
        try {
            return em.createQuery(
                            "SELECT v FROM Veiculo v WHERE v.placa = :placa",
                            Veiculo.class)
                    .setParameter("placa", placa)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Lista todos os veículos.
     *
     * @return Lista de veículos.
     */
    public List<Veiculo> listarVeiculos() {
        return listar();
    }

    /**
     * Busca veículos pelo status.
     *
     * Exemplo:
     * DISPONIVEL
     * EM_VIAGEM
     * MANUTENCAO
     *
     * @param status Status do veículo.
     * @return Lista de veículos.
     */
    public List<Veiculo> buscarPorStatus(String status) {
        return em.createQuery(
                        "SELECT v FROM Veiculo v WHERE v.statusVeiculo = :status",
                        Veiculo.class)
                .setParameter("status", status)
                .getResultList();
    }

    /**
     * Busca veículos pelo tipo.
     *
     * Exemplo:
     * CARRO
     * CAMINHAO
     * MOTO
     *
     * @param tipo Tipo do veículo.
     * @return Lista de veículos.
     */
    public List<Veiculo> buscarPorTipo(String tipo) {
        return em.createQuery(
                        "SELECT v FROM Veiculo v WHERE v.tipo = :tipo",
                        Veiculo.class)
                .setParameter("tipo", tipo)
                .getResultList();
    }

    /**
     * Remove um veículo pela placa.
     *
     * @param placa Placa do veículo.
     */
    public void excluir(String placa) {
        Veiculo veiculo = buscarPorPlaca(placa);

        if (veiculo != null) {
            super.excluir(veiculo);
        }
    }

}