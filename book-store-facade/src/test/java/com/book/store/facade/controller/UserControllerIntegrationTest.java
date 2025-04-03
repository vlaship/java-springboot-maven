package com.book.store.facade.controller;

import com.book.store.facade.model.UserFacadeResponse;
import com.book.store.facade.model.CreateUserFacadeRequest;
import com.book.store.facade.model.UpdateUserFacadeRequest;
import com.book.store.facade.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserControllerIntegrationTest {

    @MockBean
    private UserService userService;

    @Autowired
    private UserController userController;

    @BeforeEach
    void setUp() {
        // Set up authentication for all tests
        setUpAuthentication();
        // Reset mocks
        reset(userService);
    }

    private void setUpAuthentication() {
        UserDetails userDetails = new User(
                "testuser",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void testGetUserById_existingUser_returnsUser() {
        // Arrange
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
        UUID userId = UUID.randomUUID();
        when(userService.findById(userId)).thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "User not found"));

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> userController.getUserById(userId));
        verify(userService, times(1)).findById(userId);
    }

    @Test
    void testCreateUser_validRequest_returnsCreatedUser() {
        // Arrange
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
        UUID userId = UUID.randomUUID();
        doThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "User not found"))
                .when(userService).delete(userId);

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> userController.delete(userId));
        verify(userService, times(1)).delete(userId);
    }

    @Test
    void testCreateUser_nullRequest_throwsException() {
        // Act & Assert
        assertThrows(javax.validation.ConstraintViolationException.class, () -> userController.create(null));
        verify(userService, never()).create(any());
    }

    @Test
    void testUpdateUser_nullRequest_throwsException() {
        // Arrange
        UUID userId = UUID.randomUUID();

        // Act & Assert
        assertThrows(javax.validation.ConstraintViolationException.class, () -> userController.update(userId, null));
        verify(userService, never()).update(any(), any());
    }

    @Test
    void testGetUserById_nullId_throwsException() {
        // Act & Assert
        assertThrows(javax.validation.ConstraintViolationException.class, () -> userController.getUserById(null));
        verify(userService, never()).findById(any());
    }

    @Test
    void testDeleteUser_nullId_throwsException() {
        // Act & Assert
        assertThrows(javax.validation.ConstraintViolationException.class, () -> userController.delete(null));
        verify(userService, never()).delete(any());
    }
}
