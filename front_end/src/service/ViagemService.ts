import api from './api';

export interface ViagemDTO {
  id?: number;
  dataSaida: string;
  dataChegada?: string;
  kmInicial: number;
  kmFinal?: number;
  status: string;
  observacao?: string;
  placaVeiculo: string;
  cpfMotorista: string;
}

export const listarViagens = async (): Promise<ViagemDTO[]> => {
  const response = await api.get('/viagens');
  return response.data;
};

export const criarViagem = async (viagem: ViagemDTO): Promise<ViagemDTO> => {
  const response = await api.post('/viagens/iniciar', null, {
    params: { placa: viagem.placaVeiculo, cpf: viagem.cpfMotorista }
  });
  return response.data;
};

export const encerrarViagem = async (id: number, kmFinal: number, observacao?: string): Promise<void> => {
  await api.post(`/viagens/encerrar/${id}`, null, {
    params: { kmFinal, observacao }
  });
};

export const excluirViagem = async (id: number): Promise<void> => {
  await api.delete(`/viagens/${id}`);
};