package org.example.menuapp.dto.response;

public record ItemResponse(Long id, String name, String description,
                           Double price, String filePath,
                           String fileName,Double rating) {}
