package com.book.store.data.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "stores")
public class Store {

    @Id
    private UUID id;
    private String name;
    private String address;

    @ManyToMany(mappedBy = "stores")
    private List<Book> books;
}
