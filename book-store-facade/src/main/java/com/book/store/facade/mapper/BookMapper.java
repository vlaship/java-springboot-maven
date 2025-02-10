package com.book.store.facade.mapper;

import com.book.store.facade.model.*;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface BookMapper {

    BookFacadeResponse map(BookDataResponse data);

    List<BookFacadeResponse> map(List<BookDataResponse> data);

    CreateBookDataRequest map(CreateBookFacadeRequest facade);

    UpdateBookDataRequest map(UpdateBookFacadeRequest facade);
}
