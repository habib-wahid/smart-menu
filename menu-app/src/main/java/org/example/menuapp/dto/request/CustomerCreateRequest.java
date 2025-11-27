package org.example.menuapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CustomerCreateRequest(
        String firstName,
        String lastName,
        @NotBlank(message = "Phone number can not be blank")
        @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 digits")
        String phone
) {
}
