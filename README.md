# Mutant API

## Tecnologias utilizadas
- Java 10
- SpringBoot
- Programação reativa com Spring WebFlux
- MongoDB
- Docker & Docker-Compose
- Kubernetes

## Instruções de como rodar a API

- Para rodar a api em ambiente local é necessário ter instalado na máquina os 
seguintes recursos:
  - Docker
  - Docker-Compose
  
  Com os recursos instalados executar os comandos, partindo da pasta principal da aplicação.
  ###### ex ambiente unix
  ```
  $ ./gradlew bootJar
  $ docker-compose up   
  ```

## Ambiente Cloud
Foi criado um cluster na google cloud para realizar o deploy da api e do banco de dados.

  - Para acessar os recursos basta substituir as urls nos exemplos abaixo pelos listados aqui:
  
    - ` http://35.241.10.77/mutant `
    - ` http://35.241.10.77/stats `

## Instruções de como testar a API

- Para verificar se um dna pertence a um mutante, enviar uma chamada POST para a uri `` /mutant `` passando um dna como parametro.
  ###### ex usando curl
  ``` 
  $ curl -X POST -v \
    -d '{"dna":["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]}' \
    -H "Content-type: application/json"  \
    http://localhost:8080/mutant
  ```

  O resultado positivo para mutante retornará status 200 (OK) e negativo retornará status 403 (FORBIDDEN)

###
- Para verificar as estatísticas da verificação de mutantes basta enviar uma chamada GET para a uri `` /stats ``.
  ###### ex usando curl
  ``` 
  $ curl http://localhost:8080/stats 
  ```
  Resultado:
  ``` 
  {"count_mutant_dna":40,"count_human_dna":100,"ratio":0.4} 
  ```

