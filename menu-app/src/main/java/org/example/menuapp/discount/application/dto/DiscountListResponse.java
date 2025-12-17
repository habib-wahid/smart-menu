package org.example.menuapp.discount.application.dto;

import java.util.List;

/**
 * DTO for paginated discount list response
 */
public record DiscountListResponse(
        List<DiscountResponse> discounts,
        Integer totalCount,
        Integer pageNumber,
        Integer pageSize
) {
}

