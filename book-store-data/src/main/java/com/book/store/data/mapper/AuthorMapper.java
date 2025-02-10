package com.book.store.data.mapper;

import com.book.store.data.dto.AuthorResponse;
import com.book.store.data.dto.CreateAuthorRequest;
import com.book.store.data.dto.UpdateAuthorRequest;
import com.book.store.data.entity.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AuthorMapper {

    AuthorResponse map(Author entity);

    Author map(AuthorResponse dto);

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    Author map(CreateAuthorRequest dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "name")
    void merge(@MappingTarget Author entity, UpdateAuthorRequest dto);
}
