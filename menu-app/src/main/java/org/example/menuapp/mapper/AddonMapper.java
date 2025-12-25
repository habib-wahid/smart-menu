package org.example.menuapp.mapper;

import org.example.menuapp.dto.request.AddonRequest;
import org.example.menuapp.dto.response.AddonResponse;
import org.example.menuapp.entity.Addon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddonMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fileName", ignore = true)
    @Mapping(target = "rating", ignore = true)
    Addon toAddon(AddonRequest request);

    AddonResponse toAddonResponse(Addon addon);
}
