package org.example.menuapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;


public record ItemRequest(
        @NotBlank(message = "Item name can't be blank") String name,
        String description,
        @NotNull(message = "Every item should have a price") double price,
        @NotEmpty(message = "Every item must have at least one category") Set<Long> categoryIds
) { }
