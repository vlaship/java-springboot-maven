package com.book.store.data.mapper;

import com.book.store.data.dto.BookResponse;
import com.book.store.data.dto.CreateBookRequest;
import com.book.store.data.dto.UpdateBookRequest;
import com.book.store.data.entity.Book;

import java.util.List;
import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface BookMapper {

    @Mapping(target = "storeIds", source = "storeIds")
    BookResponse map(Book entity, List<UUID> storeIds);

    @Mapping(target = "storeIds", ignore = true)
    BookResponse map(Book entity);

    List<BookResponse> map(List<Book> entities);

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    Book map(CreateBookRequest dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authorId", source = "authorId")
    void merge(UpdateBookRequest dto, @MappingTarget Book entity);
}
