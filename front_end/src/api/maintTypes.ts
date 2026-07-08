/**
 * api/maintTypes.ts
 *
 * Módulo responsável pelos tipos de manutenção preventiva.
 *
 * Cada tipo define um serviço (ex: troca de óleo) e o intervalo em km
 * a partir do qual ele deve ser realizado. O quadro de manutenção na
 * aba Manutenção cruza esses tipos com o km atual de cada veículo para
 * sinalizar serviços vencidos ou próximos do vencimento.
 */

/** Representa um tipo de manutenção preventiva. */
export interface MaintType {
  id: number;       // identificador único
  descricao: string; // nome do serviço (ex: "Troca de óleo")
  kmRef: string;    // intervalo de km para realização (ex: "5000" = a cada 5.000 km)
}

/**
 * Store em memória com tipos pré-cadastrados.
 * Simula o retorno de um endpoint GET /maint-types.
 */
let store: MaintType[] = [
  { id: 1, descricao: "Troca de óleo",     kmRef: "5000"  },
  { id: 2, descricao: "Revisão de freios", kmRef: "20000" },
  { id: 3, descricao: "Alinhamento",       kmRef: "10000" },
];

/**
 * Retorna todos os tipos de manutenção cadastrados.
 * O spread garante imutabilidade do store.
 */
export async function getMaintTypes(): Promise<MaintType[]> {
  return [...store];
}

/**
 * Cadastra um novo tipo de manutenção.
 * O `id` é gerado por `Date.now()` — em produção deve vir do servidor.
 */
export async function addMaintType(data: Omit<MaintType, "id">): Promise<MaintType> {
  const m = { ...data, id: Date.now() };
  store = [...store, m];
  return m;
}

/** Remove um tipo de manutenção pelo seu `id`. */
export async function deleteMaintType(id: number): Promise<void> {
  store = store.filter(m => m.id !== id);
}
