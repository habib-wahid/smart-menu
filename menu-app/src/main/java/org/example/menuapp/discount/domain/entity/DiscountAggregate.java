package org.example.menuapp.discount.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.menuapp.discount.domain.AggregateRoot;
import org.example.menuapp.discount.domain.exception.DiscountConditionNotMatchException;
import org.example.menuapp.discount.domain.exception.DiscountExpiredException;
import org.example.menuapp.discount.domain.exception.DiscountNotActiveException;
import org.example.menuapp.discount.domain.exception.DiscountUsageLimitExceededException;
import org.example.menuapp.discount.domain.exception.DomainExceptionMessages;
import org.example.menuapp.discount.domain.exception.MinimumOrderAmountNotMetException;
import org.example.menuapp.discount.domain.snapshot.DiscountableOrderSnapshot;
import org.example.menuapp.discount.domain.vo.DiscountAmount;
import org.example.menuapp.discount.domain.vo.DiscountCode;
import org.example.menuapp.discount.domain.vo.DiscountPercentage;
import org.example.menuapp.discount.domain.vo.DiscountPeriod;
import org.example.menuapp.discount.domain.vo.UsageLimit;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiscountAggregate implements AggregateRoot {
    private Long id;
    private DiscountCode discountCode;
    private String description;
    private DiscountStrategy discountStrategy;
    private DiscountPercentage discountPercentage;
    private DiscountAmount discountAmount;
    private DiscountPeriod discountPeriod;
    private UsageLimit usageLimit;
    private Integer currentUsageCount = 0;
    private DiscountAmount minimumDiscountAmount;
    private DiscountAmount maximumDiscountAmount;
    private Boolean active = true;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;
    private Set<DiscountConditionDomain> conditions = new HashSet<>();

    public void validateApplicability(DiscountableOrderSnapshot order, LocalDateTime currentTime) {
        validateIfActive();
        validateTimePeriod(currentTime);
        validateUsageLimit();
        validateMinimumOrderAmount(order);
        validateConditions(order);
    }

    public DiscountAmount calculateDiscountAmount(DiscountableOrderSnapshot order) {
        BigDecimal totalPrice = new BigDecimal(order.totalPrice().toString());
        BigDecimal discountValue;

        switch (discountStrategy) {
            case PERCENTAGE:
                discountValue = discountPercentage.applyToAmount(new DiscountAmount(totalPrice)).getAmount();
                break;
            case FIXED_AMOUNT:
                discountValue = new BigDecimal(discountAmount.getAmount().toString());
                break;
            case BUY_ONE_GET_ONE_FREE:
                // Implement BOGO logic as needed
                discountValue = BigDecimal.ZERO; // Placeholder
                break;
            default:
                throw new IllegalArgumentException("Unknown discount strategy");
        }

        if (maximumDiscountAmount != null && discountValue.compareTo(maximumDiscountAmount.getAmount()) > 0) {
            discountValue = maximumDiscountAmount.getAmount();
        }

        return new DiscountAmount(discountValue);
    }

    private void validateConditions(DiscountableOrderSnapshot order) {
        if (conditions.isEmpty()) {
            return;
        }

        boolean anyConditionMatch = conditions.stream().anyMatch(condition -> condition.matches(order));
        if (!anyConditionMatch) {
            throw new DiscountConditionNotMatchException(DomainExceptionMessages.DISCOUNT_CONDITION_NOT_MATCH);
        }
    }

    private void validateMinimumOrderAmount(DiscountableOrderSnapshot order) {
        BigDecimal totalPrice = new BigDecimal(order.totalPrice().toString());
        
        if (minimumDiscountAmount != null && totalPrice.compareTo(minimumDiscountAmount.getAmount()) < 0) {
            throw new MinimumOrderAmountNotMetException(DomainExceptionMessages.DISCOUNT_MINIMUM_AMOUNT_NOT_MET);
        }
    }

    private void validateUsageLimit() {
        if (usageLimit != null && usageLimit.isExceeded(currentUsageCount)) {
            throw new DiscountUsageLimitExceededException(DomainExceptionMessages.DISCOUNT_USAGE_LIMIT_EXCEEDED);
        }
    }

    private void validateTimePeriod(LocalDateTime currentTime) {
        if (!discountPeriod.isActive(currentTime)) {
            throw new DiscountExpiredException(DomainExceptionMessages.DISCOUNT_EXPIRED);
        }
    }

    private void validateIfActive() {
        if (!active) {
            throw new DiscountNotActiveException(DomainExceptionMessages.DISCOUNT_NOT_ACTIVE);
        }
    }

    public enum DiscountStrategy {
        PERCENTAGE,
        FIXED_AMOUNT,
        BUY_ONE_GET_ONE_FREE
    }
}
