package org.example.menuapp.discount.infrastructure.persistence.mapper;

import org.example.menuapp.discount.domain.entity.DiscountAggregate;
import org.example.menuapp.discount.domain.entity.DiscountConditionDomain;
import org.example.menuapp.discount.domain.vo.DiscountAmount;
import org.example.menuapp.discount.domain.vo.DiscountCode;
import org.example.menuapp.discount.domain.vo.DiscountPercentage;
import org.example.menuapp.discount.domain.vo.DiscountPeriod;
import org.example.menuapp.discount.domain.vo.OrderAmount;
import org.example.menuapp.discount.domain.vo.UsageLimit;
import org.example.menuapp.discount.infrastructure.persistence.entity.Discount;
import org.example.menuapp.discount.infrastructure.persistence.entity.DiscountCondition;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DiscountFactory {

    public DiscountAggregate toDomain(Discount entity) {
        if (entity == null) {
            return null;
        }

        // Map conditions
        Set<DiscountConditionDomain> conditions = entity.getConditions().stream()
                .map(this::conditionToDomain)
                .collect(Collectors.toSet());

        // Build immutable aggregate
        return DiscountAggregate.builder()
                .id(entity.getId())
                .discountCode(new DiscountCode(entity.getCode()))
                .description(entity.getDescription())
                .discountStrategy(entity.getStrategy())
                .discountPercentage(entity.getPercentage() != null ?
                        new DiscountPercentage(entity.getPercentage()) : null)
                .discountAmount(entity.getFixedAmount() != null ?
                        new DiscountAmount(entity.getFixedAmount()) : null)
                .discountPeriod(new DiscountPeriod(entity.getStartDate(), entity.getEndDate()))
                .usageLimit(new UsageLimit(entity.getMaxUsageCount()))
                .currentUsageCount(entity.getCurrentUsageCount())
                .minimumOrderAmount(entity.getMinOrderAmount() != null ?
                        new OrderAmount(entity.getMinOrderAmount()) : null)
                .maximumDiscountAmount(entity.getMaxDiscountAmount() != null ?
                        new DiscountAmount(entity.getMaxDiscountAmount()) : null)
                .conditions(conditions)
                .active(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .version(entity.getVersion())
                .build();
    }

    public Discount toEntity(DiscountAggregate aggregate) {
        if (aggregate == null) {
            return null;
        }

        Discount entity = new Discount();
        entity.setId(aggregate.getId());
        entity.setCode(aggregate.getDiscountCode().toString());
        entity.setDescription(aggregate.getDescription());

        entity.setStrategy(aggregate.getDiscountStrategy());

        if (aggregate.getDiscountAmount() != null) {
            entity.setFixedAmount(aggregate.getDiscountAmount().getAmount());
        }

        if (aggregate.getDiscountPercentage() != null) {
            entity.setPercentage(aggregate.getDiscountPercentage().getValue());
        }

        entity.setStartDate(aggregate.getDiscountPeriod().startDate());
        entity.setEndDate(aggregate.getDiscountPeriod().endDate());

        if (aggregate.getUsageLimit().maxCount() != null) {
            entity.setMaxUsageCount(aggregate.getUsageLimit().maxCount());
        }

        entity.setCurrentUsageCount(aggregate.getCurrentUsageCount());

        if (aggregate.getMinimumOrderAmount() != null) {
            entity.setMinOrderAmount(aggregate.getMinimumOrderAmount().getAmount());
        }

        if (aggregate.getMaximumDiscountAmount() != null) {
            entity.setMaxDiscountAmount(aggregate.getMaximumDiscountAmount().getAmount());
        }

        Set<DiscountCondition> conditions = aggregate.getConditions().stream()
                .map(this::conditionToEntity)
                .collect(Collectors.toSet());
        entity.setConditions(conditions);

        entity.setIsActive(aggregate.getActive());
        entity.setVersion(aggregate.getVersion());
        entity.setCreatedAt(aggregate.getCreatedAt());
        entity.setUpdatedAt(aggregate.getUpdatedAt());

        return entity;
    }

    private DiscountConditionDomain conditionToDomain(DiscountCondition entity) {
        return new DiscountConditionDomain(
                entity.getId(),
                DiscountConditionDomain.ConditionType.valueOf(entity.getConditionType()),
                entity.getConditionValue()
        );
    }

    private DiscountCondition conditionToEntity(DiscountConditionDomain condition) {
        DiscountCondition entity = new DiscountCondition();
        entity.setId(condition.getId());
        entity.setConditionType(condition.getConditionType().name());
        entity.setConditionValue(condition.getConditionValue());
        return entity;
    }

}
