package org.example.menuapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

import java.util.Set;

public record AddonRequest(
        @NotBlank(message = "Addon must have a name") String name,
        String description,
        @Positive(message = "Price must be grater than 0") Double price,
        @NotEmpty(message = "Must select an item") Set<Long> itemIds)
{ }
