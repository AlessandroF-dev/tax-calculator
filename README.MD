# Tax Calculator

- Este sistema tem como objetivo calcular o valor do imposto de renda.

## Endpoint

- POST: Create(WorkerDTO request)
- DELETE: delete (Long id)
- GET: /[login]&[password] login (String login, String password)
- GET: /[id] taxCalculator (Long id)

## Model

- Worker: 
```json
    "name": "Teste Teste",
    "age": "25",
    "login": "teste@dominio.com",
    "password": "password",
    "office": "Manager",
    "salary": 7000.00
```

# Regras de negócio

- O mesmo e-mail só poderá ser cadastrado uma vez 
- A senha deve conter de 8 a 16 caracteres, um dígito, uma letra minúscula, uma letra maiúscula, um caractere especial e não conter espaçõ em branco 
- O delete ira inativar o usuário
- Sálarios iguais ou menores que R$1.903,98 deverão informar ao trabalhador que ele esta isento de pagar o imposto de renda, pois são isentos 
- O salário não pode conter mais do que 10 dígitos 
- Nome e sobrenome não podem passar de 30 caracteres, e não pode conter caracteres especiais ou número

## Tecnologias

- Java 11
- Spring Boot
- Spring Validation
- LOMBOK
- BCrypt
- Docker
  - Utilizaremos dockerfile para subir as dependências do banco de dados (com aberturas para possíveis melhorias e melhor aproveitamento do docker)
- Spring Data JPA (MySQL)