package com.book.store.facade.mapper;

import com.book.store.facade.model.*;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class BookMapper {

    public BookFacadeResponse map(BookDataResponse data) {
        if (data == null) {
            return null;
        }

        List<UUID> storeIds = null;

        List<UUID> list = data.getStoreIds();
        if (list != null) {
            storeIds = new ArrayList<>(list);
        }
        UUID id = data.getId();
        String title = data.getTitle();
        String isbn = data.getIsbn();
        BookType type = data.getType();
        UUID authorId = data.getAuthorId();

        return new BookFacadeResponse(id, title, isbn, type, authorId, storeIds);
    }

    public List<BookFacadeResponse> map(List<BookDataResponse> data) {
        if (data == null) {
            return null;
        }

        List<BookFacadeResponse> list = new ArrayList<>(data.size());
        for (BookDataResponse bookDataResponse : data) {
            list.add(map(bookDataResponse));
        }

        return list;
    }

    public CreateBookDataRequest map(CreateBookFacadeRequest facade) {
        if (facade == null) {
            return null;
        }

        String title = facade.getTitle();
        String isbn = facade.getIsbn();
        UUID authorId = facade.getAuthorId();
        BookType type = facade.getType();

        return new CreateBookDataRequest(title, isbn, authorId, type);
    }

    public UpdateBookDataRequest map(UpdateBookFacadeRequest facade) {
        if (facade == null) {
            return null;
        }

        String title = facade.getTitle();
        String isbn = facade.getIsbn();
        BookType type = facade.getType();
        UUID authorId = facade.getAuthorId();

        return new UpdateBookDataRequest(title, isbn, type, authorId);
    }
}
