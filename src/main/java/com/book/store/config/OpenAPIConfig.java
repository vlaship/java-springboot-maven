package com.book.store.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.info.GitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI openAPI(GitProperties gitProperties) {
        return new OpenAPI()
                .info(new Info()
                        .title("Book Store API")
                        .description("REST API for managing a book store. Application version: "+ gitProperties.get("build.version"))
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Book Store Team")
                                .email("support@book-store.com")
                                .url("https://book-store.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT"))
                );
    }
}
