package com.book.store.data.service;

import com.book.store.data.dto.CreateBookRequest;
import com.book.store.data.dto.BookResponse;
import com.book.store.data.dto.UpdateBookRequest;
import com.book.store.data.entity.Book;
import com.book.store.data.exception.NotFoundException;
import com.book.store.data.mapper.BookMapper;
import com.book.store.data.repository.BookRepository;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BookService {

    private final BookRepository repository;
    private final BookMapper mapper;

    public BookResponse create(CreateBookRequest dto) {
        log.debug("create({})", dto);
        Book book = mapper.map(dto);
        Book saved = repository.save(book);
        return mapper.map(saved);
    }

    public BookResponse update(UUID id, UpdateBookRequest dto) {
        log.debug("update({},{})", id, dto);
        Book book = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("book", id));
        mapper.merge(dto, book);
        Book saved = repository.save(book);
        return mapper.map(saved);
    }

    public void delete(UUID id) {
        log.debug("delete({})", id);
        repository.deleteById(id);
    }

    public BookResponse findById(UUID id) {
        log.debug("findById({})", id);
        Book book = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("book", id));
        return mapper.map(book);
    }

    public List<BookResponse> findBooksByAuthorId(UUID authorId) {
        log.debug("findBooksByAuthorId({})", authorId);
        List<Book> books = repository.findAllByAuthorId(authorId);
        return mapper.map(books);
    }
}
