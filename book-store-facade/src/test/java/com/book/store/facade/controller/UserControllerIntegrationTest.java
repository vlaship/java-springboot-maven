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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerIntegrationTest {

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
        // Reset mocks
        reset(userService);
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
    void testGetUserById_existingUser_returnsUser() {
        // Arrange
        setUpUserAuthentication();
        UUID userId = UUID.randomUUID();
        UserFacadeResponse expectedResponse = new UserFacadeResponse(
                userId,
                "testuser",
                "test@example.com",
                Collections.singletonList("ROLE_USER")
        );
        when(userService.findById(userId)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<UserFacadeResponse> response = userController.getUserById(userId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
        verify(userService, times(1)).findById(userId);
    }

    @Test
    void testGetUserById_withAdminRole_shouldSucceed() {
        // Arrange
        setUpAdminAuthentication();
        UUID userId = UUID.randomUUID();
        UserFacadeResponse expectedResponse = new UserFacadeResponse(
                userId,
                "testuser",
                "test@example.com",
                Collections.singletonList("ROLE_USER")
        );
        when(userService.findById(userId)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<UserFacadeResponse> response = userController.getUserById(userId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
        verify(userService, times(1)).findById(userId);
    }

    @Test
    void testGetUserById_nonExistingUser_throwsException() {
        // Arrange
        setUpUserAuthentication();
        UUID userId = UUID.randomUUID();
        when(userService.findById(userId)).thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "User not found"));

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> userController.getUserById(userId));
        verify(userService, times(1)).findById(userId);
    }

    @Test
    void testCreateUser_validRequest_returnsCreatedUser() {
        // Arrange
        setUpUserAuthentication();
        UUID userId = UUID.randomUUID();
        CreateUserFacadeRequest request = new CreateUserFacadeRequest(
                "newuser",
                "password12345",
                "new@example.com",
                Collections.singletonList("ROLE_USER")
        );
        UserFacadeResponse expectedResponse = new UserFacadeResponse(
                userId,
                "newuser",
                "new@example.com",
                Collections.singletonList("ROLE_USER")
        );
        when(userService.create(any(CreateUserFacadeRequest.class))).thenReturn(expectedResponse);

        // Act
        ResponseEntity<UserFacadeResponse> response = userController.create(request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
        verify(userService, times(1)).create(request);
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
        UserFacadeResponse expectedResponse = new UserFacadeResponse(
                userId,
                "newuser",
                "new@example.com",
                Collections.singletonList("ROLE_USER")
        );
        when(userService.create(any(CreateUserFacadeRequest.class))).thenReturn(expectedResponse);

        // Act
        ResponseEntity<UserFacadeResponse> response = userController.create(request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
        verify(userService, times(1)).create(request);
    }

    @Test
    void testCreateUser_duplicateUsername_throwsException() {
        // Arrange
        setUpUserAuthentication();
        CreateUserFacadeRequest request = new CreateUserFacadeRequest(
                "existinguser",
                "password12345",
                "existing@example.com",
                Collections.singletonList("ROLE_USER")
        );
        when(userService.create(any(CreateUserFacadeRequest.class)))
                .thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.CONFLICT, "Username already exists"));

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> userController.create(request));
        verify(userService, times(1)).create(request);
    }

    @Test
    void testUpdateUser_existingUser_returnsUpdatedUser() {
        // Arrange
        setUpUserAuthentication();
        UUID userId = UUID.randomUUID();
        UpdateUserFacadeRequest request = new UpdateUserFacadeRequest(
                "updateduser",
                "newpassword12345",
                "updated@example.com",
                Collections.singletonList("ROLE_USER")
        );
        UserFacadeResponse expectedResponse = new UserFacadeResponse(
                userId,
                "updateduser",
                "updated@example.com",
                Collections.singletonList("ROLE_USER")
        );
        when(userService.update(eq(userId), any(UpdateUserFacadeRequest.class))).thenReturn(expectedResponse);

        // Act
        ResponseEntity<UserFacadeResponse> response = userController.update(userId, request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
        verify(userService, times(1)).update(userId, request);
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
        UserFacadeResponse expectedResponse = new UserFacadeResponse(
                userId,
                "updateduser",
                "updated@example.com",
                Collections.singletonList("ROLE_USER")
        );
        when(userService.update(eq(userId), any(UpdateUserFacadeRequest.class))).thenReturn(expectedResponse);

        // Act
        ResponseEntity<UserFacadeResponse> response = userController.update(userId, request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
        verify(userService, times(1)).update(userId, request);
    }

    @Test
    void testUpdateUser_nonExistingUser_throwsException() {
        // Arrange
        setUpUserAuthentication();
        UUID userId = UUID.randomUUID();
        UpdateUserFacadeRequest request = new UpdateUserFacadeRequest(
                "updateduser",
                "newpassword12345",
                "updated@example.com",
                Collections.singletonList("ROLE_USER")
        );
        when(userService.update(eq(userId), any(UpdateUserFacadeRequest.class)))
                .thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "User not found"));

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> userController.update(userId, request));
        verify(userService, times(1)).update(userId, request);
    }

    @Test
    void testDeleteUser_existingUser_returnsAccepted() {
        // Arrange
        setUpUserAuthentication();
        UUID userId = UUID.randomUUID();
        doNothing().when(userService).delete(userId);

        // Act
        ResponseEntity<Void> response = userController.delete(userId);

        // Assert
        assertEquals(202, response.getStatusCodeValue());
        verify(userService, times(1)).delete(userId);
    }

    @Test
    void testDeleteUser_withAdminRole_shouldSucceed() {
        // Arrange
        setUpAdminAuthentication();
        UUID userId = UUID.randomUUID();
        doNothing().when(userService).delete(userId);

        // Act
        ResponseEntity<Void> response = userController.delete(userId);

        // Assert
        assertEquals(202, response.getStatusCodeValue());
        verify(userService, times(1)).delete(userId);
    }

    @Test
    void testDeleteUser_nonExistingUser_throwsException() {
        // Arrange
        setUpUserAuthentication();
        UUID userId = UUID.randomUUID();
        doThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "User not found"))
                .when(userService).delete(userId);

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> userController.delete(userId));
        verify(userService, times(1)).delete(userId);
    }

    @Test
    void testCreateUser_nullRequest_throwsException() {
        // Arrange
        setUpUserAuthentication();

        // Act & Assert
        assertThrows(javax.validation.ConstraintViolationException.class, () -> userController.create(null));
        verify(userService, never()).create(any());
    }

    @Test
    void testUpdateUser_nullRequest_throwsException() {
        // Arrange
        setUpUserAuthentication();
        UUID userId = UUID.randomUUID();

        // Act & Assert
        assertThrows(javax.validation.ConstraintViolationException.class, () -> userController.update(userId, null));
        verify(userService, never()).update(any(), any());
    }

    @Test
    void testGetUserById_nullId_throwsException() {
        // Arrange
        setUpUserAuthentication();

        // Act & Assert
        assertThrows(javax.validation.ConstraintViolationException.class, () -> userController.getUserById(null));
        verify(userService, never()).findById(any());
    }

    @Test
    void testDeleteUser_nullId_throwsException() {
        // Arrange
        setUpUserAuthentication();

        // Act & Assert
        assertThrows(javax.validation.ConstraintViolationException.class, () -> userController.delete(null));
        verify(userService, never()).delete(any());
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
