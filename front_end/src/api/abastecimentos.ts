/**
 * api/abastecimentos.ts
 *
 * Módulo responsável pelo registro de abastecimentos da frota.
 *
 * Permite registrar cada evento de abastecimento com veículo, data,
 * tipo de combustível, litros abastecidos e custo. Os dados são
 * mantidos em memória — para produção substitua por chamadas HTTP.
 */

/** Representa um registro de abastecimento. */
export interface Abastecimento {
  id: number;          // identificador único gerado no cadastro
  veiculoId: number;   // referência ao id do veículo abastecido
  data: string;        // data e hora do abastecimento (ISO 8601: "YYYY-MM-DDTHH:mm")
  litros: string;      // quantidade abastecida em litros
  combustivel: string; // tipo: "Diesel" | "Gasolina" | "Etanol" | "GNV"
  custo: string;       // valor total pago em reais
  observacoes: string; // informações adicionais (posto, nota fiscal, etc.)
}

/**
 * Store em memória, iniciado vazio.
 * Todos os registros são criados pelo usuário durante a sessão.
 */
let store: Abastecimento[] = [];

/**
 * Retorna todos os abastecimentos registrados.
 * O spread garante imutabilidade do store.
 */
export async function getAbastecimentos(): Promise<Abastecimento[]> {
  return [...store];
}

/**
 * Registra um novo abastecimento.
 * O `id` é gerado por `Date.now()` — em produção deve vir do servidor.
 */
export async function addAbastecimento(data: Omit<Abastecimento, "id">): Promise<Abastecimento> {
  const a = { ...data, id: Date.now() };
  store = [...store, a];
  return a;
}

/** Remove um registro de abastecimento pelo seu `id`. */
export async function deleteAbastecimento(id: number): Promise<void> {
  store = store.filter(a => a.id !== id);
}
