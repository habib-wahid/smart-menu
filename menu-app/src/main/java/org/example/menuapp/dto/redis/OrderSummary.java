package org.example.menuapp.dto.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Data
@Builder
public class OrderSummary {
    private Long orderId;
    private Long userId;
    private String orderStatus;
    private Double totalPrice;
    private LocalDateTime orderTime;
    private List<OrderItemSummary> orderItemSummaryList;
}
