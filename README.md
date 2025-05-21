# Sistema de E-Commerce
## Domínio
O sistema pertence ao domínio de comércio eletrônico (e-commerce), permitindo a conexão entre vendedores e compradores para negociação e compra de produtos.

## Escopo
- Cadastro de usuários e produtos
- Busca por produtos
- Gerenciamento de carrinho
- Finalização de pedidos
- Simulação de pagamento
- Envio de Notificações

## Requisitos Funcionais

- RF01 Cadastro e login de usuários.
- RF02 Cadastro de produtos à venda.
- RF03 Busca e listagem de produtos.
- RF04 Adicionar produtos ao carrinho.
- RF05 Finalização de pedidos com simulação de pagamento.
- RF06 Registro de histórico de pedidos.
- RF07 Envio de notificações de confirmação por e-mail.

 ![alt text](images/modelos-processos.png)


 ## Requisitos Não Funcionais

- RNF01	O sistema deve seguir arquitetura de microsserviços.
- RNF02 Cada serviço deve rodar em contêiner Docker separado.
- RNF03 O sistema deve responder às requisições em até 2 segundos.
- RNF04	O sistema deve expor APIs RESTful com documentação Swagger/OpenAPI.
- RNF05 O sistema deve estar preparado para escalabilidade horizontal.

## Modelo de Processo: Metodologia Ágil - Kanban

O Kanban foi escolhido por ser uma metodologia ágil enxuta, que permite visualizar o fluxo de trabalho, controlar o progresso das tarefas em tempo real e adaptar as prioridades com flexibilidade. 
