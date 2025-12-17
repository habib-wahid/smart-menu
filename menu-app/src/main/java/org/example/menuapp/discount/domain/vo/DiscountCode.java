package org.example.menuapp.discount.domain.vo;

public record DiscountCode(
        String code
) {
    public DiscountCode {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Discount code is required");
        }
    }

    @Override
    public String toString() {
        return code;
    }
}
