/**
 * api/vehicles.ts
 *
 * Módulo responsável pelo gerenciamento de veículos da frota.
 *
 * Enquanto não houver integração com um servidor real, os dados
 * ficam em memória (variável `store`). Para migrar para uma API REST,
 * basta substituir o corpo de cada função por chamadas fetch/axios
 * mantendo as mesmas assinaturas — o restante da aplicação não precisará
 * ser alterado.
 */

/** Representa um veículo cadastrado na frota. */
export interface Vehicle {
  id: number;       // identificador único gerado no momento do cadastro
  placa: string;    // placa no formato AAA-0000 ou Mercosul AAA0A00
  modelo: string;   // nome comercial do veículo (ex: "Sprinter 415")
  ano: string;      // ano de fabricação (string para facilitar inputs de formulário)
  km: string;       // quilometragem atual do odômetro
  tipo: string;     // categoria: "Carro" | "Van" | "Caminhão"
  status: string;   // situação atual: "Disponível" | "Em Rota" | "Manutenção"
}

/**
 * Store em memória que simula o banco de dados.
 * Inicializado com dados de exemplo para facilitar o desenvolvimento.
 */
let store: Vehicle[] = [
  { id: 1, placa: "BRA-2E19", modelo: "Sprinter 415", ano: "2022", km: "48200", tipo: "Van",      status: "Disponível" },
  { id: 2, placa: "QRS-5K31", modelo: "Kombi",         ano: "2019", km: "91500", tipo: "Van",      status: "Manutenção" },
  { id: 3, placa: "TUV-7M44", modelo: "Scania K360",   ano: "2023", km: "12300", tipo: "Caminhão", status: "Em Rota"    },
];

/**
 * Retorna todos os veículos cadastrados.
 * O spread cria uma cópia para evitar mutações diretas no store.
 */
export async function getVehicles(): Promise<Vehicle[]> {
  return [...store];
}

/**
 * Cadastra um novo veículo.
 * O `id` é gerado automaticamente com `Date.now()` — em produção
 * deve vir do servidor.
 */
export async function addVehicle(data: Omit<Vehicle, "id">): Promise<Vehicle> {
  const v = { ...data, id: Date.now() };
  store = [...store, v];
  return v;
}

/**
 * Atualiza campos específicos de um veículo pelo `id`.
 * Usado principalmente para alterar o status (ex: "Disponível" → "Manutenção").
 */
export async function updateVehicle(id: number, patch: Partial<Vehicle>): Promise<Vehicle> {
  store = store.map(v => v.id === id ? { ...v, ...patch } : v);
  return store.find(v => v.id === id)!;
}

/** Remove um veículo do cadastro pelo seu `id`. */
export async function deleteVehicle(id: number): Promise<void> {
  store = store.filter(v => v.id !== id);
}
