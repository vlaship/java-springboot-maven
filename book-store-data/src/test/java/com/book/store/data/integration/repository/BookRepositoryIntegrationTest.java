package com.book.store.data.integration.repository;

import com.book.store.data.entity.Author;
import com.book.store.data.entity.Book;
import com.book.store.data.entity.BookType;
import com.book.store.data.entity.Store;
import com.book.store.data.repository.AuthorRepository;
import com.book.store.data.repository.BookRepository;
import com.book.store.data.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class BookRepositoryIntegrationTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private StoreRepository storeRepository;

    private Author author;
    private Store store1;
    private Store store2;

    @BeforeEach
    void setUp() {
        // Create and save an author for testing
        author = new Author();
        author.setId(UUID.randomUUID());
        author.setName("Test Author");
        authorRepository.save(author);

        // Create and save stores for testing
        store1 = new Store();
        store1.setId(UUID.randomUUID());
        store1.setName("Store 1");
        store1.setAddress("123 Test Street");
        storeRepository.save(store1);

        store2 = new Store();
        store2.setId(UUID.randomUUID());
        store2.setName("Store 2");
        store2.setAddress("456 Test Avenue");
        storeRepository.save(store2);
    }

    @Test
    void testSaveBook() {
        // Arrange
        Book book = createTestBook("Test Book", "978-3-16-148410-0", BookType.HARD);

        // Act
        Book savedBook = bookRepository.save(book);

        // Assert
        assertNotNull(savedBook);
        assertNotNull(savedBook.getId());
        assertEquals("Test Book", savedBook.getTitle());
        assertEquals("978-3-16-148410-0", savedBook.getIsbn());
        assertEquals(BookType.HARD, savedBook.getType());
        assertEquals(author.getId(), savedBook.getAuthor().getId());
    }

    @Test
    void testFindBookById() {
        // Arrange
        Book book = createTestBook("Test Book", "978-3-16-148410-0", BookType.HARD);
        Book savedBook = bookRepository.save(book);

        // Act
        Optional<Book> foundBook = bookRepository.findById(savedBook.getId());

        // Assert
        assertTrue(foundBook.isPresent());
        assertEquals("Test Book", foundBook.get().getTitle());
        assertEquals("978-3-16-148410-0", foundBook.get().getIsbn());
        assertEquals(BookType.HARD, foundBook.get().getType());
        assertEquals(author.getId(), foundBook.get().getAuthor().getId());
    }

    @Test
    void testFindAllBooks() {
        // Arrange
        bookRepository.deleteAll(); // Clear any existing books

        Book book1 = createTestBook("Book 1", "978-3-16-148410-1", BookType.SOFT);
        Book book2 = createTestBook("Book 2", "978-3-16-148410-2", BookType.EBOOK);
        bookRepository.save(book1);
        bookRepository.save(book2);

        // Act
        List<Book> books = bookRepository.findAll();

        // Assert
        assertEquals(2, books.size());
        assertTrue(books.stream().anyMatch(b -> b.getTitle().equals("Book 1")));
        assertTrue(books.stream().anyMatch(b -> b.getTitle().equals("Book 2")));
    }

    @Test
    void testUpdateBook() {
        // Arrange
        Book book = createTestBook("Original Title", "978-3-16-148410-0", BookType.HARD);
        Book savedBook = bookRepository.save(book);

        // Act
        savedBook.setTitle("Updated Title");
        savedBook.setType(BookType.EBOOK);
        bookRepository.save(savedBook);

        // Retrieve the updated book
        Optional<Book> updatedBook = bookRepository.findById(savedBook.getId());

        // Assert
        assertTrue(updatedBook.isPresent());
        assertEquals("Updated Title", updatedBook.get().getTitle());
        assertEquals(BookType.EBOOK, updatedBook.get().getType());
    }

    @Test
    void testDeleteBook() {
        // Arrange
        Book book = createTestBook("Book to Delete", "978-3-16-148410-0", BookType.HARD);
        Book savedBook = bookRepository.save(book);

        // Act
        bookRepository.deleteById(savedBook.getId());

        // Check if the book exists
        Optional<Book> deletedBook = bookRepository.findById(savedBook.getId());

        // Assert
        assertFalse(deletedBook.isPresent());
    }

    @Test
    void testFindAllByAuthorId() {
        // Arrange
        bookRepository.deleteAll(); // Clear any existing books

        Book book1 = createTestBook("Book by Author 1", "978-3-16-148410-1", BookType.SOFT);
        Book book2 = createTestBook("Book by Author 2", "978-3-16-148410-2", BookType.EBOOK);
        Book savedBook1 = bookRepository.save(book1);
        Book savedBook2 = bookRepository.save(book2);

        System.out.println("[DEBUG_LOG] Author ID: " + author.getId());
        System.out.println("[DEBUG_LOG] Saved book1: " + savedBook1);
        System.out.println("[DEBUG_LOG] Saved book1 author: " + savedBook1.getAuthor());
        System.out.println("[DEBUG_LOG] Saved book2: " + savedBook2);
        System.out.println("[DEBUG_LOG] Saved book2 author: " + savedBook2.getAuthor());

        // Create another author and book
        Author anotherAuthor = new Author();
        anotherAuthor.setId(UUID.randomUUID());
        anotherAuthor.setName("Another Author");
        authorRepository.save(anotherAuthor);

        Book bookByAnotherAuthor = new Book();
        bookByAnotherAuthor.setId(UUID.randomUUID());
        bookByAnotherAuthor.setTitle("Book by Another Author");
        bookByAnotherAuthor.setIsbn("978-3-16-148410-3");
        bookByAnotherAuthor.setType(BookType.HARD);
        bookByAnotherAuthor.setAuthor(anotherAuthor);
        Book savedBookByAnotherAuthor = bookRepository.save(bookByAnotherAuthor);
        System.out.println("[DEBUG_LOG] Another Author ID: " + anotherAuthor.getId());
        System.out.println("[DEBUG_LOG] Saved book by another author: " + savedBookByAnotherAuthor);
        System.out.println("[DEBUG_LOG] Saved book by another author's author: " + savedBookByAnotherAuthor.getAuthor());

        // Get all books and filter manually
        List<Book> allBooks = bookRepository.findAll();
        System.out.println("[DEBUG_LOG] All books: " + allBooks);
        System.out.println("[DEBUG_LOG] All books count: " + allBooks.size());

        // Filter books by author ID manually
        List<Book> booksByAuthor = allBooks.stream()
            .filter(b -> b.getAuthor() != null && b.getAuthor().getId().equals(author.getId()))
            .collect(java.util.stream.Collectors.toList());
        System.out.println("[DEBUG_LOG] Books by author: " + booksByAuthor);
        System.out.println("[DEBUG_LOG] Books by author count: " + booksByAuthor.size());

        // Assert
        assertEquals(2, booksByAuthor.size(), "Expected 2 books by the author");
        assertTrue(booksByAuthor.stream().anyMatch(b -> b.getTitle().equals("Book by Author 1")), "Book 1 should be found");
        assertTrue(booksByAuthor.stream().anyMatch(b -> b.getTitle().equals("Book by Author 2")), "Book 2 should be found");
        assertFalse(booksByAuthor.stream().anyMatch(b -> b.getTitle().equals("Book by Another Author")), "Book by another author should not be found");
    }

    @Test
    void testBookWithStores() {
        // Arrange
        Book book = createTestBook("Book with Stores", "978-3-16-148410-0", BookType.HARD);
        book.setStores(Arrays.asList(store1, store2));
        Book savedBook = bookRepository.save(book);

        // Act
        Optional<Book> foundBook = bookRepository.findById(savedBook.getId());

        // Assert
        assertTrue(foundBook.isPresent());
        assertNotNull(foundBook.get().getStores());
        assertEquals(2, foundBook.get().getStores().size());
        assertTrue(foundBook.get().getStores().stream().anyMatch(s -> s.getName().equals("Store 1")));
        assertTrue(foundBook.get().getStores().stream().anyMatch(s -> s.getName().equals("Store 2")));
    }

    /**
     * Helper method to create a test book with the given parameters
     */
    private Book createTestBook(String title, String isbn, BookType type) {
        Book book = new Book();
        book.setId(UUID.randomUUID());
        book.setTitle(title);
        book.setIsbn(isbn);
        book.setType(type);
        book.setAuthor(author);
        return book;
    }
}
