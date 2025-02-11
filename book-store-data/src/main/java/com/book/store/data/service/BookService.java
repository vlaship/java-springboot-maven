package com.book.store.data.service;

import com.book.store.data.dto.CreateBookRequest;
import com.book.store.data.dto.BookResponse;
import com.book.store.data.dto.UpdateBookRequest;

import java.util.List;
import java.util.UUID;

public interface BookService {

    BookResponse create(CreateBookRequest dto);

    BookResponse update(UUID id, UpdateBookRequest dto);

    void delete(UUID id);

    BookResponse findById(UUID id);

    List<BookResponse> findBooksByAuthorId(UUID authorId);
}
