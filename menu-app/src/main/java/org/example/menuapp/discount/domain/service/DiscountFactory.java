package org.example.menuapp.discount.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.example.menuapp.discount.application.dto.CreateDiscountRequest;
import org.example.menuapp.discount.application.dto.UpdateDiscountRequest;
import org.example.menuapp.discount.domain.entity.DiscountAggregate;
import org.example.menuapp.discount.domain.vo.DiscountAmount;
import org.example.menuapp.discount.domain.vo.DiscountCode;
import org.example.menuapp.discount.domain.vo.DiscountPercentage;
import org.example.menuapp.discount.domain.vo.DiscountPeriod;
import org.example.menuapp.discount.domain.vo.OrderAmount;
import org.example.menuapp.discount.domain.vo.UsageLimit;
import org.springframework.stereotype.Service;

/**
 * Domain Service: DiscountFactory
 * DDD: Factory pattern for creating and reconstructing aggregates
 * - Encapsulates complex aggregate creation logic
 * - Ensures all invariants are satisfied at creation time
 * - Separates aggregate construction from application logic
 */
@Slf4j
public class DiscountFactory {

    /**
     * Create a new discount aggregate from request
     * DDD: Factory method - ensures aggregate is created in valid state
     */
    public DiscountAggregate createDiscount(CreateDiscountRequest request) {
        log.debug("Factory creating discount with code: {}", request.code());

        return DiscountAggregate.builder()
                .discountCode(new DiscountCode(request.code()))
                .description(request.description())
                .discountStrategy(DiscountAggregate.DiscountStrategy.valueOf(request.strategy().toUpperCase()))
                .discountPercentage(request.strategy().equalsIgnoreCase("PERCENTAGE") ?
                        new DiscountPercentage(request.discountValue()) : null)
                .discountAmount(request.strategy().equalsIgnoreCase("FIXED_AMOUNT") ?
                        new DiscountAmount(request.discountValue()) : null)
                .discountPeriod(new DiscountPeriod(request.startDate(), request.endDate()))
                .usageLimit(new UsageLimit(request.maxUsageCount()))
                .currentUsageCount(0)
                .minimumOrderAmount(request.minOrderAmount() != null ?
                        new OrderAmount(request.minOrderAmount()) : null)
                .maximumDiscountAmount(request.maxDiscountAmount() != null ?
                        new DiscountAmount(request.maxDiscountAmount()) : null)
                .active(true)
                .build();
    }

    /**
     * Update an existing discount aggregate
     * DDD: Creates new aggregate with updated values (immutability pattern)
     * Since aggregate is immutable, we create a new one with updated fields
     */
    public static DiscountAggregate updateDiscount(DiscountAggregate current, UpdateDiscountRequest request) {
        log.debug("Factory updating discount: {}", current.getId());

        return DiscountAggregate.builder()
                .id(current.getId())
                .discountCode(current.getDiscountCode())
                .description(request.description() != null ? request.description() : current.getDescription())
                .discountStrategy(DiscountAggregate.DiscountStrategy.valueOf(request.strategy().toUpperCase()))
                .discountPercentage(current.getDiscountPercentage())
                .discountAmount(current.getDiscountAmount())
                .discountPeriod(current.getDiscountPeriod())
                .usageLimit(current.getUsageLimit())
                .currentUsageCount(current.getCurrentUsageCount())
                .minimumOrderAmount(current.getMinimumOrderAmount())
                .maximumDiscountAmount(current.getMaximumDiscountAmount())
                .conditions(current.getConditions())
                .active(request.isActive() != null ? request.isActive() : current.getActive())
                .createdAt(current.getCreatedAt())
                .updatedAt(java.time.LocalDateTime.now())
                .version(current.getVersion())
                .build();
    }
}



