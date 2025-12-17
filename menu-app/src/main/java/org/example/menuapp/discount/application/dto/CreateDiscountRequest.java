package org.example.menuapp.discount.application.dto;

import java.time.LocalDateTime;
import java.math.BigDecimal;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

public record CreateDiscountRequest(
        BigDecimal maxDiscountAmount,

        BigDecimal minOrderAmount,

        Integer maxUsageCount,

        LocalDateTime endDate,

        @NotNull(message = "Start date is required")
        LocalDateTime startDate,

        @Positive(message = "Discount value must be positive")
        @NotNull(message = "Discount value is required")
        BigDecimal discountValue,

        @NotBlank(message = "Discount strategy is required")
        String strategy,

        String description,

        @NotBlank(message = "Discount code is required")
        String code
) {

}


