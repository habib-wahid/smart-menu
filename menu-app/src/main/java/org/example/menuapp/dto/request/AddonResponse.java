package org.example.menuapp.dto.request;

import lombok.*;

import java.util.Set;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class AddonResponse {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String filePath;
    private String fullPathUrl;
    private Double rating;
    private Set<Long> itemIds;
}
