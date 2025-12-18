package org.example.menuapp.discount.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
import org.example.menuapp.discount.domain.vo.OrderAmount;
import org.example.menuapp.discount.domain.vo.UsageLimit;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiscountAggregate implements AggregateRoot {

    //todo: multiple aggregate with aggregate root, service domain
    private Long id;
    private String description;
    private DiscountCode discountCode;
    private DiscountStrategy discountStrategy;
    private DiscountPercentage discountPercentage;
    private DiscountAmount discountAmount;
    private DiscountPeriod discountPeriod;
    private UsageLimit usageLimit;
    private Integer currentUsageCount = 0;
    private OrderAmount minimumOrderAmount;
    private DiscountAmount maximumDiscountAmount;
    private Boolean active = true;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;
    private Set<DiscountConditionDomain> conditions = new HashSet<>();

    public DiscountAmount applyDiscount(DiscountableOrderSnapshot orderSnapshot) {
        validateApplicability(orderSnapshot, LocalDateTime.now());
        DiscountAmount discountAmount = calculateDiscountAmount(orderSnapshot);
        incrementUsageCount();
        this.updatedAt = LocalDateTime.now();
        return discountAmount;
    }

    public void validateApplicability(DiscountableOrderSnapshot order, LocalDateTime currentTime) {
        validateIfActive();
        validateTimePeriod(currentTime);
        validateUsageLimit();
        validateMinimumOrderAmount(order);
        validateConditions(order);
    }

    /**
     * Pure query method - calculates discount amount without mutating state
     * Does not modify this.discountAmount
     */
    public DiscountAmount calculateDiscountAmount(DiscountableOrderSnapshot order) {
        BigDecimal totalPrice = new BigDecimal(order.totalPrice().toString());

        BigDecimal discountValue = switch (discountStrategy) {
            case PERCENTAGE -> discountPercentage.applyToAmount(new DiscountAmount(totalPrice)).getAmount();
            case FIXED_AMOUNT -> new BigDecimal(discountAmount.getAmount().toString());
            case BUY_ONE_GET_ONE_FREE ->
                // Implement BOGO logic as needed
                    BigDecimal.ZERO; // Placeholder
            default -> throw new IllegalArgumentException("Unknown discount strategy");
        };

        if (maximumDiscountAmount != null && discountValue.compareTo(maximumDiscountAmount.getAmount()) > 0) {
            discountValue = maximumDiscountAmount.getAmount();
        }

        return new DiscountAmount(discountValue);
    }

    /**
     * Command method - explicitly sets the calculated discount amount
     * Use this after calculateDiscountAmount() to persist the value in the aggregate
     */
    public void applyCalculatedDiscount(DiscountAmount calculatedDiscount) {
        this.discountAmount = calculatedDiscount;
    }


    public void incrementUsageCount() {
        this.currentUsageCount += 1;
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
        
        if (minimumOrderAmount != null && totalPrice.compareTo(minimumOrderAmount.getAmount()) < 0) {
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

    public void deactivate() {
        this.active = false;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.active = true;
        this.updatedAt = LocalDateTime.now();
    }


    public static DiscountAggregate create(String description,
                                           DiscountCode discountCode,
                                           DiscountStrategy discountStrategy,
                                           DiscountPercentage discountPercentage,
                                           DiscountAmount discountAmount,
                                           DiscountPeriod discountPeriod,
                                           UsageLimit usageLimit,
                                           OrderAmount minimumOrderAmount,
                                           DiscountAmount maximumDiscountAmount,
                                           Boolean active,
                                           Set<DiscountConditionDomain> conditions) {

        return DiscountAggregate.builder()
                .description(description)
                .discountCode(discountCode)
                .discountStrategy(discountStrategy)
                .discountPercentage(discountPercentage)
                .discountAmount(discountAmount)
                .discountPeriod(discountPeriod)
                .usageLimit(usageLimit)
                .minimumOrderAmount(minimumOrderAmount)
                .maximumDiscountAmount(maximumDiscountAmount)
                .active(active)
                .conditions(conditions)
                .currentUsageCount(0)
                .build();
    }



    public enum DiscountStrategy {
        PERCENTAGE,
        FIXED_AMOUNT,
        BUY_ONE_GET_ONE_FREE
    }
}
