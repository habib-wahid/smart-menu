package org.example.menuapp.discount.infrastructure.persistence.mapper;

import lombok.RequiredArgsConstructor;
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

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DiscountMapper {

    public DiscountAggregate toDomain(Discount discount) {
        Set<DiscountConditionDomain> domainConditions = discount.getConditions() != null
                ? discount.getConditions().stream()
                .map(this::toConditionDomain)
                .collect(Collectors.toSet())
                : new HashSet<>();

        return DiscountAggregate.builder()
                .id(discount.getId())
                .description(discount.getDescription())
                .discountCode(new DiscountCode(discount.getCode()))
                .discountStrategy(discount.getStrategy())
                .discountPercentage(discount.getPercentage() != null
                        ? new DiscountPercentage(discount.getPercentage())
                        : null)
                .discountAmount(discount.getFixedAmount() != null
                        ? new DiscountAmount(discount.getFixedAmount())
                        : null)
                .discountPeriod(new DiscountPeriod(discount.getStartDate(), discount.getEndDate()))
                .usageLimit(discount.getMaxUsageCount() != null
                        ? new UsageLimit(discount.getMaxUsageCount())
                        : null)
                .currentUsageCount(discount.getCurrentUsageCount())
                .minimumOrderAmount(discount.getMinOrderAmount() != null
                        ? new OrderAmount(discount.getMinOrderAmount())
                        : null)
                .maximumDiscountAmount(discount.getMaxDiscountAmount() != null
                        ? new DiscountAmount(discount.getMaxDiscountAmount())
                        : null)
                .active(discount.getIsActive())
                .createdAt(discount.getCreatedAt())
                .updatedAt(discount.getUpdatedAt())
                .version(discount.getVersion())
                .conditions(domainConditions)
                .build();
    }

    public Discount toEntity(DiscountAggregate aggregate) {
        Discount discount = new Discount();
        discount.setId(aggregate.getId());
        discount.setCode(aggregate.getDiscountCode().toString());
        discount.setDescription(aggregate.getDescription());
        discount.setStrategy(aggregate.getDiscountStrategy());
        discount.setPercentage(aggregate.getDiscountPercentage() != null
                ? aggregate.getDiscountPercentage().getValue()
                : null);
        discount.setFixedAmount(aggregate.getDiscountAmount() != null
                ? aggregate.getDiscountAmount().getAmount()
                : null);
        discount.setStartDate(aggregate.getDiscountPeriod().startDate());
        discount.setEndDate(aggregate.getDiscountPeriod().endDate());
        discount.setMaxUsageCount(aggregate.getUsageLimit() != null
                ? aggregate.getUsageLimit().maxCount()
                : null);
        discount.setCurrentUsageCount(aggregate.getCurrentUsageCount());
        discount.setMinOrderAmount(aggregate.getMinimumOrderAmount() != null
                ? aggregate.getMinimumOrderAmount().getAmount()
                : null);
        discount.setMaxDiscountAmount(aggregate.getMaximumDiscountAmount() != null
                ? aggregate.getMaximumDiscountAmount().getAmount()
                : null);
        discount.setIsActive(aggregate.getActive());
        discount.setCreatedAt(aggregate.getCreatedAt());
        discount.setUpdatedAt(aggregate.getUpdatedAt());
        discount.setVersion(aggregate.getVersion());

        Set<DiscountCondition> entityConditions = aggregate.getConditions() != null
                ? aggregate.getConditions().stream()
                .map(conditionDomain -> toConditionEntity(conditionDomain, discount))
                .collect(Collectors.toSet())
                : new HashSet<>();
        discount.setConditions(entityConditions);

        return discount;
    }

    private DiscountConditionDomain toConditionDomain(DiscountCondition condition) {
        return DiscountConditionDomain.builder()
                .id(condition.getId())
                .conditionType(DiscountConditionDomain.ConditionType.valueOf(condition.getConditionType()))
                .conditionValue(condition.getConditionValue())
                .build();
    }

    private DiscountCondition toConditionEntity(DiscountConditionDomain domain, Discount discount) {
        DiscountCondition condition = new DiscountCondition();
        condition.setId(domain.getId());
        condition.setConditionType(domain.getConditionType().name());
        condition.setConditionValue(domain.getConditionValue());
        condition.setDiscount(discount);
        return condition;
    }
}

