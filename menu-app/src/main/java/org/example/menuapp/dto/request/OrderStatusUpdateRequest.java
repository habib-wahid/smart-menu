package org.example.menuapp.dto.request;

import org.example.menuapp.enums.OrderStatus;


public record OrderStatusUpdateRequest(
        OrderStatus status
) { }
