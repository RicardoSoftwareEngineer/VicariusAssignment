# Vicarius assignment

## Requirements to get the project up and running:
#### - Run the command "docker compose up" in the project´s directory

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


