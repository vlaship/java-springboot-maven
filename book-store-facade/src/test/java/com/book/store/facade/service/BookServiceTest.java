package com.book.store.facade.service;

import com.book.store.facade.mapper.BookMapper;
import com.book.store.facade.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    private static final String DATA_SERVICE_URL = "http://localhost:8080/book-store-data-service/v1/book";

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(bookService, "bookUrl", DATA_SERVICE_URL);
    }

    @Test
    void create_shouldCallRestTemplateAndReturnMappedResponse() {
        // Arrange
        UUID authorId = UUID.randomUUID();
        CreateBookFacadeRequest facadeRequest = new CreateBookFacadeRequest("Test Book", "1234567890", authorId, BookType.HARD);
        CreateBookDataRequest dataRequest = new CreateBookDataRequest("Test Book", "1234567890", authorId, BookType.HARD);
        
        BookDataResponse dataResponse = new BookDataResponse();
        UUID bookId = UUID.randomUUID();
        dataResponse.setId(bookId);
        dataResponse.setTitle("Test Book");
        dataResponse.setIsbn("1234567890");
        dataResponse.setType(BookType.HARD);
        dataResponse.setAuthorId(facadeRequest.getAuthorId());
        dataResponse.setStoreIds(Collections.emptyList());
        
        BookFacadeResponse expectedResponse = new BookFacadeResponse(
                dataResponse.getId(),
                dataResponse.getTitle(),
                dataResponse.getIsbn(),
                dataResponse.getType(),
                dataResponse.getAuthorId(),
                dataResponse.getStoreIds()
        );

        when(bookMapper.map(facadeRequest)).thenReturn(dataRequest);
        when(restTemplate.exchange(
                eq(DATA_SERVICE_URL),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(BookDataResponse.class)
        )).thenReturn(ResponseEntity.ok(dataResponse));
        when(bookMapper.map(dataResponse)).thenReturn(expectedResponse);

        // Act
        BookFacadeResponse result = bookService.create(facadeRequest);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse.getId(), result.getId());
        assertEquals(expectedResponse.getTitle(), result.getTitle());
        assertEquals(expectedResponse.getIsbn(), result.getIsbn());
        assertEquals(expectedResponse.getType(), result.getType());
        assertEquals(expectedResponse.getAuthorId(), result.getAuthorId());
        assertEquals(expectedResponse.getStoreIds(), result.getStoreIds());
        verify(bookMapper).map(facadeRequest);
        verify(restTemplate).exchange(
                eq(DATA_SERVICE_URL),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(BookDataResponse.class)
        );
        verify(bookMapper).map(dataResponse);
    }

    @Test
    void update_shouldCallRestTemplateAndReturnMappedResponse() {
        // Arrange
        UUID bookId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        UpdateBookFacadeRequest facadeRequest = new UpdateBookFacadeRequest("Updated Book", "0987654321", BookType.SOFT, authorId);
        UpdateBookDataRequest dataRequest = new UpdateBookDataRequest("Updated Book", "0987654321", BookType.SOFT, authorId);
        
        BookDataResponse dataResponse = new BookDataResponse();
        dataResponse.setId(bookId);
        dataResponse.setTitle("Updated Book");
        dataResponse.setIsbn("0987654321");
        dataResponse.setType(BookType.SOFT);
        dataResponse.setAuthorId(facadeRequest.getAuthorId());
        dataResponse.setStoreIds(Collections.emptyList());
        
        BookFacadeResponse expectedResponse = new BookFacadeResponse(
                dataResponse.getId(),
                dataResponse.getTitle(),
                dataResponse.getIsbn(),
                dataResponse.getType(),
                dataResponse.getAuthorId(),
                dataResponse.getStoreIds()
        );

        when(bookMapper.map(facadeRequest)).thenReturn(dataRequest);
        when(restTemplate.exchange(
                eq(DATA_SERVICE_URL + "/" + bookId.toString()),
                eq(HttpMethod.PATCH),
                any(HttpEntity.class),
                eq(BookDataResponse.class)
        )).thenReturn(ResponseEntity.ok(dataResponse));
        when(bookMapper.map(dataResponse)).thenReturn(expectedResponse);

        // Act
        BookFacadeResponse result = bookService.update(bookId, facadeRequest);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse.getId(), result.getId());
        assertEquals(expectedResponse.getTitle(), result.getTitle());
        assertEquals(expectedResponse.getIsbn(), result.getIsbn());
        assertEquals(expectedResponse.getType(), result.getType());
        assertEquals(expectedResponse.getAuthorId(), result.getAuthorId());
        assertEquals(expectedResponse.getStoreIds(), result.getStoreIds());
        verify(bookMapper).map(facadeRequest);
        verify(restTemplate).exchange(
                eq(DATA_SERVICE_URL + "/" + bookId.toString()),
                eq(HttpMethod.PATCH),
                any(HttpEntity.class),
                eq(BookDataResponse.class)
        );
        verify(bookMapper).map(dataResponse);
    }

    @Test
    void delete_shouldCallRestTemplate() {
        // Arrange
        UUID bookId = UUID.randomUUID();
        doNothing().when(restTemplate).delete(DATA_SERVICE_URL + "/" + bookId.toString());

        // Act
        bookService.delete(bookId);

        // Assert
        verify(restTemplate).delete(DATA_SERVICE_URL + "/" + bookId.toString());
    }

    @Test
    void findById_shouldCallRestTemplateAndReturnMappedResponse() {
        // Arrange
        UUID bookId = UUID.randomUUID();
        BookDataResponse dataResponse = new BookDataResponse();
        dataResponse.setId(bookId);
        dataResponse.setTitle("Test Book");
        dataResponse.setIsbn("1234567890");
        dataResponse.setType(BookType.HARD);
        dataResponse.setAuthorId(UUID.randomUUID());
        dataResponse.setStoreIds(Collections.emptyList());
        
        BookFacadeResponse expectedResponse = new BookFacadeResponse(
                dataResponse.getId(),
                dataResponse.getTitle(),
                dataResponse.getIsbn(),
                dataResponse.getType(),
                dataResponse.getAuthorId(),
                dataResponse.getStoreIds()
        );

        when(restTemplate.exchange(
                eq(DATA_SERVICE_URL + "/" + bookId.toString()),
                eq(HttpMethod.GET),
                eq(HttpEntity.EMPTY),
                eq(BookDataResponse.class)
        )).thenReturn(ResponseEntity.ok(dataResponse));
        when(bookMapper.map(dataResponse)).thenReturn(expectedResponse);

        // Act
        BookFacadeResponse result = bookService.findById(bookId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse.getId(), result.getId());
        assertEquals(expectedResponse.getTitle(), result.getTitle());
        assertEquals(expectedResponse.getIsbn(), result.getIsbn());
        assertEquals(expectedResponse.getType(), result.getType());
        assertEquals(expectedResponse.getAuthorId(), result.getAuthorId());
        assertEquals(expectedResponse.getStoreIds(), result.getStoreIds());
        verify(restTemplate).exchange(
                eq(DATA_SERVICE_URL + "/" + bookId.toString()),
                eq(HttpMethod.GET),
                eq(HttpEntity.EMPTY),
                eq(BookDataResponse.class)
        );
        verify(bookMapper).map(dataResponse);
    }

    @Test
    void findBooksByAuthorId_shouldCallRestTemplateAndReturnMappedResponses() {
        // Arrange
        UUID authorId = UUID.randomUUID();
        
        BookDataResponse dataResponse1 = new BookDataResponse();
        dataResponse1.setId(UUID.randomUUID());
        dataResponse1.setTitle("Book 1");
        dataResponse1.setIsbn("1111111111");
        dataResponse1.setType(BookType.HARD);
        dataResponse1.setAuthorId(authorId);
        dataResponse1.setStoreIds(Collections.emptyList());
        
        BookDataResponse dataResponse2 = new BookDataResponse();
        dataResponse2.setId(UUID.randomUUID());
        dataResponse2.setTitle("Book 2");
        dataResponse2.setIsbn("2222222222");
        dataResponse2.setType(BookType.SOFT);
        dataResponse2.setAuthorId(authorId);
        dataResponse2.setStoreIds(Collections.emptyList());
        
        List<BookDataResponse> dataResponses = Arrays.asList(dataResponse1, dataResponse2);
        
        BookFacadeResponse facadeResponse1 = new BookFacadeResponse(
                dataResponse1.getId(),
                dataResponse1.getTitle(),
                dataResponse1.getIsbn(),
                dataResponse1.getType(),
                dataResponse1.getAuthorId(),
                dataResponse1.getStoreIds()
        );
        
        BookFacadeResponse facadeResponse2 = new BookFacadeResponse(
                dataResponse2.getId(),
                dataResponse2.getTitle(),
                dataResponse2.getIsbn(),
                dataResponse2.getType(),
                dataResponse2.getAuthorId(),
                dataResponse2.getStoreIds()
        );
        
        List<BookFacadeResponse> expectedResponses = Arrays.asList(facadeResponse1, facadeResponse2);

        when(restTemplate.exchange(
                eq(DATA_SERVICE_URL + "/author/" + authorId.toString()),
                eq(HttpMethod.GET),
                eq(HttpEntity.EMPTY),
                any(ParameterizedTypeReference.class)
        )).thenReturn(ResponseEntity.ok(dataResponses));
        when(bookMapper.map(dataResponses)).thenReturn(expectedResponses);

        // Act
        List<BookFacadeResponse> results = bookService.findBooksByAuthorId(authorId);

        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals(expectedResponses.get(0).getId(), results.get(0).getId());
        assertEquals(expectedResponses.get(0).getTitle(), results.get(0).getTitle());
        assertEquals(expectedResponses.get(1).getId(), results.get(1).getId());
        assertEquals(expectedResponses.get(1).getTitle(), results.get(1).getTitle());
        verify(restTemplate).exchange(
                eq(DATA_SERVICE_URL + "/author/" + authorId.toString()),
                eq(HttpMethod.GET),
                eq(HttpEntity.EMPTY),
                any(ParameterizedTypeReference.class)
        );
        verify(bookMapper).map(dataResponses);
    }
}