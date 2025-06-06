# Development Setup with Containerized Infrastructure

This guide explains how to run the book-store services in IntelliJ IDEA while using containerized infrastructure (Postgres, Prometheus, Grafana).

## Prerequisites

- Docker and Docker Compose installed
- IntelliJ IDEA with Spring Boot support
- Java 8 or higher

## Step 1: Start the Infrastructure Containers

1. Open a terminal in the project root directory
2. Run the following command to start the infrastructure containers:

```bash
docker-compose -f docker-compose.dev.yaml up -d
```

This will start the following containers:
- PostgreSQL database
- Prometheus (for metrics collection)
- Grafana (for metrics visualization)

## Step 2: Configure and Run the Data Service in IDEA

1. Open the book-store project in IntelliJ IDEA
2. Create a Run Configuration for the data service:
   - Click on "Run" > "Edit Configurations..."
   - Click the "+" button and select "Spring Boot"
   - Set the following parameters:
     - Name: `BookStoreDataApplication`
     - Main class: `com.book.store.data.BookStoreDataApplication`
     - VM options: `-Dspring.profiles.active=dev`
     - Environment variables:
       ```
       DB_HOST=localhost
       DB_PORT=5432
       DB_USER=postgres
       DB_NAME=postgres
       DB_PASS=postgres
       DB_SCHEMA=book_store
       SERVER_PORT_DATA=18888
       ```
   - Click "Apply" and "OK"
3. Run the data service using the created configuration

## Step 3: Configure and Run the Facade Service in IDEA

1. Create a Run Configuration for the facade service:
   - Click on "Run" > "Edit Configurations..."
   - Click the "+" button and select "Spring Boot"
   - Set the following parameters:
     - Name: `BookStoreFacadeApplication`
     - Main class: `com.book.store.facade.BookStoreFacadeApplication`
     - VM options: `-Dspring.profiles.active=dev`
     - Environment variables:
       ```
       SERVER_PORT_FACADE=19999
       SERVER_PORT_DATA=18888
       DATA_URL=http://localhost:18888
       JWT_SECRET=qL3u4gqtmn1mqQUzG2UhTTIG5BvkfvDmi0zVrSNKnI0a9nPyS7D9FoIdSVBTK7hza95NZtRQNFzcogO4OuVqPXopRrqO9EOhmGRREpN33QpMPqtAKGuoRctvJmul86Y6fgP7BRKqvXPxTjHT5LBlzq9rRL3u49DA0g0mW7tMP7Xdk1mzKDdleOFReWYusLgLOqGDYDaFs6SyuXH8YG2ti10F25papYNzRCYtCXBBmK4saRMO9OvPNn8Z9CeEqQJ2BXhOy0asjVngCMHz1ylzCFMs0QIsff1TOXlUcPz0HDrM
       JWT_EXPIRATION_SEC=3600
       ```
   - Click "Apply" and "OK"
2. Run the facade service using the created configuration

## Step 4: Access the Services

- Book Store Data Service: http://localhost:18888/book-store-data-service
- Book Store Facade Service: http://localhost:19999/book-store-facade-service
- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000 (admin/admin)

## Step 5: Stopping the Infrastructure

When you're done, stop the infrastructure containers:

```bash
docker-compose -f docker-compose.dev.yaml down
```

## Troubleshooting

### Database Connection Issues

If the data service cannot connect to the database, ensure that:
1. The PostgreSQL container is running: `docker ps | grep postgres`
2. The database environment variables are correctly set in the run configuration
3. The database is accessible from your host: `psql -h localhost -U postgres -d postgres`

### Prometheus Metrics Collection

If Prometheus cannot collect metrics from the services:
1. Ensure that the services are running and exposing metrics
2. Check that the services are accessible at the expected URLs:
   - http://localhost:18888/book-store-data-service/actuator/prometheus
   - http://localhost:19999/book-store-facade-service/actuator/prometheus
3. Check the Prometheus targets page: http://localhost:9090/targets
4. Note that the prometheus.dev.yml file has hardcoded port values (18888 for data service and 19999 for facade service). If you change these ports in your environment variables, you'll need to update the prometheus.dev.yml file as well.

### Grafana Dashboard

If Grafana doesn't show any data:
1. Ensure that Prometheus is correctly collecting metrics
2. Check that both Prometheus and PostgreSQL data sources are configured in Grafana
3. Import or create dashboards for Spring Boot applications
4. For PostgreSQL data, you can create custom dashboards to visualize database metrics

### PostgreSQL Monitoring

For monitoring PostgreSQL:
1. Grafana is configured to connect directly to PostgreSQL using the PostgreSQL data source
2. A pre-configured PostgreSQL dashboard is available with common database metrics:
   - Database connections
   - Database size
   - Transactions committed
   - Rows fetched
3. To access the pre-configured dashboard:
   - Go to Grafana (http://localhost:3000)
   - Navigate to Dashboards > PostgreSQL > PostgreSQL Monitoring
4. To create a custom PostgreSQL dashboard:
   - Go to Grafana (http://localhost:3000)
   - Click on "Create" > "Dashboard"
   - Add a new panel
   - Select the PostgreSQL data source for direct database queries
   - Write SQL queries directly to visualize database metrics
