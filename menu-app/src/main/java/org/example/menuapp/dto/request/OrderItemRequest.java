package org.example.menuapp.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter @Setter
public class OrderItemRequest {
    private Long orderItemId;

    @NotNull(message = "Id can not be null")
    private Long itemId;

    @NotNull(message = "Every order must contain item quantity")
    private Integer quantity;

    private Long price;

    @Valid
    private Set<OrderAddonRequest> orderAddons;
}
