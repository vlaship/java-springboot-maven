package com.book.store.service;

import com.book.store.dto.CreateBookRequest;
import com.book.store.dto.BookResponse;
import com.book.store.dto.UpdateBookRequest;
import com.book.store.entity.BookStore;
import com.book.store.exception.NotFoundException;
import com.book.store.mapper.BookMapper;
import com.book.store.repository.BookRepository;
import com.book.store.repository.BookStoreRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

  private final BookRepository repository;
  private final BookMapper mapper;
  private final BookStoreRepository bookStoreRepository;

  @NonNull
  @Transactional
  public BookResponse create(@NonNull CreateBookRequest dto) {
    log.debug("create({})", dto);
    var author = mapper.map(dto);
    var saved = repository.save(author);
    var storeIds = getStoreIds(saved.getId());
    return mapper.map(saved, storeIds);
  }

  @NonNull
  @Transactional
  public BookResponse update(@NonNull Long id, @NonNull UpdateBookRequest dto) {
    log.debug("update({},{})", id, dto);
    var author = repository.findById(id)
        .orElseThrow(() -> new NotFoundException("book", id));
    mapper.merge(dto, author);
    var saved = repository.save(author);
    var storeIds = getStoreIds(saved.getId());
    return mapper.map(saved, storeIds);
  }

  @Transactional
  public void delete(@NonNull Long id) {
    log.debug("delete({})", id);
    repository.deleteById(id);
  }

  @NonNull
  @Transactional(readOnly = true)
  public BookResponse findById(@NonNull Long id) {
    log.debug("findById({})", id);
    var author = repository.findById(id)
        .orElseThrow(() -> new NotFoundException("book", id));
    var storeIds = getStoreIds(id);
    return mapper.map(author, storeIds);
  }

  @NonNull
  @Transactional(readOnly = true)
  public List<BookResponse> findBooksByAuthorId(@NonNull Long authorId) {
    log.debug("findBooksByAuthorId({})", authorId);
    var books = repository.findByAuthorId(authorId);
    return mapper.map(books);
  }

  @NonNull
  @Transactional(readOnly = true)
  public List<BookResponse> findBooksByStoreId(@NonNull Long storeId) {
    log.debug("findBooksByStoreId({})", storeId);
    var books = repository.findByAuthorId(storeId);
    return mapper.map(books);
  }

  @NonNull
  private List<Long> getStoreIds(@NonNull Long bookId) {
    log.debug("getStoreIds({})", bookId);
    return bookStoreRepository.findByBookId(bookId)
        .stream()
        .map(BookStore::getStoreId)
        .toList();
  }
}
