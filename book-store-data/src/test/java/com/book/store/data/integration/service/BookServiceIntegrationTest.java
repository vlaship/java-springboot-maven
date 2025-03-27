package com.book.store.data.integration.service;

import com.book.store.data.dto.CreateBookRequest;
import com.book.store.data.dto.BookResponse;
import com.book.store.data.dto.UpdateBookRequest;
import com.book.store.data.entity.BookType;
import com.book.store.data.repository.BookRepository;
import com.book.store.data.repository.AuthorRepository;
import com.book.store.data.repository.StoreRepository;
import com.book.store.data.entity.Author;
import com.book.store.data.entity.Store;
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
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class BookServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private StoreRepository storeRepository;

    private CreateBookRequest createBookRequest;
    private UpdateBookRequest updateBookRequest;
    private UUID authorId;
    private List<UUID> storeIds;

    @BeforeEach
    void setUp() {
        // Clear the database before each test
        bookRepository.deleteAll();

        // Create test author
        Author author = new Author();
        author.setId(UUID.randomUUID());
        author.setName("Test Author");
        authorRepository.save(author);
        authorId = author.getId();

        // Create test stores
        Store store1 = new Store();
        store1.setId(UUID.randomUUID());
        store1.setName("Test Store 1");
        store1.setAddress("123 Test Street");
        storeRepository.save(store1);

        Store store2 = new Store();
        store2.setId(UUID.randomUUID());
        store2.setName("Test Store 2");
        store2.setAddress("456 Test Avenue");
        storeRepository.save(store2);

        storeIds = Arrays.asList(store1.getId(), store2.getId());

        // Initialize test data
        createBookRequest = new CreateBookRequest(
            "Test Book", 
            "978-3-16-148410-0", 
            BookType.HARD, 
            authorId, 
            storeIds
        );

        updateBookRequest = new UpdateBookRequest(
            "Updated Book", 
            "978-3-16-148410-1", 
            BookType.SOFT, 
            authorId, 
            storeIds
        );
    }

    @Test
    void createBook_ShouldReturnCreatedBook() throws Exception {
        // Debug
        System.out.println("[DEBUG_LOG] CreateBookRequest: " + objectMapper.writeValueAsString(createBookRequest));

        // Act
        MvcResult result = mockMvc.perform(post("/v1/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createBookRequest)))
                .andExpect(status().isOk())
                .andReturn();

        // Debug
        String content = result.getResponse().getContentAsString();
        System.out.println("[DEBUG_LOG] Response content: " + content);

        // Assert
        BookResponse response = objectMapper.readValue(content, BookResponse.class);

        assertNotNull(response.getId());
        assertEquals("Test Book", response.getTitle());
        assertEquals("978-3-16-148410-0", response.getIsbn());
        assertEquals(BookType.HARD, response.getType());
        assertEquals(authorId, response.getAuthorId());
        assertTrue(response.getStoreIds() != null);

        // Verify the book was saved to the database
        assertTrue(bookRepository.findById(response.getId()).isPresent());
    }

    @Test
    void getBookById_WhenBookExists_ShouldReturnBook() throws Exception {
        // Arrange
        MvcResult createResult = mockMvc.perform(post("/v1/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createBookRequest)))
                .andExpect(status().isOk())
                .andReturn();

        BookResponse createdBook = objectMapper.readValue(
                createResult.getResponse().getContentAsString(), BookResponse.class);

        // Act & Assert
        mockMvc.perform(get("/v1/book/{id}", createdBook.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdBook.getId().toString()))
                .andExpect(jsonPath("$.title").value("Test Book"))
                .andExpect(jsonPath("$.isbn").value("978-3-16-148410-0"))
                .andExpect(jsonPath("$.type").value("Hardcover"));
    }

    @Test
    void getBookById_WhenBookDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/v1/book/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateBook_WhenBookExists_ShouldReturnUpdatedBook() throws Exception {
        // Arrange
        MvcResult createResult = mockMvc.perform(post("/v1/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createBookRequest)))
                .andExpect(status().isOk())
                .andReturn();

        BookResponse createdBook = objectMapper.readValue(
                createResult.getResponse().getContentAsString(), BookResponse.class);

        // Act & Assert
        MvcResult updateResult = mockMvc.perform(patch("/v1/book/{id}", createdBook.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateBookRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdBook.getId().toString()))
                .andExpect(jsonPath("$.title").value("Updated Book"))
                .andExpect(jsonPath("$.isbn").value("978-3-16-148410-1"))
                .andExpect(jsonPath("$.type").value("Softcover"))
                .andReturn();

        // Verify the book was updated in the database
        BookResponse updatedBook = objectMapper.readValue(
                updateResult.getResponse().getContentAsString(), BookResponse.class);
        assertEquals("Updated Book", updatedBook.getTitle());
        assertEquals("978-3-16-148410-1", updatedBook.getIsbn());
        assertEquals(BookType.SOFT, updatedBook.getType());

        assertTrue(bookRepository.findById(createdBook.getId()).isPresent());
    }

    @Test
    void updateBook_WhenBookDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(patch("/v1/book/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateBookRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteBook_WhenBookExists_ShouldDeleteBook() throws Exception {
        // Arrange
        MvcResult createResult = mockMvc.perform(post("/v1/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createBookRequest)))
                .andExpect(status().isOk())
                .andReturn();

        BookResponse createdBook = objectMapper.readValue(
                createResult.getResponse().getContentAsString(), BookResponse.class);

        // Act
        mockMvc.perform(delete("/v1/book/{id}", createdBook.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());

        // Assert
        assertFalse(bookRepository.findById(createdBook.getId()).isPresent());
    }

    @Test
    void findBooksByAuthorId_ShouldReturnBooksForAuthor() throws Exception {
        // Arrange
        // Create a book for the test author
        mockMvc.perform(post("/v1/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createBookRequest)))
                .andExpect(status().isOk());

        // Act & Assert
        MvcResult result = mockMvc.perform(get("/v1/book/author/{authorId}", authorId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        List<BookResponse> books = objectMapper.readValue(content, 
                objectMapper.getTypeFactory().constructCollectionType(List.class, BookResponse.class));

        assertFalse(books.isEmpty());
        assertTrue(books.stream().allMatch(book -> book.getAuthorId().equals(authorId)));
    }
}
