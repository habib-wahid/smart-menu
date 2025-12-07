package org.example.menuapp.discount.domain.vo;

public record DiscountCode(
        String code
) {
    public DiscountCode(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Discount code is required");
        }
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}
