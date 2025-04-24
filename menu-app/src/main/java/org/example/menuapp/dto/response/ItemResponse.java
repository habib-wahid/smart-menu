package org.example.menuapp.dto.response;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class ItemResponse {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String filePath;
    private String fullPathUrl;
    private Double rating;
}
