package org.example.menuapp.discount.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for discount response
 */
public record DiscountResponse(
        Long id,
        String code,
        String description,
        String strategy,
        BigDecimal discountValue,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Integer maxUsageCount,
        Integer currentUsageCount,
        BigDecimal minOrderAmount,
        BigDecimal maxDiscountAmount,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long version
) {
}

