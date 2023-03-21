# Tax Calculator

- Este sistema tem como objetivo cadastrar um usuário e calcular o valor do imposto de renda.


- POST: /[tax-api] Create(WorkerDTO request)
- DELETE: /[id] delete (Long id)
- POST: /[calculator] taxCalculator (WorkerAdmDTO admDTO)

## Model

- Worker: 
```json
    "name": "Teste Teste",
    "age": "25",
    "email": "teste@dominio.com",
    "password": "password",
    "office": "Manager",
    "salary": 7000.00
```

# Regras de negócio

- O mesmo e-mail só poderá ser cadastrado uma vez [TO-DO]
- Nome e sobrenome não podem passar de 100 caracteres, e não pode conter caracteres especiais ou número [TO-DO]
- A senha deve conter de 8 a 16 caracteres, um dígito, uma letra minúscula, uma letra maiúscula, um caractere especial e não conter espaço em branco [OK]
- O delete será lógico que irá inativar o usuário [OK]
- Sálarios iguais ou menores que R$1.903,98 deverão informar ao trabalhador que ele esta isento de pagar o imposto de renda, pois são isentos [OK]

## Tecnologias

- Java 11
- Spring Boot
- Spring Validation
- LOMBOK
- BCrypt
- Docker
  - Utilizaremos dockerfile para subir as dependências do banco de dados (com aberturas para possíveis melhorias e melhor aproveitamento do docker)
- Spring Data JPA (MySQL)