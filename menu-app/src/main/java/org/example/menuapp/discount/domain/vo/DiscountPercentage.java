package org.example.menuapp.discount.domain.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record DiscountPercentage(
        BigDecimal percentage
) {
    public DiscountPercentage(BigDecimal percentage) {
        if (percentage == null || percentage.compareTo(BigDecimal.ZERO) <= 0 || percentage.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        }

        this.percentage = percentage.setScale(2, RoundingMode.HALF_UP);
    }

    public DiscountAmount applyToAmount(DiscountAmount baseAmount) {
       BigDecimal result = baseAmount.getAmount()
               .multiply(this.percentage)
                .divide(new BigDecimal("100"), RoundingMode.HALF_UP);

       return new DiscountAmount(result);
    }

    public BigDecimal getValue() {
        return percentage;
    }
}
