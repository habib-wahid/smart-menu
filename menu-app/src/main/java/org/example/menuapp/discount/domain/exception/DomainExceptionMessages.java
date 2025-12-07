package org.example.menuapp.discount.domain.exception;

public class DomainExceptionMessages {
    private DomainExceptionMessages() {
    }

    public static final String DISCOUNT_NOT_ACTIVE = "Discount not active";
    public static final String DISCOUNT_EXPIRED = "Discount expired";
    public static final String DISCOUNT_USAGE_LIMIT_EXCEEDED = "Usage limit for discount exceeded";
    public static final String DISCOUNT_MINIMUM_AMOUNT_NOT_MET = "Minimum order amount for discount not met";
    public static final String DISCOUNT_CONDITION_NOT_MATCH = "Discount condition not match";
}
