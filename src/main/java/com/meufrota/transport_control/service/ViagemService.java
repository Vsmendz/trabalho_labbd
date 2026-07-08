package com.meufrota.transport_control.service;

import com.meufrota.transport_control.entity.Motorista;
import com.meufrota.transport_control.entity.Veiculo;
import com.meufrota.transport_control.entity.Viagem;
import com.meufrota.transport_control.repository.MotoristaRepository;
import com.meufrota.transport_control.repository.VeiculoRepository;
import com.meufrota.transport_control.repository.ViagemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ViagemService {

    private final ViagemRepository viagemRepository;
    private final VeiculoRepository veiculoRepository;
    private final MotoristaRepository motoristaRepository;

    public ViagemService(ViagemRepository viagemRepository,
                         VeiculoRepository veiculoRepository,
                         MotoristaRepository motoristaRepository) {
        this.viagemRepository = viagemRepository;
        this.veiculoRepository = veiculoRepository;
        this.motoristaRepository = motoristaRepository;
    }

    // Listar todas as viagens
    public List<Viagem> listarTodas() {
        return viagemRepository.findAll();
    }

    // Listar viagens em andamento
    public List<Viagem> listarEmAndamento() {
        return viagemRepository.findByStatus("EM_ANDAMENTO");
    }

    // Buscar viagem por ID (retorna Viagem, não Optional)
    public Viagem buscarPorId(Integer id) {
        Optional<Viagem> optional = viagemRepository.findById(id);
        return optional.orElseThrow(() -> new RuntimeException("Viagem não encontrada com ID: " + id));
    }

    // Iniciar uma nova viagem
    @Transactional
    public Viagem iniciarViagem(String placaVeiculo, String cpfMotorista) {
        Veiculo veiculo = veiculoRepository.findById(placaVeiculo)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));

        if (!"DISPONIVEL".equals(veiculo.getStatusVeiculo())) {
            throw new RuntimeException("Veículo não está disponível para viagem");
        }

        Motorista motorista = motoristaRepository.findById(cpfMotorista)
                .orElseThrow(() -> new RuntimeException("Motorista não encontrado"));

        Viagem viagem = new Viagem();
        viagem.setVeiculo(veiculo);
        viagem.setMotorista(motorista);
        viagem.setDataSaida(LocalDateTime.now());
        viagem.setKmInicial(veiculo.getQuilometragemAtual());
        viagem.setStatus("EM_ANDAMENTO"); // Se não tiver default

        // Salva a viagem
        Viagem saved = viagemRepository.save(viagem);

        // Atualiza status do veículo
        veiculo.setStatusVeiculo("EM_ROTA");
        veiculoRepository.save(veiculo);

        return saved;
    }

    // Encerrar viagem usando a stored procedure (ou método alternativo)
    @Transactional
    public void encerrarViagem(Integer idViagem, Double kmFinal, String observacao) {
        // Validação básica: existe a viagem?
        Viagem viagem = buscarPorId(idViagem); // usa o método acima
        if (!"EM_ANDAMENTO".equals(viagem.getStatus())) {
            throw new RuntimeException("A viagem não está em andamento");
        }
        if (kmFinal < viagem.getKmInicial()) {
            throw new RuntimeException("KM final não pode ser menor que KM inicial");
        }

        // Chama a stored procedure (se definida)
        viagemRepository.encerrarViagem(idViagem, kmFinal, observacao);

        // Atualiza o veículo após a procedure (opcional, mas a procedure pode fazer isso)
        // Se a procedure não atualizar o veículo, faça manualmente:
        Veiculo veiculo = viagem.getVeiculo();
        veiculo.setQuilometragemAtual(kmFinal);
        veiculo.setStatusVeiculo("DISPONIVEL");
        veiculoRepository.save(veiculo);
    }

    // Atualizar viagem (ex: mudar status, observação)
    public Viagem atualizar(Viagem viagem) {
        if (!viagemRepository.existsById(viagem.getId())) {
            throw new RuntimeException("Viagem não encontrada para atualizar");
        }
        return viagemRepository.save(viagem);
    }

    // Excluir viagem
    public void excluir(Integer id) {
        if (!viagemRepository.existsById(id)) {
            throw new RuntimeException("Viagem não encontrada para excluir");
        }
        viagemRepository.deleteById(id);
    }
}