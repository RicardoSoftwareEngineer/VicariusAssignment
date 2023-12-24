# Vicarius assignment
## How to get up and running:
#### - Run "docker compose up" in the project´s directory.
#### - Import the pom file as a maven project using Intellij or Eclipse IDE.
#### - Start the Spring Boot project.

## Architecture:
#### - H2 database used as SQL database during daytime.
#### - Elasticsearch used as NoSQL database during nighttime.
#### - Redis used as shared cache to limit the requests number per user.
#### - RabbitMQ used for database synchronization.

## Requirements:
#### - User CRUD operations.
#### - Database is SQL during the day and NoSQL during nighttime.
#### - Configurable limit for total requests per user. 

## Endpoints to interact with:
#### - POST http://localhost:8080/user/v1
#### - Body example{
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
