import { useState } from "react";

// ── Types ──────────────────────────────────────────────────────────────
interface Vehicle {
  id: number; placa: string; modelo: string;
  ano: string; km: string; tipo: string; status: string;
}
interface Driver {
  id: number; nome: string; cpf: string; cnh: string;
  categoria: string; telefone: string; status: string;
}
interface Trip {
  id: number; origem: string; destino: string; distancia: string;
  saida: string; chegada: string; veiculoId: number; motoristaId: number;
  passageiros: string; observacoes: string; status: string;
}

type Tab = "painel" | "viagens" | "veiculos" | "motoristas" | "manutencao";

// ── Helpers ────────────────────────────────────────────────────────────
function vehicleBadge(s: string) {
  return ({ "Disponível": "badge-green", "Em Rota": "badge-blue", "Manutenção": "badge-yellow" } as Record<string,string>)[s] ?? "badge-gray";
}
function driverBadge(s: string) {
  return ({ Ativo: "badge-green", "De Férias": "badge-yellow", Afastado: "badge-red", Inativo: "badge-red" } as Record<string,string>)[s] ?? "badge-gray";
}
function tripBadge(s: string) {
  return ({ Agendada: "badge-blue", "Em Andamento": "badge-yellow", Concluída: "badge-green", Cancelada: "badge-red" } as Record<string,string>)[s] ?? "badge-gray";
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

  const [vehicles, setVehicles] = useState<Vehicle[]>([
    { id: 1, placa: "BRA-2E19", modelo: "Sprinter 415", ano: "2022", km: "48200", tipo: "Van",      status: "Disponível" },
    { id: 2, placa: "QRS-5K31", modelo: "Kombi",         ano: "2019", km: "91500", tipo: "Van",      status: "Manutenção" },
    { id: 3, placa: "TUV-7M44", modelo: "Scania K360",   ano: "2023", km: "12300", tipo: "Caminhão", status: "Em Rota" },
  ]);
  const [drivers, setDrivers] = useState<Driver[]>([
    { id: 1, nome: "Carlos Eduardo Silva",  cpf: "012.345.678-90", cnh: "98765432100", categoria: "D", telefone: "(11) 98201-3344", status: "Ativo" },
    { id: 2, nome: "Fernanda Rocha Matos",  cpf: "123.456.789-01", cnh: "87654321099", categoria: "B", telefone: "(21) 97302-5511", status: "De Férias" },
    { id: 3, nome: "Rodrigo Alves Pereira", cpf: "234.567.890-12", cnh: "76543210988", categoria: "E", telefone: "(31) 99403-7722", status: "Ativo" },
  ]);
  const [trips, setTrips] = useState<Trip[]>([
    { id: 1, origem: "São Paulo", destino: "Campinas",        distancia: "100", saida: "2025-07-05T08:00", chegada: "2025-07-05T10:00", veiculoId: 1, motoristaId: 1, passageiros: "12", observacoes: "", status: "Concluída" },
    { id: 2, origem: "Campinas",  destino: "Ribeirão Preto",  distancia: "240", saida: "2025-07-06T07:00", chegada: "2025-07-06T10:30", veiculoId: 3, motoristaId: 3, passageiros: "38", observacoes: "", status: "Em Andamento" },
  ]);

  // ── Vehicle form ───────────────────────────────────────────────────
  const emptyVehicle = { placa: "", modelo: "", ano: "", km: "", tipo: "Carro", status: "Disponível" };
  const [vForm, setVForm] = useState(emptyVehicle);
  const [vMsg,  setVMsg]  = useState<{ type: "ok"|"err"; text: string }|null>(null);
  function submitVehicle(e: React.FormEvent) {
    e.preventDefault();
    if (!vForm.placa || !vForm.modelo || !vForm.ano) {
      setVMsg({ type: "err", text: "Preencha todos os campos obrigatórios (*)." }); return;
    }
    setVehicles(p => [...p, { ...vForm, id: Date.now() }]);
    setVForm(emptyVehicle);
    setVMsg({ type: "ok", text: "Veículo cadastrado com sucesso!" });
    setTimeout(() => setVMsg(null), 3000);
  }

  // ── Driver form ────────────────────────────────────────────────────
  const [dForm, setDForm] = useState({ nome: "", cpf: "", cnh: "", categoria: "B", telefone: "", status: "Ativo" });
  const [dMsg,  setDMsg]  = useState<{ type: "ok"|"err"; text: string }|null>(null);
  function submitDriver(e: React.FormEvent) {
    e.preventDefault();
    if (!dForm.nome || !dForm.cpf) {
      setDMsg({ type: "err", text: "Preencha todos os campos obrigatórios (*)." }); return;
    }
    setDrivers(p => [...p, { ...dForm, id: Date.now() }]);
    setDForm({ nome: "", cpf: "", cnh: "", categoria: "B", telefone: "", status: "Ativo" });
    setDMsg({ type: "ok", text: "Motorista cadastrado com sucesso!" });
    setTimeout(() => setDMsg(null), 3000);
  }

  // ── Trip form ──────────────────────────────────────────────────────
  const emptyTrip = { origem: "", destino: "", distancia: "", saida: "", chegada: "", veiculoId: 0, motoristaId: 0, passageiros: "", observacoes: "", status: "Em Andamento", km: "" };
  const [tForm, setTForm] = useState(emptyTrip);
  const [tMsg,  setTMsg]  = useState<{ type: "ok"|"err"; text: string }|null>(null);
  const [cTripId, setCTripId] = useState(0);
  const [cMsg,    setCMsg]    = useState<{ type: "ok"|"err"; text: string }|null>(null);
  function submitTrip(e: React.FormEvent) {
    e.preventDefault();
    if (!tForm.saida || !tForm.veiculoId || !tForm.motoristaId) {
      setTMsg({ type: "err", text: "Preencha data de saída, veículo e motorista (*)." }); return;
    }
    setTrips(p => [...p, { ...tForm, id: Date.now() }]);
    setTForm(emptyTrip);
    setTMsg({ type: "ok", text: "Viagem cadastrada com sucesso!" });
    setTimeout(() => setTMsg(null), 3000);
  }

  function concluirViagem() {
    if (!cTripId) { setCMsg({ type: "err", text: "Selecione uma viagem." }); return; }
    setTrips(p => p.map(t => t.id === cTripId ? { ...t, status: "Concluída" } : t));
    setCTripId(0);
    setCMsg({ type: "ok", text: "Viagem concluída com sucesso!" });
    setTimeout(() => setCMsg(null), 3000);
  }

  const activeVehicles = vehicles.filter(v => v.status === "Disponível");

  // ── Maintenance form ───────────────────────────────────────────────
  const [mVeiculoId, setMVeiculoId] = useState(0);
  const [mObs,       setMObs]       = useState("");
  const [mMsg,       setMMsg]       = useState<{ type: "ok"|"err"; text: string }|null>(null);

  function enviarManutencao() {
    if (!mVeiculoId) { setMMsg({ type: "err", text: "Selecione um veículo." }); return; }
    setVehicles(p => p.map(v => v.id === mVeiculoId ? { ...v, status: "Manutenção" } : v));
    setMVeiculoId(0); setMObs("");
    setMMsg({ type: "ok", text: "Veículo enviado para manutenção!" });
    setTimeout(() => setMMsg(null), 3000);
  }
  function despachar(id: number) {
    setVehicles(p => p.map(v => v.id === id ? { ...v, status: "Disponível" } : v));
  }

  // ── Maintenance types ──────────────────────────────────────────────
  interface MaintType { id: number; descricao: string; kmRef: string; }
  const [maintTypes, setMaintTypes] = useState<MaintType[]>([
    { id: 1, descricao: "Troca de óleo",       kmRef: "5000"  },
    { id: 2, descricao: "Revisão de freios",   kmRef: "20000" },
    { id: 3, descricao: "Alinhamento",          kmRef: "10000" },
  ]);
  const [mtForm, setMtForm] = useState({ descricao: "", kmRef: "" });
  const [mtMsg,  setMtMsg]  = useState<{ type: "ok"|"err"; text: string }|null>(null);

  function submitMaintType(e: React.FormEvent) {
    e.preventDefault();
    if (!mtForm.descricao) { setMtMsg({ type: "err", text: "Informe a descrição." }); return; }
    setMaintTypes(p => [...p, { ...mtForm, id: Date.now() }]);
    setMtForm({ descricao: "", kmRef: "" });
    setMtMsg({ type: "ok", text: "Tipo de manutenção cadastrado!" });
    setTimeout(() => setMtMsg(null), 3000);
  }
  const activeDrivers  = drivers.filter(d => d.status === "Ativo");

  if (!role) return <LoginScreen onLogin={setRole} />;

  return (
    <>
      <style>{styles}</style>
      <div className="layout">

        {/* ── Sidebar ── */}
        <aside className="sidebar">
          <div className="sidebar-logo">
            <span className="sidebar-icon">🚌</span>
            <div>
              <div className="sidebar-title">TransportControl</div>
              <div className="sidebar-sub">Gestão de Frota</div>
            </div>
          </div>

          <nav className="sidebar-nav">
            <button className={`nav-item ${tab === "painel"     ? "active" : ""}`} onClick={() => setTab("painel")}>
              <span>📋</span> Painel Geral
            </button>
            <button className={`nav-item ${tab === "viagens"    ? "active" : ""}`} onClick={() => setTab("viagens")}>
              <span>🗺️</span> Viagens
            </button>
            <button className={`nav-item ${tab === "veiculos"   ? "active" : ""}`} onClick={() => setTab("veiculos")}>
              <span>🚗</span> Veículos
            </button>
            <button className={`nav-item ${tab === "motoristas"  ? "active" : ""}`} onClick={() => setTab("motoristas")}>
              <span>👤</span> Motoristas
            </button>
            {role === "gerente" && (
              <button className={`nav-item ${tab === "manutencao" ? "active" : ""}`} onClick={() => setTab("manutencao")}>
                <span>🔧</span> Manutenção
              </button>
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

        {/* ── Main ── */}
        <main className="main">

          {/* ── Painel ── */}
          {tab === "painel" && (
            <div className="page">
              <div className="page-header">
                <h1 className="page-title">Painel Geral</h1>
                <p className="page-sub">Visão geral da operação</p>
              </div>
              <div className="kpi-grid">
                <KpiCard icon="🗺️" label="Total de Viagens"    value={trips.length}                                        color="blue"   />
                <KpiCard icon="🔄" label="Em Andamento"        value={trips.filter(t => t.status === "Em Andamento").length} color="orange" />
                <KpiCard icon="🚗" label="Veículos"            value={vehicles.length}                                      color="blue"   />
                <KpiCard icon="👤" label="Motoristas"          value={drivers.length}                                       color="purple" />
              </div>
              <div className="two-col">
                <div className="card">
                  <div className="card-header">
                    <h2 className="card-title">Últimas Viagens</h2>
                    <button className="btn-link" onClick={() => setTab("viagens")}>Ver todas →</button>
                  </div>
                  {trips.length === 0
                    ? <p className="empty">Nenhuma viagem cadastrada.</p>
                    : <div className="table-wrap"><table className="table">
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
                      </table></div>
                  }
                </div>
                <div style={{ display: "flex", flexDirection: "column", gap: 16 }}>
                  <div className="card">
                    <div className="card-header">
                      <h2 className="card-title">Frota</h2>
                      <button className="btn-link" onClick={() => setTab("veiculos")}>Ver todos →</button>
                    </div>
                    <VehicleTable vehicles={vehicles} onDelete={() => {}} compact readonly />
                  </div>
                  <div className="card">
                    <div className="card-header">
                      <h2 className="card-title">Motoristas</h2>
                      <button className="btn-link" onClick={() => setTab("motoristas")}>Ver todos →</button>
                    </div>
                    <DriverTable drivers={drivers} onDelete={() => {}} compact readonly />
                  </div>
                </div>
              </div>
            </div>
          )}

          {/* ── Viagens ── */}
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
                    <input className="input" type="datetime-local"
                      value={tForm.saida} onChange={e => setTForm(f => ({ ...f, saida: e.target.value }))} />
                  </Field>
                  <Field label="Distância (km)">
                    <input className="input" placeholder="Ex: 320" type="number" min="0"
                      value={tForm.km} onChange={e => setTForm(f => ({ ...f, km: e.target.value }))} />
                  </Field>
                  <Field label="Veículo *">
                    <select className="input" value={tForm.veiculoId || ""}
                      onChange={e => setTForm(f => ({ ...f, veiculoId: Number(e.target.value) }))}>
                      <option value="">— Selecione —</option>
                      {activeVehicles.map(v => (
                        <option key={v.id} value={v.id}>{v.placa} — {v.modelo}</option>
                      ))}
                    </select>
                  </Field>
                  <Field label="Motorista *">
                    <select className="input" value={tForm.motoristaId || ""}
                      onChange={e => setTForm(f => ({ ...f, motoristaId: Number(e.target.value) }))}>
                      <option value="">— Selecione —</option>
                      {activeDrivers.map(d => (
                        <option key={d.id} value={d.id}>{d.nome}</option>
                      ))}
                    </select>
                  </Field>
                  <Field label="Observações" wide>
                    <input className="input" placeholder="Informações adicionais..."
                      value={tForm.observacoes} onChange={e => setTForm(f => ({ ...f, observacoes: e.target.value }))} />
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
                <h2 className="card-title" style={{ marginBottom: 16 }}>
                  Viagens Cadastradas <span className="badge-count">{trips.length}</span>
                </h2>
                {trips.length === 0
                  ? <p className="empty">Nenhuma viagem cadastrada ainda.</p>
                  : <div className="table-wrap">
                      <table className="table">
                        <thead>
                          <tr><th>Data de Saída</th><th>Distância</th><th>Veículo</th><th>Motorista</th><th>Status</th><th>Obs.</th><th></th></tr>
                        </thead>
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
                                <td><button className="btn-del" onClick={() => setTrips(p => p.filter(x => x.id !== t.id))} title="Remover">✕</button></td>
                              </tr>
                            );
                          })}
                        </tbody>
                      </table>
                    </div>
                }
              </div>
            </div>
          )}

          {/* ── Veículos ── */}
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
                    <input className="input" placeholder="ABC-1234" value={vForm.placa}
                      onChange={e => setVForm(f => ({ ...f, placa: e.target.value.toUpperCase() }))} />
                  </Field>
                  <Field label="Modelo *">
                    <input className="input" placeholder="Ex: Sprinter 415" value={vForm.modelo}
                      onChange={e => setVForm(f => ({ ...f, modelo: e.target.value }))} />
                  </Field>
                  <Field label="Ano *">
                    <input className="input" placeholder="2024" maxLength={4} value={vForm.ano}
                      onChange={e => setVForm(f => ({ ...f, ano: e.target.value }))} />
                  </Field>
                  <Field label="KM atual">
                    <input className="input" placeholder="Ex: 48200" type="number" min="0" value={vForm.km}
                      onChange={e => setVForm(f => ({ ...f, km: e.target.value }))} />
                  </Field>
                  <Field label="Tipo">
                    <select className="input" value={vForm.tipo} onChange={e => setVForm(f => ({ ...f, tipo: e.target.value }))}>
                      {["Carro","Van","Caminhão"].map(t => <option key={t}>{t}</option>)}
                    </select>
                  </Field>
                  <Field label="Status">
                    <select className="input" value={vForm.status} onChange={e => setVForm(f => ({ ...f, status: e.target.value }))}>
                      {["Disponível","Em Rota","Manutenção"].map(s => <option key={s}>{s}</option>)}
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
                <VehicleTable vehicles={vehicles} onDelete={id => setVehicles(v => v.filter(x => x.id !== id))} />
              </div>
            </div>
          )}

          {/* ── Motoristas ── */}
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
                    <input className="input" placeholder="Nome do motorista" value={dForm.nome}
                      onChange={e => setDForm(f => ({ ...f, nome: e.target.value }))} />
                  </Field>
                  <Field label="CPF *">
                    <input className="input" placeholder="000.000.000-00" value={dForm.cpf}
                      onChange={e => setDForm(f => ({ ...f, cpf: e.target.value }))} />
                  </Field>
                  <Field label="Categoria CNH *">
                    <select className="input" value={dForm.categoria} onChange={e => setDForm(f => ({ ...f, categoria: e.target.value }))}>
                      {["B","C","D","E"].map(c => <option key={c}>{c}</option>)}
                    </select>
                  </Field>
                  <div className="form-actions">
                    <button type="submit" className="btn-primary">Cadastrar Motorista</button>
                    <button type="button" className="btn-ghost"
                      onClick={() => setDForm({ nome:"",cpf:"",cnh:"",categoria:"B",telefone:"",status:"Ativo" })}>
                      Limpar
                    </button>
                  </div>
                </form>
                {dMsg && <div className={`alert ${dMsg.type === "ok" ? "alert-ok" : "alert-err"}`}>{dMsg.text}</div>}
              </div>
              <div className="card" style={{ marginTop: 20 }}>
                <h2 className="card-title" style={{ marginBottom: 16 }}>Motoristas Cadastrados <span className="badge-count">{drivers.length}</span></h2>
                <DriverTable drivers={drivers} onDelete={id => setDrivers(d => d.filter(x => x.id !== id))} />
              </div>
            </div>
          )}

          {/* ── Manutenção ── */}
          {tab === "manutencao" && (
            <div className="page">
              <div className="page-header">
                <h1 className="page-title">Manutenção</h1>
                <p className="page-sub">Gerencie veículos em manutenção e tipos de serviço</p>
              </div>

              <div className="kpi-grid" style={{ gridTemplateColumns: "repeat(3,1fr)" }}>
                <KpiCard icon="🔧" label="Em Manutenção" value={vehicles.filter(v => v.status === "Manutenção").length} color="orange" />
                <KpiCard icon="✅" label="Disponíveis"   value={vehicles.filter(v => v.status === "Disponível").length}  color="green"  />
                <KpiCard icon="🚗" label="Em Rota"       value={vehicles.filter(v => v.status === "Em Rota").length}     color="blue"   />
              </div>

              <div className="two-col" style={{ marginBottom: 20 }}>

                {/* ── Despachar da manutenção ── */}
                <div className="card" style={{ display: "flex", flexDirection: "column", gap: 20 }}>

                  {/* Enviar para manutenção */}
                  <div>
                    <h2 className="card-title" style={{ marginBottom: 14 }}>Enviar para Manutenção</h2>
                    <div style={{ display: "flex", gap: 10, alignItems: "flex-end" }}>
                      <div className="field" style={{ flex: 1 }}>
                        <label className="field-label">Veículo *</label>
                        <select className="input" value={mVeiculoId || ""}
                          onChange={e => setMVeiculoId(Number(e.target.value))}>
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

                  {/* Despachar da manutenção */}
                  <div>
                    <h2 className="card-title" style={{ marginBottom: 14 }}>Despachar da Manutenção</h2>
                    <div style={{ display: "flex", flexDirection: "column", gap: 10 }}>
                      <div className="field">
                        <label className="field-label">Veículo *</label>
                        <select className="input" value={mVeiculoId || ""}
                          onChange={e => setMVeiculoId(Number(e.target.value))}>
                          <option value="">— Selecione —</option>
                          {vehicles.filter(v => v.status === "Manutenção").map(v => (
                            <option key={v.id} value={v.id}>{v.placa} — {v.modelo}</option>
                          ))}
                        </select>
                      </div>
                      <div className="field">
                        <label className="field-label">Observação</label>
                        <input className="input" placeholder="Observação (opcional)"
                          value={mObs} onChange={e => setMObs(e.target.value)} />
                      </div>
                      <div>
                        <button className="btn-primary" onClick={() => {
                          if (!mVeiculoId) { setMMsg({ type: "err", text: "Selecione um veículo." }); return; }
                          despachar(mVeiculoId);
                          setMVeiculoId(0); setMObs("");
                          setMMsg({ type: "ok", text: "Veículo despachado com sucesso!" });
                          setTimeout(() => setMMsg(null), 3000);
                        }}>Despachar</button>
                      </div>
                    </div>
                  </div>

                  {mMsg && <div className={`alert ${mMsg.type === "ok" ? "alert-ok" : "alert-err"}`}>{mMsg.text}</div>}
                </div>

                {/* ── Cadastro de tipo de manutenção ── */}
                <div className="card">
                  <h2 className="card-title" style={{ marginBottom: 20 }}>Tipos de Manutenção</h2>
                  <form className="form-grid" style={{ gridTemplateColumns: "1fr 1fr" }} onSubmit={submitMaintType}>
                    <Field label="Descrição *">
                      <input className="input" placeholder="Ex: Troca de óleo"
                        value={mtForm.descricao} onChange={e => setMtForm(f => ({ ...f, descricao: e.target.value }))} />
                    </Field>
                    <Field label="KM de Referência">
                      <input className="input" placeholder="Ex: 5000" type="number" min="0"
                        value={mtForm.kmRef} onChange={e => setMtForm(f => ({ ...f, kmRef: e.target.value }))} />
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
                              <td><button className="btn-del" onClick={() => setMaintTypes(p => p.filter(x => x.id !== t.id))}>✕</button></td>
                            </tr>
                          ))}
                        </tbody>
                      </table>
                    </div>
                  )}
                </div>
              </div>

              {/* ── Quadro de veículos que precisam de manutenção ── */}
              <div className="card" style={{ marginBottom: 20 }}>
                <div className="card-header">
                  <h2 className="card-title">Quadro de Necessidade de Manutenção</h2>
                  <span style={{ fontSize: 12, color: "#6B7280" }}>Baseado no KM atual vs. KM de referência</span>
                </div>
                {maintTypes.length === 0 || vehicles.length === 0
                  ? <p className="empty">Cadastre tipos de manutenção e veículos para ver o quadro.</p>
                  : <div className="table-wrap">
                      <table className="table">
                        <thead>
                          <tr><th>Veículo</th><th>Placa</th><th>KM Atual</th><th>Tipo de Manutenção</th><th>KM Ref.</th><th>Situação</th></tr>
                        </thead>
                        <tbody>
                          {vehicles.flatMap(v =>
                            maintTypes.map(t => {
                              const kmAtual = Number(v.km) || 0;
                              const kmRef   = Number(t.kmRef) || 0;
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
                                    {vencido
                                      ? <span className="badge badge-red">⚠ Vencido</span>
                                      : proximo
                                      ? <span className="badge badge-yellow">Próximo</span>
                                      : <span className="badge badge-green">Em dia</span>
                                    }
                                  </td>
                                </tr>
                              );
                            })
                          )}
                        </tbody>
                      </table>
                    </div>
                }
              </div>

              {/* ── Veículos atualmente em manutenção ── */}
              <div className="card">
                <div className="card-header">
                  <h2 className="card-title">
                    Veículos em Manutenção
                    <span className="badge-count">{vehicles.filter(v => v.status === "Manutenção").length}</span>
                  </h2>
                </div>
                {vehicles.filter(v => v.status === "Manutenção").length === 0
                  ? <p className="empty">Nenhum veículo em manutenção no momento. ✅</p>
                  : <div className="table-wrap">
                      <table className="table">
                        <thead>
                          <tr><th>Placa</th><th>Modelo</th><th>Ano</th><th>KM</th><th>Tipo</th><th>Status</th></tr>
                        </thead>
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
                }
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

function VehicleTable({ vehicles, onDelete, compact, readonly }: { vehicles: Vehicle[]; onDelete: (id: number) => void; compact?: boolean; readonly?: boolean }) {
  if (vehicles.length === 0) return <p className="empty">Nenhum veículo cadastrado ainda.</p>;
  return (
    <div className="table-wrap">
      <table className="table">
        <thead>
          <tr>
            <th>Placa</th><th>Modelo</th>
            {!compact && <><th>Ano</th><th>KM</th></>}
            <th>Tipo</th><th>Status</th><th></th>
          </tr>
        </thead>
        <tbody>
          {vehicles.map(v => (
            <tr key={v.id}>
              <td><span className="placa">{v.placa}</span></td>
              <td>{v.modelo}</td>
              {!compact && <><td>{v.ano}</td><td className="mono">{v.km ? Number(v.km).toLocaleString("pt-BR") + " km" : "—"}</td></>}
              <td>{v.tipo}</td>
              <td><span className={`badge ${vehicleBadge(v.status)}`}>{v.status}</span></td>
              {!readonly && <td><button className="btn-del" onClick={() => onDelete(v.id)}>✕</button></td>}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

function DriverTable({ drivers, onDelete, readonly }: { drivers: Driver[]; onDelete: (id: number) => void; compact?: boolean; readonly?: boolean }) {
  if (drivers.length === 0) return <p className="empty">Nenhum motorista cadastrado ainda.</p>;
  return (
    <div className="table-wrap">
      <table className="table">
        <thead>
          <tr><th>Nome</th><th>CPF</th><th>Categoria</th><th></th></tr>
        </thead>
        <tbody>
          {drivers.map(d => (
            <tr key={d.id}>
              <td><strong>{d.nome}</strong></td>
              <td className="mono">{d.cpf}</td>
              <td><span className="cat-badge">{d.categoria}</span></td>
              {!readonly && <td><button className="btn-del" onClick={() => onDelete(d.id)}>✕</button></td>}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

// ── Login Screen ───────────────────────────────────────────────────────
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
            <button
              className={`role-card ${selected === "operador" ? "selected" : ""}`}
              onClick={() => setSelected("operador")}>
              <span className="role-card-icon">👷</span>
              <strong>Operador</strong>
              <span className="role-card-desc">Viagens, Veículos e Motoristas</span>
            </button>
            <button
              className={`role-card ${selected === "gerente" ? "selected" : ""}`}
              onClick={() => setSelected("gerente")}>
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

// ── Styles ─────────────────────────────────────────────────────────────
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

  .rota       { display: inline-flex; align-items: center; gap: 5px; font-size: 13px; }
  .rota-arrow { color: #9CA3AF; font-size: 12px; }

  .badge        { display: inline-flex; align-items: center; padding: 3px 9px; border-radius: 999px; font-size: 11px; font-weight: 600; white-space: nowrap; }
  .badge-green  { background: #ECFDF5; color: #065F46; }
  .badge-red    { background: #FEF2F2; color: #991B1B; }
  .badge-yellow { background: #FFFBEB; color: #92400E; }
  .badge-blue   { background: #EBF5FF; color: #1A56DB; }
  .badge-gray   { background: #F3F4F6; color: #6B7280; }

  .cat-badge { display: inline-flex; align-items: center; justify-content: center; width: 28px; height: 22px; background: #EBF5FF; color: #1A56DB; font-size: 11px; font-weight: 700; border-radius: 5px; }

  .btn-del { background: none; border: none; color: #D1D5DB; cursor: pointer; font-size: 12px; padding: 4px 6px; border-radius: 5px; transition: background .15s, color .15s; }
  .btn-del:hover { background: #FEF2F2; color: #E02424; }
  .btn-despachar { padding: 5px 12px; background: #ECFDF5; color: #065F46; border: 1px solid #A7F3D0; border-radius: 6px; font-size: 12px; font-weight: 600; cursor: pointer; transition: background .15s; font-family: inherit; }
  .btn-despachar:hover { background: #D1FAE5; }

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

  /* Sidebar user */
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
