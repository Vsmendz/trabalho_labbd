/**
 * api/drivers.ts
 *
 * Módulo responsável pelo gerenciamento de motoristas.
 *
 * Os dados são mantidos em memória (`store`). Para conectar a uma API
 * real, substitua o corpo das funções por chamadas HTTP mantendo as
 * mesmas assinaturas — o componente App.tsx não precisará mudar.
 */

/** Representa um motorista cadastrado no sistema. */
export interface Driver {
  id: number;        // identificador único gerado no cadastro
  nome: string;      // nome completo do motorista
  cpf: string;       // CPF no formato 000.000.000-00
  cnh: string;       // número do registro da CNH
  categoria: string; // categoria habilitada: "B" | "C" | "D" | "E"
  telefone: string;  // telefone de contato com DDD
  status: string;    // situação: "Ativo" | "De Férias" | "Afastado" | "Inativo"
}

/**
 * Store em memória com dados de exemplo.
 * Simula o retorno de um endpoint GET /drivers.
 */
let store: Driver[] = [
  { id: 1, nome: "Carlos Eduardo Silva",  cpf: "012.345.678-90", cnh: "98765432100", categoria: "D", telefone: "(11) 98201-3344", status: "Ativo"     },
  { id: 2, nome: "Fernanda Rocha Matos",  cpf: "123.456.789-01", cnh: "87654321099", categoria: "B", telefone: "(21) 97302-5511", status: "De Férias" },
  { id: 3, nome: "Rodrigo Alves Pereira", cpf: "234.567.890-12", cnh: "76543210988", categoria: "E", telefone: "(31) 99403-7722", status: "Ativo"     },
];

/**
 * Retorna todos os motoristas cadastrados.
 * O spread garante imutabilidade do store interno.
 */
export async function getDrivers(): Promise<Driver[]> {
  return [...store];
}

/**
 * Cadastra um novo motorista.
 * O `id` é gerado por `Date.now()` — em produção deve vir do servidor.
 */
export async function addDriver(data: Omit<Driver, "id">): Promise<Driver> {
  const d = { ...data, id: Date.now() };
  store = [...store, d];
  return d;
}

/** Remove um motorista do cadastro pelo seu `id`. */
export async function deleteDriver(id: number): Promise<void> {
  store = store.filter(d => d.id !== id);
}
