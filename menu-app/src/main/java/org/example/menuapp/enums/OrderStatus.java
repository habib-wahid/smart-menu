package org.example.menuapp.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PLACED("order_placed"),
    PROCESSING("order_processing"),
    SHIPPING("order_shipping"),
    COMPLETED("order_completed"),
    CANCELED("order_canceled");

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }

}
