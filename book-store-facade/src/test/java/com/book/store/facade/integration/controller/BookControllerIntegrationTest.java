package com.book.store.facade.integration.controller;

import com.book.store.facade.model.BookFacadeResponse;
import com.book.store.facade.model.BookType;
import com.book.store.facade.model.CreateBookFacadeRequest;
import com.book.store.facade.model.UpdateBookFacadeRequest;
import com.book.store.facade.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
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
class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @Test
    void testGetBookById() throws Exception {
        // Arrange
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

        // Act & Assert
        mockMvc.perform(get("/v1/book/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(bookId.toString())))
                .andExpect(jsonPath("$.title", is("Test Book")))
                .andExpect(jsonPath("$.isbn", is("978-3-16-148410-0")))
                .andExpect(jsonPath("$.type", is("Hardcover")))
                .andExpect(jsonPath("$.authorId", is(authorId.toString())))
                .andExpect(jsonPath("$.storeIds", hasSize(1)))
                .andExpect(jsonPath("$.storeIds[0]", is(storeId.toString())));
    }

    @Test
    void testGetBooksByAuthorId() throws Exception {
        // Arrange
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

        // Act & Assert
        mockMvc.perform(get("/v1/book/author/{authorId}", authorId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(bookId1.toString())))
                .andExpect(jsonPath("$[0].title", is("Book 1")))
                .andExpect(jsonPath("$[1].id", is(bookId2.toString())))
                .andExpect(jsonPath("$[1].title", is("Book 2")));
    }

    @Test
    void testCreateBook() throws Exception {
        // Arrange
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

        // Act & Assert
        mockMvc.perform(post("/v1/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(bookId.toString())))
                .andExpect(jsonPath("$.title", is("New Book")))
                .andExpect(jsonPath("$.isbn", is("978-3-16-148410-0")))
                .andExpect(jsonPath("$.type", is("Hardcover")))
                .andExpect(jsonPath("$.authorId", is(authorId.toString())))
                .andExpect(jsonPath("$.storeIds", hasSize(1)))
                .andExpect(jsonPath("$.storeIds[0]", is(storeId.toString())));
    }

    @Test
    void testUpdateBook() throws Exception {
        // Arrange
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

        // Act & Assert
        mockMvc.perform(patch("/v1/book/{id}", bookId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(bookId.toString())))
                .andExpect(jsonPath("$.title", is("Updated Book")))
                .andExpect(jsonPath("$.isbn", is("978-3-16-148410-1")))
                .andExpect(jsonPath("$.type", is("Softcover")))
                .andExpect(jsonPath("$.authorId", is(authorId.toString())))
                .andExpect(jsonPath("$.storeIds", hasSize(1)))
                .andExpect(jsonPath("$.storeIds[0]", is(storeId.toString())));
    }

    @Test
    void testDeleteBook() throws Exception {
        // Arrange
        UUID bookId = UUID.randomUUID();
        doNothing().when(bookService).delete(bookId);

        // Act & Assert
        mockMvc.perform(delete("/v1/book/{id}", bookId))
                .andExpect(status().isAccepted());
    }
}