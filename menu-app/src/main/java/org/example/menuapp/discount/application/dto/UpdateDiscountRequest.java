package org.example.menuapp.discount.application.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for updating an existing discount
 */
public record UpdateDiscountRequest(
        String description,

        @NotBlank(message = "Strategy is required")
        String strategy,

        Integer maxUsageCount,

        Boolean isActive
) {
}

