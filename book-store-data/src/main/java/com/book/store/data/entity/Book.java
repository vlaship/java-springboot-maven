package com.book.store.data.entity;

import javax.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "books")
public class Book {

    @Id
    private UUID id;

    private String title;
    private String isbn;

    @Enumerated(EnumType.STRING)
    private BookType type;

    @ManyToOne
    private Author author;

    @ManyToMany
    @JoinTable(
            name = "books_stores",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "store_id")
    )
    private List<Store> stores;
}
