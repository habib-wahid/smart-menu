package org.example.menuapp.discount.domain.snapshot;

import org.example.menuapp.discount.domain.entity.DiscountConditionDomain;

import java.util.Set;

public record DiscountableOrderSnapshot(
        Long orderId,
        Long customerId,
        Double totalPrice,
        Double totalItemPrice,
        Double totalAddonPrice,
        Set<DiscountConditionDomain.ConditionType> conditions
) {

}
