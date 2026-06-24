USE frota;

INSERT INTO VEICULO (
    PLACA,
    MODELO,
    ANO,
    QUILOMETRAGEM_ATUAL,
    STATUS_VEICULO,
    TIPO
)
VALUES
(
    'ABC1234',
    'Volkswagen Gol',
    2022,
    9500,
    'DISPONIVEL',
    'CARRO'
),
(
    'DEF5678',
    'Mercedes Actros',
    2020,
    9800,
    'DISPONIVEL',
    'CAMINHAO'
),
(
    'GHI9012',
    'Fiat Ducato',
    2021,
    4500,
    'DISPONIVEL',
    'VAN'
);

INSERT INTO MOTORISTA (
    CPF,
    NOME,
    CATEGORIA_CNH
)
VALUES
(
    '11111111111',
    'Pedro Franz',
    'B'
),
(
    '22222222222',
    'Maria Souza',
    'D'
),
(
    '33333333333',
    'Carlos Silva',
    'E'
);

INSERT INTO MANUTENCAO (
    DESCRICAO,
    KM_REFERENCIA
)
VALUES
(
    'Troca de Oleo',
    10000
),
(
    'Revisao Geral',
    20000
),
(
    'Troca de Pneus',
    30000
);

INSERT INTO VIAGEM (
    DATA_SAIDA,
    KM_INICIAL,
    PLACA_VEICULO,
    CPF_MOTORISTA
)
VALUES
(
    NOW(),
    9500,
    'ABC1234',
    '11111111111'
),
(
    NOW(),
    9800,
    'DEF5678',
    '22222222222'
);