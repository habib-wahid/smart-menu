package org.example.menuapp.discount.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.menuapp.discount.application.dto.CreateDiscountRequest;
import org.example.menuapp.discount.application.dto.UpdateDiscountRequest;
import org.example.menuapp.discount.domain.entity.DiscountAggregate;
import org.example.menuapp.discount.infrastructure.persistence.repository.DiscountRepository;
import org.springframework.stereotype.Service;

/**
 * Domain Service: DiscountValidationDomainService
 * DDD: Encapsulates validation business rules that don't fit in a single aggregate
 * - Validates domain invariants
 * - Checks cross-aggregate constraints
 * - Enforces business rules
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DiscountValidationDomainService {

    private final DiscountRepository discountRepository;

    /**
     * Validate discount creation request
     * DDD: Business rule validation
     */
    public void validateDiscountCreation(CreateDiscountRequest request) {
        log.debug("Validating discount creation for code: {}", request.code());

        // Rule: Code must be unique
        if (discountRepository.existsByCode(request.code())) {
            throw new IllegalArgumentException("Discount code already exists: " + request.code());
        }

        log.debug("Discount creation validation passed for code: {}", request.code());
    }

    /**
     * Validate discount update request
     * DDD: Business rule validation for updates
     */
    public void validateDiscountUpdate(UpdateDiscountRequest request, DiscountAggregate current) {
        log.debug("Validating discount update for ID: {}", current.getId());

        // Rule: Cannot update to invalid strategy
        if (request.strategy() != null) {
            try {
                DiscountAggregate.DiscountStrategy.valueOf(request.strategy().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid discount strategy: " + request.strategy());
            }
        }

        log.debug("Discount update validation passed for ID: {}", current.getId());
    }

    /**
     * Validate discount can be deactivated
     * DDD: Business rule - check if deactivation is allowed
     */
    public void validateCanDeactivate(DiscountAggregate discount) {
        log.debug("Validating deactivation for discount: {}", discount.getId());

        if (!discount.getActive()) {
            throw new IllegalArgumentException("Discount is already inactive");
        }

        log.debug("Deactivation validation passed for discount: {}", discount.getId());
    }

    /**
     * Validate discount can be activated
     * DDD: Business rule - check if activation is allowed
     */
    public void validateCanActivate(DiscountAggregate discount) {
        log.debug("Validating activation for discount: {}", discount.getId());

        if (discount.getActive()) {
            throw new IllegalArgumentException("Discount is already active");
        }

        log.debug("Activation validation passed for discount: {}", discount.getId());
    }
}

