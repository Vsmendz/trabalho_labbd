import { useEffect, useState } from "react";
import { listarVeiculos, criarVeiculo, atualizarStatusVeiculo, excluirVeiculo, VeiculoDTO } from '../service/VeiculoService';
import { listarMotoristas, criarMotorista, excluirMotorista, MotoristaDTO } from '../service/MotoristaService';
import { listarViagens, criarViagem, excluirViagem, encerrarViagem, ViagemDTO } from '../service/ViagemService';
import { listarManutencoes, criarManutencao, excluirManutencao, ManutencaoDTO } from '../service/ManutencaoService';
import { listarAbastecimentos, criarAbastecimento, excluirAbastecimento, AbastecimentoDTO } from '../service/AbastecimentoService';

// ── Types ──────────────────────────────────────────────────────────────
interface Vehicle {
  id: number;
  placa: string;
  modelo: string;
  ano: string;
  km: string;
  tipo: string;
  status: string;
}
interface Driver {
  id: number;
  nome: string;
  cpf: string;
  cnh: string;
  categoria: string;
  telefone: string;
  status: string;
}
interface Trip {
  id: number;
  origem: string;
  destino: string;
  distancia: string;
  saida: string;
  chegada: string;
  veiculoId: number;
  motoristaId: number;
  passageiros: string;
  observacoes: string;
  status: string;
  km?: string;
}
interface Abastecimento {
  id: number;
  veiculoId: number;
  placaVeiculo: string;
  data: string;
  litros: string;
  combustivel: string;
  custo: string;
  observacoes: string;
}
type Tab = "painel" | "viagens" | "veiculos" | "motoristas" | "manutencao" | "abastecimentos";

// ── Helpers ────────────────────────────────────────────────────────────
function statusFrontToBack(frontStatus: string): string {
  const map: Record<string, string> = {
    "Disponível": "DISPONIVEL",
    "Em Rota": "EM_ROTA",
    "Manutenção": "MANUTENCAO",
  };
  return map[frontStatus] || frontStatus;
}
function statusBackToFront(backStatus: string): string {
  const map: Record<string, string> = {
    "DISPONIVEL": "Disponível",
    "EM_ROTA": "Em Rota",
    "MANUTENCAO": "Manutenção",
  };
  return map[backStatus] || backStatus;
}

function vehicleBadge(s: string) {
  return ({ "Disponível": "badge-green", "Em Rota": "badge-blue", "Manutenção": "badge-yellow" } as Record<string,string>)[s] ?? "badge-gray";
}
function tripBadge(s: string) {
  return ({ Agendada: "badge-blue", "Em Andamento": "badge-yellow", "Concluída": "badge-green", Cancelada: "badge-red" } as Record<string,string>)[s] ?? "badge-gray";
}
function fmt(dt: string) {
  if (!dt) return "—";
  const d = new Date(dt);
  return d.toLocaleDateString("pt-BR") + " " + d.toLocaleTimeString("pt-BR", { hour: "2-digit", minute: "2-digit" });
}

// ── Main App ───────────────────────────────────────────────────────────
export default function App() {
  const [role, setRole] = useState<"operador" | "gerente" | null>(null);
  const [tab, setTab] = useState<Tab>("painel");

  // Proteção de rotas (apenas gerente)
  useEffect(() => {
    if ((tab === "abastecimentos" || tab === "manutencao") && role !== "gerente") {
      setTab("painel");
    }
  }, [tab, role]);

  // Estados
  const [vehicles, setVehicles] = useState<Vehicle[]>([]);
  const [drivers, setDrivers] = useState<Driver[]>([]);
  const [trips, setTrips] = useState<Trip[]>([]);
  const [maintTypes, setMaintTypes] = useState<{ id: number; descricao: string; kmRef: string }[]>([]);
  const [abastecimentos, setAbastecimentos] = useState<Abastecimento[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // ── Carregar dados da API ──────────────────────────────────────────
  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        const [veiculosData, motoristasData, viagensData, manutencoesData, abastecimentosData] = await Promise.all([
          listarVeiculos(),
          listarMotoristas(),
          listarViagens(),
          listarManutencoes(),
          listarAbastecimentos().catch(() => []),
        ]);

        const mappedVeiculos: Vehicle[] = veiculosData.map(v => ({
          id: Date.now() + Math.random(),
          placa: v.placa,
          modelo: v.modelo,
          ano: String(v.ano),
          km: String(v.quilometragemAtual || 0),
          tipo: v.tipo,
          status: statusBackToFront(v.statusVeiculo),
        }));

        const mappedMotoristas: Driver[] = motoristasData.map(m => ({
          id: Date.now() + Math.random(),
          nome: m.nome,
          cpf: m.cpf,
          cnh: '',
          categoria: m.categoriaCnh,
          telefone: '',
          status: 'Ativo',
        }));

        const mappedViagens: Trip[] = viagensData.map(t => ({
          id: t.id || Date.now() + Math.random(),
          origem: '',
          destino: '',
          distancia: '',
          saida: t.dataSaida || '',
          chegada: t.dataChegada || '',
          veiculoId: 0,
          motoristaId: 0,
          passageiros: '',
          observacoes: t.observacao || '',
          status: t.status === 'EM_ANDAMENTO' ? 'Em Andamento' : 'Concluída',
          km: String(t.kmInicial || 0),
        }));

        const mappedMaint = manutencoesData.map(m => ({
          id: m.id || Date.now() + Math.random(),
          descricao: m.descricao,
          kmRef: String(m.kmReferencia || 0),
        }));

        const mappedAbast: Abastecimento[] = abastecimentosData.map(a => ({
          id: a.id || Date.now() + Math.random(),
          veiculoId: 0,
          placaVeiculo: a.placaVeiculo || '',
          data: a.data || '',
          litros: String(a.litro || 0),
          combustivel: a.tipo || '',
          custo: String(a.preco || 0),
          observacoes: a.obs || '',
        }));

        setVehicles(mappedVeiculos);
        setDrivers(mappedMotoristas);
        setTrips(mappedViagens);
        setMaintTypes(mappedMaint);
        setAbastecimentos(mappedAbast);
      } catch (err) {
        setError('Erro ao carregar dados do servidor.');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  // ── Handlers de exclusão via seletor ───────────────────────────────
  // Veículos
  const [selectedVehicleId, setSelectedVehicleId] = useState<number | null>(null);
  const [delVehicleMsg, setDelVehicleMsg] = useState<{ type: "ok"|"err"; text: string } | null>(null);
  const handleDeleteVehicle = async () => {
    if (!selectedVehicleId) {
      setDelVehicleMsg({ type: "err", text: "Selecione um veículo." });
      setTimeout(() => setDelVehicleMsg(null), 3000);
      return;
    }
    const veiculo = vehicles.find(v => v.id === selectedVehicleId);
    if (!veiculo) return;
    try {
      await excluirVeiculo(veiculo.placa);
      setVehicles(prev => prev.filter(v => v.id !== selectedVehicleId));
      setSelectedVehicleId(null);
      setDelVehicleMsg({ type: "ok", text: "Veículo removido com sucesso!" });
      setTimeout(() => setDelVehicleMsg(null), 3000);
    } catch (err) {
      setDelVehicleMsg({ type: "err", text: "Erro ao excluir veículo. Verifique se ele não está em uso." });
      console.error(err);
    }
  };

  // Motoristas
  const [selectedDriverId, setSelectedDriverId] = useState<number | null>(null);
  const [delDriverMsg, setDelDriverMsg] = useState<{ type: "ok"|"err"; text: string } | null>(null);
  const handleDeleteDriver = async () => {
    if (!selectedDriverId) {
      setDelDriverMsg({ type: "err", text: "Selecione um motorista." });
      setTimeout(() => setDelDriverMsg(null), 3000);
      return;
    }
    const motorista = drivers.find(d => d.id === selectedDriverId);
    if (!motorista) return;
    try {
      await excluirMotorista(motorista.cpf);
      setDrivers(prev => prev.filter(d => d.id !== selectedDriverId));
      setSelectedDriverId(null);
      setDelDriverMsg({ type: "ok", text: "Motorista removido com sucesso!" });
      setTimeout(() => setDelDriverMsg(null), 3000);
    } catch (err) {
      setDelDriverMsg({ type: "err", text: "Erro ao excluir motorista. Verifique se ele não está em uso." });
      console.error(err);
    }
  };

  // Viagens
  const [selectedTripId, setSelectedTripId] = useState<number | null>(null);
  const [delTripMsg, setDelTripMsg] = useState<{ type: "ok"|"err"; text: string } | null>(null);
  const handleDeleteTrip = async () => {
    if (!selectedTripId) {
      setDelTripMsg({ type: "err", text: "Selecione uma viagem." });
      setTimeout(() => setDelTripMsg(null), 3000);
      return;
    }
    try {
      await excluirViagem(selectedTripId);
      setTrips(prev => prev.filter(t => t.id !== selectedTripId));
      setSelectedTripId(null);
      setDelTripMsg({ type: "ok", text: "Viagem removida com sucesso!" });
      setTimeout(() => setDelTripMsg(null), 3000);
    } catch (err) {
      setDelTripMsg({ type: "err", text: "Erro ao excluir viagem." });
      console.error(err);
    }
  };

  // Abastecimentos
  const [selectedAbastId, setSelectedAbastId] = useState<number | null>(null);
  const [delAbastMsg, setDelAbastMsg] = useState<{ type: "ok"|"err"; text: string } | null>(null);
  const handleDeleteAbastecimento = async () => {
    if (!selectedAbastId) {
      setDelAbastMsg({ type: "err", text: "Selecione um abastecimento." });
      setTimeout(() => setDelAbastMsg(null), 3000);
      return;
    }
    try {
      await excluirAbastecimento(selectedAbastId);
      setAbastecimentos(prev => prev.filter(a => a.id !== selectedAbastId));
      setSelectedAbastId(null);
      setDelAbastMsg({ type: "ok", text: "Abastecimento removido com sucesso!" });
      setTimeout(() => setDelAbastMsg(null), 3000);
    } catch (err) {
      setDelAbastMsg({ type: "err", text: "Erro ao excluir abastecimento." });
      console.error(err);
    }
  };

  // ── Vehicle form ───────────────────────────────────────────────────
  const emptyVehicle = { placa: "", modelo: "", ano: "", km: "", tipo: "Carro", status: "Disponível" };
  const [vForm, setVForm] = useState(emptyVehicle);
  const [vMsg, setVMsg] = useState<{ type: "ok" | "err"; text: string } | null>(null);

  function submitVehicle(e: React.FormEvent) {
    e.preventDefault();
    if (!vForm.placa || !vForm.modelo || !vForm.ano) {
      setVMsg({ type: "err", text: "Preencha todos os campos obrigatórios (*)." });
      return;
    }
    const novoVeiculo = {
      placa: vForm.placa,
      modelo: vForm.modelo,
      ano: Number(vForm.ano),
      quilometragemAtual: Number(vForm.km) || 0,
      statusVeiculo: statusFrontToBack(vForm.status),
      tipo: vForm.tipo,
    };
    criarVeiculo(novoVeiculo)
      .then(data => {
        const newVehicle: Vehicle = {
          id: Date.now() + Math.random(),
          placa: data.placa,
          modelo: data.modelo,
          ano: String(data.ano),
          km: String(data.quilometragemAtual || 0),
          tipo: data.tipo,
          status: statusBackToFront(data.statusVeiculo),
        };
        setVehicles(prev => [...prev, newVehicle]);
        setVForm(emptyVehicle);
        setVMsg({ type: "ok", text: "Veículo cadastrado com sucesso!" });
        setTimeout(() => setVMsg(null), 3000);
      })
      .catch(err => {
        setVMsg({ type: "err", text: "Erro ao cadastrar veículo. Verifique a placa." });
        console.error(err);
      });
  }

  // ── Driver form ────────────────────────────────────────────────────
  const emptyDriver = { nome: "", cpf: "", cnh: "", categoria: "B", telefone: "", status: "Ativo" };
  const [dForm, setDForm] = useState(emptyDriver);
  const [dMsg, setDMsg] = useState<{ type: "ok" | "err"; text: string } | null>(null);

  function submitDriver(e: React.FormEvent) {
    e.preventDefault();
    if (!dForm.nome || !dForm.cpf) {
      setDMsg({ type: "err", text: "Preencha todos os campos obrigatórios (*)." });
      return;
    }
    const cpfLimpo = dForm.cpf.replace(/[.\-]/g, '');
    const novoMotorista = {
      cpf: cpfLimpo,
      nome: dForm.nome,
      categoriaCnh: dForm.categoria,
    };
    criarMotorista(novoMotorista)
      .then(data => {
        const newDriver: Driver = {
          id: Date.now() + Math.random(),
          nome: data.nome,
          cpf: data.cpf,
          cnh: dForm.cnh,
          categoria: data.categoriaCnh,
          telefone: dForm.telefone,
          status: dForm.status,
        };
        setDrivers(prev => [...prev, newDriver]);
        setDForm(emptyDriver);
        setDMsg({ type: "ok", text: "Motorista cadastrado com sucesso!" });
        setTimeout(() => setDMsg(null), 3000);
      })
      .catch(err => {
        setDMsg({ type: "err", text: "Erro ao cadastrar motorista. CPF já existe?" });
        console.error(err);
      });
  }

  // ── Trip form ──────────────────────────────────────────────────────
  const emptyTrip = { origem: "", destino: "", distancia: "", saida: "", chegada: "", veiculoId: 0, motoristaId: 0, passageiros: "", observacoes: "", status: "Em Andamento", km: "" };
  const [tForm, setTForm] = useState(emptyTrip);
  const [tMsg, setTMsg] = useState<{ type: "ok" | "err"; text: string } | null>(null);
  const [cTripId, setCTripId] = useState(0);
  const [cMsg, setCMsg] = useState<{ type: "ok" | "err"; text: string } | null>(null);

  function submitTrip(e: React.FormEvent) {
    e.preventDefault();
    if (!tForm.saida || !tForm.veiculoId || !tForm.motoristaId) {
      setTMsg({ type: "err", text: "Preencha data de saída, veículo e motorista (*)." });
      return;
    }
    const veiculo = vehicles.find(v => v.id === tForm.veiculoId);
    const motorista = drivers.find(d => d.id === tForm.motoristaId);
    if (!veiculo || !motorista) {
      setTMsg({ type: "err", text: "Veículo ou motorista não encontrado." });
      return;
    }
    const novaViagem = {
      dataSaida: new Date(tForm.saida).toISOString(),
      kmInicial: Number(veiculo.km) || 0,
      status: tForm.status === 'Em Andamento' ? 'EM_ANDAMENTO' : 'FINALIZADA',
      observacao: tForm.observacoes,
      placaVeiculo: veiculo.placa,
      cpfMotorista: motorista.cpf,
    };
    criarViagem(novaViagem)
      .then(data => {
        const newTrip: Trip = {
          id: data.id || Date.now() + Math.random(),
          origem: tForm.origem,
          destino: tForm.destino,
          distancia: tForm.distancia,
          saida: tForm.saida,
          chegada: tForm.chegada,
          veiculoId: tForm.veiculoId,
          motoristaId: tForm.motoristaId,
          passageiros: tForm.passageiros,
          observacoes: tForm.observacoes,
          status: tForm.status,
          km: tForm.km,
        };
        setTrips(prev => [...prev, newTrip]);
        // Atualizar status do veículo para "Em Rota"
        setVehicles(prev =>
          prev.map(v =>
            v.id === tForm.veiculoId
              ? { ...v, status: "Em Rota" }
              : v
          )
        );
        setTForm(emptyTrip);
        setTMsg({ type: "ok", text: "Viagem cadastrada com sucesso!" });
        setTimeout(() => setTMsg(null), 3000);
      })
      .catch(err => {
        setTMsg({ type: "err", text: "Erro ao cadastrar viagem." });
        console.error(err);
      });
  }

  function concluirViagem() {
    if (!cTripId) { setCMsg({ type: "err", text: "Selecione uma viagem." }); return; }
    const kmFinal = prompt("Informe a quilometragem final:");
    if (kmFinal === null) return;
    const kmNum = Number(kmFinal);
    if (isNaN(kmNum) || kmNum <= 0) {
      setCMsg({ type: "err", text: "KM final inválido." });
      return;
    }
    encerrarViagem(cTripId, kmNum, "")
      .then(() => {
        // Encontrar veículo associado e atualizar para "Disponível"
        const trip = trips.find(t => t.id === cTripId);
        if (trip) {
          setVehicles(prev =>
            prev.map(v =>
              v.id === trip.veiculoId
                ? { ...v, status: "Disponível" }
                : v
            )
          );
        }
        setTrips(prev => prev.map(t => t.id === cTripId ? { ...t, status: "Concluída" } : t));
        setCTripId(0);
        setCMsg({ type: "ok", text: "Viagem concluída com sucesso!" });
        setTimeout(() => setCMsg(null), 3000);
      })
      .catch(err => {
        setCMsg({ type: "err", text: "Erro ao concluir viagem." });
        console.error(err);
      });
  }

  const activeVehicles = vehicles.filter(v => v.status === "Disponível");
  const activeDrivers = drivers.filter(d => d.status === "Ativo");

  // ── Maintenance ──────────────────────────────────────────────────────
  const [mVeiculoId, setMVeiculoId] = useState(0);
  const [mObs, setMObs] = useState("");
  const [mMsg, setMMsg] = useState<{ type: "ok" | "err"; text: string } | null>(null);

  function enviarManutencao() {
    if (!mVeiculoId) { setMMsg({ type: "err", text: "Selecione um veículo." }); return; }
    const veiculo = vehicles.find(v => v.id === mVeiculoId);
    if (!veiculo) return;
    atualizarStatusVeiculo(veiculo.placa, "MANUTENCAO")
      .then(data => {
        setVehicles(prev => prev.map(v => v.placa === data.placa ? { ...v, status: statusBackToFront(data.statusVeiculo) } : v));
        setMVeiculoId(0); setMObs("");
        setMMsg({ type: "ok", text: "Veículo enviado para manutenção!" });
        setTimeout(() => setMMsg(null), 3000);
      })
      .catch(err => {
        setMMsg({ type: "err", text: "Erro ao enviar para manutenção." });
        console.error(err);
      });
  }

  function despachar(id: number) {
    const veiculo = vehicles.find(v => v.id === id);
    if (!veiculo) return;
    atualizarStatusVeiculo(veiculo.placa, "DISPONIVEL")
      .then(data => {
        setVehicles(prev => prev.map(v => v.placa === data.placa ? { ...v, status: statusBackToFront(data.statusVeiculo) } : v));
        setMVeiculoId(0); setMObs("");
        setMMsg({ type: "ok", text: "Veículo despachado com sucesso!" });
        setTimeout(() => setMMsg(null), 3000);
      })
      .catch(err => {
        setMMsg({ type: "err", text: "Erro ao despachar veículo." });
        console.error(err);
      });
  }

  // ── Maintenance types ──────────────────────────────────────────────
  const [mtForm, setMtForm] = useState({ descricao: "", kmRef: "" });
  const [mtMsg, setMtMsg] = useState<{ type: "ok" | "err"; text: string } | null>(null);

  function submitMaintType(e: React.FormEvent) {
    e.preventDefault();
    if (!mtForm.descricao) { setMtMsg({ type: "err", text: "Informe a descrição." }); return; }
    const novaManutencao = {
      descricao: mtForm.descricao,
      kmReferencia: Number(mtForm.kmRef) || 0,
    };
    criarManutencao(novaManutencao)
      .then(data => {
        setMaintTypes(prev => [...prev, {
          id: data.id || Date.now() + Math.random(),
          descricao: data.descricao,
          kmRef: String(data.kmReferencia || 0),
        }]);
        setMtForm({ descricao: "", kmRef: "" });
        setMtMsg({ type: "ok", text: "Tipo de manutenção cadastrado!" });
        setTimeout(() => setMtMsg(null), 3000);
      })
      .catch(err => {
        setMtMsg({ type: "err", text: "Erro ao cadastrar tipo de manutenção." });
        console.error(err);
      });
  }

  // Função para deletar tipo de manutenção
  const handleDeleteMaintType = async (id: number) => {
    try {
      await excluirManutencao(id);
      setMaintTypes(prev => prev.filter(x => x.id !== id));
      setMtMsg({ type: "ok", text: "Tipo de manutenção removido com sucesso!" });
      setTimeout(() => setMtMsg(null), 3000);
    } catch (err) {
      setMtMsg({ type: "err", text: "Erro ao remover tipo de manutenção. Verifique se ele não está em uso." });
      console.error(err);
    }
  };

  // ── Abastecimento form ──────────────────────────────────────────────
  const emptyAbast = { placaVeiculo: "", data: "", litros: "", combustivel: "Diesel", custo: "", observacoes: "" };
  const [aForm, setAForm] = useState(emptyAbast);
  const [aMsg, setAMsg] = useState<{ type: "ok" | "err"; text: string } | null>(null);

  function submitAbastecimento(e: React.FormEvent) {
    e.preventDefault();
    if (!aForm.placaVeiculo || !aForm.data || !aForm.litros || !aForm.custo) {
      setAMsg({ type: "err", text: "Preencha veículo, data, litros e custo (*)." });
      return;
    }

    const dataOnly = new Date(aForm.data).toISOString().split('T')[0];

    const novoAbast: AbastecimentoDTO = {
      placaVeiculo: aForm.placaVeiculo,
      data: dataOnly,
      litro: Number(aForm.litros),
      tipo: aForm.combustivel,
      preco: Number(aForm.custo),
      obs: aForm.observacoes || '',
    };
    criarAbastecimento(novoAbast)
      .then(data => {
        const newAbast: Abastecimento = {
          id: data.id || Date.now() + Math.random(),
          veiculoId: 0,
          placaVeiculo: data.placaVeiculo,
          data: data.data,
          litros: String(data.litro),
          combustivel: data.tipo,
          custo: String(data.preco),
          observacoes: data.obs || '',
        };
        setAbastecimentos(prev => [...prev, newAbast]);
        setAForm(emptyAbast);
        setAMsg({ type: "ok", text: "Abastecimento registrado com sucesso!" });
        setTimeout(() => setAMsg(null), 3000);
      })
      .catch(err => {
        const msg = err.response?.data?.message || "Erro ao registrar abastecimento.";
        setAMsg({ type: "err", text: msg });
        console.error(err);
      });
  }

  // ── Renderização ──────────────────────────────────────────────────
  if (loading) return <div style={{ display: 'flex', justifyContent: 'center', padding: 40 }}>Carregando dados...</div>;
  if (error) return <div style={{ color: 'red', padding: 40 }}>{error}</div>;

  if (!role) {
    return <LoginScreen onLogin={setRole} />;
  }

  return (
    <>
      <style>{styles}</style>
      <div className="layout">
        <aside className="sidebar">
          <div className="sidebar-logo">
            <span className="sidebar-icon">🚌</span>
            <div>
              <div className="sidebar-title">TransportControl</div>
              <div className="sidebar-sub">Gestão de Frota</div>
            </div>
          </div>

          <nav className="sidebar-nav">
            <button className={`nav-item ${tab === "painel" ? "active" : ""}`} onClick={() => setTab("painel")}>
              <span>📋</span> Painel Geral
            </button>
            <button className={`nav-item ${tab === "viagens" ? "active" : ""}`} onClick={() => setTab("viagens")}>
              <span>🗺️</span> Viagens
            </button>
            <button className={`nav-item ${tab === "veiculos" ? "active" : ""}`} onClick={() => setTab("veiculos")}>
              <span>🚗</span> Veículos
            </button>
            <button className={`nav-item ${tab === "motoristas" ? "active" : ""}`} onClick={() => setTab("motoristas")}>
              <span>👤</span> Motoristas
            </button>

            {role === "gerente" && (
              <>
                <button className={`nav-item ${tab === "abastecimentos" ? "active" : ""}`} onClick={() => setTab("abastecimentos")}>
                  <span>⛽</span> Abastecimentos
                </button>
                <button className={`nav-item ${tab === "manutencao" ? "active" : ""}`} onClick={() => setTab("manutencao")}>
                  <span>🔧</span> Manutenção
                </button>
              </>
            )}
          </nav>

          <div className="sidebar-footer">
            <div className="sidebar-stats">
              <div className="stat-row"><span>Viagens</span><strong>{trips.length}</strong></div>
              <div className="stat-row"><span>Veículos</span><strong>{vehicles.length}</strong></div>
              <div className="stat-row"><span>Motoristas</span><strong>{drivers.length}</strong></div>
              {role === "gerente" && <div className="stat-row"><span>Manutenção</span><strong>{vehicles.filter(v => v.status === "Manutenção").length}</strong></div>}
            </div>
            <div className="sidebar-user">
              <span className={`role-badge ${role}`}>{role === "gerente" ? "Gerente" : "Operador"}</span>
              <button className="btn-logout" onClick={() => { setRole(null); setTab("painel"); }}>Sair</button>
            </div>
          </div>
        </aside>

        <main className="main">
          {tab === "painel" && (
            <div className="page">
              <div className="page-header">
                <h1 className="page-title">Painel Geral</h1>
                <p className="page-sub">Visão geral da operação</p>
              </div>
              <div className="kpi-grid">
                <KpiCard icon="🗺️" label="Total de Viagens" value={trips.length} color="blue" />
                <KpiCard icon="🔄" label="Em Andamento" value={trips.filter(t => t.status === "Em Andamento").length} color="orange" />
                <KpiCard icon="🚗" label="Veículos" value={vehicles.length} color="blue" />
                <KpiCard icon="👤" label="Motoristas" value={drivers.length} color="purple" />
              </div>
              <div className="two-col">
                <div className="card">
                  <div className="card-header">
                    <h2 className="card-title">Últimas Viagens</h2>
                    <button className="btn-link" onClick={() => setTab("viagens")}>Ver todas →</button>
                  </div>
                  {trips.length === 0 ? <p className="empty">Nenhuma viagem cadastrada.</p> : (
                    <div className="table-wrap">
                      <table className="table">
                        <thead><tr><th>Data de Saída</th><th>Distância</th><th>Veículo</th><th>Motorista</th></tr></thead>
                        <tbody>
                          {trips.slice().reverse().slice(0, 5).map(t => {
                            const v = vehicles.find(x => x.id === t.veiculoId);
                            const m = drivers.find(x => x.id === t.motoristaId);
                            return (
                              <tr key={t.id}>
                                <td className="mono">{fmt(t.saida)}</td>
                                <td className="mono">{t.km ? Number(t.km).toLocaleString("pt-BR") + " km" : "—"}</td>
                                <td>{v ? <span className="placa">{v.placa}</span> : "—"}</td>
                                <td>{m ? m.nome.split(" ").slice(0, 2).join(" ") : "—"}</td>
                              </tr>
                            );
                          })}
                        </tbody>
                      </table>
                    </div>
                  )}
                </div>
                <div style={{ display: "flex", flexDirection: "column", gap: 16 }}>
                  <div className="card">
                    <div className="card-header">
                      <h2 className="card-title">Frota</h2>
                      <button className="btn-link" onClick={() => setTab("veiculos")}>Ver todos →</button>
                    </div>
                    <VehicleTable vehicles={vehicles} compact readonly />
                  </div>
                  <div className="card">
                    <div className="card-header">
                      <h2 className="card-title">Motoristas</h2>
                      <button className="btn-link" onClick={() => setTab("motoristas")}>Ver todos →</button>
                    </div>
                    <DriverTable drivers={drivers} compact readonly />
                  </div>
                </div>
              </div>
            </div>
          )}

          {tab === "viagens" && (
            <div className="page">
              <div className="page-header">
                <h1 className="page-title">Viagens</h1>
                <p className="page-sub">Cadastre e acompanhe as viagens da frota</p>
              </div>

              <div className="card">
                <h2 className="card-title" style={{ marginBottom: 20 }}>Cadastrar Nova Viagem</h2>
                <form className="form-grid" onSubmit={submitTrip}>
                  <Field label="Data e Hora de Saída *">
                    <input className="input" type="datetime-local" value={tForm.saida} onChange={e => setTForm(f => ({ ...f, saida: e.target.value }))} />
                  </Field>
                  <Field label="Distância (km)">
                    <input className="input" placeholder="Ex: 320" type="number" min="0" value={tForm.km} onChange={e => setTForm(f => ({ ...f, km: e.target.value }))} />
                  </Field>
                  <Field label="Veículo *">
                    <select className="input" value={tForm.veiculoId || ""} onChange={e => setTForm(f => ({ ...f, veiculoId: Number(e.target.value) }))}>
                      <option value="">— Selecione —</option>
                      {activeVehicles.map(v => <option key={v.id} value={v.id}>{v.placa} — {v.modelo}</option>)}
                    </select>
                  </Field>
                  <Field label="Motorista *">
                    <select className="input" value={tForm.motoristaId || ""} onChange={e => setTForm(f => ({ ...f, motoristaId: Number(e.target.value) }))}>
                      <option value="">— Selecione —</option>
                      {activeDrivers.map(d => <option key={d.id} value={d.id}>{d.nome}</option>)}
                    </select>
                  </Field>
                  <Field label="Status" wide>
                    <select className="input" value={tForm.status} onChange={e => setTForm(f => ({ ...f, status: e.target.value }))}>
                      <option value="Em Andamento">Em Andamento</option>
                      <option value="Concluída">Concluída</option>
                    </select>
                  </Field>
                  <Field label="Observações" wide>
                    <input className="input" placeholder="Informações adicionais..." value={tForm.observacoes} onChange={e => setTForm(f => ({ ...f, observacoes: e.target.value }))} />
                  </Field>
                  <div className="form-actions">
                    <button type="submit" className="btn-primary">Cadastrar Viagem</button>
                    <button type="button" className="btn-ghost" onClick={() => setTForm(emptyTrip)}>Limpar</button>
                  </div>
                </form>
                {tMsg && <div className={`alert ${tMsg.type === "ok" ? "alert-ok" : "alert-err"}`}>{tMsg.text}</div>}
              </div>

              <div className="card" style={{ marginTop: 20 }}>
                <h2 className="card-title" style={{ marginBottom: 16 }}>Concluir Viagem</h2>
                <div className="form-grid">
                  <Field label="Viagem em Andamento" wide>
                    <select className="input" value={cTripId || ""} onChange={e => setCTripId(Number(e.target.value))}>
                      <option value="">— Selecione —</option>
                      {trips.filter(t => t.status === "Em Andamento").map(t => {
                        const v = vehicles.find(x => x.id === t.veiculoId);
                        const m = drivers.find(x => x.id === t.motoristaId);
                        return (
                          <option key={t.id} value={t.id}>
                            {fmt(t.saida)} — {v ? v.placa : "?"} / {m ? m.nome.split(" ")[0] : "?"}
                          </option>
                        );
                      })}
                    </select>
                  </Field>
                  <div className="form-actions">
                    <button type="button" className="btn-primary" onClick={concluirViagem}>Concluir Viagem</button>
                  </div>
                </div>
                {cMsg && <div className={`alert ${cMsg.type === "ok" ? "alert-ok" : "alert-err"}`}>{cMsg.text}</div>}
              </div>

              <div className="card" style={{ marginTop: 20 }}>
                <h2 className="card-title" style={{ marginBottom: 16 }}>Viagens Cadastradas <span className="badge-count">{trips.length}</span></h2>
                {trips.length === 0 ? <p className="empty">Nenhuma viagem cadastrada ainda.</p> : (
                  <div className="table-wrap">
                    <table className="table">
                      <thead><tr><th>Data de Saída</th><th>Distância</th><th>Veículo</th><th>Motorista</th><th>Status</th><th>Obs.</th></tr></thead>
                      <tbody>
                        {trips.map(t => {
                          const v = vehicles.find(x => x.id === t.veiculoId);
                          const m = drivers.find(x => x.id === t.motoristaId);
                          return (
                            <tr key={t.id}>
                              <td className="mono">{fmt(t.saida)}</td>
                              <td className="mono">{t.km ? Number(t.km).toLocaleString("pt-BR") + " km" : "—"}</td>
                              <td>{v ? <span className="placa">{v.placa}</span> : "—"}</td>
                              <td>{m ? m.nome.split(" ").slice(0, 2).join(" ") : "—"}</td>
                              <td><span className={`badge ${tripBadge(t.status)}`}>{t.status}</span></td>
                              <td style={{ maxWidth: 180, overflow: "hidden", textOverflow: "ellipsis", whiteSpace: "nowrap" }}>{t.observacoes || "—"}</td>
                            </tr>
                          );
                        })}
                      </tbody>
                    </table>
                  </div>
                )}
              </div>

              {/* Seletor de exclusão de viagens */}
              <div className="card" style={{ marginTop: 20 }}>
                <h2 className="card-title" style={{ marginBottom: 16 }}>Remover Viagem</h2>
                <div className="form-grid">
                  <Field label="Selecione uma viagem" wide>
                    <select className="input" value={selectedTripId || ""} onChange={e => setSelectedTripId(Number(e.target.value))}>
                      <option value="">— Selecione —</option>
                      {trips.map(t => {
                        const v = vehicles.find(x => x.id === t.veiculoId);
                        return (
                          <option key={t.id} value={t.id}>
                            {fmt(t.saida)} — {v ? v.placa : "?"} - {t.status}
                          </option>
                        );
                      })}
                    </select>
                  </Field>
                  <div className="form-actions">
                    <button className="btn-primary" onClick={handleDeleteTrip}>Remover Viagem</button>
                  </div>
                </div>
                {delTripMsg && <div className={`alert ${delTripMsg.type === "ok" ? "alert-ok" : "alert-err"}`}>{delTripMsg.text}</div>}
              </div>
            </div>
          )}

          {tab === "veiculos" && (
            <div className="page">
              <div className="page-header">
                <h1 className="page-title">Veículos</h1>
                <p className="page-sub">Cadastre e gerencie sua frota</p>
              </div>
              <div className="card">
                <h2 className="card-title" style={{ marginBottom: 20 }}>Cadastrar Novo Veículo</h2>
                <form className="form-grid" onSubmit={submitVehicle}>
                  <Field label="Placa *">
                    <input className="input" placeholder="ABC-1234" value={vForm.placa} onChange={e => setVForm(f => ({ ...f, placa: e.target.value.toUpperCase() }))} />
                  </Field>
                  <Field label="Modelo *">
                    <input className="input" placeholder="Ex: Sprinter 415" value={vForm.modelo} onChange={e => setVForm(f => ({ ...f, modelo: e.target.value }))} />
                  </Field>
                  <Field label="Ano *">
                    <input className="input" placeholder="2024" maxLength={4} value={vForm.ano} onChange={e => setVForm(f => ({ ...f, ano: e.target.value }))} />
                  </Field>
                  <Field label="KM atual">
                    <input className="input" placeholder="Ex: 48200" type="number" min="0" value={vForm.km} onChange={e => setVForm(f => ({ ...f, km: e.target.value }))} />
                  </Field>
                  <Field label="Tipo">
                    <select className="input" value={vForm.tipo} onChange={e => setVForm(f => ({ ...f, tipo: e.target.value }))}>
                      {["Carro", "Van", "Caminhão"].map(t => <option key={t}>{t}</option>)}
                    </select>
                  </Field>
                  <Field label="Status">
                    <select className="input" value={vForm.status} onChange={e => setVForm(f => ({ ...f, status: e.target.value }))}>
                      {["Disponível", "Em Rota", "Manutenção"].map(s => <option key={s}>{s}</option>)}
                    </select>
                  </Field>
                  <div className="form-actions">
                    <button type="submit" className="btn-primary">Cadastrar Veículo</button>
                    <button type="button" className="btn-ghost" onClick={() => setVForm(emptyVehicle)}>Limpar</button>
                  </div>
                </form>
                {vMsg && <div className={`alert ${vMsg.type === "ok" ? "alert-ok" : "alert-err"}`}>{vMsg.text}</div>}
              </div>
              <div className="card" style={{ marginTop: 20 }}>
                <h2 className="card-title" style={{ marginBottom: 16 }}>Veículos Cadastrados <span className="badge-count">{vehicles.length}</span></h2>
                <VehicleTable vehicles={vehicles} />
              </div>

              {/* Seletor de exclusão de veículos */}
              <div className="card" style={{ marginTop: 20 }}>
                <h2 className="card-title" style={{ marginBottom: 16 }}>Remover Veículo</h2>
                <div className="form-grid">
                  <Field label="Selecione um veículo" wide>
                    <select className="input" value={selectedVehicleId || ""} onChange={e => setSelectedVehicleId(Number(e.target.value))}>
                      <option value="">— Selecione —</option>
                      {vehicles.map(v => (
                        <option key={v.id} value={v.id}>{v.placa} — {v.modelo} ({v.status})</option>
                      ))}
                    </select>
                  </Field>
                  <div className="form-actions">
                    <button className="btn-primary" onClick={handleDeleteVehicle}>Remover Veículo</button>
                  </div>
                </div>
                {delVehicleMsg && <div className={`alert ${delVehicleMsg.type === "ok" ? "alert-ok" : "alert-err"}`}>{delVehicleMsg.text}</div>}
              </div>
            </div>
          )}

          {tab === "motoristas" && (
            <div className="page">
              <div className="page-header">
                <h1 className="page-title">Motoristas</h1>
                <p className="page-sub">Cadastre e gerencie seus motoristas</p>
              </div>
              <div className="card">
                <h2 className="card-title" style={{ marginBottom: 20 }}>Cadastrar Novo Motorista</h2>
                <form className="form-grid" onSubmit={submitDriver}>
                  <Field label="Nome Completo *" wide>
                    <input className="input" placeholder="Nome do motorista" value={dForm.nome} onChange={e => setDForm(f => ({ ...f, nome: e.target.value }))} />
                  </Field>
                  <Field label="CPF *">
                    <input className="input" placeholder="000.000.000-00" value={dForm.cpf} onChange={e => setDForm(f => ({ ...f, cpf: e.target.value }))} />
                  </Field>
                  <Field label="Categoria CNH *">
                    <select className="input" value={dForm.categoria} onChange={e => setDForm(f => ({ ...f, categoria: e.target.value }))}>
                      {["B", "C", "D", "E"].map(c => <option key={c}>{c}</option>)}
                    </select>
                  </Field>
                  <div className="form-actions">
                    <button type="submit" className="btn-primary">Cadastrar Motorista</button>
                    <button type="button" className="btn-ghost" onClick={() => setDForm(emptyDriver)}>Limpar</button>
                  </div>
                </form>
                {dMsg && <div className={`alert ${dMsg.type === "ok" ? "alert-ok" : "alert-err"}`}>{dMsg.text}</div>}
              </div>
              <div className="card" style={{ marginTop: 20 }}>
                <h2 className="card-title" style={{ marginBottom: 16 }}>Motoristas Cadastrados <span className="badge-count">{drivers.length}</span></h2>
                <DriverTable drivers={drivers} />
              </div>

              {/* Seletor de exclusão de motoristas */}
              <div className="card" style={{ marginTop: 20 }}>
                <h2 className="card-title" style={{ marginBottom: 16 }}>Remover Motorista</h2>
                <div className="form-grid">
                  <Field label="Selecione um motorista" wide>
                    <select className="input" value={selectedDriverId || ""} onChange={e => setSelectedDriverId(Number(e.target.value))}>
                      <option value="">— Selecione —</option>
                      {drivers.map(d => (
                        <option key={d.id} value={d.id}>{d.nome} - {d.cpf}</option>
                      ))}
                    </select>
                  </Field>
                  <div className="form-actions">
                    <button className="btn-primary" onClick={handleDeleteDriver}>Remover Motorista</button>
                  </div>
                </div>
                {delDriverMsg && <div className={`alert ${delDriverMsg.type === "ok" ? "alert-ok" : "alert-err"}`}>{delDriverMsg.text}</div>}
              </div>
            </div>
          )}

          {tab === "abastecimentos" && role === "gerente" && (
            <div className="page">
              <div className="page-header">
                <h1 className="page-title">Abastecimentos</h1>
                <p className="page-sub">Registre e acompanhe os abastecimentos da frota</p>
              </div>
              <div className="card">
                <h2 className="card-title" style={{ marginBottom: 20 }}>Registrar Abastecimento</h2>
                <form className="form-grid" onSubmit={submitAbastecimento}>
                  <Field label="Veículo *">
                    <select className="input" value={aForm.placaVeiculo} onChange={e => setAForm(f => ({ ...f, placaVeiculo: e.target.value }))}>
                      <option value="">— Selecione —</option>
                      {vehicles.map(v => <option key={v.id} value={v.placa}>{v.placa} — {v.modelo}</option>)}
                    </select>
                  </Field>
                  <Field label="Data *">
                    <input className="input" type="date" value={aForm.data} onChange={e => setAForm(f => ({ ...f, data: e.target.value }))} />
                  </Field>
                  <Field label="Litros *">
                    <input className="input" placeholder="Ex: 50" type="number" min="0" step="0.01" value={aForm.litros} onChange={e => setAForm(f => ({ ...f, litros: e.target.value }))} />
                  </Field>
                  <Field label="Combustível">
                    <select className="input" value={aForm.combustivel} onChange={e => setAForm(f => ({ ...f, combustivel: e.target.value }))}>
                      {["Diesel", "Gasolina", "Etanol", "GNV"].map(c => <option key={c}>{c}</option>)}
                    </select>
                  </Field>
                  <Field label="Custo (R$) *">
                    <input className="input" placeholder="Ex: 300.50" type="number" min="0" step="0.01" value={aForm.custo} onChange={e => setAForm(f => ({ ...f, custo: e.target.value }))} />
                  </Field>
                  <Field label="Observações" wide>
                    <input className="input" placeholder="Posto, nota fiscal, etc." value={aForm.observacoes} onChange={e => setAForm(f => ({ ...f, observacoes: e.target.value }))} />
                  </Field>
                  <div className="form-actions">
                    <button type="submit" className="btn-primary">Registrar Abastecimento</button>
                    <button type="button" className="btn-ghost" onClick={() => setAForm(emptyAbast)}>Limpar</button>
                  </div>
                </form>
                {aMsg && <div className={`alert ${aMsg.type === "ok" ? "alert-ok" : "alert-err"}`}>{aMsg.text}</div>}
              </div>

              <div className="card" style={{ marginTop: 20 }}>
                <h2 className="card-title" style={{ marginBottom: 16 }}>Histórico de Abastecimentos <span className="badge-count">{abastecimentos.length}</span></h2>
                {abastecimentos.length === 0 ? <p className="empty">Nenhum abastecimento registrado.</p> : (
                  <div className="table-wrap">
                    <table className="table">
                      <thead><tr><th>Data</th><th>Veículo</th><th>Litros</th><th>Combustível</th><th>Custo</th><th>Obs.</th></tr></thead>
                      <tbody>
                        {abastecimentos.map(a => (
                          <tr key={a.id}>
                            <td className="mono">{fmt(a.data)}</td>
                            <td><span className="placa">{a.placaVeiculo}</span></td>
                            <td className="mono">{Number(a.litros).toFixed(2)} L</td>
                            <td>{a.combustivel}</td>
                            <td className="mono">R$ {Number(a.custo).toFixed(2)}</td>
                            <td style={{ maxWidth: 150, overflow: "hidden", textOverflow: "ellipsis", whiteSpace: "nowrap" }}>{a.observacoes || "—"}</td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                )}
              </div>

              {/* Seletor de exclusão de abastecimentos */}
              <div className="card" style={{ marginTop: 20 }}>
                <h2 className="card-title" style={{ marginBottom: 16 }}>Remover Abastecimento</h2>
                <div className="form-grid">
                  <Field label="Selecione um abastecimento" wide>
                    <select className="input" value={selectedAbastId || ""} onChange={e => setSelectedAbastId(Number(e.target.value))}>
                      <option value="">— Selecione —</option>
                      {abastecimentos.map(a => (
                        <option key={a.id} value={a.id}>
                          {fmt(a.data)} — {a.placaVeiculo} - R$ {Number(a.custo).toFixed(2)}
                        </option>
                      ))}
                    </select>
                  </Field>
                  <div className="form-actions">
                    <button className="btn-primary" onClick={handleDeleteAbastecimento}>Remover Abastecimento</button>
                  </div>
                </div>
                {delAbastMsg && <div className={`alert ${delAbastMsg.type === "ok" ? "alert-ok" : "alert-err"}`}>{delAbastMsg.text}</div>}
              </div>
            </div>
          )}

          {tab === "manutencao" && role === "gerente" && (
            <div className="page">
              <div className="page-header">
                <h1 className="page-title">Manutenção</h1>
                <p className="page-sub">Gerencie veículos em manutenção e tipos de serviço</p>
              </div>

              <div className="kpi-grid" style={{ gridTemplateColumns: "repeat(3,1fr)" }}>
                <KpiCard icon="🔧" label="Em Manutenção" value={vehicles.filter(v => v.status === "Manutenção").length} color="orange" />
                <KpiCard icon="✅" label="Disponíveis" value={vehicles.filter(v => v.status === "Disponível").length} color="green" />
                <KpiCard icon="🚗" label="Em Rota" value={vehicles.filter(v => v.status === "Em Rota").length} color="blue" />
              </div>

              <div className="two-col" style={{ marginBottom: 20 }}>
                <div className="card" style={{ display: "flex", flexDirection: "column", gap: 20 }}>
                  <div>
                    <h2 className="card-title" style={{ marginBottom: 14 }}>Enviar para Manutenção</h2>
                    <div style={{ display: "flex", gap: 10, alignItems: "flex-end" }}>
                      <div className="field" style={{ flex: 1 }}>
                        <label className="field-label">Veículo *</label>
                        <select className="input" value={mVeiculoId || ""} onChange={e => setMVeiculoId(Number(e.target.value))}>
                          <option value="">— Selecione —</option>
                          {vehicles.filter(v => v.status !== "Manutenção").map(v => (
                            <option key={v.id} value={v.id}>{v.placa} — {v.modelo} ({v.status})</option>
                          ))}
                        </select>
                      </div>
                      <button className="btn-primary" onClick={enviarManutencao}>Enviar</button>
                    </div>
                  </div>
                  <hr style={{ border: "none", borderTop: "1px solid #E5E7EB" }} />
                  <div>
                    <h2 className="card-title" style={{ marginBottom: 14 }}>Despachar da Manutenção</h2>
                    <div style={{ display: "flex", flexDirection: "column", gap: 10 }}>
                      <div className="field">
                        <label className="field-label">Veículo *</label>
                        <select className="input" value={mVeiculoId || ""} onChange={e => setMVeiculoId(Number(e.target.value))}>
                          <option value="">— Selecione —</option>
                          {vehicles.filter(v => v.status === "Manutenção").map(v => (
                            <option key={v.id} value={v.id}>{v.placa} — {v.modelo}</option>
                          ))}
                        </select>
                      </div>
                      <div className="field">
                        <label className="field-label">Observação</label>
                        <input className="input" placeholder="Observação (opcional)" value={mObs} onChange={e => setMObs(e.target.value)} />
                      </div>
                      <div>
                        <button className="btn-primary" onClick={() => {
                          if (!mVeiculoId) { setMMsg({ type: "err", text: "Selecione um veículo." }); return; }
                          despachar(mVeiculoId);
                        }}>Despachar</button>
                      </div>
                    </div>
                  </div>
                  {mMsg && <div className={`alert ${mMsg.type === "ok" ? "alert-ok" : "alert-err"}`}>{mMsg.text}</div>}
                </div>

                <div className="card">
                  <h2 className="card-title" style={{ marginBottom: 20 }}>Tipos de Manutenção</h2>
                  <form className="form-grid" style={{ gridTemplateColumns: "1fr 1fr" }} onSubmit={submitMaintType}>
                    <Field label="Descrição *">
                      <input className="input" placeholder="Ex: Troca de óleo" value={mtForm.descricao} onChange={e => setMtForm(f => ({ ...f, descricao: e.target.value }))} />
                    </Field>
                    <Field label="KM de Referência">
                      <input className="input" placeholder="Ex: 5000" type="number" min="0" value={mtForm.kmRef} onChange={e => setMtForm(f => ({ ...f, kmRef: e.target.value }))} />
                    </Field>
                    <div className="form-actions" style={{ gridColumn: "span 2" }}>
                      <button type="submit" className="btn-primary">Cadastrar Tipo</button>
                      <button type="button" className="btn-ghost" onClick={() => setMtForm({ descricao: "", kmRef: "" })}>Limpar</button>
                    </div>
                  </form>
                  {mtMsg && <div className={`alert ${mtMsg.type === "ok" ? "alert-ok" : "alert-err"}`}>{mtMsg.text}</div>}
                  {maintTypes.length > 0 && (
                    <div className="table-wrap" style={{ marginTop: 16 }}>
                      <table className="table">
                        <thead><tr><th>Descrição</th><th>KM Referência</th><th></th></tr></thead>
                        <tbody>
                          {maintTypes.map(t => (
                            <tr key={t.id}>
                              <td><strong>{t.descricao}</strong></td>
                              <td className="mono">{t.kmRef ? Number(t.kmRef).toLocaleString("pt-BR") + " km" : "—"}</td>
                              <td><button className="btn-del" onClick={() => handleDeleteMaintType(t.id)}>✕</button></td>
                            </tr>
                          ))}
                        </tbody>
                      </table>
                    </div>
                  )}
                </div>
              </div>

              <div className="card" style={{ marginBottom: 20 }}>
                <div className="card-header">
                  <h2 className="card-title">Quadro de Necessidade de Manutenção</h2>
                  <span style={{ fontSize: 12, color: "#6B7280" }}>Baseado no KM atual vs. KM de referência</span>
                </div>
                {maintTypes.length === 0 || vehicles.length === 0 ? (
                  <p className="empty">Cadastre tipos de manutenção e veículos para ver o quadro.</p>
                ) : (
                  <div className="table-wrap">
                    <table className="table">
                      <thead><tr><th>Veículo</th><th>Placa</th><th>KM Atual</th><th>Tipo de Manutenção</th><th>KM Ref.</th><th>Situação</th></tr></thead>
                      <tbody>
                        {vehicles.flatMap(v =>
                          maintTypes.map(t => {
                            const kmAtual = Number(v.km) || 0;
                            const kmRef = Number(t.kmRef) || 0;
                            const vencido = kmRef > 0 && kmAtual >= kmRef;
                            const proximo = kmRef > 0 && kmAtual >= kmRef * 0.85 && !vencido;
                            return (
                              <tr key={`${v.id}-${t.id}`}>
                                <td><strong>{v.modelo}</strong></td>
                                <td><span className="placa">{v.placa}</span></td>
                                <td className="mono">{kmAtual ? kmAtual.toLocaleString("pt-BR") + " km" : "—"}</td>
                                <td>{t.descricao}</td>
                                <td className="mono">{kmRef ? kmRef.toLocaleString("pt-BR") + " km" : "—"}</td>
                                <td>
                                  {vencido ? <span className="badge badge-red">⚠ Vencido</span> :
                                   proximo ? <span className="badge badge-yellow">Próximo</span> :
                                   <span className="badge badge-green">Em dia</span>}
                                </td>
                              </tr>
                            );
                          })
                        )}
                      </tbody>
                    </table>
                  </div>
                )}
              </div>

              <div className="card">
                <div className="card-header">
                  <h2 className="card-title">Veículos em Manutenção <span className="badge-count">{vehicles.filter(v => v.status === "Manutenção").length}</span></h2>
                </div>
                {vehicles.filter(v => v.status === "Manutenção").length === 0 ? (
                  <p className="empty">Nenhum veículo em manutenção no momento. ✅</p>
                ) : (
                  <div className="table-wrap">
                    <table className="table">
                      <thead><tr><th>Placa</th><th>Modelo</th><th>Ano</th><th>KM</th><th>Tipo</th><th>Status</th></tr></thead>
                      <tbody>
                        {vehicles.filter(v => v.status === "Manutenção").map(v => (
                          <tr key={v.id}>
                            <td><span className="placa">{v.placa}</span></td>
                            <td><strong>{v.modelo}</strong></td>
                            <td>{v.ano}</td>
                            <td className="mono">{v.km ? Number(v.km).toLocaleString("pt-BR") + " km" : "—"}</td>
                            <td>{v.tipo}</td>
                            <td><span className="badge badge-yellow">Manutenção</span></td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                )}
              </div>
            </div>
          )}
        </main>
      </div>
    </>
  );
}

// ── Sub-components ─────────────────────────────────────────────────────
function KpiCard({ icon, label, value, color }: { icon: string; label: string; value: number; color: string }) {
  return (
    <div className={`kpi-card kpi-${color}`}>
      <div className="kpi-icon">{icon}</div>
      <div className="kpi-value">{value}</div>
      <div className="kpi-label">{label}</div>
    </div>
  );
}

function Field({ label, children, wide }: { label: string; children: React.ReactNode; wide?: boolean }) {
  return (
    <div className={`field ${wide ? "field-wide" : ""}`}>
      <label className="field-label">{label}</label>
      {children}
    </div>
  );
}

function VehicleTable({ vehicles, compact, readonly }: { vehicles: Vehicle[]; compact?: boolean; readonly?: boolean }) {
  if (vehicles.length === 0) return <p className="empty">Nenhum veículo cadastrado ainda.</p>;
  return (
    <div className="table-wrap">
      <table className="table">
        <thead>
          <tr><th>Placa</th><th>Modelo</th>{!compact && <><th>Ano</th><th>KM</th></>}<th>Tipo</th><th>Status</th></tr>
        </thead>
        <tbody>
          {vehicles.map(v => (
            <tr key={v.id}>
              <td><span className="placa">{v.placa}</span></td>
              <td>{v.modelo}</td>
              {!compact && <><td>{v.ano}</td><td className="mono">{v.km ? Number(v.km).toLocaleString("pt-BR") + " km" : "—"}</td></>}
              <td>{v.tipo}</td>
              <td><span className={`badge ${vehicleBadge(v.status)}`}>{v.status}</span></td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

function DriverTable({ drivers, compact, readonly }: { drivers: Driver[]; compact?: boolean; readonly?: boolean }) {
  if (drivers.length === 0) return <p className="empty">Nenhum motorista cadastrado ainda.</p>;
  return (
    <div className="table-wrap">
      <table className="table">
        <thead>
          <tr>{!compact && <th>Nome</th>}<th>CPF</th><th>Categoria</th></tr>
        </thead>
        <tbody>
          {drivers.map(d => (
            <tr key={d.id}>
              {!compact && <td><strong>{d.nome}</strong></td>}
              <td className="mono">{d.cpf}</td>
              <td><span className="cat-badge">{d.categoria}</span></td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

function LoginScreen({ onLogin }: { onLogin: (role: "operador" | "gerente") => void }) {
  const [selected, setSelected] = useState<"operador" | "gerente" | null>(null);
  return (
    <>
      <style>{styles}</style>
      <div className="login-bg">
        <div className="login-card">
          <div className="login-logo">
            <span style={{ fontSize: 40 }}>🚌</span>
            <h1 className="login-title">TransportControl</h1>
            <p className="login-sub">Gestão de Frota</p>
          </div>
          <p className="login-label">Selecione seu perfil de acesso</p>
          <div className="login-roles">
            <button className={`role-card ${selected === "operador" ? "selected" : ""}`} onClick={() => setSelected("operador")}>
              <span className="role-card-icon">👷</span>
              <strong>Operador</strong>
              <span className="role-card-desc">Viagens, Veículos e Motoristas</span>
            </button>
            <button className={`role-card ${selected === "gerente" ? "selected" : ""}`} onClick={() => setSelected("gerente")}>
              <span className="role-card-icon">🧑‍💼</span>
              <strong>Gerente</strong>
              <span className="role-card-desc">Acesso completo incluindo Manutenção</span>
            </button>
          </div>
          <button
            className="btn-primary"
            style={{ width: "100%", padding: "11px", fontSize: 14, opacity: selected ? 1 : 0.45, cursor: selected ? "pointer" : "not-allowed" }}
            disabled={!selected}
            onClick={() => selected && onLogin(selected)}>
            Entrar
          </button>
        </div>
      </div>
    </>
  );
}

// ── Styles (mantido igual) ─────────────────────────────────────────────
const styles = `
  * { box-sizing: border-box; margin: 0; padding: 0; }
  body { font-family: 'Inter', system-ui, sans-serif; background: #F0F2F5; color: #111928; }
  .layout { display: flex; height: 100vh; overflow: hidden; }

  .sidebar { width: 240px; min-width: 240px; background: #111928; display: flex; flex-direction: column; overflow: hidden; }
  .sidebar-logo { display: flex; align-items: center; gap: 12px; padding: 24px 20px 20px; border-bottom: 1px solid rgba(255,255,255,0.08); }
  .sidebar-icon  { font-size: 24px; }
  .sidebar-title { font-size: 15px; font-weight: 700; color: #fff; line-height: 1.2; }
  .sidebar-sub   { font-size: 11px; color: #6B7280; margin-top: 2px; }
  .sidebar-nav   { padding: 16px 12px; flex: 1; display: flex; flex-direction: column; gap: 4px; }
  .nav-item {
    display: flex; align-items: center; gap: 10px; width: 100%; padding: 10px 12px;
    background: none; border: none; border-radius: 8px; font-size: 13px; font-weight: 500;
    color: #9CA3AF; cursor: pointer; text-align: left; transition: background .15s, color .15s;
  }
  .nav-item:hover  { background: rgba(255,255,255,0.06); color: #fff; }
  .nav-item.active { background: #1A56DB; color: #fff; }
  .sidebar-footer { padding: 16px 20px 24px; border-top: 1px solid rgba(255,255,255,0.08); }
  .sidebar-stats  { display: flex; flex-direction: column; gap: 8px; }
  .stat-row { display: flex; justify-content: space-between; font-size: 12px; color: #6B7280; }
  .stat-row strong { color: #D1D5DB; }

  .main  { flex: 1; overflow-y: auto; }
  .page  { padding: 32px 32px 40px; max-width: 1200px; }
  .page-header { margin-bottom: 28px; }
  .page-title  { font-size: 22px; font-weight: 700; color: #111928; }
  .page-sub    { font-size: 13px; color: #6B7280; margin-top: 4px; }

  .kpi-grid { display: grid; grid-template-columns: repeat(4,1fr); gap: 16px; margin-bottom: 24px; }
  .kpi-card { background: #fff; border-radius: 12px; border: 1px solid #E5E7EB; padding: 20px 20px 18px; }
  .kpi-icon  { font-size: 22px; margin-bottom: 10px; }
  .kpi-value { font-size: 34px; font-weight: 700; line-height: 1; margin-bottom: 6px; }
  .kpi-label { font-size: 12px; color: #6B7280; }
  .kpi-blue   .kpi-value { color: #1A56DB; }
  .kpi-green  .kpi-value { color: #0E9F6E; }
  .kpi-purple .kpi-value { color: #7C3AED; }
  .kpi-orange .kpi-value { color: #D97706; }

  .two-col { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }

  .card { background: #fff; border: 1px solid #E5E7EB; border-radius: 12px; padding: 20px 22px; }
  .card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
  .card-title  { font-size: 14px; font-weight: 600; color: #111928; display: flex; align-items: center; gap: 8px; }
  .btn-link    { font-size: 12px; color: #1A56DB; background: none; border: none; cursor: pointer; font-weight: 500; }
  .btn-link:hover { text-decoration: underline; }
  .badge-count {
    display: inline-flex; align-items: center; justify-content: center;
    background: #EBF5FF; color: #1A56DB; font-size: 11px; font-weight: 600;
    padding: 1px 8px; border-radius: 999px;
  }

  .form-grid { display: grid; grid-template-columns: repeat(3,1fr); gap: 16px 20px; align-items: end; }
  .field      { display: flex; flex-direction: column; gap: 6px; }
  .field-wide { grid-column: span 2; }
  .field-label { font-size: 12px; font-weight: 500; color: #374151; }
  .input {
    width: 100%; padding: 9px 12px; border: 1px solid #D1D5DB; border-radius: 8px;
    font-size: 13px; color: #111928; background: #fff; outline: none;
    transition: border-color .15s, box-shadow .15s; font-family: inherit; appearance: none;
  }
  .input:focus { border-color: #1A56DB; box-shadow: 0 0 0 3px rgba(26,86,219,0.12); }
  .form-actions { display: flex; gap: 10px; align-items: center; grid-column: span 3; padding-top: 4px; }

  .btn-primary {
    padding: 9px 22px; background: #1A56DB; color: #fff; border: none; border-radius: 8px;
    font-size: 13px; font-weight: 600; cursor: pointer; transition: background .15s; font-family: inherit;
  }
  .btn-primary:hover { background: #1043A8; }
  .btn-ghost {
    padding: 9px 18px; background: #fff; color: #6B7280; border: 1px solid #D1D5DB; border-radius: 8px;
    font-size: 13px; font-weight: 500; cursor: pointer; transition: background .15s; font-family: inherit;
  }
  .btn-ghost:hover { background: #F9FAFB; }

  .alert { margin-top: 14px; padding: 10px 14px; border-radius: 8px; font-size: 13px; }
  .alert-ok  { background: #ECFDF5; color: #065F46; border: 1px solid #A7F3D0; }
  .alert-err { background: #FEF2F2; color: #991B1B; border: 1px solid #FECACA; }

  .table-wrap { overflow-x: auto; }
  .table { width: 100%; border-collapse: collapse; font-size: 13px; }
  .table th {
    text-align: left; padding: 9px 12px; font-size: 11px; font-weight: 600; color: #6B7280;
    background: #F9FAFB; border-bottom: 1px solid #E5E7EB;
    text-transform: uppercase; letter-spacing: .04em; white-space: nowrap;
  }
  .table td { padding: 11px 12px; border-bottom: 1px solid #F3F4F6; color: #374151; vertical-align: middle; }
  .table tr:last-child td { border-bottom: none; }
  .table tr:hover td { background: #F9FAFB; }

  .placa { font-family: 'Courier New', monospace; font-weight: 700; font-size: 12px; background: #F3F4F6; color: #111928; padding: 3px 8px; border-radius: 5px; letter-spacing: .05em; }
  .mono  { font-family: 'Courier New', monospace; font-size: 12px; color: #6B7280; }

  .badge        { display: inline-flex; align-items: center; padding: 3px 9px; border-radius: 999px; font-size: 11px; font-weight: 600; white-space: nowrap; }
  .badge-green  { background: #ECFDF5; color: #065F46; }
  .badge-red    { background: #FEF2F2; color: #991B1B; }
  .badge-yellow { background: #FFFBEB; color: #92400E; }
  .badge-blue   { background: #EBF5FF; color: #1A56DB; }
  .badge-gray   { background: #F3F4F6; color: #6B7280; }

  .cat-badge { display: inline-flex; align-items: center; justify-content: center; width: 28px; height: 22px; background: #EBF5FF; color: #1A56DB; font-size: 11px; font-weight: 700; border-radius: 5px; }

  .btn-del { background: none; border: none; color: #D1D5DB; cursor: pointer; font-size: 12px; padding: 4px 6px; border-radius: 5px; transition: background .15s, color .15s; }
  .btn-del:hover { background: #FEF2F2; color: #E02424; }

  .empty { color: #9CA3AF; font-size: 13px; padding: 16px 0; text-align: center; }

  /* Login */
  .login-bg {
    min-height: 100vh; display: flex; align-items: center; justify-content: center;
    background: #F0F2F5;
  }
  .login-card {
    background: #fff; border: 1px solid #E5E7EB; border-radius: 16px;
    padding: 40px 36px; width: 100%; max-width: 420px;
    display: flex; flex-direction: column; gap: 24px;
    box-shadow: 0 4px 24px rgba(0,0,0,0.07);
  }
  .login-logo { display: flex; flex-direction: column; align-items: center; gap: 6px; }
  .login-title { font-size: 22px; font-weight: 700; color: #111928; }
  .login-sub   { font-size: 13px; color: #6B7280; }
  .login-label { font-size: 13px; font-weight: 500; color: #374151; text-align: center; }

  .login-roles { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; }
  .role-card {
    display: flex; flex-direction: column; align-items: center; gap: 6px;
    padding: 20px 12px; background: #F9FAFB;
    border: 2px solid #E5E7EB; border-radius: 12px;
    cursor: pointer; transition: border-color .15s, background .15s;
    font-family: inherit;
  }
  .role-card:hover { border-color: #93C5FD; background: #EFF6FF; }
  .role-card.selected { border-color: #1A56DB; background: #EBF5FF; }
  .role-card-icon { font-size: 28px; }
  .role-card strong { font-size: 14px; color: #111928; }
  .role-card-desc { font-size: 11px; color: #6B7280; text-align: center; line-height: 1.4; }

  .sidebar-user { display: flex; align-items: center; justify-content: space-between; margin-top: 12px; padding-top: 12px; border-top: 1px solid rgba(255,255,255,0.08); }
  .role-badge { font-size: 11px; font-weight: 600; padding: 3px 10px; border-radius: 999px; }
  .role-badge.gerente  { background: #1A56DB; color: #fff; }
  .role-badge.operador { background: rgba(255,255,255,0.12); color: #D1D5DB; }
  .btn-logout { font-size: 11px; color: #6B7280; background: none; border: none; cursor: pointer; font-family: inherit; transition: color .15s; }
  .btn-logout:hover { color: #fff; }

  ::-webkit-scrollbar { width: 5px; height: 5px; }
  ::-webkit-scrollbar-track { background: transparent; }
  ::-webkit-scrollbar-thumb { background: #D1D5DB; border-radius: 99px; }

  @media (max-width: 900px) {
    .sidebar { width: 56px; min-width: 56px; }
    .sidebar-logo, .sidebar-footer, .nav-item span + * { display: none; }
    .sidebar-nav { padding: 12px 6px; }
    .nav-item { justify-content: center; padding: 10px; }
    .page { padding: 20px 16px; }
    .kpi-grid { grid-template-columns: 1fr 1fr; }
    .two-col  { grid-template-columns: 1fr; }
    .form-grid { grid-template-columns: 1fr 1fr; }
    .form-actions { grid-column: span 2; }
    .field-wide { grid-column: span 2; }
  }
`;