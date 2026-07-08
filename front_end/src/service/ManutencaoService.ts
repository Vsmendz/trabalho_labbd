import api from './api';

export interface ManutencaoDTO {
  id?: number;
  descricao: string;
  kmReferencia: number;
}

export const listarManutencoes = async (): Promise<ManutencaoDTO[]> => {
  const response = await api.get('/manutencoes');
  return response.data;
};

export const criarManutencao = async (manutencao: ManutencaoDTO): Promise<ManutencaoDTO> => {
  const response = await api.post('/manutencoes', manutencao);
  return response.data;
};

export const excluirManutencao = async (id: number): Promise<void> => {
  await api.delete(`/manutencoes/${id}`);
};