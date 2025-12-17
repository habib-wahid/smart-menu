package org.example.menuapp.discount.domain.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record OrderAmount(
        BigDecimal amount
) {
    public OrderAmount {
        if (amount == null) {
            throw new IllegalArgumentException("Order amount is required");
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Order amount must be greater than zero");
        }

        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
