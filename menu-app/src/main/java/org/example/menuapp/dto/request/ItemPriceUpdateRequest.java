package org.example.menuapp.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemPriceUpdateRequest(
        @NotNull(message = "Item Price can not be null")
        @Positive(message = "Price must be grater than 0")
        Double price
) {
}
