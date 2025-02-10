package com.book.store.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@ActiveProfiles("test")
@WebMvcTest(OpenApiController.class)
class OpenApiControllerTest {

    //region Generated with Explyt. Tests for OpenApiController

    @Autowired
    private MockMvc mockMvc;

    /**
     * Given the application is running and accessible<br> When a user navigates to the root URL of
     * the application<br> Then the user should be redirected to the Swagger UI page<br> And the
     * response status should be 302 (Found)
     */
    @Test
    void redirectToSwaggerUIWhenAccessingRootURL() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("swagger-ui.html"));
    }

    /**
     * Given the application is deployed and operational<br> When a request is made to the root
     * URL<br> Then the system should redirect the request to the Swagger UI<br> And the user should
     * see the Swagger UI interface
     */
    @Test
    void ensureProperRedirectionFromRootURL() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("swagger-ui.html"));
    }

    /**
     * Given the application is live and reachable<br> When multiple users access the root URL
     * simultaneously<br> Then each user should be redirected to the Swagger UI page<br> And all users
     * should receive a 302 response status
     */
    @Test
    void verifyRedirectionBehaviorForMultipleRequestsToRootURL() throws Exception {
        for (int i = 0; i < 10; i++) {
            mockMvc.perform(get("/"))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("swagger-ui.html"));
        }
    }

    /**
     * Given the application has been restarted<br> When a user accesses the root URL<br> Then the
     * user should be redirected to the Swagger UI page<br> And the response should indicate a
     * successful redirection
     */
    @Test
    void checkRedirectionAfterServerRestart() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("swagger-ui.html"));
    }

    /**
     * Given the application is running in a staging environment<br> When a user navigates to the root
     * URL<br> Then the user should be redirected to the Swagger UI page<br> And the redirection
     * should function identically to the production environment
     */
    @Test
    void validateRedirectionFromRootURLInDifferentEnvironments() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("swagger-ui.html"));
    }

    //endregion

}
