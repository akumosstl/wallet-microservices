## Spring-boot Microservices environment

Spring-boot microservices environment example.

### Stack

- java 11
- Spring-boot
- Service registry (eureka)
- Api gateway (spring cloud gateway)
- Kafka
- kafdrop (kafka UI)
- OpenSearch
- MySql
- Maven
- Docker
- Windows SO
- Postman

### Compile

Navigate till root folder project and run:

```
mvn clean install

```

### Run Locally

To run locally we have two options

#### Running manually 

1 - First install: MySql, Kafka, Zookeeper, OpenSearch, OpenSearch Dashboard;
2 - Start each one of microservices using the command 'mvn sping-boot:run' fowling bellow order:
```
- eureka-service 
- auth-service
- gateway-service
- other three services: account-service, wallet-service, transaction-service

```
#### Using script

Run the fowling script at root project folder:

```
start_all_local_microservices.bat

```

### Run with docker

Navigate till root folder project and run:

```
start_all_docker_microservices.bat

```
### URIs

Eureka Server:

[http://localhost:8761](http://localhost:8761)

Kafdrop UI:

[http://localhost:9000](http://localhost:9000)

Swagger:

[http://localhost:9191/auth-api/swagger-ui.html](http://localhost:9191/auth-api/swagger-ui.html)

[http://localhost:8282/account-api/swagger-ui.html](http://localhost:8282/account-api/swagger-ui.html)

[http://localhost:8280/transaction-api/swagger-ui.html](http://localhost:8280/transaction-api/swagger-ui.html)

[http://localhost:8284/event-api/swagger-ui.html](http://localhost:8284/event-api/swagger-ui.html)

## Using

Open the Postman application and import the collection from:

```
./_postman/java-cloud.postman_collection.json

```

To use any endpoint you need a jwt token to get it use the postman request:

```
getToken

```
Then add the token at header 'Authorization' in all others postman requests.

Starting adding a user using the postman request:

```
user

```
Further create an account using the postman request:

```
create

```

Then make a fund:

```
fund

```

Play using the fowling postman requests:

```
withdraw and transfer

```
And finally to see a log transaction:

```
allTransactions

```
