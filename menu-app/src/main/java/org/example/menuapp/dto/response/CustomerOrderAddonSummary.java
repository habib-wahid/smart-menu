package org.example.menuapp.dto.response;

public record CustomerOrderAddonSummary(
        Long orderAddonId,
        Long addonId,
        Integer quantity
) {
}
