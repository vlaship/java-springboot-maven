package com.book.store.facade.integration.controller;

import com.book.store.facade.model.StoreFacadeResponse;
import com.book.store.facade.model.CreateStoreFacadeRequest;
import com.book.store.facade.model.UpdateStoreFacadeRequest;
import com.book.store.facade.service.StoreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser
class StoreControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StoreService storeService;

    @Test
    void testGetStoreById() throws Exception {
        // Arrange
        UUID storeId = UUID.randomUUID();
        StoreFacadeResponse response = new StoreFacadeResponse(storeId, "Test Store", "123 Test Street");
        when(storeService.findById(storeId)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/v1/store/{id}", storeId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(storeId.toString())))
                .andExpect(jsonPath("$.name", is("Test Store")))
                .andExpect(jsonPath("$.address", is("123 Test Street")));
    }

    @Test
    void testGetStores() throws Exception {
        // Arrange
        UUID storeId1 = UUID.randomUUID();
        UUID storeId2 = UUID.randomUUID();
        List<StoreFacadeResponse> stores = Arrays.asList(
                new StoreFacadeResponse(storeId1, "Store 1", "123 First Street"),
                new StoreFacadeResponse(storeId2, "Store 2", "456 Second Street")
        );
        when(storeService.findAll()).thenReturn(stores);

        // Act & Assert
        mockMvc.perform(get("/v1/store"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(storeId1.toString())))
                .andExpect(jsonPath("$[0].name", is("Store 1")))
                .andExpect(jsonPath("$[0].address", is("123 First Street")))
                .andExpect(jsonPath("$[1].id", is(storeId2.toString())))
                .andExpect(jsonPath("$[1].name", is("Store 2")))
                .andExpect(jsonPath("$[1].address", is("456 Second Street")));
    }

    @Test
    void testCreateStore() throws Exception {
        // Arrange
        UUID storeId = UUID.randomUUID();
        CreateStoreFacadeRequest request = new CreateStoreFacadeRequest("New Store", "123 New Street");
        StoreFacadeResponse response = new StoreFacadeResponse(storeId, "New Store", "123 New Street");
        when(storeService.create(any(CreateStoreFacadeRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/v1/store")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(storeId.toString())))
                .andExpect(jsonPath("$.name", is("New Store")))
                .andExpect(jsonPath("$.address", is("123 New Street")));
    }

    @Test
    void testUpdateStore() throws Exception {
        // Arrange
        UUID storeId = UUID.randomUUID();
        UpdateStoreFacadeRequest request = new UpdateStoreFacadeRequest("Updated Store", "456 Updated Street");
        StoreFacadeResponse response = new StoreFacadeResponse(storeId, "Updated Store", "456 Updated Street");
        when(storeService.update(eq(storeId), any(UpdateStoreFacadeRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(patch("/v1/store/{id}", storeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(storeId.toString())))
                .andExpect(jsonPath("$.name", is("Updated Store")))
                .andExpect(jsonPath("$.address", is("456 Updated Street")));
    }

    @Test
    void testDeleteStore() throws Exception {
        // Arrange
        UUID storeId = UUID.randomUUID();
        doNothing().when(storeService).delete(storeId);

        // Act & Assert
        mockMvc.perform(delete("/v1/store/{id}", storeId))
                .andExpect(status().isAccepted());
    }
}