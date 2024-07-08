```markdown
# Library Management System

This Spring Boot application manages a library system with CRUD operations for books and authors using an H2 in-memory database.

## Table of Contents

- [Features](#features)
- [Setup](#setup)
- [Running the Application](#running-the-application)
- [H2 Database Console](#h2-database-console)
- [API Documentation](#api-documentation)
- [Swagger](#swagger)
- [Testing](#testing)
- [Security](#security)
- [Technologies](#technologies)
- [License](#license)

## Features

- CRUD operations for managing books and authors.
- Basic authentication and authorization.
- Input validation and custom exception handling.

## Setup

1. Clone the repository:
   ```sh
   git clone <repository-url>
   cd library-management-system
   ```
## Running the Application

Build and run:
```sh
mvn clean install
java -jar target/library-management-system-0.0.1-SNAPSHOT.jar
```

## H2 Database Console

Access at `http://localhost:8080/h2-console` with:
- **JDBC URL**: `jdbc:h2:mem:library_management`
- **Username**: `andrei`
- **Password**: `password`

## API Documentation

### CRUD REST APIs for Authors in Library Management System

- `PUT /authors/update/{id}`: Update Author
- `POST /authors/create`: Create Author
- `GET /authors/getAuthorById/{id}`: Fetch Author By Id
- `GET /authors/getAll`: Fetch All Authors
- `DELETE /authors/delete/{id}`: Delete Author

### CRUD REST APIs for Books in Library Management System

- `PUT /books/update/{id}`: Update Book
- `POST /books/create`: Create Book
- `GET /books/getBook/{id}`: Fetch Book By Id
- `GET /books/getAllBooks`: Fetch All Books
- `DELETE /books/delete/{id}`: Delete Book

## Swagger

Access Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```
## Security

Basic authentication is used to secure the endpoints. Configure settings in `SecurityConfig`.

## Technologies

- Spring Boot
- Spring Data JPA
- Hibernate
- H2 Database
- MapStruct
- Lombok
- Spring Security
- JUnit
- Mockito
- SpringDoc OpenAPI (Swagger)
