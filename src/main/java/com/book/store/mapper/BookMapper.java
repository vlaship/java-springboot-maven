package com.book.store.mapper;

import com.book.store.dto.BookListResponse;
import com.book.store.dto.BookResponse;
import com.book.store.dto.CreateBookRequest;
import com.book.store.dto.UpdateBookRequest;
import com.book.store.entity.Book;
import java.util.List;
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
  BookResponse map(Book entity, List<Long> storeIds);

  BookListResponse map(Book entity, String author);

  @Mapping(target = "storeIds", ignore = true)
  BookResponse map(Book entity);

  List<BookResponse> map(List<Book> entities);

  @Mapping(target = "id", ignore = true)
  Book map(CreateBookRequest dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "authorId", source = "authorId")
  void merge(UpdateBookRequest dto, @MappingTarget Book entity);
}
