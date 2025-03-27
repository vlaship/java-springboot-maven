package com.book.store.data.integration.service;

import com.book.store.data.dto.CreateStoreRequest;
import com.book.store.data.dto.StoreResponse;
import com.book.store.data.dto.UpdateStoreRequest;
import com.book.store.data.repository.StoreRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class StoreServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StoreRepository storeRepository;

    private CreateStoreRequest createStoreRequest;
    private UpdateStoreRequest updateStoreRequest;

    @BeforeEach
    void setUp() {
        // Clear the database before each test
        storeRepository.deleteAll();

        // Initialize test data
        createStoreRequest = new CreateStoreRequest("Test Store", "123 Test Street");
        updateStoreRequest = new UpdateStoreRequest("Updated Store", "456 Updated Street");
    }

    @Test
    void createStore_ShouldReturnCreatedStore() throws Exception {
        // Act
        MvcResult result = mockMvc.perform(post("/v1/store")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createStoreRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Store"))
                .andExpect(jsonPath("$.address").value("123 Test Street"))
                .andReturn();

        // Assert
        String content = result.getResponse().getContentAsString();
        StoreResponse response = objectMapper.readValue(content, StoreResponse.class);
        
        assertNotNull(response.getId());
        assertEquals("Test Store", response.getName());
        assertEquals("123 Test Street", response.getAddress());
        
        // Verify the store was saved to the database
        assertTrue(storeRepository.findById(response.getId()).isPresent());
    }

    @Test
    void getAllStores_ShouldReturnAllStores() throws Exception {
        // Arrange
        // Create a few stores
        mockMvc.perform(post("/v1/store")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CreateStoreRequest("Store 1", "Address 1"))))
                .andExpect(status().isOk());

        mockMvc.perform(post("/v1/store")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CreateStoreRequest("Store 2", "Address 2"))))
                .andExpect(status().isOk());

        // Act & Assert
        MvcResult result = mockMvc.perform(get("/v1/store")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        List<StoreResponse> stores = objectMapper.readValue(content, 
                objectMapper.getTypeFactory().constructCollectionType(List.class, StoreResponse.class));
        
        assertEquals(2, stores.size());
        assertTrue(stores.stream().anyMatch(store -> "Store 1".equals(store.getName())));
        assertTrue(stores.stream().anyMatch(store -> "Store 2".equals(store.getName())));
    }

    @Test
    void getStoreById_WhenStoreExists_ShouldReturnStore() throws Exception {
        // Arrange
        MvcResult createResult = mockMvc.perform(post("/v1/store")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createStoreRequest)))
                .andExpect(status().isOk())
                .andReturn();

        StoreResponse createdStore = objectMapper.readValue(
                createResult.getResponse().getContentAsString(), StoreResponse.class);

        // Act & Assert
        mockMvc.perform(get("/v1/store/{id}", createdStore.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdStore.getId().toString()))
                .andExpect(jsonPath("$.name").value("Test Store"))
                .andExpect(jsonPath("$.address").value("123 Test Street"));
    }

    @Test
    void getStoreById_WhenStoreDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/v1/store/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateStore_WhenStoreExists_ShouldReturnUpdatedStore() throws Exception {
        // Arrange
        MvcResult createResult = mockMvc.perform(post("/v1/store")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createStoreRequest)))
                .andExpect(status().isOk())
                .andReturn();

        StoreResponse createdStore = objectMapper.readValue(
                createResult.getResponse().getContentAsString(), StoreResponse.class);

        // Act & Assert
        MvcResult updateResult = mockMvc.perform(patch("/v1/store/{id}", createdStore.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateStoreRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdStore.getId().toString()))
                .andExpect(jsonPath("$.name").value("Updated Store"))
                .andExpect(jsonPath("$.address").value("456 Updated Street"))
                .andReturn();

        // Verify the store was updated in the database
        StoreResponse updatedStore = objectMapper.readValue(
                updateResult.getResponse().getContentAsString(), StoreResponse.class);
        assertEquals("Updated Store", updatedStore.getName());
        assertEquals("456 Updated Street", updatedStore.getAddress());
        
        assertTrue(storeRepository.findById(createdStore.getId()).isPresent());
        assertEquals("Updated Store", storeRepository.findById(createdStore.getId()).get().getName());
        assertEquals("456 Updated Street", storeRepository.findById(createdStore.getId()).get().getAddress());
    }

    @Test
    void updateStore_WhenStoreDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(patch("/v1/store/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateStoreRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteStore_WhenStoreExists_ShouldDeleteStore() throws Exception {
        // Arrange
        MvcResult createResult = mockMvc.perform(post("/v1/store")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createStoreRequest)))
                .andExpect(status().isOk())
                .andReturn();

        StoreResponse createdStore = objectMapper.readValue(
                createResult.getResponse().getContentAsString(), StoreResponse.class);

        // Act
        mockMvc.perform(delete("/v1/store/{id}", createdStore.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());

        // Assert
        assertFalse(storeRepository.findById(createdStore.getId()).isPresent());
    }
}