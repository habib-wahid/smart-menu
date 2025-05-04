package org.example.menuapp.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.example.menuapp.enums.OrderStatus;

@Getter @Setter
public class StatusUpdateRequest {
    private OrderStatus status;
}
