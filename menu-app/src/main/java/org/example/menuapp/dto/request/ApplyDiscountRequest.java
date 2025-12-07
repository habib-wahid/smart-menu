package org.example.menuapp.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for applying discount to order
 */
public record ApplyDiscountRequest(
        @NotBlank(message = "Discount code is required")
        String discountCode
) {
}

