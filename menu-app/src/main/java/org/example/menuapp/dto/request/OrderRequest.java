package org.example.menuapp.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.util.Set;

public record OrderRequest(

        @Pattern(regexp = "^[0-9]{10,11}$", message = "Phone number must be 10-12 digits")
        String phone,

        @NotNull(message = "Must provide table number")
        @Positive
        Integer tableNumber,

        @Valid
        @NotNull(message = "Order must contain items")
        @NotEmpty(message = "Order must contain at least one item")
        Set<OrderItemRequest> orderItems
) {
}
