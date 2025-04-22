package org.example.menuapp.dto;

import lombok.Data;

@Data
public class CategoryRequest {
    private Long categoryId;
    private String name;
    private String description;
}
