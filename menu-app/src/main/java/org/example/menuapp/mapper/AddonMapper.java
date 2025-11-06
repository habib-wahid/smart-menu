package org.example.menuapp.mapper;

import org.example.menuapp.dto.response.AddonResponse;
import org.example.menuapp.entity.AddOn;
import org.springframework.stereotype.Component;

@Component
public class AddonMapper {

    public AddonResponse mapToAddonResponse(AddOn addOn) {
        return AddonResponse.builder()
                .id(addOn.getId())
                .name(addOn.getName())
                .description(addOn.getDescription())
                .price(addOn.getPrice())
                .filePath(addOn.getFilePath())
              //  .fullPathUrl(addOn.getFullPathUrl())
                .rating(addOn.getRating())
                .build();
    }
}
