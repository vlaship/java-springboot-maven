package com.book.store.data.service;

import com.book.store.data.dto.CreateBookRequest;
import com.book.store.data.dto.BookResponse;
import com.book.store.data.dto.UpdateBookRequest;
import com.book.store.data.entity.BookStore;
import com.book.store.data.exception.NotFoundException;
import com.book.store.data.mapper.BookMapper;
import com.book.store.data.repository.BookRepository;
import com.book.store.data.repository.BookStoreRepository;

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
    private final BookStoreRepository bookStoreRepository;

    public BookResponse create(CreateBookRequest dto) {
        log.debug("create({})", dto);
        var author = mapper.map(dto);
        var saved = repository.save(author);
        var storeIds = getStoreIds(saved.getId());
        return mapper.map(saved, storeIds);
    }

    public BookResponse update(UUID id, UpdateBookRequest dto) {
        log.debug("update({},{})", id, dto);
        var author = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("book", id));
        mapper.merge(dto, author);
        var saved = repository.save(author);
        var storeIds = getStoreIds(saved.getId());
        return mapper.map(saved, storeIds);
    }

    public void delete(UUID id) {
        log.debug("delete({})", id);
        repository.deleteById(id);
    }

    public BookResponse findById(UUID id) {
        log.debug("findById({})", id);
        var author = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("book", id));
        var storeIds = getStoreIds(id);
        return mapper.map(author, storeIds);
    }

    public List<BookResponse> findBooksByAuthorId(UUID authorId) {
        log.debug("findBooksByAuthorId({})", authorId);
        var books = repository.findByAuthorId(authorId);
        return mapper.map(books);
    }

    public List<BookResponse> findBooksByStoreId(UUID storeId) {
        log.debug("findBooksByStoreId({})", storeId);
        var books = repository.findByAuthorId(storeId);
        return mapper.map(books);
    }

    private List<UUID> getStoreIds(UUID bookId) {
        log.debug("getStoreIds({})", bookId);
        return bookStoreRepository.findByBookId(bookId)
                .stream()
                .map(BookStore::getStoreId)
                .toList();
    }
}
