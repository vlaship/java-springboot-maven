package com.book.store.facade.service;

import com.book.store.facade.mapper.StoreMapper;
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
class StoreServiceTest {

    private static final String DATA_SERVICE_URL = "http://localhost:8080/book-store-data-service/v1/store";

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private StoreMapper storeMapper;

    @InjectMocks
    private StoreService storeService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(storeService, "bookUrl", DATA_SERVICE_URL);
    }

    @Test
    void create_shouldCallRestTemplateAndReturnMappedResponse() {
        // Arrange
        CreateStoreFacadeRequest facadeRequest = new CreateStoreFacadeRequest("Test Store", "123 Test St");
        CreateStoreDataRequest dataRequest = new CreateStoreDataRequest("Test Store", "123 Test St");
        
        StoreDataResponse dataResponse = new StoreDataResponse();
        UUID storeId = UUID.randomUUID();
        dataResponse.setId(storeId);
        dataResponse.setName("Test Store");
        dataResponse.setAddress("123 Test St");
        
        StoreFacadeResponse expectedResponse = new StoreFacadeResponse(
                dataResponse.getId(),
                dataResponse.getName(),
                dataResponse.getAddress()
        );

        when(storeMapper.map(facadeRequest)).thenReturn(dataRequest);
        when(restTemplate.exchange(
                eq(DATA_SERVICE_URL),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(StoreDataResponse.class)
        )).thenReturn(ResponseEntity.ok(dataResponse));
        when(storeMapper.map(dataResponse)).thenReturn(expectedResponse);

        // Act
        StoreFacadeResponse result = storeService.create(facadeRequest);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse.getId(), result.getId());
        assertEquals(expectedResponse.getName(), result.getName());
        assertEquals(expectedResponse.getAddress(), result.getAddress());
        verify(storeMapper).map(facadeRequest);
        verify(restTemplate).exchange(
                eq(DATA_SERVICE_URL),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(StoreDataResponse.class)
        );
        verify(storeMapper).map(dataResponse);
    }

    @Test
    void update_shouldCallRestTemplateAndReturnMappedResponse() {
        // Arrange
        UUID storeId = UUID.randomUUID();
        UpdateStoreFacadeRequest facadeRequest = new UpdateStoreFacadeRequest("Updated Store", "456 Updated St");
        UpdateStoreDataRequest dataRequest = new UpdateStoreDataRequest("Updated Store", "456 Updated St");
        
        StoreDataResponse dataResponse = new StoreDataResponse();
        dataResponse.setId(storeId);
        dataResponse.setName("Updated Store");
        dataResponse.setAddress("456 Updated St");
        
        StoreFacadeResponse expectedResponse = new StoreFacadeResponse(
                dataResponse.getId(),
                dataResponse.getName(),
                dataResponse.getAddress()
        );

        when(storeMapper.map(facadeRequest)).thenReturn(dataRequest);
        when(restTemplate.exchange(
                eq(DATA_SERVICE_URL + "/" + storeId.toString()),
                eq(HttpMethod.PATCH),
                any(HttpEntity.class),
                eq(StoreDataResponse.class)
        )).thenReturn(ResponseEntity.ok(dataResponse));
        when(storeMapper.map(dataResponse)).thenReturn(expectedResponse);

        // Act
        StoreFacadeResponse result = storeService.update(storeId, facadeRequest);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse.getId(), result.getId());
        assertEquals(expectedResponse.getName(), result.getName());
        assertEquals(expectedResponse.getAddress(), result.getAddress());
        verify(storeMapper).map(facadeRequest);
        verify(restTemplate).exchange(
                eq(DATA_SERVICE_URL + "/" + storeId.toString()),
                eq(HttpMethod.PATCH),
                any(HttpEntity.class),
                eq(StoreDataResponse.class)
        );
        verify(storeMapper).map(dataResponse);
    }

    @Test
    void delete_shouldCallRestTemplate() {
        // Arrange
        UUID storeId = UUID.randomUUID();
        doNothing().when(restTemplate).delete(DATA_SERVICE_URL + "/" + storeId.toString());

        // Act
        storeService.delete(storeId);

        // Assert
        verify(restTemplate).delete(DATA_SERVICE_URL + "/" + storeId.toString());
    }

    @Test
    void findAll_shouldCallRestTemplateAndReturnMappedResponses() {
        // Arrange
        UUID storeId1 = UUID.randomUUID();
        UUID storeId2 = UUID.randomUUID();
        
        StoreDataResponse dataResponse1 = new StoreDataResponse();
        dataResponse1.setId(storeId1);
        dataResponse1.setName("Store 1");
        dataResponse1.setAddress("123 Store 1 St");
        
        StoreDataResponse dataResponse2 = new StoreDataResponse();
        dataResponse2.setId(storeId2);
        dataResponse2.setName("Store 2");
        dataResponse2.setAddress("456 Store 2 St");
        
        List<StoreDataResponse> dataResponses = Arrays.asList(dataResponse1, dataResponse2);
        
        StoreFacadeResponse facadeResponse1 = new StoreFacadeResponse(
                dataResponse1.getId(),
                dataResponse1.getName(),
                dataResponse1.getAddress()
        );
        
        StoreFacadeResponse facadeResponse2 = new StoreFacadeResponse(
                dataResponse2.getId(),
                dataResponse2.getName(),
                dataResponse2.getAddress()
        );
        
        List<StoreFacadeResponse> expectedResponses = Arrays.asList(facadeResponse1, facadeResponse2);

        when(restTemplate.exchange(
                eq(DATA_SERVICE_URL),
                eq(HttpMethod.GET),
                eq(HttpEntity.EMPTY),
                any(ParameterizedTypeReference.class)
        )).thenReturn(ResponseEntity.ok(dataResponses));
        when(storeMapper.map(dataResponses)).thenReturn(expectedResponses);

        // Act
        List<StoreFacadeResponse> results = storeService.findAll();

        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals(expectedResponses.get(0).getId(), results.get(0).getId());
        assertEquals(expectedResponses.get(0).getName(), results.get(0).getName());
        assertEquals(expectedResponses.get(0).getAddress(), results.get(0).getAddress());
        assertEquals(expectedResponses.get(1).getId(), results.get(1).getId());
        assertEquals(expectedResponses.get(1).getName(), results.get(1).getName());
        assertEquals(expectedResponses.get(1).getAddress(), results.get(1).getAddress());
        verify(restTemplate).exchange(
                eq(DATA_SERVICE_URL),
                eq(HttpMethod.GET),
                eq(HttpEntity.EMPTY),
                any(ParameterizedTypeReference.class)
        );
        verify(storeMapper).map(dataResponses);
    }

    @Test
    void findById_shouldCallRestTemplateAndReturnMappedResponse() {
        // Arrange
        UUID storeId = UUID.randomUUID();
        StoreDataResponse dataResponse = new StoreDataResponse();
        dataResponse.setId(storeId);
        dataResponse.setName("Test Store");
        dataResponse.setAddress("123 Test St");
        
        StoreFacadeResponse expectedResponse = new StoreFacadeResponse(
                dataResponse.getId(),
                dataResponse.getName(),
                dataResponse.getAddress()
        );

        when(restTemplate.exchange(
                eq(DATA_SERVICE_URL + "/" + storeId.toString()),
                eq(HttpMethod.GET),
                eq(HttpEntity.EMPTY),
                eq(StoreDataResponse.class)
        )).thenReturn(ResponseEntity.ok(dataResponse));
        when(storeMapper.map(dataResponse)).thenReturn(expectedResponse);

        // Act
        StoreFacadeResponse result = storeService.findById(storeId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse.getId(), result.getId());
        assertEquals(expectedResponse.getName(), result.getName());
        assertEquals(expectedResponse.getAddress(), result.getAddress());
        verify(restTemplate).exchange(
                eq(DATA_SERVICE_URL + "/" + storeId.toString()),
                eq(HttpMethod.GET),
                eq(HttpEntity.EMPTY),
                eq(StoreDataResponse.class)
        );
        verify(storeMapper).map(dataResponse);
    }
}