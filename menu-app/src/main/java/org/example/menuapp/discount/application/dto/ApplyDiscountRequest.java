package org.example.menuapp.discount.application.dto;

import java.util.Set;

public record ApplyDiscountRequest(
        Long orderId,
        String discountCode,
        Long customerId,
        Double totalPrice,
        Double totalItemPrice,
        Double totalAddonPrice,
        Set<String> conditions
) {
}
