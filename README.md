# Golden Raspberry Awards API

Esta API RESTful permite a leitura da lista de indicados e vencedores da categoria Pior Filme do Golden Raspberry Awards e fornece informações sobre os produtores com maior e menor intervalo entre prêmios consecutivos.

## Como Funciona

A aplicação lê os dados a partir de um arquivo CSV e os armazena em um banco de memória H2. O caminho padrão do arquivo CSV pode ser alterado via argumento de execução. O banco H2 é acessível via interface web para facilitar a inspeção dos dados carregados.

## Requisitos

### Usando Maven
- Java 21+
- Maven

### Usando Docker
- Docker

## Como Rodar o Projeto

### Usando Maven

1. Clone o repositório:
   ```bash
   git clone https://github.com/felipetenfen/golden-raspberry-awards.git
   cd golden-raspberry-awards
   ```

2. Compile o projeto:
   ```bash
   mvn clean install
   ```

3. Rode a aplicação:
   ```bash
   mvn spring-boot:run
   ```

4. Caso desejar rodar a aplicação com um arquivo de entrada diferente do definido:
   ```bash
   mvn spring-boot:run 
   -Dspring-boot.run.arguments="--spring.csv.file-path=/caminho_arquivo/nome_arquivo.csv"
   ```

### Usando Docker

1. Baixe a imagem Docker:  
    ```bash
    docker pull lipetenfen/golden-raspberry-awards:1.3
    ```
   
2. Execute o container:
    ```bash
    docker run -d -p 8080:8080 --name golden-raspberry-awards lipetenfen/golden-raspberry-awards:1.3
    ```

3. Para rodar a aplicação com um arquivo de entrada diferente do definido:
    ```bash
    docker run -p 8080:8080 \
   -v /caminho_arquivo/nome_arquivo.csv:/app/nome_arquivo.csv \
   lipetenfen/golden-raspberry-awards:1.3 --spring.csv.file-path=/app/nome_arquivo.csv

    ```

4. Acesse a aplicação em seu navegador: 
  http://localhost:8080

## Endpoints

- `GET /awards/intervals`: Retorna os produtores com maior e menor intervalo entre prêmios consecutivos.

## Testes de Integração

Para rodar os testes de integração usando Maven, execute:
   ```bash
    mvn test
   ```

Os testes de integração garantem que os dados retornados pela API estão corretos de acordo com os dados fornecidos.

## Documentação da API

A documentação da API pode ser acessada através do Swagger UI. Siga os passos abaixo para acessar a documentação:

1. Certifique-se de que a aplicação está em execução.
2. Abra o navegador e acesse a URL: http://localhost:8080/swagger-ui.html

A partir dessa URL, você poderá visualizar e interagir com os endpoints da API.

## Banco de Dados H2
A aplicação utiliza o banco de memória H2 para armazenar os dados extraídos do arquivo CSV. Para acessar o banco:

1. Certifique-se de que a aplicação está em execução.
2. Acesse o console do H2 no navegador: http://localhost:8080/h2-console
3. Use as seguintes configurações para login:
   - `JDBC URL: jdbc:h2:mem:testdb`
   - `Usuário: sa`
   - `Senha: (deixe em branco)`

Isso permitirá a visualização e manipulação dos dados diretamente no banco de memória.