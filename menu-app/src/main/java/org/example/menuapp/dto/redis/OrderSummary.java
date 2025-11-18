package org.example.menuapp.dto.redis;

import java.time.LocalDateTime;
import java.util.List;

public record OrderSummary(
         Long orderId,
         Long userId,
         String orderStatus,
         Double totalPrice,
         LocalDateTime orderTime,
         List<OrderItemSummary> orderItemSummaryList
) {

}
