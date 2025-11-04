# loan-calculator
Loan Calculator Demo Application

A Spring Boot application that calculates loan schedules with installments and persists them in a database.

## Features
- Calculate loan schedules with monthly installments.
- Calculate principal, interest abount, as well as balance owed per installment.
- Persist **Loan** and **Installments** in a database using JPA.
- RESTful API for creating loans and retrieving installments.
- Validation of input data.
- Unit and integration tests to ensure correctness.

## Technologies
- Java 17+
- Spring Boot
- Spring Data JPA / Hibernate
- H2 in-memory database
- Maven
- Lombok
- JUnit 5 + AssertJ

## Getting Started
Clone the repository

You can access the H2 console at:
http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:loan_db
Username: sa
Password: (leave empty)