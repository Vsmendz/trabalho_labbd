import api from './api';

export interface MotoristaDTO {
  cpf: string;
  nome: string;
  categoriaCnh: string;
}

export const listarMotoristas = async (): Promise<MotoristaDTO[]> => {
  const response = await api.get('/motoristas');
  return response.data;
};

export const criarMotorista = async (motorista: MotoristaDTO): Promise<MotoristaDTO> => {
  const response = await api.post('/motoristas', motorista);
  return response.data;
};