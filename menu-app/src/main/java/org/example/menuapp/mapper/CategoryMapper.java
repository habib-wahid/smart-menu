package org.example.menuapp.mapper;

import org.example.menuapp.dto.request.CategoryRequest;
import org.example.menuapp.dto.response.CategoryResponseDto;
import org.example.menuapp.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Category toCategory(CategoryRequest categoryRequest);

    CategoryResponseDto mapToCategoryResponseDto(Category category);
}
