package org.example.menuapp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class AddonRequest {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private String filePath;
    private String fullPathUrl;
    private Double rating;
    private Set<Long> itemIds;

}
