package org.example.menuapp.dto.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
public class OrderItemSummary {
    private Long orderItemId;
    private Long itemId;
    private String itemName;
    private Integer quantity;
    private List<OrderAddonSummary> orderAddonSummaryList;
}
