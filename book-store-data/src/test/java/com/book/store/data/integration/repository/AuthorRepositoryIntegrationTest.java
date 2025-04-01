package com.book.store.data.integration.repository;

import com.book.store.data.entity.Author;
import com.book.store.data.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class AuthorRepositoryIntegrationTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void testSaveAndFindById() {
        // Arrange
        UUID id = UUID.randomUUID();
        Author author = new Author();
        author.setId(id);
        author.setName("Test Author");

        // Act
        authorRepository.save(author);
        Optional<Author> foundAuthor = authorRepository.findById(id);

        // Assert
        assertTrue(foundAuthor.isPresent());
        assertEquals("Test Author", foundAuthor.get().getName());
    }

    @Test
    void testFindAll() {
        // Arrange
        UUID id1 = UUID.randomUUID();
        Author author1 = new Author();
        author1.setId(id1);
        author1.setName("Author 1");

        UUID id2 = UUID.randomUUID();
        Author author2 = new Author();
        author2.setId(id2);
        author2.setName("Author 2");

        authorRepository.save(author1);
        authorRepository.save(author2);

        // Act
        List<Author> authors = authorRepository.findAll();

        // Assert
        assertEquals(2, authors.size());
        assertTrue(authors.stream().anyMatch(a -> a.getName().equals("Author 1")));
        assertTrue(authors.stream().anyMatch(a -> a.getName().equals("Author 2")));
    }

    @Test
    void testUpdate() {
        // Arrange
        UUID id = UUID.randomUUID();
        Author author = new Author();
        author.setId(id);
        author.setName("Original Name");
        authorRepository.save(author);

        // Act
        author.setName("Updated Name");
        authorRepository.save(author);
        Optional<Author> updatedAuthor = authorRepository.findById(id);

        // Assert
        assertTrue(updatedAuthor.isPresent());
        assertEquals("Updated Name", updatedAuthor.get().getName());
    }

    @Test
    void testDelete() {
        // Arrange
        UUID id = UUID.randomUUID();
        Author author = new Author();
        author.setId(id);
        author.setName("Test Author");
        authorRepository.save(author);

        // Act
        authorRepository.deleteById(id);
        Optional<Author> deletedAuthor = authorRepository.findById(id);

        // Assert
        assertFalse(deletedAuthor.isPresent());
    }
}