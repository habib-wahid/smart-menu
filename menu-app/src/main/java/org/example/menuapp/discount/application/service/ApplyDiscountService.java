package org.example.menuapp.discount.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.menuapp.discount.application.dto.ApplyDiscountRequest;
import org.example.menuapp.discount.application.dto.ApplyDiscountResponse;
import org.example.menuapp.discount.domain.entity.DiscountConditionDomain;
import org.example.menuapp.discount.domain.repository.DiscountDomainRepository;
import org.example.menuapp.discount.domain.snapshot.DiscountableOrderSnapshot;
import org.example.menuapp.discount.domain.vo.DiscountAmount;
import org.example.menuapp.discount.domain.vo.DiscountCode;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplyDiscountService {
    private final DiscountDomainRepository discountDomainRepository;

    public ApplyDiscountResponse applyDiscount(ApplyDiscountRequest applyDiscountRequest) {

        try {
            DiscountCode discountCode = new DiscountCode(applyDiscountRequest.discountCode());
            var discount = discountDomainRepository.findByCode(discountCode.toString())
                    .orElseThrow(() -> new IllegalArgumentException("Discount code not found: " + discountCode));


            DiscountableOrderSnapshot orderSnapshot = new DiscountableOrderSnapshot(
                    applyDiscountRequest.orderId(),
                    applyDiscountRequest.customerId(),
                    applyDiscountRequest.totalPrice(),
                    applyDiscountRequest.totalItemPrice(),
                    applyDiscountRequest.totalAddonPrice(),
                    mapConditions(applyDiscountRequest.conditions())
            );


            DiscountAmount discountAmount = discount.applyDiscount(orderSnapshot);

            log.info("Discount of amount {} applied using code {}", discountAmount.getAmount(), discountCode);
            discountDomainRepository.save(discount);
            return new ApplyDiscountResponse(discountAmount.getAmount());

        } catch (Exception e) {
            log.error("Failed to apply discount: {}", e.getMessage());
            throw e;
        }

    }


    private Set<DiscountConditionDomain.ConditionType> mapConditions(Set<String> conditions) {
        return conditions.stream()
                .map(condition -> DiscountConditionDomain.ConditionType.valueOf(condition.toUpperCase()))
                .collect(Collectors.toSet());
    }

}
