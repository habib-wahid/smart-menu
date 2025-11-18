package org.example.menuapp.dto.request;

import jakarta.validation.constraints.NotNull;

public record OrderAddonRequest(
        @NotNull(message = "Addon must have an ID")
        Long addonId,

        @NotNull(message = "Must")
        Integer quantity
) {
}
