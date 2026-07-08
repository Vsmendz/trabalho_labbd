/**
 * api/trips.ts
 *
 * Módulo responsável pelo gerenciamento de viagens.
 *
 * Os dados são mantidos em memória (`store`). Para integrar a um backend,
 * substitua as implementações por chamadas HTTP — as assinaturas permanecem
 * iguais e o App.tsx não precisará de alterações.
 */

/** Representa uma viagem cadastrada no sistema. */
export interface Trip {
  id: number;          // identificador único gerado no cadastro
  origem: string;      // cidade/local de partida
  destino: string;     // cidade/local de chegada
  distancia: string;   // distância total da rota em km
  saida: string;       // data e hora de saída (formato ISO 8601: "YYYY-MM-DDTHH:mm")
  chegada: string;     // data e hora de chegada prevista
  veiculoId: number;   // referência ao id do veículo utilizado
  motoristaId: number; // referência ao id do motorista responsável
  passageiros: string; // número de passageiros transportados
  observacoes: string; // informações adicionais da viagem
  status: string;      // situação: "Em Andamento" | "Concluída"
  km: string;          // quilometragem registrada para a viagem
}

/**
 * Store em memória com viagens de exemplo.
 * Simula o retorno de um endpoint GET /trips.
 */
let store: Trip[] = [
  { id: 1, origem: "São Paulo", destino: "Campinas",       distancia: "100", saida: "2025-07-05T08:00", chegada: "2025-07-05T10:00", veiculoId: 1, motoristaId: 1, passageiros: "12", observacoes: "", status: "Concluída",     km: "100" },
  { id: 2, origem: "Campinas",  destino: "Ribeirão Preto", distancia: "240", saida: "2025-07-06T07:00", chegada: "2025-07-06T10:30", veiculoId: 3, motoristaId: 3, passageiros: "38", observacoes: "", status: "Em Andamento", km: "240" },
];

/**
 * Retorna todas as viagens cadastradas.
 * O spread cria uma cópia para preservar a imutabilidade do store.
 */
export async function getTrips(): Promise<Trip[]> {
  return [...store];
}

/**
 * Cadastra uma nova viagem.
 * O `id` é gerado por `Date.now()` — em produção deve vir do servidor.
 */
export async function addTrip(data: Omit<Trip, "id">): Promise<Trip> {
  const t = { ...data, id: Date.now() };
  store = [...store, t];
  return t;
}

/**
 * Atualiza campos de uma viagem pelo `id`.
 * Usado principalmente para marcar a viagem como "Concluída".
 */
export async function updateTrip(id: number, patch: Partial<Trip>): Promise<Trip> {
  store = store.map(t => t.id === id ? { ...t, ...patch } : t);
  return store.find(t => t.id === id)!;
}

/** Remove uma viagem do cadastro pelo seu `id`. */
export async function deleteTrip(id: number): Promise<void> {
  store = store.filter(t => t.id !== id);
}
