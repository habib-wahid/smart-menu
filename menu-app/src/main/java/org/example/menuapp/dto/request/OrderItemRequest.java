package org.example.menuapp.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record OrderItemRequest(

        @NotNull(message = "Id can not be null")
        Long itemId,
        @NotNull(message = "Every order must contain item quantity")
        Integer quantity,
        Set<OrderAddonRequest> orderAddons
) {
}
