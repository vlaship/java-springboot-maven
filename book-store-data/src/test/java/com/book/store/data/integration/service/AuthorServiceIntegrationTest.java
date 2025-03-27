package com.book.store.data.integration.service;

import com.book.store.data.dto.AuthorResponse;
import com.book.store.data.dto.CreateAuthorRequest;
import com.book.store.data.dto.UpdateAuthorRequest;
import com.book.store.data.entity.Author;
import com.book.store.data.exception.NotFoundException;
import com.book.store.data.repository.AuthorRepository;
import com.book.store.data.service.AuthorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AuthorServiceIntegrationTest {

    @Autowired
    private AuthorService authorService;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void testCreateAuthor() {
        // Arrange
        CreateAuthorRequest request = new CreateAuthorRequest("New Author");

        // Act
        AuthorResponse response = authorService.create(request);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals("New Author", response.getName());

        // Verify the author was saved to the repository
        assertTrue(authorRepository.findById(response.getId()).isPresent());
    }

    @Test
    void testUpdateAuthor() {
        // Arrange
        // First create an author
        CreateAuthorRequest createRequest = new CreateAuthorRequest("Original Author");
        AuthorResponse createdAuthor = authorService.create(createRequest);
        UUID authorId = createdAuthor.getId();

        // Now update it
        UpdateAuthorRequest updateRequest = new UpdateAuthorRequest("Updated Author");

        // Act
        AuthorResponse updatedResponse = authorService.update(authorId, updateRequest);

        // Assert
        assertNotNull(updatedResponse);
        assertEquals(authorId, updatedResponse.getId());
        assertEquals("Updated Author", updatedResponse.getName());

        // Verify the author was updated in the repository
        Author author = authorRepository.findById(authorId).orElseThrow(() -> new RuntimeException("Author not found"));
        assertEquals("Updated Author", author.getName());
    }

    @Test
    void testUpdateAuthor_NotFound() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        UpdateAuthorRequest updateRequest = new UpdateAuthorRequest("Updated Author");

        // Act & Assert
        assertThrows(NotFoundException.class, () -> authorService.update(nonExistentId, updateRequest));
    }

    @Test
    void testDeleteAuthor() {
        // Arrange
        // First create an author
        CreateAuthorRequest createRequest = new CreateAuthorRequest("Author to Delete");
        AuthorResponse createdAuthor = authorService.create(createRequest);
        UUID authorId = createdAuthor.getId();

        // Act
        authorService.delete(authorId);

        // Assert
        // Verify the author was deleted from the repository
        assertFalse(authorRepository.findById(authorId).isPresent());
    }

    @Test
    void testFindAllAuthors() {
        // Arrange
        // Create a few authors
        authorService.create(new CreateAuthorRequest("Author 1"));
        authorService.create(new CreateAuthorRequest("Author 2"));
        authorService.create(new CreateAuthorRequest("Author 3"));

        // Act
        List<AuthorResponse> authors = authorService.findAll();

        // Assert
        assertNotNull(authors);
        assertTrue(authors.size() >= 3);
        assertTrue(authors.stream().anyMatch(a -> a.getName().equals("Author 1")));
        assertTrue(authors.stream().anyMatch(a -> a.getName().equals("Author 2")));
        assertTrue(authors.stream().anyMatch(a -> a.getName().equals("Author 3")));
    }

    @Test
    void testFindAuthorById() {
        // Arrange
        // First create an author
        CreateAuthorRequest createRequest = new CreateAuthorRequest("Author to Find");
        AuthorResponse createdAuthor = authorService.create(createRequest);
        UUID authorId = createdAuthor.getId();

        // Act
        AuthorResponse foundAuthor = authorService.findById(authorId);

        // Assert
        assertNotNull(foundAuthor);
        assertEquals(authorId, foundAuthor.getId());
        assertEquals("Author to Find", foundAuthor.getName());
    }

    @Test
    void testFindAuthorById_NotFound() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();

        // Act & Assert
        assertThrows(NotFoundException.class, () -> authorService.findById(nonExistentId));
    }
}
