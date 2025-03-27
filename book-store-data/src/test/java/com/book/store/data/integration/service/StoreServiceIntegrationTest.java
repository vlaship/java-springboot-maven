package com.book.store.data.integration.service;

import com.book.store.data.dto.CreateStoreRequest;
import com.book.store.data.dto.StoreResponse;
import com.book.store.data.dto.UpdateStoreRequest;
import com.book.store.data.entity.Store;
import com.book.store.data.exception.NotFoundException;
import com.book.store.data.repository.StoreRepository;
import com.book.store.data.service.StoreService;
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
class StoreServiceIntegrationTest {

    @Autowired
    private StoreService storeService;

    @Autowired
    private StoreRepository storeRepository;

    @Test
    void testCreateStore() {
        // Arrange
        CreateStoreRequest request = new CreateStoreRequest("New Store", "123 New Street");

        // Act
        StoreResponse response = storeService.create(request);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals("New Store", response.getName());
        assertEquals("123 New Street", response.getAddress());

        // Verify the store was saved to the repository
        assertTrue(storeRepository.findById(response.getId()).isPresent());
    }

    @Test
    void testUpdateStore() {
        // Arrange
        // First create a store
        CreateStoreRequest createRequest = new CreateStoreRequest("Original Store", "123 Original Street");
        StoreResponse createdStore = storeService.create(createRequest);
        UUID storeId = createdStore.getId();

        // Now update it
        UpdateStoreRequest updateRequest = new UpdateStoreRequest("Updated Store", "456 Updated Street");

        // Act
        StoreResponse updatedResponse = storeService.update(storeId, updateRequest);

        // Assert
        assertNotNull(updatedResponse);
        assertEquals(storeId, updatedResponse.getId());
        assertEquals("Updated Store", updatedResponse.getName());
        assertEquals("456 Updated Street", updatedResponse.getAddress());

        // Verify the store was updated in the repository
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new RuntimeException("Store not found"));
        assertEquals("Updated Store", store.getName());
        assertEquals("456 Updated Street", store.getAddress());
    }

    @Test
    void testUpdateStore_NotFound() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        UpdateStoreRequest updateRequest = new UpdateStoreRequest("Updated Store", "456 Updated Street");

        // Act & Assert
        assertThrows(NotFoundException.class, () -> storeService.update(nonExistentId, updateRequest));
    }

    @Test
    void testDeleteStore() {
        // Arrange
        // First create a store
        CreateStoreRequest createRequest = new CreateStoreRequest("Store to Delete", "123 Delete Street");
        StoreResponse createdStore = storeService.create(createRequest);
        UUID storeId = createdStore.getId();

        // Act
        storeService.delete(storeId);

        // Assert
        // Verify the store was deleted from the repository
        assertFalse(storeRepository.findById(storeId).isPresent());
    }

    @Test
    void testFindAllStores() {
        // Arrange
        // Create a few stores
        storeService.create(new CreateStoreRequest("Store 1", "123 First Street"));
        storeService.create(new CreateStoreRequest("Store 2", "456 Second Street"));
        storeService.create(new CreateStoreRequest("Store 3", "789 Third Street"));

        // Act
        List<StoreResponse> stores = storeService.findAll();

        // Assert
        assertNotNull(stores);
        assertTrue(stores.size() >= 3);
        assertTrue(stores.stream().anyMatch(s -> s.getName().equals("Store 1")));
        assertTrue(stores.stream().anyMatch(s -> s.getName().equals("Store 2")));
        assertTrue(stores.stream().anyMatch(s -> s.getName().equals("Store 3")));
    }

    @Test
    void testFindStoreById() {
        // Arrange
        // First create a store
        CreateStoreRequest createRequest = new CreateStoreRequest("Store to Find", "123 Find Street");
        StoreResponse createdStore = storeService.create(createRequest);
        UUID storeId = createdStore.getId();

        // Act
        StoreResponse foundStore = storeService.findById(storeId);

        // Assert
        assertNotNull(foundStore);
        assertEquals(storeId, foundStore.getId());
        assertEquals("Store to Find", foundStore.getName());
        assertEquals("123 Find Street", foundStore.getAddress());
    }

    @Test
    void testFindStoreById_NotFound() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();

        // Act & Assert
        assertThrows(NotFoundException.class, () -> storeService.findById(nonExistentId));
    }
}