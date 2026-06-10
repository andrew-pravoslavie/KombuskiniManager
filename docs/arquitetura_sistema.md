Arquitetura do Sistema Kombuskini Manager
1. Objetivo do Projeto

O Kombuskini Manager é um sistema desktop desenvolvido em Java para auxiliar no gerenciamento de clientes, produtos, estoque e vendas de kombuskinis.

O projeto foi estruturado seguindo a arquitetura proposta na disciplina, baseada nas camadas:

Fronteira (Boundary);
Controle (Control);
Entidade (Entity);
DAO (Data Access Object).

Além disso, o sistema deve atender aos requisitos do trabalho final:

CRUD completo para entidades principais;
utilização de TableView para exibição de dados;
validação e tratamento de entradas do usuário;
mensagens de erro e sucesso;
persistência em banco de dados utilizando JDBC;
separação adequada das responsabilidades entre as camadas.
2. Tecnologias Utilizadas

O projeto utiliza as seguintes tecnologias:

Java 21;
Gradle;
JavaFX;
FXML;
MySQL;
JDBC;
Docker Compose;
dotenv-java.
3. Estrutura Atual do Projeto

A estrutura principal do projeto encontra-se organizada da seguinte forma:

KombuskiniManager/
├── build.gradle.kts
├── docker-compose.yml
├── database/
│   └── schema.sql
├── docs/
│   └── arquitetura_sistema.md
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── br/com/kombuskini/
│   │   │       ├── Main.java
│   │   │       ├── boundary/
│   │   │       │   ├── controller/
│   │   │       │   └── repository/
│   │   │       ├── control/
│   │   │       ├── entity/
│   │   │       └── util/
│   │   └── resources/
│   │       ├── br/com/kombuskini/view/
│   │       ├── db/
│   │       └── images/
│   └── test/
└── README.md

A pasta build/ não faz parte da arquitetura lógica do sistema, pois contém apenas arquivos gerados automaticamente durante a compilação.

4. Arquitetura em Camadas

O sistema segue o modelo BCE + DAO solicitado na disciplina.

graph TD
Usuario[Usuário]
View[Telas FXML]
Boundary[Controllers]
Control[Camada de Controle]
Entity[Entidades]
DAO[DAOs]
Banco[(MySQL)]

    Usuario --> View
    View --> Boundary
    Boundary --> Control
    Control --> Entity
    Control --> DAO
    DAO --> Banco
Responsabilidades das Camadas
Camada	Responsabilidade
Fronteira	Interação com o usuário
Controle	Casos de uso e regras de aplicação
Entidade	Regras de domínio e representação dos objetos
DAO	Persistência e consultas ao banco
5. Camada de Fronteira (Boundary)

A camada de Fronteira é responsável pela comunicação entre o usuário e o sistema.

Localização
src/main/java/br/com/kombuskini/boundary/controller/
src/main/resources/br/com/kombuskini/view/
Controllers Implementados
ClienteCrudController.java
EstoqueController.java
MainController.java
ProdutoCrudController.java
VendaController.java
Telas FXML
cliente_crud.fxml
estoque_gerenciador.fxml
main.fxml
produto_crud.fxml
venda_gerenciador.fxml
Responsabilidades
receber dados digitados pelo usuário;
atualizar componentes gráficos;
preencher TableView;
exibir mensagens de erro e sucesso;
encaminhar operações para a camada de Controle;
não acessar diretamente o banco de dados.
6. Camada de Controle (Control)

A camada de Controle coordena os casos de uso do sistema.

Localização
src/main/java/br/com/kombuskini/control/
Classes Atuais
ClienteControl.java
EstoqueControl.java
VendaControl.java
Responsabilidades
ClienteControl
cadastrar clientes;
editar clientes;
excluir clientes;
listar clientes;
validar informações antes da persistência.
EstoqueControl
controlar entrada de produtos;
controlar saída de produtos;
consultar estoque;
validar quantidades.
VendaControl
criar pedidos;
adicionar itens ao pedido;
registrar pedidos;
enviar pedidos;
finalizar pedidos;
controlar mudanças de status.
7. Camada de Entidade (Entity)

As entidades representam os objetos do domínio do sistema.

Localização
src/main/java/br/com/kombuskini/entity/
Entidades Existentes
Cliente.java
Estoque.java
ItemEstoque.java
ItemPedido.java
Kombuskini.java
Pedido.java
Cliente

Representa um cliente cadastrado.

Campos principais:

id;
nome;
telefone;
instagram;
email.

Validações implementadas:

nome obrigatório;
pelo menos uma forma de contato obrigatória.
Kombuskini

Representa um produto vendido pela empresa.

Campos principais:

id;
cor;
quantidade de nós;
tipo de divisões;
quantidade de divisões;
tipo de cruz;
tassel;
preço.

Essa entidade atende ao requisito de possuir mais de cinco atributos relevantes para o CRUD.

Pedido

Representa uma venda realizada para um cliente.

Campos principais:

id;
cliente;
itens;
status.

Status disponíveis:

RASCUNHO
REGISTRADO
ENVIADO
FINALIZADO

Regras implementadas:

somente pedidos em rascunho podem receber itens;
pedidos vazios não podem ser registrados;
apenas pedidos registrados podem ser enviados;
apenas pedidos enviados podem ser ser finalizados.
ItemPedido

Representa um item dentro de um pedido.

Campos:

produto;
quantidade.

Validações:

produto obrigatório;
quantidade maior que zero.
Estoque e ItemEstoque

Responsáveis pelo controle das quantidades disponíveis dos produtos cadastrados.

8. Camada DAO

A camada DAO é responsável pela persistência dos dados.

Localização
src/main/java/br/com/kombuskini/boundary/repository/
Interfaces DAO
ClienteDAO.java
KombuskiniDAO.java
PedidoDAO.java
Implementações MySQL
DatabaseConnection.java
MySQLClienteDAO.java
MySQLKombuskiniDAO.java
MySQLPedidoDAO.java
Responsabilidades
inserir registros;
atualizar registros;
excluir registros;
consultar registros;
encapsular comandos SQL;
fornecer acesso ao banco para a camada de Controle.
9. Banco de Dados

O sistema utiliza MySQL como mecanismo de persistência.

Arquivos Relacionados
database/schema.sql

e

src/main/resources/db/schema.sql
Conexão

A conexão é centralizada na classe:

DatabaseConnection.java

Utilizando:

JDBC;
DriverManager;
variáveis de ambiente carregadas pelo dotenv.
10. CRUDs do Sistema

Conforme solicitado no trabalho, o sistema possui dois CRUDs principais.

CRUD de Clientes
Tela
cliente_crud.fxml
Controller
ClienteCrudController.java
Entidade
Cliente.java
DAO
ClienteDAO.java
MySQLClienteDAO.java
Operações
cadastrar;
listar;
editar;
excluir;
validar dados;
exibir mensagens de erro.
CRUD de Produtos
Tela
produto_crud.fxml
Controller
ProdutoCrudController.java
Entidade
Kombuskini.java
DAO
KombuskiniDAO.java
MySQLKombuskiniDAO.java
Operações
cadastrar;
listar;
editar;
excluir;
validar campos obrigatórios;
validar campos numéricos;
exibir mensagens de erro.
11. Funcionalidades Complementares
    Gerenciamento de Estoque

Tela:

estoque_gerenciador.fxml

Classes relacionadas:

EstoqueController.java
EstoqueControl.java
Estoque.java
ItemEstoque.java

Funcionalidades:

visualizar estoque;
adicionar quantidade;
remover quantidade;
impedir estoque negativo.
Gerenciamento de Vendas

Tela:

venda_gerenciador.fxml

Classes relacionadas:

VendaController.java
VendaControl.java
Pedido.java
ItemPedido.java
PedidoDAO.java
MySQLPedidoDAO.java

Funcionalidades:

criar pedidos;
adicionar produtos;
registrar pedidos;
enviar pedidos;
finalizar pedidos;
controlar status.
12. Atendimento aos Critérios do Trabalho
    Critério 1 — Arquitetura em Camadas

O projeto está organizado em:

Fronteira;
Controle;
Entidade;
DAO.

Atendendo ao modelo solicitado pela disciplina.

Critério 2 — CRUD Completo

Os CRUDs principais são:

Clientes;
Produtos.

Ambos utilizam TableView para exibição dos registros.

Critério 3 — Tratamento de Dados

Validações previstas:

nome obrigatório;
contato obrigatório;
preço positivo;
quantidade positiva;
produto obrigatório.

Mensagens de erro devem ser exibidas diretamente ao usuário.

Critério 4 — Persistência

Persistência realizada através de:

JDBC;
MySQL;
DAOs especializados.
13. Estado Atual do Projeto

Atualmente o projeto possui:

estrutura Gradle configurada;
Docker Compose configurado;
JavaFX integrado;
telas FXML criadas;
controllers implementados;
entidades implementadas;
estrutura DAO criada;
conexão com banco configurada;
schema inicial criado.

Pendências atuais:

implementação completa dos métodos SQL;
integração total entre controllers e controles;
carregamento automático das tabelas;
validações visuais completas;
testes dos fluxos principais.
14. Fluxo Geral de Execução

Fluxo principal do sistema:

Usuário
↓
Tela FXML
↓
Controller
↓
Control
↓
Entity
↓
DAO
↓
Banco MySQL

Exemplo de cadastro de cliente:

Usuário
↓
ClienteCrudController
↓
ClienteControl
↓
Cliente
↓
ClienteDAO
↓
MySQL
15. Considerações Finais

A arquitetura atual do Kombuskini Manager segue o modelo solicitado pela disciplina e está organizada em camadas bem definidas.

A estrutura existente já contempla os principais componentes necessários para o trabalho final: interface gráfica em JavaFX, entidades de domínio, camada de controle, persistência via DAO e integração com banco MySQL.

As próximas etapas concentram-se na conclusão dos DAOs, integração completa dos CRUDs, validações de interface e finalização dos fluxos de estoque e vendas.