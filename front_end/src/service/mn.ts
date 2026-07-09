import api from './api'; // ou axios, dependendo da sua configuração

export interface ManutencaoVeiculoDTO {
  id?: number;
  dataGeracao?: string;
  dataManutencao?: string;
  kmFeito?: number;
  status?: string;
  observacao?: string;
  placaVeiculo: string;
  descricaoManutencao?: string;
  idManutencao?: number;
}

const BASE_URL = '/manutencoes-veiculos';

export async function listarManutencoesVeiculo(): Promise<ManutencaoVeiculoDTO[]> {
  const response = await api.get(BASE_URL);
  return response.data;
}

export async function buscarManutencaoVeiculoPorId(id: number): Promise<ManutencaoVeiculoDTO> {
  const response = await api.get(`${BASE_URL}/${id}`);
  return response.data;
}

export async function buscarManutencoesPorVeiculo(placa: string): Promise<ManutencaoVeiculoDTO[]> {
  const response = await api.get(`${BASE_URL}/veiculo/${placa}`);
  return response.data;
}


export async function buscarManutencoesPorStatus(status: string): Promise<ManutencaoVeiculoDTO[]> {
  const response = await api.get(`${BASE_URL}/status/${status}`);
  return response.data;
}

export async function criarManutencaoVeiculo(data: ManutencaoVeiculoDTO): Promise<ManutencaoVeiculoDTO> {
  const response = await api.post(BASE_URL, data);
  return response.data;
}

export async function atualizarStatusManutencao(id: number, status: string, observacao?: string): Promise<void> {
  console.log('Enviando PATCH para:', `/manutencoes-veiculos/${id}/status`, { status, observacao });
  await api.patch(`/manutencoes-veiculos/${id}/status`, { 
    status, 
    observacao: observacao || '' 
  });
}

export async function atualizarManutencaoVeiculo(id: number, data: ManutencaoVeiculoDTO): Promise<ManutencaoVeiculoDTO> {
  console.log('Enviando PUT para:', `/manutencoes-veiculos/${id}`, data); // Debug
  const response = await api.put(`/manutencoes-veiculos/${id}`, data);
  console.log('Resposta:', response.data); // Debug
  return response.data;
}

export async function excluirManutencaoVeiculo(id: number): Promise<void> {
  await api.delete(`${BASE_URL}/${id}`);
}