package com.book.store.data.service.impl;

import com.book.store.data.dto.StoreResponse;
import com.book.store.data.dto.CreateStoreRequest;
import com.book.store.data.dto.UpdateStoreRequest;
import com.book.store.data.entity.Store;
import com.book.store.data.exception.NotFoundException;
import com.book.store.data.mapper.StoreMapper;
import com.book.store.data.repository.StoreRepository;
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
class StoreServiceImplTest {

    @Mock
    private StoreRepository repository;

    @Mock
    private StoreMapper mapper;

    @InjectMocks
    private StoreServiceImpl service;

    private UUID storeId;
    private Store store;
    private StoreResponse storeResponse;
    private CreateStoreRequest createRequest;
    private UpdateStoreRequest updateRequest;

    @BeforeEach
    void setUp() {
        storeId = UUID.randomUUID();
        
        store = new Store();
        store.setId(storeId);
        store.setName("Test Store");
        store.setAddress("123 Test Street");
        
        storeResponse = new StoreResponse(storeId, "Test Store", "123 Test Street");
        
        createRequest = new CreateStoreRequest("New Store", "456 New Street");
        
        updateRequest = new UpdateStoreRequest("Updated Store", "789 Updated Street");
    }

    @Test
    void create_ShouldReturnStoreResponse() {
        // Arrange
        when(mapper.map(createRequest)).thenReturn(store);
        when(repository.save(store)).thenReturn(store);
        when(mapper.map(store)).thenReturn(storeResponse);

        // Act
        StoreResponse result = service.create(createRequest);

        // Assert
        assertNotNull(result);
        assertEquals(storeId, result.getId());
        assertEquals("Test Store", result.getName());
        assertEquals("123 Test Street", result.getAddress());
        
        verify(mapper).map(createRequest);
        verify(repository).save(store);
        verify(mapper).map(store);
    }

    @Test
    void update_WhenStoreExists_ShouldReturnUpdatedStoreResponse() {
        // Arrange
        when(repository.findById(storeId)).thenReturn(Optional.of(store));
        when(repository.save(store)).thenReturn(store);
        when(mapper.map(store)).thenReturn(storeResponse);

        // Act
        StoreResponse result = service.update(storeId, updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals(storeId, result.getId());
        assertEquals("Test Store", result.getName());
        assertEquals("123 Test Street", result.getAddress());
        
        verify(repository).findById(storeId);
        verify(mapper).merge(store, updateRequest);
        verify(repository).save(store);
        verify(mapper).map(store);
    }

    @Test
    void update_WhenStoreDoesNotExist_ShouldThrowNotFoundException() {
        // Arrange
        when(repository.findById(storeId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> service.update(storeId, updateRequest));
        
        verify(repository).findById(storeId);
        verifyNoMoreInteractions(mapper, repository);
    }

    @Test
    void delete_ShouldCallRepositoryDeleteById() {
        // Act
        service.delete(storeId);

        // Assert
        verify(repository).deleteById(storeId);
    }

    @Test
    void findAll_ShouldReturnListOfStoreResponses() {
        // Arrange
        List<Store> stores = Arrays.asList(store);
        when(repository.findAll()).thenReturn(stores);
        when(mapper.map(store)).thenReturn(storeResponse);

        // Act
        List<StoreResponse> result = service.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(storeId, result.get(0).getId());
        assertEquals("Test Store", result.get(0).getName());
        assertEquals("123 Test Street", result.get(0).getAddress());
        
        verify(repository).findAll();
        verify(mapper).map(store);
    }

    @Test
    void findById_WhenStoreExists_ShouldReturnStoreResponse() {
        // Arrange
        when(repository.findById(storeId)).thenReturn(Optional.of(store));
        when(mapper.map(store)).thenReturn(storeResponse);

        // Act
        StoreResponse result = service.findById(storeId);

        // Assert
        assertNotNull(result);
        assertEquals(storeId, result.getId());
        assertEquals("Test Store", result.getName());
        assertEquals("123 Test Street", result.getAddress());
        
        verify(repository).findById(storeId);
        verify(mapper).map(store);
    }

    @Test
    void findById_WhenStoreDoesNotExist_ShouldThrowNotFoundException() {
        // Arrange
        when(repository.findById(storeId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> service.findById(storeId));
        
        verify(repository).findById(storeId);
        verifyNoMoreInteractions(mapper);
    }
}