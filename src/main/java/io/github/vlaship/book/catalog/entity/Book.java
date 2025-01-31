package io.github.vlaship.book.catalog.entity;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@Table("books")
public record Book(
    @Id Long id,
    String title,
    String isbn,
    Long authorId
) {

}
