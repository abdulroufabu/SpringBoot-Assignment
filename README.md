# SpringBoot-Assignment
The assignment project has done using SpringBoot framework

<!-- ABOUT THE PROJECT -->
## About The Project
This project provide endpoints to load the account statement by account id and perform simple search on date and amount ranges.
* The request should specify the account id.
* The request can specify from date and to date (the date range).
* The request can specify from amount and to amount (the amount range).
* If the request does not specify any parameter, then the search will return three months back statement.



## Built With
* [Spring Boot](https://spring.io/projects/spring-boot)
* [Maven](https://maven.apache.org/)


<!-- GETTING STARTED -->
## Getting Started

## 1. Run from the sources

#### Prerequisites
* Java 11
* Maven > 3.2.1

Run the Assignment app from source

```
git clone https://github.com/abdulroufabu/SpringBoot-Assignment.git

cd SpringBoot-Assignment\Assignment

# Building
mvn clean install

# Running
mvn clean install spring-boot:run
```
We can access the API endpoints on http://localhost:8080


## 2. Run using Docker

### Prerequisites
* Docker

We have to install the Docker Community Edition (CE).

The installation instructions can be followed in the [Official Docker documentation](https://docs.docker.com/get-docker/).

### Run Assignment app using docker 
```
git clone https://github.com/abdulroufabu/SpringBoot-Assignment.git

cd SpringBoot-Assignment\Assignment

# Package spring boot backend app using maven
mvn clean package

# Run docker command to build image and run the container for assignment app
 docker build -t assignment:1
 docker run -p 8080:8080 assignment:1
```

## Authentication and Security:

The following endpoints would be available:

* POST /api/v1/accounts

This endpoint require authorization, use the "Basic Auth" and enter credential from the below authenticated users:
* User1: Username: admin & Password: admin
* User2: Username: user & Password: user


## API Documentation

### Access Swagger UI

Open a browser and navigate to: http://localhost:8080/swagger-ui/index.html

![swagger-screenshot!](/images/swagger-ui.PNG)

<!-- CONTACT -->
## Contact

Abdul Rouf Abu - abdulroufak@hotmail.com
