# API simples de pagamentos 

## Descrição
Esta é a resolução de um desafio técnico que era usado em entrevistas, pode encontrar o desafio aqui: https://github.com/PicPay/picpay-desafio-backend

## Objetivos

- [x] Permitir a criação de usuários
- [x] Permitir que esses usuários façam transferências entre si.
- [x] Não permitir que usuários do tipo lojista consigam realizar a transferência, apenas receber
- [x] Consultar um autorizador externo
- [x] Usar um serviço externo para notificar

##  Tecnologias Utilizadas

- **Java 21**
-  **Spring Boot**

### Dependências 
-  **JPA**
-  **Spring Boot Web**
-  **Spring Security**
-  **Spring Validation**
- **h2 (banco em menória)**

## Pré-requisitos

- JDK - versão 21 ou superior
-  maven - versão 4.0.0 ou superior

## Como Executar

### 1. Clone o repositório
```bash
git clone https://github.com/vini-basilio/api-pagamentos-simples.git
cd api-pagamentos-simples
```

### 2. Instale as dependências
```bash
maven install
```

### 3. Configure as variáveis de ambiente
```env
notification=https://util.devi.tools/api/v1/notify  
authorization=https://util.devi.tools/api/v2/authorize
```


## Testes

Os testes estão dentro da pasta de testes do Spring, no caminho `src/test`

## Documentação da API

### Endpoints Principais

#### GET `/user`
- **Descrição:**  Retorna uma lista de todos usuários. Se não houver usuários, retorna uma lista vazia.
- **Resposta:** 
```json
[
	{
		"id": "d93e92bf-2aa8-4f52-9f5c-757a63a59f9e",
		"firstName": "joão",
		"lastName": "silva",
		"balance": 1000.00,
		"email": "joao.silva@example.com",
		"userType": "COMMON"
	}
]
```

#### POST `/user`
- **Descrição:** Cria um novo usuário
- **Body:**
```json
{
	"firstName":"joão",
	"lastName":"silva",
	"document":"123.456.789-01",
	"balance": 1000.00,
	"email":"joao.silva@example.com",
	"password": "senhaSegura123",
	"userType": "COMMON"
}
```

- **Resposta:** 
```json

{
		"id": "d93e92bf-2aa8-4f52-9f5c-757a63a59f9e",
		"firstName": "joão",
		"lastName": "silva",
		"balance": 1000.00,
		"email": "joao.silva@example.com",
		"userType": "COMMON"
}
```

#### POST `/transfer`
- **Descrição:** Cria um nova transferência
- **Body:**
```json
{
	"value": 5.00,
	"payer":"09f85f43-1cf9-4b38-9724-78d7b081c434", 
	"payee":"d93e92bf-2aa8-4f52-9f5c-757a63a59f9e",
}
```

- **Resposta:** 
```json
{
    "id": "e64ef5b3-bd9c-4bcc-ba12-b30e222c7c81",
    "payer": {
        "id": "647ec499-b497-4bd7-81de-d1cf5ef6353e",
        "firstName": "joão",
        "lastName": "silva",
        "balance": 995.00,
        "email": "joao.silva@example.com",
        "userType": "COMMON"
    },
    "payee": {
        "id": "4be0cb09-20b9-42a7-a24d-38c3beca7b60",
        "firstName": "maria",
        "lastName": "silva",
        "balance": 1005.00,
        "email": "maria@email.com",
        "userType": "COMMON"
    }
}
```
##  Funcionalidades Adicionais Implementadas

-  Logs: criação de usuários e transferências autorizadas geram logos, tentativas de notificações.
- Validação de DTO: as requisições passam por um filtro antes de entrar nas classes de serviço.

