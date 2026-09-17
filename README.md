# Sistema Confeitaria

Sistema desktop de gestão para confeitaria, desenvolvido para uso local, sem necessidade de internet. Feito sob medida para organizar produtos, clientes, vendas e o caixa de um pequeno negócio de confeitaria.

📋 Sobre o projeto

O Sistema Confeitaria foi criado para resolver um problema real: dar a uma confeiteira uma ferramenta simples e bonita para controlar o dia a dia do negócio, sem depender de planilhas soltas ou papel. Todos os dados ficam salvos localmente, em um banco SQLite, garantindo que nada se perca e que o app funcione sem internet.

✨ Funcionalidades
📊 Dashboard — visão geral do negócio: saldo atual, faturamento do mês, vendas do dia, despesas do mês, produtos e clientes cadastrados, gráfico de vendas dos últimos 7 dias e ranking dos doces mais vendidos.
🧁 Produtos — cadastro, edição, ativação/desativação e exclusão de produtos, com preços formatados automaticamente (máscara de moeda em tempo real).
👥 Clientes — cadastro e gerenciamento da base de clientes.
🛒 Vendas — montagem de carrinho com múltiplos produtos por venda, seleção de cliente, forma de pagamento (Pix, Cartão de Crédito, Cartão de Débito, Dinheiro, Outros) e histórico completo de vendas realizadas.
💰 Caixa — controle de entradas e saídas, com geração automática de entrada no caixa a cada venda finalizada, saldo do mês e visual em tons pastel.
🛠️ Tecnologias
Java 21
JavaFX 21 — interface gráfica desktop
Maven — gerenciamento de dependências e build
SQLite (JDBC) — persistência local dos dados
CSS — estilização customizada das telas (sidebar, cards, tabelas, gráficos)
🏗️ Arquitetura

O projeto segue uma separação clara entre camadas:

Modelo de domínio: Produto, Cliente, Venda, ItemVenda (classe associativa produto + quantidade + preço congelado no momento da venda), MovimentoCaixa (classe abstrata) com subclasses Entrada e Saida usando polimorfismo para cálculo de saldo.
Persistência: um Repository/DAO por entidade (ProdutoRepository, ClienteRepository, VendaRepository, MovimentoCaixaRepository), usando JDBC puro com PreparedStatement e transações (commit/rollback) nas operações que envolvem múltiplas tabelas, como o registro de uma venda com seus itens.
Regras de negócio: RelatorioCaixa, responsável pelo cálculo de saldo, saldo do mês, total de entradas e total de saídas.
Interface: telas em FXML + Controllers (padrão MVC do JavaFX), com um layout principal (main-layout.fxml) contendo a sidebar e trocando o conteúdo central conforme a navegação.
🚀 Como rodar
Pré-requisitos
JDK 21 instalado
Maven (ou usar o Maven integrado do IntelliJ, já que o projeto não depende de mvn estar no PATH)
Executando pela IDE
Clone o repositório
Abra o projeto no IntelliJ IDEA
No painel Maven, rode o goal javafx:run (dentro de AppConfeitaria > Plugins > javafx)
Gerando o executável (.exe)

O projeto usa maven-shade-plugin + jpackage para gerar um executável standalone para Windows, já que o layout de pacote padrão (sem módulos) não é compatível com jlink:

bash
mvn clean package
jpackage --input target --main-jar AppConfeitaria-1.0-SNAPSHOT.jar --main-class Launcher --name AppConfeitaria --type app-image --dest saida

Isso gera a pasta saida/AppConfeitaria/, contendo o AppConfeitaria.exe e o runtime Java embutido — pronta para copiar para qualquer computador Windows, sem precisar instalar Java.

Para gerar um instalador .exe (em vez de apenas a pasta executável), é necessário ter o WiX Toolset instalado e trocar --type app-image por --type exe.

📌 Status do projeto

Projeto pessoal em desenvolvimento contínuo. Backend (modelagem, persistência e regras de negócio) e as telas principais (Dashboard, Produtos, Clientes, Vendas, Caixa) já estão funcionando de ponta a ponta.

📄 Licença

Este projeto está sob a licença MIT.