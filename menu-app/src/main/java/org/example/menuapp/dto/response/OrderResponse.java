package org.example.menuapp.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class OrderResponse {
    private Long orderId;
    private Long userId;
    private String orderStatus;
    private Double totalPrice;
    private Boolean isPaid;
    private Boolean isServed;
    private Integer tableNo;
    private LocalDateTime orderTime;
    private LocalDateTime updateTime;
    private LocalDateTime deliveryTime;
    private List<OrderItemResponse> orderItems;
}
