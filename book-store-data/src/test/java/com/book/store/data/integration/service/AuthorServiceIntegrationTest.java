package com.book.store.data.integration.service;

import com.book.store.data.dto.CreateAuthorRequest;
import com.book.store.data.dto.AuthorResponse;
import com.book.store.data.dto.UpdateAuthorRequest;
import com.book.store.data.entity.Author;
import com.book.store.data.repository.AuthorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class AuthorServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthorRepository authorRepository;

    private CreateAuthorRequest createAuthorRequest;
    private UpdateAuthorRequest updateAuthorRequest;

    @BeforeEach
    void setUp() {
        // Clear the database before each test
        authorRepository.deleteAll();

        // Initialize test data
        createAuthorRequest = new CreateAuthorRequest("Test Author");
        updateAuthorRequest = new UpdateAuthorRequest("Updated Author");
    }

    @Test
    void createAuthor_ShouldReturnCreatedAuthor() throws Exception {
        // Act
        MvcResult result = mockMvc.perform(post("/v1/author")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createAuthorRequest)))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        String content = result.getResponse().getContentAsString();
        AuthorResponse response = objectMapper.readValue(content, AuthorResponse.class);

        assertNotNull(response.getId());
        assertEquals("Test Author", response.getName());

        // Verify the author was saved to the database
        assertTrue(authorRepository.findById(response.getId()).isPresent());
    }

    @Test
    void findById_WhenAuthorExists_ShouldReturnAuthor() throws Exception {
        // Arrange
        MvcResult createResult = mockMvc.perform(post("/v1/author")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createAuthorRequest)))
                .andExpect(status().isOk())
                .andReturn();

        AuthorResponse createdAuthor = objectMapper.readValue(
                createResult.getResponse().getContentAsString(), AuthorResponse.class);

        // Act & Assert
        mockMvc.perform(get("/v1/author/{id}", createdAuthor.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdAuthor.getId().toString()))
                .andExpect(jsonPath("$.name").value("Test Author"));
    }

    @Test
    void findById_WhenAuthorDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/v1/author/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateAuthor_WhenAuthorExists_ShouldReturnUpdatedAuthor() throws Exception {
        // Arrange
        MvcResult createResult = mockMvc.perform(post("/v1/author")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createAuthorRequest)))
                .andExpect(status().isOk())
                .andReturn();

        AuthorResponse createdAuthor = objectMapper.readValue(
                createResult.getResponse().getContentAsString(), AuthorResponse.class);

        // Act & Assert
        MvcResult updateResult = mockMvc.perform(patch("/v1/author/{authorId}", createdAuthor.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateAuthorRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdAuthor.getId().toString()))
                .andExpect(jsonPath("$.name").value("Updated Author"))
                .andReturn();

        // Verify the author was updated in the database
        Author author = authorRepository.findById(createdAuthor.getId()).orElseThrow(() -> new RuntimeException("Author not found"));
        assertEquals("Updated Author", author.getName());
    }

    @Test
    void updateAuthor_WhenAuthorDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(patch("/v1/author/{authorId}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateAuthorRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteAuthor_WhenAuthorExists_ShouldDeleteAuthor() throws Exception {
        // Arrange
        MvcResult createResult = mockMvc.perform(post("/v1/author")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createAuthorRequest)))
                .andExpect(status().isOk())
                .andReturn();

        AuthorResponse createdAuthor = objectMapper.readValue(
                createResult.getResponse().getContentAsString(), AuthorResponse.class);

        // Act
        mockMvc.perform(delete("/v1/author/{authorId}", createdAuthor.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());

        // Assert
        assertFalse(authorRepository.findById(createdAuthor.getId()).isPresent());
    }

    @Test
    void findAll_ShouldReturnAllAuthors() throws Exception {
        // Arrange
        mockMvc.perform(post("/v1/author")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CreateAuthorRequest("Test Author 1"))))
                .andExpect(status().isOk());

        mockMvc.perform(post("/v1/author")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CreateAuthorRequest("Test Author 2"))))
                .andExpect(status().isOk());

        // Act & Assert
        MvcResult result = mockMvc.perform(get("/v1/author")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Parse the response
        String content = result.getResponse().getContentAsString();
        List<AuthorResponse> authors = objectMapper.readValue(content, 
                objectMapper.getTypeFactory().constructCollectionType(List.class, AuthorResponse.class));

        // Assert
        assertEquals(2, authors.size());
        assertTrue(authors.stream().anyMatch(author -> author.getName().equals("Test Author 1")));
        assertTrue(authors.stream().anyMatch(author -> author.getName().equals("Test Author 2")));
    }
}
