package org.example.menuapp.mapper;

import org.example.menuapp.dto.request.ItemRequest;
import org.example.menuapp.dto.response.ItemResponse;
import org.example.menuapp.entity.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "filePath", source = "filePath")
    @Mapping(target = "fileName", source = "fileName")
    Item toItem(ItemRequest itemRequest, String fileName, String filePath);


    ItemResponse toItemResponse(Item item);
}
