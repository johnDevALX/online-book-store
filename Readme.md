# Online Book Store API

This is a RESTful API built with Spring Boot for managing an online book system. It allows admins to add book and users to view available books.

## Features

- **Book Management**:
- Get a list of available books
- Get details of a specific book by ID
- Add a new book
- Update an existing book
- Remove a book

## Technologies Used

- Java
- Spring Boot
- Spring Data JPA
- PostgreSQL
- JSON Web Token (JWT) for authentication and authorization
- Redis for caching

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 11
- PostgreSQL installed and running
- Redis installed and running

### Installation

1. Clone the repository:

```bash
git clone https://github.com/johnDevALX/online-book-store.git

2. Navigation, building running the application:

cd online-book-store
gradle build
java -jar build/libs/online-book-store-0.0.1-SNAPSHOT.jar
