package org.example.menuapp.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter @Setter
public class OrderRequest {
    private String orderId;
    private Long userId;
    private Double totalPrice;
    private Integer tableNumber;
    private Set<OrderItemRequest> orderItems;
}
