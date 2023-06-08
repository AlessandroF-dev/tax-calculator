# Tax Calculator

- Este sistema tem como objetivo cadastrar um usuário e calcular o valor do imposto de renda.


- POST: /[tax-api] Create(WorkerDTO request)
- DELETE: /[id] delete (Long id)
- POST: /[calculator] taxCalculator (WorkerAdmDTO admDTO)
- GET: /[taxMeter] impostometro ()
- GET: /[] getAll()
- 
## Model

- Worker: 

```json:
    "name": "Teste Teste",
    "age": "25",
    "email": "teste@dominio.com",
    "password": "password",
    "office": "Manager",
    "salary": 7000.00
```

# Regras de negócio

- Implementar front-end com Angular [TO-DO]
- Refatorar código para limpeza dos métodos [TO-DO]
- Documentar projeto com swagger [OK]
- O mesmo e-mail só poderá ser cadastrado uma vez [OK]
- A senha deve conter de 8 a 16 caracteres, um dígito, uma letra minúscula, uma letra maiúscula, um caractere especial e não conter espaço em branco [OK]
- O delete será lógico que irá inativar o usuário [OK]
- Sálarios iguais ou menores que R$1.903,98 deverão informar ao trabalhador que ele esta isento de pagar o imposto de renda, pois são isentos [OK]

## Tecnologias

- Java 11
- Spring Boot
- Spring Validation
- Spring cache
- Spring Data JPA
- LOMBOK
- BCrypt
- Docker
  - Utilizaremos dockerfile para subir as dependências do banco de dados (com aberturas para possíveis melhorias e melhor aproveitamento do docker)
