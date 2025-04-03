package com.book.store.facade.controller;

import com.book.store.facade.model.UserFacadeResponse;
import com.book.store.facade.model.CreateUserFacadeRequest;
import com.book.store.facade.model.UpdateUserFacadeRequest;
import com.book.store.facade.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerAuthorizationTest {

    @MockBean
    private UserService userService;

    @Autowired
    private UserController userController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
    void testGetUserById_withUserRole_shouldSucceed() {
        // Arrange
        setUpUserAuthentication();
        UUID userId = UUID.randomUUID();
        UserFacadeResponse response = new UserFacadeResponse(
                userId,
                "testuser",
                "test@example.com",
                Collections.singletonList("ROLE_USER")
        );
        when(userService.findById(userId)).thenReturn(response);

        // Act
        ResponseEntity<UserFacadeResponse> result = userController.getUserById(userId);

        // Assert
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(response, result.getBody());
    }

    @Test
    void testGetUserById_withAdminRole_shouldSucceed() {
        // Arrange
        setUpAdminAuthentication();
        UUID userId = UUID.randomUUID();
        UserFacadeResponse response = new UserFacadeResponse(
                userId,
                "testuser",
                "test@example.com",
                Collections.singletonList("ROLE_USER")
        );
        when(userService.findById(userId)).thenReturn(response);

        // Act
        ResponseEntity<UserFacadeResponse> result = userController.getUserById(userId);

        // Assert
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(response, result.getBody());
    }

    @Test
    void testCreateUser_withUserRole_shouldSucceed() {
        // Arrange
        setUpUserAuthentication();
        UUID userId = UUID.randomUUID();
        CreateUserFacadeRequest request = new CreateUserFacadeRequest(
                "newuser",
                "password12345",
                "new@example.com",
                Collections.singletonList("ROLE_USER")
        );
        UserFacadeResponse response = new UserFacadeResponse(
                userId,
                "newuser",
                "new@example.com",
                Collections.singletonList("ROLE_USER")
        );
        when(userService.create(any(CreateUserFacadeRequest.class))).thenReturn(response);

        // Act
        ResponseEntity<UserFacadeResponse> result = userController.create(request);

        // Assert
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(response, result.getBody());
    }

    @Test
    void testCreateUser_withAdminRole_shouldSucceed() {
        // Arrange
        setUpAdminAuthentication();
        UUID userId = UUID.randomUUID();
        CreateUserFacadeRequest request = new CreateUserFacadeRequest(
                "newuser",
                "password12345",
                "new@example.com",
                Collections.singletonList("ROLE_USER")
        );
        UserFacadeResponse response = new UserFacadeResponse(
                userId,
                "newuser",
                "new@example.com",
                Collections.singletonList("ROLE_USER")
        );
        when(userService.create(any(CreateUserFacadeRequest.class))).thenReturn(response);

        // Act
        ResponseEntity<UserFacadeResponse> result = userController.create(request);

        // Assert
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(response, result.getBody());
    }

    @Test
    void testUpdateUser_withUserRole_shouldSucceed() {
        // Arrange
        setUpUserAuthentication();
        UUID userId = UUID.randomUUID();
        UpdateUserFacadeRequest request = new UpdateUserFacadeRequest(
                "updateduser",
                "newpassword12345",
                "updated@example.com",
                Collections.singletonList("ROLE_USER")
        );
        UserFacadeResponse response = new UserFacadeResponse(
                userId,
                "updateduser",
                "updated@example.com",
                Collections.singletonList("ROLE_USER")
        );
        when(userService.update(eq(userId), any(UpdateUserFacadeRequest.class))).thenReturn(response);

        // Act
        ResponseEntity<UserFacadeResponse> result = userController.update(userId, request);

        // Assert
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(response, result.getBody());
    }

    @Test
    void testUpdateUser_withAdminRole_shouldSucceed() {
        // Arrange
        setUpAdminAuthentication();
        UUID userId = UUID.randomUUID();
        UpdateUserFacadeRequest request = new UpdateUserFacadeRequest(
                "updateduser",
                "newpassword12345",
                "updated@example.com",
                Collections.singletonList("ROLE_USER")
        );
        UserFacadeResponse response = new UserFacadeResponse(
                userId,
                "updateduser",
                "updated@example.com",
                Collections.singletonList("ROLE_USER")
        );
        when(userService.update(eq(userId), any(UpdateUserFacadeRequest.class))).thenReturn(response);

        // Act
        ResponseEntity<UserFacadeResponse> result = userController.update(userId, request);

        // Assert
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(response, result.getBody());
    }

    @Test
    void testDeleteUser_withUserRole_shouldSucceed() {
        // Arrange
        setUpUserAuthentication();
        UUID userId = UUID.randomUUID();
        doNothing().when(userService).delete(userId);

        // Act
        ResponseEntity<Void> result = userController.delete(userId);

        // Assert
        assertEquals(202, result.getStatusCodeValue());
    }

    @Test
    void testDeleteUser_withAdminRole_shouldSucceed() {
        // Arrange
        setUpAdminAuthentication();
        UUID userId = UUID.randomUUID();
        doNothing().when(userService).delete(userId);

        // Act
        ResponseEntity<Void> result = userController.delete(userId);

        // Assert
        assertEquals(202, result.getStatusCodeValue());
    }

    @Test
    void testGetUserById_withNoAuthentication_shouldFail() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();

        // Act & Assert
        mockMvc.perform(get("/v1/user/{id}", userId))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testCreateUser_withNoAuthentication_shouldFail() throws Exception {
        // Arrange
        CreateUserFacadeRequest request = new CreateUserFacadeRequest(
                "newuser",
                "password12345",
                "new@example.com",
                Collections.singletonList("ROLE_USER")
        );

        // Act & Assert
        mockMvc.perform(post("/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testUpdateUser_withNoAuthentication_shouldFail() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();
        UpdateUserFacadeRequest request = new UpdateUserFacadeRequest(
                "updateduser",
                "newpassword12345",
                "updated@example.com",
                Collections.singletonList("ROLE_USER")
        );

        // Act & Assert
        mockMvc.perform(patch("/v1/user/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testDeleteUser_withNoAuthentication_shouldFail() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();

        // Act & Assert
        mockMvc.perform(delete("/v1/user/{id}", userId))
                .andExpect(status().isUnauthorized());
    }
}
