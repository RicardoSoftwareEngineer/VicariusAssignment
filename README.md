# Vicarius assignment

## Requirements:
#### - User CRUD operations.
#### - Robust access-limiting mechanism through shared cache.
#### - Synchronization between 2 databases through message queue.
#### - SQL database during daytime and Elasticsearch during nighttime.


## Architecture:
#### - Is important to say that this microservice was made with scalability in mind, the idea is to get a thousand or more instances of this connecting to the same Redis, RabbitMQ, Elastic and SQL services. H2 was choose for simplicity’s sake, this is just a proof of concept. For production deployment is better to replace with Postgres or MariaDB.
#### - RabbitMQ used for database synchronization.
#### - H2 database used as SQL database during daytime.
#### - Elasticsearch used as NoSQL database during nighttime.
#### - Redis used as shared cache to limit the requests number per user.
#### - Configurable maximum requests per user through the environment variable:
+ QUOTA_LIMIT (default value: 5)

## How to get up and running:
#### - Run "docker compose up" in the project´s directory to get:
+ RabbitMQ
+ Redis cache
+ Elastic search
#### - The project will use these services without any configuration of yours.
#### - Import the pom file as a maven project.
#### - Start the Spring Boot project.

## Endpoints to interact with:
#### - POST http://localhost:8080/user/v1
#### - Body example
#### {
    "firstName":"Ricardo",
    "lastName":"Ribeiro"
}
#### - GET http://localhost:8080/user/v1
#### - GET http://localhost:8080/user/v1/{id}
#### - PUT http://localhost:8080/user/v1/{id}
#### - Body example{
    "firstName":"Ricardo",
    "lastName":"Rib"
}
#### - DELETE http://localhost:8080/user/v1/{id}
#### - POST http://localhost:8080/quota/v1/consume/{id}
#### - GET http://localhost:8080/quota/v1
