package com.book.store.data.service.impl;

import com.book.store.data.dto.BookResponse;
import com.book.store.data.dto.CreateBookRequest;
import com.book.store.data.dto.UpdateBookRequest;
import com.book.store.data.entity.Book;
import com.book.store.data.exception.NotFoundException;
import com.book.store.data.mapper.BookMapper;
import com.book.store.data.repository.BookRepository;
import com.book.store.data.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository repository;
    private final BookMapper mapper;

    @Override
    public BookResponse create(CreateBookRequest dto) {
        log.debug("create({})", dto);
        Book book = mapper.map(dto);
        Book saved = repository.save(book);
        return mapper.map(saved);
    }

    @Override
    public BookResponse update(UUID id, UpdateBookRequest dto) {
        log.debug("update({},{})", id, dto);
        Book book = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("book", id));
        mapper.merge(dto, book);
        Book saved = repository.save(book);
        return mapper.map(saved);
    }

    @Override
    public void delete(UUID id) {
        log.debug("delete({})", id);
        repository.deleteById(id);
    }

    @Override
    public BookResponse findById(UUID id) {
        log.debug("findById({})", id);
        Book book = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("book", id));
        return mapper.map(book);
    }

    @Override
    public List<BookResponse> findBooksByAuthorId(UUID authorId) {
        log.debug("findBooksByAuthorId({})", authorId);
        List<Book> books = repository.findAllByAuthorId(authorId);
        return mapper.map(books);
    }
}
