package org.example.menuapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;
import org.example.menuapp.entity.Item;
import org.example.menuapp.entity.Order;

import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class OrderItemResponse {
    private Long orderItemId;
    private Long orderId;
    private Long itemId;
    private String itemName;
    private Double itemUnitPrice;
    private Integer quantity;
    private Double totalPrice;
    private List<OrderAddonResponse> orderAddons;
}
