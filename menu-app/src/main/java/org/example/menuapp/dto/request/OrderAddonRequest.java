package org.example.menuapp.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderAddonRequest {
    private Long addonId;
    private Long orderItemId;
    private Integer quantity;
    private Double price;
}
