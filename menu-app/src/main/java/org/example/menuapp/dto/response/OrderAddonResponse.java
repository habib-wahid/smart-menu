package org.example.menuapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;
import org.example.menuapp.entity.AddOn;
import org.example.menuapp.entity.OrderItem;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class OrderAddonResponse {
    private Long orderAddonId;
    private Long orderItemId;
    private Long addonId;
    private String addonName;
    private Double addonUnitPrice;
    private Integer quantity;
    private Double totalPrice;
}
