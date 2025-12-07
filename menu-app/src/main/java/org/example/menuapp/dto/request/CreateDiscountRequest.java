package org.example.menuapp.dto.request;
}
) {
        Integer perCustomerUsageLimit

        Integer totalUsageLimit,

        LocalDateTime validTill,
        @NotNull(message = "Valid till is required")

        LocalDateTime validFrom,
        @NotNull(message = "Valid from is required")

        Double maximumDiscountAmount,

        Double minimumOrderAmount,

        Double discountValue,
        @Positive(message = "Discount value must be positive")
        @NotNull(message = "Discount value is required")

        String discountType,
        @NotBlank(message = "Discount type is required")

        String description,

        String discountCode,
        @NotBlank(message = "Discount code is required")
public record CreateDiscountRequest(
 */
 * Request DTO for creating a discount
/**

import java.time.LocalDateTime;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;


