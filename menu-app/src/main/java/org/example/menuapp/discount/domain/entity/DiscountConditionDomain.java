package org.example.menuapp.discount.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.menuapp.discount.domain.snapshot.DiscountableOrderSnapshot;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiscountConditionDomain {
    private Long id;
    private ConditionType conditionType;
    private String conditionValue;

    public boolean matches(DiscountableOrderSnapshot order) {
       return matchesCondition(order);
    }

    private boolean matchesCondition(DiscountableOrderSnapshot order) {
        return order.conditions().stream()
                .anyMatch(orderConditionType -> orderConditionType == conditionType);
    }

    public enum ConditionType {
        CATEGORY, ITEM, CUSTOMER_SEGMENT, MINIMUM_ITEMS
    }
}
