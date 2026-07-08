import api from './api';

export interface AbastecimentoDTO {
  id?: number;
  placaVeiculo: string;
  data: string;
  tipo: string;
  litro: number;
  preco: number;
  obs?: string;
}

export const listarAbastecimentos = async (): Promise<AbastecimentoDTO[]> => {
  const response = await api.get('/abastecimentos');
  return response.data;
};

export const criarAbastecimento = async (abastecimento: AbastecimentoDTO): Promise<AbastecimentoDTO> => {
  const response = await api.post('/abastecimentos', abastecimento);
  return response.data;
};

export const excluirAbastecimento = async (id: number): Promise<void> => {
  await api.delete(`/abastecimentos/${id}`);
};