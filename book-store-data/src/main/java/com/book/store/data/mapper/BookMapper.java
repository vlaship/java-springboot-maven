package com.book.store.data.mapper;

import com.book.store.data.dto.BookResponse;
import com.book.store.data.dto.CreateBookRequest;
import com.book.store.data.dto.UpdateBookRequest;
import com.book.store.data.entity.Author;
import com.book.store.data.entity.Book;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.book.store.data.entity.BookType;
import com.book.store.data.entity.Store;
import com.book.store.data.exception.NotFoundException;
import com.book.store.data.repository.AuthorRepository;
import com.book.store.data.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookMapper {

    private final AuthorRepository authorRepository;
    private final StoreRepository storeRepository;

    public BookResponse map(Book entity) {
        if (entity == null) {
            return null;
        }

        List<UUID> storeIds = storesToIds(entity.getStores());
        UUID authorId = entity.getAuthor().getId();
        UUID id = entity.getId();
        String title = entity.getTitle();
        String isbn = entity.getIsbn();
        BookType type = entity.getType();

        return new BookResponse(id, title, isbn, type, authorId, storeIds);
    }

    public List<BookResponse> map(List<Book> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }

        List<BookResponse> list = new ArrayList<>(entities.size());
        for (Book book : entities) {
            list.add(map(book));
        }

        return list;
    }

    public Book map(CreateBookRequest dto) {
        if (dto == null) {
            return null;
        }

        Book book = new Book();

        book.setStores(findStoreByIds(dto.getStoreIds()));
        book.setAuthor(findAuthorById(dto.getAuthorId()));
        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setType(dto.getType());

        book.setId(java.util.UUID.randomUUID());

        return book;
    }

    public void merge(UpdateBookRequest dto, Book entity) {
        if (dto == null) {
            return;
        }

        if (entity.getStores() != null) {
            List<Store> list = findStoreByIds(dto.getStoreIds());
            if (list != null) {
                entity.getStores().clear();
                entity.getStores().addAll(list);
            }
        } else {
            List<Store> list = findStoreByIds(dto.getStoreIds());
            if (list != null) {
                entity.setStores(list);
            }
        }
        if (dto.getAuthorId() != null) {
            entity.setAuthor(findAuthorById(dto.getAuthorId()));
        }
        if (dto.getTitle() != null) {
            entity.setTitle(dto.getTitle());
        }
        if (dto.getIsbn() != null) {
            entity.setIsbn(dto.getIsbn());
        }
        if (dto.getType() != null) {
            entity.setType(dto.getType());
        }
    }

    private Author findAuthorById(UUID authorId) {
        if (authorId == null) {
            return null;
        }
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("author", authorId));
    }

    private List<Store> findStoreByIds(List<UUID> storeIds) {
        if (storeIds == null) {
            return null;
        }
        return storeRepository.findAllById(storeIds);
    }

    private List<UUID> storesToIds(List<Store> stores) {
        if (stores == null) {
            return Collections.emptyList();
        }
        return stores
                .stream()
                .map(Store::getId)
                .collect(Collectors.toList());
    }
}
