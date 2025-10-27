package org.example.menuapp.mapper;

import org.example.menuapp.dto.request.CategoryRequest;
import org.example.menuapp.dto.response.CategoryResponseDto;
import org.example.menuapp.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toCategory(CategoryRequest categoryRequest);

    CategoryResponseDto mapToCategoryResponseDto(Category category);
}
