package com.book.store.facade.controller;

import com.book.store.facade.model.StoreFacadeResponse;
import com.book.store.facade.model.CreateStoreFacadeRequest;
import com.book.store.facade.model.UpdateStoreFacadeRequest;
import com.book.store.facade.service.StoreService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class StoreControllerAuthorizationTest {

    @MockBean
    private StoreService storeService;

    @Autowired
    private StoreController storeController;

    @BeforeEach
    void setUp() {
        // Clear the security context before each test
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        // Clear the security context after each test
        SecurityContextHolder.clearContext();
    }

    private void setUpUserAuthentication() {
        UserDetails userDetails = new User(
                "testuser",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void setUpManagerAuthentication() {
        UserDetails userDetails = new User(
                "testmanager",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_MANAGER"))
        );
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void setUpAdminAuthentication() {
        UserDetails userDetails = new User(
                "testadmin",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void testCreateStore_withAdminRole_shouldSucceed() {
        // Arrange
        setUpAdminAuthentication();
        UUID storeId = UUID.randomUUID();
        CreateStoreFacadeRequest request = new CreateStoreFacadeRequest(
                "New Store",
                "123 Test Street"
        );
        StoreFacadeResponse response = new StoreFacadeResponse(
                storeId,
                "New Store",
                "123 Test Street"
        );
        when(storeService.create(any(CreateStoreFacadeRequest.class))).thenReturn(response);

        // Act
        ResponseEntity<StoreFacadeResponse> result = storeController.create(request);

        // Assert
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(response, result.getBody());
    }

    @Test
    void testCreateStore_withUserRole_shouldFail() {
        // Arrange
        setUpUserAuthentication();
        CreateStoreFacadeRequest request = new CreateStoreFacadeRequest(
                "New Store",
                "123 Test Street"
        );

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> storeController.create(request));
    }

    @Test
    void testCreateStore_withManagerRole_shouldFail() {
        // Arrange
        setUpManagerAuthentication();
        CreateStoreFacadeRequest request = new CreateStoreFacadeRequest(
                "New Store",
                "123 Test Street"
        );

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> storeController.create(request));
    }

    @Test
    void testUpdateStore_withAdminRole_shouldSucceed() {
        // Arrange
        setUpAdminAuthentication();
        UUID storeId = UUID.randomUUID();
        UpdateStoreFacadeRequest request = new UpdateStoreFacadeRequest(
                "Updated Store",
                "456 Updated Street"
        );
        StoreFacadeResponse response = new StoreFacadeResponse(
                storeId,
                "Updated Store",
                "456 Updated Street"
        );
        when(storeService.update(eq(storeId), any(UpdateStoreFacadeRequest.class))).thenReturn(response);

        // Act
        ResponseEntity<StoreFacadeResponse> result = storeController.update(storeId, request);

        // Assert
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(response, result.getBody());
    }

    @Test
    void testUpdateStore_withUserRole_shouldFail() {
        // Arrange
        setUpUserAuthentication();
        UUID storeId = UUID.randomUUID();
        UpdateStoreFacadeRequest request = new UpdateStoreFacadeRequest(
                "Updated Store",
                "456 Updated Street"
        );

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> storeController.update(storeId, request));
    }

    @Test
    void testUpdateStore_withManagerRole_shouldFail() {
        // Arrange
        setUpManagerAuthentication();
        UUID storeId = UUID.randomUUID();
        UpdateStoreFacadeRequest request = new UpdateStoreFacadeRequest(
                "Updated Store",
                "456 Updated Street"
        );

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> storeController.update(storeId, request));
    }

    @Test
    void testDeleteStore_withAdminRole_shouldSucceed() {
        // Arrange
        setUpAdminAuthentication();
        UUID storeId = UUID.randomUUID();
        doNothing().when(storeService).delete(storeId);

        // Act
        ResponseEntity<Void> result = storeController.delete(storeId);

        // Assert
        assertEquals(202, result.getStatusCodeValue());
    }

    @Test
    void testDeleteStore_withUserRole_shouldFail() {
        // Arrange
        setUpUserAuthentication();
        UUID storeId = UUID.randomUUID();

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> storeController.delete(storeId));
    }

    @Test
    void testDeleteStore_withManagerRole_shouldFail() {
        // Arrange
        setUpManagerAuthentication();
        UUID storeId = UUID.randomUUID();

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> storeController.delete(storeId));
    }

    @Test
    void testGetStoreById_withUserRole_shouldSucceed() {
        // Arrange
        setUpUserAuthentication();
        UUID storeId = UUID.randomUUID();
        StoreFacadeResponse response = new StoreFacadeResponse(
                storeId,
                "Test Store",
                "123 Test Street"
        );
        when(storeService.findById(storeId)).thenReturn(response);

        // Act
        ResponseEntity<StoreFacadeResponse> result = storeController.getStoreById(storeId);

        // Assert
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(response, result.getBody());
    }

    @Test
    void testGetStores_withUserRole_shouldSucceed() {
        // Arrange
        setUpUserAuthentication();
        UUID storeId1 = UUID.randomUUID();
        UUID storeId2 = UUID.randomUUID();
        List<StoreFacadeResponse> stores = Arrays.asList(
                new StoreFacadeResponse(
                        storeId1,
                        "Store 1",
                        "123 First Street"
                ),
                new StoreFacadeResponse(
                        storeId2,
                        "Store 2",
                        "456 Second Street"
                )
        );
        when(storeService.findAll()).thenReturn(stores);

        // Act
        ResponseEntity<List<StoreFacadeResponse>> result = storeController.getStores();

        // Assert
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(stores, result.getBody());
    }
}