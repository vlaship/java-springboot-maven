package io.github.vlaship.book.catalog.entity;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@Table("stores")
public record Store(
    @Id Long id,
    String name,
    String address
) {

}
