
# Gerenciamento de vendas em um granja de patos
## API Rest Backend Java

![Java](https://img.shields.io/badge/Java-v17-blue)
![Spring](https://img.shields.io/badge/Spring-v3.3.4-green)
![MySQL](https://img.shields.io/badge/MySQL-v8.0.38-blue)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ_IDEA-v2024.2.3-black)
![JasperReports](https://img.shields.io/badge/JasperReports-v6.21.2-orange)
![Apache POI](https://img.shields.io/badge/Apache_POI-v5.2.3-red)
![Postman](https://img.shields.io/badge/Postman-v11.14-blueviolet)

Este projeto é uma API construída usando **Java, Java Spring, MySQL, IntelliJ IDEA, JasperReport, Apache POI e Postman.**

API Rest usando Java/Spring Boot para gerenciar vendas de uma granja de patos. Os patos devem ser cadastrados individualmente, indicando sempre a identificação da mãe de cada pato (exceto mães). A venda será feita definindo a identificação dos patos vendidos para um cliente cadastrado no sistema. Caso o cliente seja elegível a desconto, será aplicado um desconto de 20% na venda. O sistema deve gerar relatórios de gerenciamento nos formatos excel e PDF.

Os preços dos patos seguem a seguinte regra:

**Pato com um filho: R$ 50,00**

**Pato com dois filhos: R$ 25,00**

**Pato sem filhos: R$ 70,00**


## Pré-requisitos
- JDK 17 ou superior;
- Maven 3 ou superior;
- IDE java, exemplo: IntelliJ IDEA, para análise de código;
- Postman ou software similares para as chamadas de Endpoints;





## Instalação

1. Clone o repositório:

```bash
git clone https://github.com/fvmvalente/granjapatos.git
```

2. Abra a pasta do projeto utilizando uma IDE Java, Intellij IDEA por exemplo.

3. Verifique as configurações das variáveis de ambiente e das credenciais MySQL que são usadas em `application.yml` e altere para as suas credencias caso esteja usando banco MySQL local ou deixe as configurações padrão. Após escrever a url de acesso de acordo com a sua configuração, adicionar **?createDatabaseIfNotExist=true** para um melhor funcionamento do banco de dados. Exemplo: **jdbc:mysql://localhost:3306/granjapatos?createDatabaseIfNotExist=true**

```spring:
  datasource:
    driver-class-name: 
    url:
    username:
    password:
```
Com isso, o banco de dados deve ser criado sem problemas.

O acesso a API será feito em http://localhost:8080
## Documentação da API

### Endpoints

#### Cadastro de cliente

```http
POST /cliente
```
```
{
    "nome": "Ana",
    "ativo": "true",
    "tipo": "COM_DESCONTO"
}
```

#### Cadastro de pata mãe

```http
POST /pato
```

```
{
    "nome": "Pata mãe 1",
    "tipo": "MAE",
    "status": "DISPONIVEL"
}
```

#### Cadastro de pato filho

```http
POST /pato
```
```
{
    "nome": "Pato filho 1",
    "tipo": "FILHO",
    "status": "DISPONIVEL",
    "mae_id": "1"
}
```
#### Registro de venda

```http
POST /venda
```
```
{
    "cliente_id": "1",
    "patosIds": [
                {"id":"3"},
                {"id":"6"}
                ],
    "ativo": "true"
}
```
#### Busca por patos vendidos

```http
GET /pato/vendidos
```

#### Relatório em PDF

```http
GET /relatorio/pdf
```

#### Relatório em Excel

```http
GET /relatorio/excel
```