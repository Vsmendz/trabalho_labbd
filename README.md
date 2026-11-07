# 🚛 Sistema de Gestão e Controle de Frotas — TransportControl (LabBD)

Sistema completo para gerenciamento de frotas, motoristas, viagens e manutenções preventivas/corretivas de veículos, desenvolvido com foco em modelagem relacional avançada, integridade de dados e arquitetura de persistência orientada a objetos.

---

## 📌 Sumário
- [Visão Geral](#-visão-geral)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Estrutura do Repositório](#-estrutura-do-repositório)
- [Regras de Negócio e Banco de Dados](#-regras-de-negócio-e-banco-de-dados)
  - [Tabelas e Modelagem](#tabelas-e-modelagem)
  - [Triggers](#triggers)
  - [Stored Procedures](#stored-procedures)
- [Diagnóstico e Status de Funcionamento](#-diagnóstico-e-status-de-funcionamento)
- [Como Configurar e Executar](#-como-configurar-e-executar)
  - [1. Configurar o Banco de Dados (MySQL)](#1-configurar-o-banco-de-dados-mysql)
  - [2. Configurar e Executar o Backend (Java / JPA)](#2-configurar-e-executar-o-backend-java--jpa)
  - [3. Executar o Frontend Desktop (Java Swing)](#3-executar-o-frontend-desktop-java-swing)
  - [4. Executar o Frontend Web (React + Vite)](#4-executar-o-frontend-web-react--vite)
- [Melhorias e Próximos Passos](#-melhorias-e-próximos-passos)

---

## 📖 Visão Geral

O projeto foi concebido para resolver o fluxo operacional de transporte de cargas e passageiros:
1. **Cadastro e Gestão de Veículos**: Controle de placa, modelo, ano, odômetro atual, categoria (Carro, Van, Caminhão) e status (Disponível, Em Rota, Manutenção).
2. **Cadastro de Motoristas**: Controle de CPF, nome e categoria da CNH (B, C, D, E).
3. **Registro e Encerramento de Viagens**: Rastreamento de quilometragem inicial e final, data/hora de saída e chegada, validação de compatibilidade entre motorista e veículo.
4. **Manutenções Preventivas e Corretivas**: Disparo automático de revisões conforme a quilometragem percorrida e registro de serviços concluídos.

---

## 🛠️ Tecnologias Utilizadas

* **Banco de Dados**: [MySQL 8.x](https://dev.mysql.com/doc/) (Tabelas com `CHECK constraints`, Triggers, Stored Procedures e Transações ACID).
* **Backend Java**:
  * **Java SE**: Versão 21 LTS (ou superior).
  * **JPA 3.0 (Jakarta Persistence)**: Mapeamento Objeto-Relacional (ORM).
  * **Hibernate ORM**: Provedor de persistência.
  * **Driver MySQL Connector/J**: Conectividade JDBC.
  * **Padrão DAO (Data Access Object)**: Com `GenericDAO<T>` genérico para operações CRUD.
* **Frontend**:
  * **Desktop**: Java Swing (`TransportControl.java`) com visual moderno Nimbus Look & Feel.
  * **Web**: React 18, TypeScript, Vite, Tailwind CSS, Radix UI e Lucide Icons (Protótipo modular).

---

## 📁 Estrutura do Repositório

```text
trabalho_labbd/
├── .idea/                           # Configurações de workspace do IntelliJ IDEA
├── front_end/                       # Interfaces com o usuário
│   ├── src/                         # Código-fonte da aplicação React
│   │   ├── api/                     # Módulos de mock/acesso a dados (veículos, motoristas, viagens...)
│   │   ├── app/                     # Componentes de páginas, diálogos e layout
│   │   └── styles/                  # Folhas de estilo (Tailwind CSS)
│   ├── TransportControl.java        # Interface Desktop completa em Java Swing
│   ├── package.json                 # Dependências do frontend Web
│   └── vite.config.ts               # Configuração do Vite
├── sql/                             # Scripts de Banco de Dados MySQL
│   ├── frota.sql                    # DDL: Criação do banco de dados, tabelas e constraints
│   ├── procedure.sql                # Stored Procedures (ENCERRAR_VIAGEM, REGISTRAR_MANUTENCAO)
│   ├── trigger.sql                  # Triggers (TRG_VALIDA_CNH, TRG_ALERTA_MANUTENCAO)
│   └── insert.sql                   # Carga de dados iniciais para testes
├── src/main/                        # Código-fonte do Backend Java
│   ├── java/
│   │   ├── lab_bd_class/            # Entidades JPA (Veiculo, Motorista, Viagem, etc.) e TesteBackend.java
│   │   └── lab_bd_DAO/              # DAOs especializados (GenericDAO, VeiculoDAO, etc.) e TesteDAO.java
│   └── resources/
│       ├── META-INF/persistence.xml # Configuração da Persistence Unit 'LabBD'
│       └── application.properties   # Configurações complementares
├── pom.xml                          # Configuração de build do Maven
└── README.md                        # Documentação do projeto
```

---

## ⚙️ Regras de Negócio e Banco de Dados

### Tabelas e Modelagem
* **`VEICULO`**: Chave primária `PLACA`. Garante via `CHECK` que o ano seja $\ge 1950$, odômetro $\ge 0$, tipos válidos (`CARRO`, `VAN`, `CAMINHAO`) e status válidos (`DISPONIVEL`, `EM_ROTA`, `MANUTENCAO`).
* **`MOTORISTA`**: Chave primária `CPF (11)`. Valida categoria da CNH (`B`, `C`, `D`, `E`).
* **`VIAGEM`**: Registra saída, chegada, odômetro inicial e final, associando veículo e motorista via Foreign Keys. Garante que `KM_FINAL >= KM_INICIAL`.
* **`MANUTENCAO`**: Cadastro de manutenções preventivas com quilometragem de referência (`KM_REFERENCIA`).
* **`MANUTENCAO_VEICULO`**: Tabela associativa com histórico, data de geração, data de realização, odômetro e status (`PENDENTE`, `EM_ANDAMENTO`, `REALIZADA`, `CANCELADA`).

### Triggers
1. **`TRG_VALIDA_CNH`** *(BEFORE INSERT ON VIAGEM)*:
   * Impede que um motorista portador apenas de CNH categoria **'B'** seja vinculado a uma viagem com veículo do tipo **'CAMINHAO'**, emitindo erro de validação (`SQLSTATE '45000'`).
2. **`TRG_ALERTA_MANUTENCAO`** *(AFTER UPDATE ON VEICULO)*:
   * Sempre que o odômetro de um veículo é atualizado, calcula matematicamente se um múltiplo da quilometragem de referência de qualquer manutenção cadastrada foi atingido (`FLOOR(OLD / KM) < FLOOR(NEW / KM)`).
   * Em caso positivo, gera automaticamente um registro de manutenção com status `PENDENTE` na tabela `MANUTENCAO_VEICULO`.

### Stored Procedures
1. **`ENCERRAR_VIAGEM(P_ID_VIAGEM, P_KM_FINAL)`**:
   * Executa em bloco transacional (`START TRANSACTION ... COMMIT`).
   * Valida se `P_KM_FINAL >= KM_INICIAL`.
   * Atualiza a viagem com a data/hora atual e km final.
   * Atualiza a quilometragem atual do veículo e restabelece seu status para `'DISPONIVEL'`.
2. **`REGISTRAR_MANUTENCAO(P_PLACA, P_ID_MANUTENCAO, P_KM_FEITO)`**:
   * Atualiza o registro pendente mais antigo daquele veículo para `'REALIZADA'`, preenchendo a data atual e odômetro da realização.

---

## 🔍 Diagnóstico e Status de Funcionamento

| Módulo | Status | Observações |
| :--- | :---: | :--- |
| **Banco de Dados (MySQL)** | **Pronto** | Scripts DDL, DML, triggers e procedures com lógica correta e consistente. |
| **Backend (Java / JPA)** | **Corrigido** | Necessitava de correções no `persistence.xml` e nos arquivos de teste (veja abaixo). |
| **Frontend Desktop (Swing)** | **100% Funcional** | Compila e roda nativamente; interface autônoma em memória. |
| **Frontend Web (React/Vite)** | **Funcional (Mock)** | Interface rica moderna, operando com dados mockados em memória. |

### 🛠️ Correções Aplicadas no Código-Fonte:
1. **Nome da Persistence Unit em `TesteBackend.java`**:
   * Alterado de `"meu-projeto-pu"` para `"LabBD"`, alinhando à declaração em `persistence.xml`.
2. **Mapeamento de Entidades em `persistence.xml`**:
   * Corrigido o nome da classe de `<class>lab_bd_class.ManutencaoRealizada</class>` para `<class>lab_bd_class.ManutencaoVeiculo</class>`, evitando `ClassNotFoundException`.
3. **Formatação de Saída em `TesteBackend.java`**:
   * Corrigido o especificador `%d` para `%.0f` no campo `Double kmReferencia`, prevenindo `IllegalFormatConversionException`.
4. **Compatibilidade no `pom.xml`**:
   * Alvo de compilação ajustado de Java 25 para Java 21 LTS.
5. **Correção no `trigger.sql`**:
   * Removido o comando `DELIMITER $$` duplicado.
6. **Configuração de Git**:
   * Adicionado `node_modules/` ao `.gitignore` para otimizar clones futuros.

---

## 🚀 Como Configurar e Executar

### 1. Configurar o Banco de Dados (MySQL)
Certifique-se de que o serviço do MySQL esteja ativo (ex.: porta padrão `3306`).

Execute os scripts SQL na pasta `sql/` na seguinte ordem:
```bash
# 1. Criação do schema e tabelas
mysql -u root -p < sql/frota.sql

# 2. Criação das Stored Procedures
mysql -u root -p frota < sql/procedure.sql

# 3. Criação das Triggers
mysql -u root -p frota < sql/trigger.sql

# 4. Inserção de dados de teste
mysql -u root -p frota < sql/insert.sql
```

> [!NOTE]
> Se estiver utilizando o MySQL Workbench ou DBeaver, abra e execute os scripts individualmente respeitando a ordem acima.

---

### 2. Configurar e Executar o Backend (Java / JPA)

1. Abra o arquivo `src/main/resources/META-INF/persistence.xml` e confirme se o usuário e a senha do seu MySQL estão corretos:
   ```xml
   <property name="jakarta.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/frota"/>
   <property name="jakarta.persistence.jdbc.user" value="root"/>
   <property name="jakarta.persistence.jdbc.password" value="SUA_SENHA_AQUI"/>
   ```

2. **Compilar e Rodar os Testes**:
   Abra o projeto no **IntelliJ IDEA**, **Eclipse** ou **VS Code** e execute uma das classes com método `main`:
   * **`lab_bd_DAO.TesteDAO`**: Testa a criação, busca, atualização e listagem de Motoristas, Veículos e Manutenções via DAOs.
   * **`lab_bd_class.TesteBackend`**: Executa um ciclo completo de operações com JPA (criação de viagem, encerramento e consulta a manutenções).

---

### 3. Executar o Frontend Desktop (Java Swing)

Para abrir a interface gráfica desktop sem necessidade de ferramentas externas:
```bash
cd front_end
javac TransportControl.java
java TransportControl
```
A janela do **TransportControl** será aberta com gerenciamento visual de veículos e motoristas.

---

### 4. Executar o Frontend Web (React + Vite)

Caso queira explorar o dashboard web moderno:

1. Certifique-se de ter o [Node.js](https://nodejs.org/) instalado.
2. Acesse a pasta do frontend e instale as dependências:
   ```bash
   cd front_end
   npm install
   ```
3. Inicie o servidor local:
   ```bash
   npm run dev
   ```
4. Abra o endereço indicado no terminal (normalmente `http://localhost:5173`) no navegador.

---

## 💡 Melhorias e Próximos Passos

- [ ] **Criar uma API REST**: Adicionar uma camada com Spring Boot (`@RestController`) ou Javalin/JAX-RS para expor os DAOs como endpoints HTTP (`/api/veiculos`, `/api/viagens`).
- [ ] **Conectar o Frontend Web à API**: Substituir as stores em memória de `front_end/src/api/*.ts` por requisições `fetch` / `axios` consumindo o backend Java.
- [ ] **Limpeza do Histórico Git**: Executar `git rm -r --cached front_end/node_modules` para remover os arquivos de bibliotecas já versionados e diminuir o tamanho do repositório.

---

## 👥 Autores
Desenvolvido como projeto prático para a disciplina de **Laboratório de Banco de Dados**.
Repositório original: [github.com/Vsmendz/trabalho_labbd](https://github.com/Vsmendz/trabalho_labbd)
