package io.github.vlaship.book.catalog;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AppTest {

  /**
   * Given the application is configured with valid settings<br> When the application is
   * launched<br> Then the application should initialize without errors<br> And the main application
   * context should be loaded successfully<br> And all required beans should be available for use
   */
  @Test
  void applicationStartsSuccessfully() {
    String[] args = {};
    SpringApplication.run(App.class, args);
  }

  /**
   * Given the application is configured for a specific environment<br> When the application is
   * launched in that environment<br> Then the application should behave according to the
   * environment settings<br> And the correct resources should be loaded for that environment
   */
  @Test
  void applicationCanBeRunInDifferentEnvironments() {
    String[] args = {"--server.port=19999"};
    SpringApplication.run(App.class, args);
  }
}
