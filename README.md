# Book Store Application

## Project Overview
This is a Spring Boot application for managing a book store. It follows a microservices architecture with two main modules:

1. **book-store-data**: Handles data access and provides REST APIs for CRUD operations on books, authors, stores, and users.
2. **book-store-facade**: Acts as an API gateway with authentication and authorization using Spring Security and JWT.

## Architecture

### Module Structure
- **book-store-data**: Core data service with JPA entities and repositories
- **book-store-facade**: Security and API gateway service

### Technology Stack
- Java 8
- Spring Boot 2.4.13
- Spring Data JPA
- Spring Security
- PostgreSQL
- Liquibase (for database migrations)
- Lombok
- MapStruct
- OpenAPI/Swagger
- Docker
- Prometheus (for metrics collection)
- Grafana (for metrics visualization)

### Domain Model
- **Book**: Represents a book with title, ISBN, and type (hardcover, softcover, e-book)
- **Author**: Represents a book author
- **Store**: Represents a physical store location
- **User**: Represents a system user with authentication details

### Key Relationships
- A book has one author (many-to-one)
- A book can be in many stores, and a store can have many books (many-to-many)

## Deployment
The application is containerized using Docker and can be deployed using Docker Compose:
- PostgreSQL database
- book-store-data service
- book-store-facade service
- Prometheus (metrics collection)
- Grafana (metrics visualization)

## Monitoring
The application includes a monitoring setup with Prometheus and Grafana:

### Prometheus
- Collects metrics from both services via Spring Boot Actuator endpoints
- Available at: http://localhost:9090

### Grafana
- Visualizes metrics collected by Prometheus
- Pre-configured with Prometheus and PostgreSQL as data sources
- Can read metrics directly from PostgreSQL for database monitoring
- Available at: http://localhost:3000
- Default credentials: admin/admin

### PostgreSQL Monitoring
- Grafana connects directly to PostgreSQL using the PostgreSQL data source
- Metrics include query statistics, connection counts, database size, etc.
- Includes a pre-configured PostgreSQL dashboard with common database metrics
- Grafana dashboards can visualize database metrics through direct SQL queries

## Development
The project follows standard Spring Boot development practices:
- Layered architecture (controller, service, repository)
- DTO pattern for API requests/responses
- Interface-based design for services
- Comprehensive test coverage (unit and integration tests)

## API Documentation
The application uses SpringDoc OpenAPI UI for API documentation, available at:
- Data service: `/book-store-data-service/swagger-ui.html`
- Facade service: `/book-store-facade-service/swagger-ui.html`
