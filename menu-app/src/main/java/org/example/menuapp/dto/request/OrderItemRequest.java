package org.example.menuapp.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter @Setter
public class OrderItemRequest {
    private Long orderItemId;
    private Long itemId;
    private Integer quantity;
    private Long price;
    private Set<OrderAddonRequest> orderAddons;
}
