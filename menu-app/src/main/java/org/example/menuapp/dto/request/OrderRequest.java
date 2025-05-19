package org.example.menuapp.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter @Setter
public class OrderRequest {
    private Long userId;

    @NotNull(message = "Must provide table number")
    @Positive
    private Integer tableNumber;

    @Valid
    private Set<OrderItemRequest> orderItems;
}
