package io.github.vlaship.book.catalog.entity;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@Table("authors")
public record Author(
    @Id Long id,
    String name
) {

}
