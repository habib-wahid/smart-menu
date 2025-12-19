package org.example.menuapp.dto.response;

public record PopularItemResponse(
        Long id,
        String name,
        String description,
        Double price,
        String filePath,
        String fileName,
        Double rating,
        Double totalRevenue
) {
}
