package com.book.store.data.service.impl;

import com.book.store.data.dto.AuthorResponse;
import com.book.store.data.dto.CreateAuthorRequest;
import com.book.store.data.dto.UpdateAuthorRequest;
import com.book.store.data.entity.Author;
import com.book.store.data.exception.NotFoundException;
import com.book.store.data.mapper.AuthorMapper;
import com.book.store.data.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

    @Mock
    private AuthorRepository repository;

    @Mock
    private AuthorMapper mapper;

    @InjectMocks
    private AuthorServiceImpl service;

    private UUID authorId;
    private Author author;
    private AuthorResponse authorResponse;
    private CreateAuthorRequest createRequest;
    private UpdateAuthorRequest updateRequest;

    @BeforeEach
    void setUp() {
        authorId = UUID.randomUUID();
        
        author = new Author();
        author.setId(authorId);
        author.setName("Test Author");
        
        authorResponse = new AuthorResponse(authorId, "Test Author");
        
        createRequest = new CreateAuthorRequest("New Author");
        
        updateRequest = new UpdateAuthorRequest("Updated Author");
    }

    @Test
    void create_ShouldReturnAuthorResponse() {
        // Arrange
        when(mapper.map(createRequest)).thenReturn(author);
        when(repository.save(author)).thenReturn(author);
        when(mapper.map(author)).thenReturn(authorResponse);

        // Act
        AuthorResponse result = service.create(createRequest);

        // Assert
        assertNotNull(result);
        assertEquals(authorId, result.getId());
        assertEquals("Test Author", result.getName());
        
        verify(mapper).map(createRequest);
        verify(repository).save(author);
        verify(mapper).map(author);
    }

    @Test
    void update_WhenAuthorExists_ShouldReturnUpdatedAuthorResponse() {
        // Arrange
        when(repository.findById(authorId)).thenReturn(Optional.of(author));
        when(repository.save(author)).thenReturn(author);
        when(mapper.map(author)).thenReturn(authorResponse);

        // Act
        AuthorResponse result = service.update(authorId, updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals(authorId, result.getId());
        assertEquals("Test Author", result.getName());
        
        verify(repository).findById(authorId);
        verify(mapper).merge(author, updateRequest);
        verify(repository).save(author);
        verify(mapper).map(author);
    }

    @Test
    void update_WhenAuthorDoesNotExist_ShouldThrowNotFoundException() {
        // Arrange
        when(repository.findById(authorId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> service.update(authorId, updateRequest));
        
        verify(repository).findById(authorId);
        verifyNoMoreInteractions(mapper, repository);
    }

    @Test
    void delete_ShouldCallRepositoryDeleteById() {
        // Act
        service.delete(authorId);

        // Assert
        verify(repository).deleteById(authorId);
    }

    @Test
    void findAll_ShouldReturnListOfAuthorResponses() {
        // Arrange
        List<Author> authors = Arrays.asList(author);
        when(repository.findAll()).thenReturn(authors);
        when(mapper.map(author)).thenReturn(authorResponse);

        // Act
        List<AuthorResponse> result = service.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(authorId, result.get(0).getId());
        assertEquals("Test Author", result.get(0).getName());
        
        verify(repository).findAll();
        verify(mapper).map(author);
    }

    @Test
    void findById_WhenAuthorExists_ShouldReturnAuthorResponse() {
        // Arrange
        when(repository.findById(authorId)).thenReturn(Optional.of(author));
        when(mapper.map(author)).thenReturn(authorResponse);

        // Act
        AuthorResponse result = service.findById(authorId);

        // Assert
        assertNotNull(result);
        assertEquals(authorId, result.getId());
        assertEquals("Test Author", result.getName());
        
        verify(repository).findById(authorId);
        verify(mapper).map(author);
    }

    @Test
    void findById_WhenAuthorDoesNotExist_ShouldThrowNotFoundException() {
        // Arrange
        when(repository.findById(authorId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> service.findById(authorId));
        
        verify(repository).findById(authorId);
        verifyNoMoreInteractions(mapper);
    }
}