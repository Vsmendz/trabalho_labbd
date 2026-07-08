import api from './api';

// Interface que o frontend espera (pode ser igual ao DTO do backend)
export interface VeiculoDTO {
  placa: string;
  modelo: string;
  ano: number;          // no backend é Integer
  quilometragemAtual: number; // no frontend é "km" (string)
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
  // Vamos usar um PATCH ou PUT para atualizar apenas o status
  const response = await api.patch(`/veiculos/${placa}/status`, { status });
  return response.data;
};