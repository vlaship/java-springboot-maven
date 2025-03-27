package com.book.store.facade.integration.controller;

import com.book.store.facade.model.AuthorFacadeResponse;
import com.book.store.facade.model.CreateAuthorFacadeRequest;
import com.book.store.facade.model.UpdateAuthorFacadeRequest;
import com.book.store.facade.service.AuthorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthorService authorService;

    @Test
    void testGetAuthorById() throws Exception {
        // Arrange
        UUID authorId = UUID.randomUUID();
        AuthorFacadeResponse response = new AuthorFacadeResponse(authorId, "Test Author");
        when(authorService.findById(authorId)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/v1/author/{id}", authorId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(authorId.toString())))
                .andExpect(jsonPath("$.name", is("Test Author")));
    }

    @Test
    void testGetAuthors() throws Exception {
        // Arrange
        UUID authorId1 = UUID.randomUUID();
        UUID authorId2 = UUID.randomUUID();
        List<AuthorFacadeResponse> authors = Arrays.asList(
                new AuthorFacadeResponse(authorId1, "Author 1"),
                new AuthorFacadeResponse(authorId2, "Author 2")
        );
        when(authorService.findAll()).thenReturn(authors);

        // Act & Assert
        mockMvc.perform(get("/v1/author"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(authorId1.toString())))
                .andExpect(jsonPath("$[0].name", is("Author 1")))
                .andExpect(jsonPath("$[1].id", is(authorId2.toString())))
                .andExpect(jsonPath("$[1].name", is("Author 2")));
    }

    @Test
    void testCreateAuthor() throws Exception {
        // Arrange
        UUID authorId = UUID.randomUUID();
        CreateAuthorFacadeRequest request = new CreateAuthorFacadeRequest("New Author");
        AuthorFacadeResponse response = new AuthorFacadeResponse(authorId, "New Author");
        when(authorService.create(any(CreateAuthorFacadeRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/v1/author")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(authorId.toString())))
                .andExpect(jsonPath("$.name", is("New Author")));
    }

    @Test
    void testUpdateAuthor() throws Exception {
        // Arrange
        UUID authorId = UUID.randomUUID();
        UpdateAuthorFacadeRequest request = new UpdateAuthorFacadeRequest("Updated Author");
        AuthorFacadeResponse response = new AuthorFacadeResponse(authorId, "Updated Author");
        when(authorService.update(eq(authorId), any(UpdateAuthorFacadeRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(patch("/v1/author/{id}", authorId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(authorId.toString())))
                .andExpect(jsonPath("$.name", is("Updated Author")));
    }

    @Test
    void testDeleteAuthor() throws Exception {
        // Arrange
        UUID authorId = UUID.randomUUID();
        doNothing().when(authorService).delete(authorId);

        // Act & Assert
        mockMvc.perform(delete("/v1/author/{id}", authorId))
                .andExpect(status().isAccepted());
    }
}