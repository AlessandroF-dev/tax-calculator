# Tax Calculator

- Este sistema tem como objetivo calcular o valor do imposto de renda.

## Endpoint

- POST: Create(WorkerDTO request)
- DELETE: inactive
- GET: /[login]&[salary]

## Model

"nomeESobrenome": "Teste Teste",
"idade": "25",
"login": "teste@dominio.com",
"password": "password",
"office": "Manager",
"salary": 7000.00

# Regras de negócio

- O mesmo e-mail só poderá ser cadastrado uma vez
- A senha deve conter no mínimo 7 letras e um caracter especial
- O delete ira inativar o usuário
- Sálarios iguais ou menores que R$1.903,98* não serão permitidos, pois são isentos
    - O salário não pode conter mais do que 10 dígitos
- Nome e sobrenome não podem passar de 30 caracteres, e não pode conter caracteres especiais ou número

## Tecnologias

- Java 11
- Spring Boot
- Spring Validation
- BCrypt
- Docker
- Spring Data JPA (MySQL)