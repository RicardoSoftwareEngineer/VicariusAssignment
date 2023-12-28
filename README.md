# Vicarius assignment

## How it Works:
This microservice is designed to be scalable, providing the following capabilities:
- CRUD operations for the User entity.
- Switching between two databases: using a SQL database during the day and Elasticsearch at night.
- Database synchronization facilitated through a message queue system.
- Robust access-limiting mechanism implemented for the "/quota/consume" endpoint via shared cache.

## Architecture:
#### Built with scalability in mind, aiming to get a thousand or more instances connecting to the same Redis, RabbitMQ, Elastic, and SQL services. H2 was chosen for simplicity's sake, primarily as a proof of concept. For production deployment, it's recommended to use Postgres or MariaDB.
#### RabbitMQ is utilized for database synchronization.
#### H2 database serves as the SQL database during daytime.
#### Elasticsearch functions as the NoSQL database during nighttime.
#### Redis acts as shared cache, limiting the number of requests per user in all instances.
#### The maximum requests per user can be configured through the environment variable:
    - QUOTA_LIMIT (default value: 5)

## How to get up and running:
#### - Run "docker-compose up" in the project's directory to set up:
+ RabbitMQ
+ Redis cache
+ Elasticsearch
#### - The project will automatically use these services without requiring any configuration.
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
