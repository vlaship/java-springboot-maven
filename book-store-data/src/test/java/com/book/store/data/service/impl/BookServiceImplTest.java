package com.book.store.data.service.impl;

import com.book.store.data.dto.BookResponse;
import com.book.store.data.dto.CreateBookRequest;
import com.book.store.data.dto.UpdateBookRequest;
import com.book.store.data.entity.Author;
import com.book.store.data.entity.Book;
import com.book.store.data.entity.BookType;
import com.book.store.data.exception.NotFoundException;
import com.book.store.data.mapper.BookMapper;
import com.book.store.data.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository repository;

    @Mock
    private BookMapper mapper;

    @InjectMocks
    private BookServiceImpl service;

    private UUID bookId;
    private UUID authorId;
    private Book book;
    private BookResponse bookResponse;
    private CreateBookRequest createRequest;
    private UpdateBookRequest updateRequest;
    private List<UUID> storeIds;

    @BeforeEach
    void setUp() {
        bookId = UUID.randomUUID();
        authorId = UUID.randomUUID();
        storeIds = Collections.singletonList(UUID.randomUUID());

        book = new Book();
        book.setId(bookId);
        book.setTitle("Test Book");
        book.setIsbn("978-3-16-148410-0");
        book.setType(BookType.HARD);

        bookResponse = new BookResponse(
            bookId, 
            "Test Book", 
            "978-3-16-148410-0", 
            BookType.HARD, 
            authorId, 
            storeIds
        );

        createRequest = new CreateBookRequest(
            "New Book", 
            "978-3-16-148410-1", 
            BookType.SOFT, 
            authorId, 
            storeIds
        );

        updateRequest = new UpdateBookRequest(
            "Updated Book", 
            "978-3-16-148410-2", 
            BookType.EBOOK, 
            authorId, 
            storeIds
        );
    }

    @Test
    void create_ShouldReturnBookResponse() {
        // Arrange
        when(mapper.map(createRequest)).thenReturn(book);
        when(repository.save(book)).thenReturn(book);
        when(mapper.map(book)).thenReturn(bookResponse);

        // Act
        BookResponse result = service.create(createRequest);

        // Assert
        assertNotNull(result);
        assertEquals(bookId, result.getId());
        assertEquals("Test Book", result.getTitle());
        assertEquals("978-3-16-148410-0", result.getIsbn());
        assertEquals(BookType.HARD, result.getType());
        assertEquals(authorId, result.getAuthorId());
        assertEquals(storeIds, result.getStoreIds());

        verify(mapper).map(createRequest);
        verify(repository).save(book);
        verify(mapper).map(book);
    }

    @Test
    void update_WhenBookExists_ShouldReturnUpdatedBookResponse() {
        // Arrange
        when(repository.findById(bookId)).thenReturn(Optional.of(book));
        when(repository.save(book)).thenReturn(book);
        when(mapper.map(book)).thenReturn(bookResponse);

        // Act
        BookResponse result = service.update(bookId, updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals(bookId, result.getId());
        assertEquals("Test Book", result.getTitle());
        assertEquals("978-3-16-148410-0", result.getIsbn());
        assertEquals(BookType.HARD, result.getType());
        assertEquals(authorId, result.getAuthorId());
        assertEquals(storeIds, result.getStoreIds());

        verify(repository).findById(bookId);
        verify(mapper).merge(updateRequest, book);
        verify(repository).save(book);
        verify(mapper).map(book);
    }

    @Test
    void update_WhenBookDoesNotExist_ShouldThrowNotFoundException() {
        // Arrange
        when(repository.findById(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> service.update(bookId, updateRequest));

        verify(repository).findById(bookId);
        verifyNoMoreInteractions(mapper, repository);
    }

    @Test
    void delete_ShouldCallRepositoryDeleteById() {
        // Act
        service.delete(bookId);

        // Assert
        verify(repository).deleteById(bookId);
    }

    @Test
    void findById_WhenBookExists_ShouldReturnBookResponse() {
        // Arrange
        when(repository.findById(bookId)).thenReturn(Optional.of(book));
        when(mapper.map(book)).thenReturn(bookResponse);

        // Act
        BookResponse result = service.findById(bookId);

        // Assert
        assertNotNull(result);
        assertEquals(bookId, result.getId());
        assertEquals("Test Book", result.getTitle());
        assertEquals("978-3-16-148410-0", result.getIsbn());
        assertEquals(BookType.HARD, result.getType());
        assertEquals(authorId, result.getAuthorId());
        assertEquals(storeIds, result.getStoreIds());

        verify(repository).findById(bookId);
        verify(mapper).map(book);
    }

    @Test
    void findById_WhenBookDoesNotExist_ShouldThrowNotFoundException() {
        // Arrange
        when(repository.findById(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> service.findById(bookId));

        verify(repository).findById(bookId);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void findBooksByAuthorId_ShouldReturnListOfBookResponses() {
        // Arrange
        // Set up the book's author
        Author author = new Author();
        author.setId(authorId);
        book.setAuthor(author);

        List<Book> allBooks = Arrays.asList(book);
        when(repository.findAll()).thenReturn(allBooks);
        when(mapper.map(any(List.class))).thenReturn(Arrays.asList(bookResponse));

        // Act
        List<BookResponse> result = service.findBooksByAuthorId(authorId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(bookId, result.get(0).getId());
        assertEquals("Test Book", result.get(0).getTitle());
        assertEquals("978-3-16-148410-0", result.get(0).getIsbn());
        assertEquals(BookType.HARD, result.get(0).getType());
        assertEquals(authorId, result.get(0).getAuthorId());
        assertEquals(storeIds, result.get(0).getStoreIds());

        verify(repository).findAll();
        verify(mapper).map(any(List.class));
    }
}
