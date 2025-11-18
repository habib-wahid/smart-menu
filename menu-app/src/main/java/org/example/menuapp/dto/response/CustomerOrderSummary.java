package org.example.menuapp.dto.response;

import java.time.LocalDateTime;
import java.util.Set;

public record CustomerOrderSummary(
        Long orderId,
        Long userId,
        String orderStatus,
        Double totalPrice,
        LocalDateTime orderTime,
        Set<CustomerOrderItemSummary> orderItems
) {
}
