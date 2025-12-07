package org.example.menuapp.discount.domain.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record DiscountAmount(
        BigDecimal amount
) {
    public DiscountAmount {
        if (amount == null) {
            throw new IllegalArgumentException("Discount amount is required");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Discount amount cannot be negative");
        }

        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    public DiscountAmount add(DiscountAmount other) {
        return new DiscountAmount(this.amount.add(other.amount));
    }

    public DiscountAmount subtract(DiscountAmount other) {
        return new DiscountAmount(this.amount.subtract(other.amount));
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
