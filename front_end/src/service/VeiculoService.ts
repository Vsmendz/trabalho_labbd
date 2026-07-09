import api from './api';

export interface VeiculoDTO {
  placa: string;
  modelo: string;
  ano: number;
  quilometragemAtual: number;
  statusVeiculo: string;
  tipo: string;
}

export const listarVeiculos = async (): Promise<VeiculoDTO[]> => {
  const response = await api.get('/veiculos');
  return response.data;
};

export const criarVeiculo = async (veiculo: VeiculoDTO): Promise<VeiculoDTO> => {
  const response = await api.post('/veiculos', veiculo);
  return response.data;
};

export const atualizarStatusVeiculo = async (placa: string, status: string): Promise<VeiculoDTO> => {
  const response = await api.patch(`/veiculos/${placa}`, { status });
  return response.data;
};

export const excluirVeiculo = async (placa: string): Promise<void> => {
  await api.delete(`/veiculos/${placa}`);
};