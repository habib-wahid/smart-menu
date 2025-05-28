package org.example.menuapp.dto.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
public class OrderAddonSummary {
    private Long orderAddonId;
    private Long addonId;
    private String addonName;
    private Integer quantity;
}
