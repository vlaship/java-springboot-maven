package io.github.vlaship.book.catalog.service;

import io.github.vlaship.book.catalog.dto.AuthorResponse;
import io.github.vlaship.book.catalog.dto.BookListResponse;
import io.github.vlaship.book.catalog.mapper.BookMapper;
import io.github.vlaship.book.catalog.repository.BookRepository;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookListService {

  private final BookRepository bookRepository;
  private final BookMapper bookMapper;
  private final AuthorService authorService;

  @Transactional(readOnly = true)
  public List<BookListResponse> findAll() {
    log.debug("findAll()");
    var authors = authorService.findAll()
        .stream()
        .collect(Collectors.toMap(AuthorResponse::id, AuthorResponse::name));

    return StreamSupport
        .stream(bookRepository.findAll().spliterator(), false)
        .map(book -> {
          var authorId = authors.get(book.authorId());
          return bookMapper.map(book, authorId);
        })
        .toList();
  }
}
