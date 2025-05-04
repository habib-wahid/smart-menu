package org.example.menuapp.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderAddonRequest {
    @NotNull(message = "Addon must have an ID")
    private Long addonId;
    private Long orderItemId;
    @NotNull(message = "Addon must have quantity")
    private Integer quantity;

    private Double price;
}
