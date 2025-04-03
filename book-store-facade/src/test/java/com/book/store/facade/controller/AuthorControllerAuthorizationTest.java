package com.book.store.facade.controller;

import com.book.store.facade.model.AuthorFacadeResponse;
import com.book.store.facade.model.CreateAuthorFacadeRequest;
import com.book.store.facade.model.UpdateAuthorFacadeRequest;
import com.book.store.facade.service.AuthorService;
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
public class AuthorControllerAuthorizationTest {

    @MockBean
    private AuthorService authorService;

    @Autowired
    private AuthorController authorController;

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
    void testCreateAuthor_withManagerRole_shouldSucceed() {
        // Arrange
        setUpManagerAuthentication();
        UUID authorId = UUID.randomUUID();
        CreateAuthorFacadeRequest request = new CreateAuthorFacadeRequest("New Author");
        AuthorFacadeResponse response = new AuthorFacadeResponse(authorId, "New Author");
        when(authorService.create(any(CreateAuthorFacadeRequest.class))).thenReturn(response);

        // Act
        ResponseEntity<AuthorFacadeResponse> result = authorController.create(request);

        // Assert
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(response, result.getBody());
    }

    @Test
    void testCreateAuthor_withUserRole_shouldFail() {
        // Arrange
        setUpUserAuthentication();
        CreateAuthorFacadeRequest request = new CreateAuthorFacadeRequest("New Author");

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> authorController.create(request));
    }

    @Test
    void testUpdateAuthor_withManagerRole_shouldSucceed() {
        // Arrange
        setUpManagerAuthentication();
        UUID authorId = UUID.randomUUID();
        UpdateAuthorFacadeRequest request = new UpdateAuthorFacadeRequest("Updated Author");
        AuthorFacadeResponse response = new AuthorFacadeResponse(authorId, "Updated Author");
        when(authorService.update(eq(authorId), any(UpdateAuthorFacadeRequest.class))).thenReturn(response);

        // Act
        ResponseEntity<AuthorFacadeResponse> result = authorController.update(authorId, request);

        // Assert
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(response, result.getBody());
    }

    @Test
    void testUpdateAuthor_withUserRole_shouldFail() {
        // Arrange
        setUpUserAuthentication();
        UUID authorId = UUID.randomUUID();
        UpdateAuthorFacadeRequest request = new UpdateAuthorFacadeRequest("Updated Author");

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> authorController.update(authorId, request));
    }

    @Test
    void testDeleteAuthor_withAdminRole_shouldSucceed() {
        // Arrange
        setUpAdminAuthentication();
        UUID authorId = UUID.randomUUID();
        doNothing().when(authorService).delete(authorId);

        // Act
        ResponseEntity<Void> result = authorController.delete(authorId);

        // Assert
        assertEquals(202, result.getStatusCodeValue());
    }

    @Test
    void testDeleteAuthor_withManagerRole_shouldSucceed() {
        // Arrange
        setUpManagerAuthentication();
        UUID authorId = UUID.randomUUID();
        doNothing().when(authorService).delete(authorId);

        // Act
        ResponseEntity<Void> result = authorController.delete(authorId);

        // Assert
        assertEquals(202, result.getStatusCodeValue());
    }

    @Test
    void testDeleteAuthor_withUserRole_shouldFail() {
        // Arrange
        setUpUserAuthentication();
        UUID authorId = UUID.randomUUID();

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> authorController.delete(authorId));
    }

    @Test
    void testGetAuthorById_withUserRole_shouldSucceed() {
        // Arrange
        setUpUserAuthentication();
        UUID authorId = UUID.randomUUID();
        AuthorFacadeResponse response = new AuthorFacadeResponse(authorId, "Test Author");
        when(authorService.findById(authorId)).thenReturn(response);

        // Act
        ResponseEntity<AuthorFacadeResponse> result = authorController.getAuthorById(authorId);

        // Assert
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(response, result.getBody());
    }

    @Test
    void testGetAuthors_withUserRole_shouldSucceed() {
        // Arrange
        setUpUserAuthentication();
        UUID authorId1 = UUID.randomUUID();
        UUID authorId2 = UUID.randomUUID();
        List<AuthorFacadeResponse> authors = Arrays.asList(
                new AuthorFacadeResponse(authorId1, "Author 1"),
                new AuthorFacadeResponse(authorId2, "Author 2")
        );
        when(authorService.findAll()).thenReturn(authors);

        // Act
        ResponseEntity<List<AuthorFacadeResponse>> result = authorController.getAuthors();

        // Assert
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(authors, result.getBody());
    }
}