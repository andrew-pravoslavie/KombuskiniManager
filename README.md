# KombuskiniManager

## Integrantes:

- Bruno Calvacante de Lira
- Gabriel Andrew Borges Gomes

## Tema escolhido:

O tema escolhido foi um sistema de gerenciamento de vendas e cadastros, para
facilitar o acompanhamento de pedidos, produtos, clientes e vendas.

## Descrição do problema resolvido:

- Problema:
Falta de controle de clientes, falta de organização de disponibilidade dos próprios produtos e necessidade de gerenciamento de vendas para acompanhar o status de cada uma

## Lista de entidades implementadas:

- Cliente
- Estoque
- ItemEstoque
- Pedido
- ItemPedido
- Kombuskini

## Instruções para execução:

Com o docker instalado, navegue com o terminal até a pasta raiz do projeto, e dê o comando:

`docker compose up -d`

O docker carregará a imagem (baixará da internet caso não tenha) e iniciará o banco de dados com a database e a tabela já configurada para ser utilizada.
Após isso, ainda na pasta raiz do projeto, basta rodar o comando abaixo que o sistema iniciará:

`./gradlew run`

## Divisão de Responsabilidades:

- Gabriel: Responsável pelos CRUDs de Cliente e Kombuskini
- Bruno: Responsável pelos CRUDs de Pedidos e Estoque

## Link do Youtube:

https://youtu.be/7qE4Yioke8o