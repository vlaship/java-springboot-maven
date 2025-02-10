package com.book.store.facade.mapper;

import com.book.store.facade.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AuthorMapper {

    AuthorFacadeResponse map(AuthorDataResponse data);

    List<AuthorFacadeResponse> map(List<AuthorDataResponse> data);

    CreateAuthorDataRequest map(CreateAuthorFacadeRequest facade);

    UpdateAuthorDataRequest map(UpdateAuthorFacadeRequest facade);
}
