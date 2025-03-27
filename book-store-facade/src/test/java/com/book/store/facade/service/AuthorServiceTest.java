package com.book.store.facade.service;

import com.book.store.facade.mapper.AuthorMapper;
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
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    private static final String DATA_SERVICE_URL = "http://localhost:8080/book-store-data-service/v1/author";

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private AuthorMapper authorMapper;

    @InjectMocks
    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authorService, "bookUrl", DATA_SERVICE_URL);
    }

    @Test
    void create_shouldCallRestTemplateAndReturnMappedResponse() {
        // Arrange
        CreateAuthorFacadeRequest facadeRequest = new CreateAuthorFacadeRequest("Test Author");
        CreateAuthorDataRequest dataRequest = new CreateAuthorDataRequest("Test Author");
        AuthorDataResponse dataResponse = new AuthorDataResponse();
        UUID authorId = UUID.randomUUID();
        dataResponse.setId(authorId);
        dataResponse.setName("Test Author");
        AuthorFacadeResponse expectedResponse = new AuthorFacadeResponse(dataResponse.getId(), dataResponse.getName());

        when(authorMapper.map(facadeRequest)).thenReturn(dataRequest);
        when(restTemplate.exchange(
                eq(DATA_SERVICE_URL),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(AuthorDataResponse.class)
        )).thenReturn(ResponseEntity.ok(dataResponse));
        when(authorMapper.map(dataResponse)).thenReturn(expectedResponse);

        // Act
        AuthorFacadeResponse result = authorService.create(facadeRequest);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse.getId(), result.getId());
        assertEquals(expectedResponse.getName(), result.getName());
        verify(authorMapper).map(facadeRequest);
        verify(restTemplate).exchange(
                eq(DATA_SERVICE_URL),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(AuthorDataResponse.class)
        );
        verify(authorMapper).map(dataResponse);
    }

    @Test
    void update_shouldCallRestTemplateAndReturnMappedResponse() {
        // Arrange
        UUID authorId = UUID.randomUUID();
        UpdateAuthorFacadeRequest facadeRequest = new UpdateAuthorFacadeRequest("Updated Author");
        UpdateAuthorDataRequest dataRequest = new UpdateAuthorDataRequest("Updated Author");
        AuthorDataResponse dataResponse = new AuthorDataResponse();
        dataResponse.setId(authorId);
        dataResponse.setName("Updated Author");
        AuthorFacadeResponse expectedResponse = new AuthorFacadeResponse(authorId, "Updated Author");

        when(authorMapper.map(facadeRequest)).thenReturn(dataRequest);
        when(restTemplate.exchange(
                eq(DATA_SERVICE_URL + "/" + authorId.toString()),
                eq(HttpMethod.PATCH),
                any(HttpEntity.class),
                eq(AuthorDataResponse.class)
        )).thenReturn(ResponseEntity.ok(dataResponse));
        when(authorMapper.map(dataResponse)).thenReturn(expectedResponse);

        // Act
        AuthorFacadeResponse result = authorService.update(authorId, facadeRequest);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse.getId(), result.getId());
        assertEquals(expectedResponse.getName(), result.getName());
        verify(authorMapper).map(facadeRequest);
        verify(restTemplate).exchange(
                eq(DATA_SERVICE_URL + "/" + authorId.toString()),
                eq(HttpMethod.PATCH),
                any(HttpEntity.class),
                eq(AuthorDataResponse.class)
        );
        verify(authorMapper).map(dataResponse);
    }

    @Test
    void delete_shouldCallRestTemplate() {
        // Arrange
        UUID authorId = UUID.randomUUID();
        doNothing().when(restTemplate).delete(DATA_SERVICE_URL + "/" + authorId.toString());

        // Act
        authorService.delete(authorId);

        // Assert
        verify(restTemplate).delete(DATA_SERVICE_URL + "/" + authorId.toString());
    }

    @Test
    void findAll_shouldCallRestTemplateAndReturnMappedResponses() {
        // Arrange
        UUID authorId1 = UUID.randomUUID();
        UUID authorId2 = UUID.randomUUID();

        AuthorDataResponse dataResponse1 = new AuthorDataResponse();
        dataResponse1.setId(authorId1);
        dataResponse1.setName("Author 1");

        AuthorDataResponse dataResponse2 = new AuthorDataResponse();
        dataResponse2.setId(authorId2);
        dataResponse2.setName("Author 2");

        List<AuthorDataResponse> dataResponses = Arrays.asList(dataResponse1, dataResponse2);
        List<AuthorFacadeResponse> expectedResponses = Arrays.asList(
                new AuthorFacadeResponse(authorId1, "Author 1"),
                new AuthorFacadeResponse(authorId2, "Author 2")
        );

        when(restTemplate.exchange(
                eq(DATA_SERVICE_URL),
                eq(HttpMethod.GET),
                eq(HttpEntity.EMPTY),
                any(ParameterizedTypeReference.class)
        )).thenReturn(ResponseEntity.ok(dataResponses));
        when(authorMapper.map(dataResponses)).thenReturn(expectedResponses);

        // Act
        List<AuthorFacadeResponse> results = authorService.findAll();

        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals(expectedResponses.get(0).getId(), results.get(0).getId());
        assertEquals(expectedResponses.get(0).getName(), results.get(0).getName());
        assertEquals(expectedResponses.get(1).getId(), results.get(1).getId());
        assertEquals(expectedResponses.get(1).getName(), results.get(1).getName());
        verify(restTemplate).exchange(
                eq(DATA_SERVICE_URL),
                eq(HttpMethod.GET),
                eq(HttpEntity.EMPTY),
                any(ParameterizedTypeReference.class)
        );
        verify(authorMapper).map(dataResponses);
    }

    @Test
    void findById_shouldCallRestTemplateAndReturnMappedResponse() {
        // Arrange
        UUID authorId = UUID.randomUUID();
        AuthorDataResponse dataResponse = new AuthorDataResponse();
        dataResponse.setId(authorId);
        dataResponse.setName("Test Author");
        AuthorFacadeResponse expectedResponse = new AuthorFacadeResponse(authorId, "Test Author");

        when(restTemplate.exchange(
                eq(DATA_SERVICE_URL + "/" + authorId.toString()),
                eq(HttpMethod.GET),
                eq(HttpEntity.EMPTY),
                eq(AuthorDataResponse.class)
        )).thenReturn(ResponseEntity.ok(dataResponse));
        when(authorMapper.map(dataResponse)).thenReturn(expectedResponse);

        // Act
        AuthorFacadeResponse result = authorService.findById(authorId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse.getId(), result.getId());
        assertEquals(expectedResponse.getName(), result.getName());
        verify(restTemplate).exchange(
                eq(DATA_SERVICE_URL + "/" + authorId.toString()),
                eq(HttpMethod.GET),
                eq(HttpEntity.EMPTY),
                eq(AuthorDataResponse.class)
        );
        verify(authorMapper).map(dataResponse);
    }
}
