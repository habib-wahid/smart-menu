package org.example.menuapp.dto.request;

import lombok.Data;

@Data
public class CategoryRequest {
    private Long categoryId;
    private String name;
    private String description;
}
