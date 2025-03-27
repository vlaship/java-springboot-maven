package com.book.store.data.integration.repository;

import com.book.store.data.entity.Store;
import com.book.store.data.repository.StoreRepository;
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
class StoreRepositoryIntegrationTest {

    @Autowired
    private StoreRepository storeRepository;

    @Test
    void testSaveAndFindById() {
        // Arrange
        UUID id = UUID.randomUUID();
        Store store = new Store();
        store.setId(id);
        store.setName("Test Store");
        store.setAddress("123 Test Street");

        // Act
        storeRepository.save(store);
        Optional<Store> foundStore = storeRepository.findById(id);

        // Assert
        assertTrue(foundStore.isPresent());
        assertEquals("Test Store", foundStore.get().getName());
        assertEquals("123 Test Street", foundStore.get().getAddress());
    }

    @Test
    void testFindAll() {
        // Arrange
        UUID id1 = UUID.randomUUID();
        Store store1 = new Store();
        store1.setId(id1);
        store1.setName("Store 1");
        store1.setAddress("123 First Street");

        UUID id2 = UUID.randomUUID();
        Store store2 = new Store();
        store2.setId(id2);
        store2.setName("Store 2");
        store2.setAddress("456 Second Street");

        storeRepository.save(store1);
        storeRepository.save(store2);

        // Act
        List<Store> stores = storeRepository.findAll();

        // Assert
        assertEquals(2, stores.size());
        assertTrue(stores.stream().anyMatch(s -> s.getName().equals("Store 1")));
        assertTrue(stores.stream().anyMatch(s -> s.getName().equals("Store 2")));
    }

    @Test
    void testUpdate() {
        // Arrange
        UUID id = UUID.randomUUID();
        Store store = new Store();
        store.setId(id);
        store.setName("Original Name");
        store.setAddress("Original Address");
        storeRepository.save(store);

        // Act
        store.setName("Updated Name");
        store.setAddress("Updated Address");
        storeRepository.save(store);
        Optional<Store> updatedStore = storeRepository.findById(id);

        // Assert
        assertTrue(updatedStore.isPresent());
        assertEquals("Updated Name", updatedStore.get().getName());
        assertEquals("Updated Address", updatedStore.get().getAddress());
    }

    @Test
    void testDelete() {
        // Arrange
        UUID id = UUID.randomUUID();
        Store store = new Store();
        store.setId(id);
        store.setName("Test Store");
        store.setAddress("123 Test Street");
        storeRepository.save(store);

        // Act
        storeRepository.deleteById(id);
        Optional<Store> deletedStore = storeRepository.findById(id);

        // Assert
        assertFalse(deletedStore.isPresent());
    }
}