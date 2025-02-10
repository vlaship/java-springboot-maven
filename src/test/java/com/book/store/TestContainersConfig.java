package com.book.store;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration
public class TestContainersConfig {

    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> postgreSQLContainer(
            @Value("${DB_NAME}") String dbName,
            @Value("${DB_USER}") String dbUser,
            @Value("${DB_PASS}") String dbPass
    ) {
        return new PostgreSQLContainer<>("postgres:17.2-alpine")
                .withDatabaseName(dbName)
                .withUsername(dbUser)
                .withPassword(dbPass);
    }
}
