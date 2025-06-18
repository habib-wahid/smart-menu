package org.example.menuapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class PopularItemResponse {
    private Long id;
    private String name;
    private  String description;
    private Double price;
    private String filePath;
    private Double rating;
    private Long numberOfOrders;
}
