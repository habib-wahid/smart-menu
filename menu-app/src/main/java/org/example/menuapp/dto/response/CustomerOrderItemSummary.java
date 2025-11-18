package org.example.menuapp.dto.response;

import java.util.Set;

public record CustomerOrderItemSummary(
        Long orderItemId,
        Long itemId,
        Integer quantity,
        Set<CustomerOrderAddonSummary> orderAddons
) {
}
