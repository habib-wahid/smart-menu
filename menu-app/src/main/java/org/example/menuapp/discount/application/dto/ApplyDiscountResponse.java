package org.example.menuapp.discount.application.dto;

import java.math.BigDecimal;

public record ApplyDiscountResponse(
        BigDecimal discountedAmount
) {
}
