package org.example.menuapp.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Set;

public record OrderRequest(

        @NotNull(message = "Must provide user id")
        Long customerId,
        @NotNull(message = "Must provide table number")
        @Positive
        Integer tableNumber,

        @Valid
        @NotNull(message = "Order must contain items")
        @NotEmpty(message = "Order must contain at least one item")
        Set<OrderItemRequest> orderItems
) {
}
