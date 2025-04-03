package com.book.store.facade.controller;

import com.book.store.facade.model.BookFacadeResponse;
import com.book.store.facade.model.BookType;
import com.book.store.facade.model.CreateBookFacadeRequest;
import com.book.store.facade.model.UpdateBookFacadeRequest;
import com.book.store.facade.service.BookService;
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
public class BookControllerIntegrationTest {

    @MockBean
    private BookService bookService;

    @Autowired
    private BookController bookController;

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
    void testCreateBook_withManagerRole_shouldSucceed() {
        // Arrange
        setUpManagerAuthentication();
        UUID bookId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        CreateBookFacadeRequest request = new CreateBookFacadeRequest(
                "New Book",
                "978-3-16-148410-0",
                authorId,
                BookType.HARD
        );
        BookFacadeResponse response = new BookFacadeResponse(
                bookId,
                "New Book",
                "978-3-16-148410-0",
                BookType.HARD,
                authorId,
                Collections.singletonList(storeId)
        );
        when(bookService.create(any(CreateBookFacadeRequest.class))).thenReturn(response);

        // Act
        ResponseEntity<BookFacadeResponse> result = bookController.create(request);

        // Assert
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(response, result.getBody());
    }

    @Test
    void testCreateBook_withUserRole_shouldFail() {
        // Arrange
        setUpUserAuthentication();
        UUID authorId = UUID.randomUUID();
        CreateBookFacadeRequest request = new CreateBookFacadeRequest(
                "New Book",
                "978-3-16-148410-0",
                authorId,
                BookType.HARD
        );

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> bookController.create(request));
    }

    @Test
    void testUpdateBook_withManagerRole_shouldSucceed() {
        // Arrange
        setUpManagerAuthentication();
        UUID bookId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        UpdateBookFacadeRequest request = new UpdateBookFacadeRequest(
                "Updated Book",
                "978-3-16-148410-1",
                BookType.SOFT,
                authorId
        );
        BookFacadeResponse response = new BookFacadeResponse(
                bookId,
                "Updated Book",
                "978-3-16-148410-1",
                BookType.SOFT,
                authorId,
                Collections.singletonList(storeId)
        );
        when(bookService.update(eq(bookId), any(UpdateBookFacadeRequest.class))).thenReturn(response);

        // Act
        ResponseEntity<BookFacadeResponse> result = bookController.update(bookId, request);

        // Assert
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(response, result.getBody());
    }

    @Test
    void testUpdateBook_withUserRole_shouldFail() {
        // Arrange
        setUpUserAuthentication();
        UUID bookId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        UpdateBookFacadeRequest request = new UpdateBookFacadeRequest(
                "Updated Book",
                "978-3-16-148410-1",
                BookType.SOFT,
                authorId
        );

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> bookController.update(bookId, request));
    }

    @Test
    void testDeleteBook_withAdminRole_shouldSucceed() {
        // Arrange
        setUpAdminAuthentication();
        UUID bookId = UUID.randomUUID();
        doNothing().when(bookService).delete(bookId);

        // Act
        ResponseEntity<Void> result = bookController.delete(bookId);

        // Assert
        assertEquals(202, result.getStatusCodeValue());
    }

    @Test
    void testDeleteBook_withManagerRole_shouldSucceed() {
        // Arrange
        setUpManagerAuthentication();
        UUID bookId = UUID.randomUUID();
        doNothing().when(bookService).delete(bookId);

        // Act
        ResponseEntity<Void> result = bookController.delete(bookId);

        // Assert
        assertEquals(202, result.getStatusCodeValue());
    }

    @Test
    void testDeleteBook_withUserRole_shouldFail() {
        // Arrange
        setUpUserAuthentication();
        UUID bookId = UUID.randomUUID();

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> bookController.delete(bookId));
    }

    @Test
    void testFindBookById_withUserRole_shouldSucceed() {
        // Arrange
        setUpUserAuthentication();
        UUID bookId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        BookFacadeResponse response = new BookFacadeResponse(
                bookId,
                "Test Book",
                "978-3-16-148410-0",
                BookType.HARD,
                authorId,
                Collections.singletonList(storeId)
        );
        when(bookService.findById(bookId)).thenReturn(response);

        // Act
        ResponseEntity<BookFacadeResponse> result = bookController.findBookById(bookId);

        // Assert
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(response, result.getBody());
    }

    @Test
    void testFindBooksByAuthorId_withUserRole_shouldSucceed() {
        // Arrange
        setUpUserAuthentication();
        UUID bookId1 = UUID.randomUUID();
        UUID bookId2 = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();
        List<BookFacadeResponse> books = Arrays.asList(
                new BookFacadeResponse(
                        bookId1,
                        "Book 1",
                        "978-3-16-148410-1",
                        BookType.HARD,
                        authorId,
                        Collections.singletonList(storeId)
                ),
                new BookFacadeResponse(
                        bookId2,
                        "Book 2",
                        "978-3-16-148410-2",
                        BookType.SOFT,
                        authorId,
                        Collections.singletonList(storeId)
                )
        );
        when(bookService.findBooksByAuthorId(authorId)).thenReturn(books);

        // Act
        ResponseEntity<List<BookFacadeResponse>> result = bookController.findBooksByAuthorId(authorId);

        // Assert
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(books, result.getBody());
    }
}
