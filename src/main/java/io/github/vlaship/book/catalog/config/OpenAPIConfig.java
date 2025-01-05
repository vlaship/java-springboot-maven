package io.github.vlaship.book.catalog.config;

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
    public OpenAPI customOpenAPI(GitProperties gitProperties) {
        return new OpenAPI()
                .info(new Info()
                        .title("Book Catalog API")
                        .description("REST API for managing a book catalog. Application version: "+ gitProperties.get("build.version"))
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Book Catalog Team")
                                .email("support@bookcatalog.com")
                                .url("https://github.com/vlaship/book-catalog"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT"))
                );
    }
}
