# SampleCompany-ComputerManager

## Description
A SampleCompany Computer management application developed in Java and Spring Boot.
This application facilitates CRUD operations on company-issued computers,
enables notification to admins for multi-device assignments, and aids in device
tracking and management for system administrators.

## Technologies Used
- Java with Spring Boot 2.x
- JPA / Hibernate
- H2 Database
- JUnit, Mockito

## Features
- CRUD operations for managing computer records.
- Notification service to alert admins when multiple devices are assigned to a single employee.

## Getting Started

### Prerequisites
- Java JDK 11 or higher
- Maven 3.6 or higher

### Building the Application
git clone https://github.com/sairambindiya/SampleCompany-ComputerManager.git
cd SampleCompany-ComputerManager
mvn clean install
java -jar target/computer-manager.jar


### Running Tests
- Run `mvn test` to execute unit and integration tests.

## Improvements and Future Work
- Implement authentication and authorization for enhanced API security.
- Adding custom validations for fields like MAC address and IP address using regex patterns to ensure data consistency and prevent invalid entries.
- Integrate application-wide logging for better monitoring and debugging.
- Dockerize the application for easier deployment and scaling.
- Document APIs using Swagger for better usability.

## Code Snippets

- check out the ./snippets) included in the repository.

## Postman Collection

To quickly get started with testing the API, you can use the Postman collection found in the ./postman of this repository. It includes pre-configured requests for all the endpoints.


## Author
Bindiya Murulidhar
