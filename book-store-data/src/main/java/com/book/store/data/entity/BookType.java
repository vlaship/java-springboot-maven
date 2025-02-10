package com.book.store.data.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BookType {
    HARD("Hardcover"), SOFT("Softcover"), EBOOK("E-Book");

    @JsonValue
    private final String type;
}
