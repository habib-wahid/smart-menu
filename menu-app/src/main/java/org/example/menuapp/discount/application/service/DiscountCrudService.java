package org.example.menuapp.discount.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.menuapp.discount.application.dto.CreateDiscountRequest;
import org.example.menuapp.discount.application.dto.DiscountListResponse;
import org.example.menuapp.discount.application.dto.DiscountResponse;
import org.example.menuapp.discount.application.dto.UpdateDiscountRequest;
import org.example.menuapp.discount.domain.entity.DiscountAggregate;
import org.example.menuapp.discount.domain.repository.DiscountDomainRepository;
import org.example.menuapp.discount.domain.service.DiscountFactory;
import org.example.menuapp.discount.domain.service.DiscountValidationDomainService;
import org.example.menuapp.discount.domain.vo.DiscountAmount;
import org.example.menuapp.discount.domain.vo.DiscountCode;
import org.example.menuapp.discount.domain.vo.DiscountPercentage;
import org.example.menuapp.discount.domain.vo.DiscountPeriod;
import org.example.menuapp.discount.domain.vo.OrderAmount;
import org.example.menuapp.discount.domain.vo.UsageLimit;
import org.example.menuapp.discount.infrastructure.persistence.entity.Discount;
import org.example.menuapp.discount.infrastructure.persistence.mapper.DiscountMapper;
import org.example.menuapp.discount.infrastructure.persistence.repository.DiscountRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
@Slf4j
public class DiscountCrudService {

    // Domain repositories and services
    private final DiscountDomainRepository discountDomainRepository;
    private final DiscountValidationDomainService validationDomainService;

    // Infrastructure layer
    private final DiscountRepository discountRepository;
    private final DiscountMapper discountMapper;

    @Transactional
    public DiscountResponse createDiscount(CreateDiscountRequest request) {
        log.info("Creating new discount with code: {}", request.code());

        // Domain Service: Validate discount creation request
        validationDomainService.validateDiscountCreation(request);

        DiscountAggregate.DiscountStrategy strategy = DiscountAggregate.DiscountStrategy.valueOf(request.strategy().toUpperCase());

        DiscountAggregate discount = DiscountAggregate.create(
                request.description(),
                new DiscountCode(request.code()),
                strategy,
                strategy == DiscountAggregate.DiscountStrategy.PERCENTAGE ? new DiscountPercentage(request.discountValue())
                        : null,

                strategy == DiscountAggregate.DiscountStrategy.FIXED_AMOUNT ? new DiscountAmount(request.discountValue())
                        : null,
                new DiscountPeriod(request.startDate(), request.endDate()),
                new UsageLimit(request.maxUsageCount()),
                new OrderAmount(request.minOrderAmount()),
                new DiscountAmount(request.maxDiscountAmount()),
                Boolean.TRUE,
                Set.of()
        );

        // Infrastructure: Persist aggregate
        discountDomainRepository.save(discount);
        log.info("Discount created successfully with code: {}", request.code());

        return toResponse(discount);
    }

    /**
     * Use Case: Get discount by ID
     */
    @Transactional(readOnly = true)
    public DiscountResponse getDiscountById(Long id) {
        log.info("Fetching discount with ID: {}", id);

        DiscountAggregate discount = discountDomainRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Discount not found with ID: " + id));

        return toResponse(discount);
    }

    /**
     * Use Case: Get discount by code
     */
    @Transactional(readOnly = true)
    public DiscountResponse getDiscountByCode(String code) {
        log.info("Fetching discount with code: {}", code);

        DiscountAggregate discount = discountDomainRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Discount not found with code: " + code));

        return toResponse(discount);
    }

    /**
     * Use Case: Get all discounts with pagination
     */
    @Transactional(readOnly = true)
    public DiscountListResponse getAllDiscounts(Pageable pageable) {
        log.info("Fetching all discounts with page size: {}", pageable.getPageSize());

        Page<Discount> discountPage = discountRepository.findAll(pageable);
        List<DiscountResponse> responses = discountPage.getContent().stream()
                .map(discountMapper::toDomain)  // Persistence entity → Domain aggregate
                .map(this::toResponse)           // Domain aggregate → Response DTO
                .toList();

        return new DiscountListResponse(
                responses,
                (int) discountPage.getTotalElements(),
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
    }

    /**
     * Use Case: Get all active discounts
     */
    @Transactional(readOnly = true)
    public List<DiscountResponse> getActiveDiscounts() {
        log.info("Fetching all active discounts");

        List<Discount> activeDiscounts = discountRepository.findByIsActiveTrue();
        return activeDiscounts.stream()
                .map(discountMapper::toDomain)  // Persistence entity → Domain aggregate
                .map(this::toResponse)           // Domain aggregate → Response DTO
                .toList();
    }

    /**
     * Use Case: Update discount
     * DDD: Uses immutable aggregate pattern - creates new state via command methods
     */
    @Transactional
    public DiscountResponse updateDiscount(Long id, UpdateDiscountRequest request) {
        log.info("Updating discount with ID: {}", id);

        // Retrieve aggregate
        DiscountAggregate discount = discountDomainRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Discount not found with ID: " + id));

        // Domain Service: Validate update request
        validationDomainService.validateDiscountUpdate(request, discount);

        // Domain Service: Factory creates updated aggregate
        DiscountAggregate updatedDiscount = DiscountFactory.updateDiscount(discount, request);

        // Infrastructure: Persist updated aggregate
        discountDomainRepository.save(updatedDiscount);
        log.info("Discount updated successfully with ID: {}", id);

        return toResponse(updatedDiscount);
    }

    /**
     * Use Case: Deactivate discount
     * DDD: Uses explicit command method on aggregate
     */
    @Transactional
    public void deactivateDiscount(Long id) {
        log.info("Deactivating discount with ID: {}", id);

        DiscountAggregate discount = discountDomainRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Discount not found with ID: " + id));

        // Domain Service: Validate deactivation is allowed
        validationDomainService.validateCanDeactivate(discount);

        // Domain Aggregate: Execute command (explicit state change)
        discount.deactivate();

        // Infrastructure: Persist changes
        discountDomainRepository.save(discount);
        log.info("Discount deactivated successfully with ID: {}", id);
    }

    /**
     * Use Case: Activate discount
     * DDD: Uses explicit command method on aggregate
     */
    @Transactional
    public void activateDiscount(Long id) {
        log.info("Activating discount with ID: {}", id);

        DiscountAggregate discount = discountDomainRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Discount not found with ID: " + id));

        // Domain Service: Validate activation is allowed
        validationDomainService.validateCanActivate(discount);

        // Domain Aggregate: Execute command (explicit state change)
        discount.activate();

        // Infrastructure: Persist changes
        discountDomainRepository.save(discount);
        log.info("Discount activated successfully with ID: {}", id);
    }

    /**
     * Use Case: Delete discount (soft delete)
     */
    @Transactional
    public void deleteDiscount(Long id) {
        log.info("Deleting discount with ID: {}", id);

        if (!discountRepository.existsById(id)) {
            throw new IllegalArgumentException("Discount not found with ID: " + id);
        }

        discountRepository.deleteById(id);
        log.info("Discount deleted successfully with ID: {}", id);
    }

    /**
     * Mapper: Convert domain aggregate to response DTO
     * DDD: Translates domain model to presentation layer
     */
    private DiscountResponse toResponse(DiscountAggregate discount) {
        return new DiscountResponse(
                discount.getId(),
                discount.getDiscountCode().toString(),
                discount.getDescription(),
                discount.getDiscountStrategy().name(),
                discount.getDiscountPercentage() != null ?
                        discount.getDiscountPercentage().getValue() :
                        discount.getDiscountAmount().getAmount(),
                discount.getDiscountPeriod().startDate(),
                discount.getDiscountPeriod().endDate(),
                discount.getUsageLimit().maxCount(),
                discount.getCurrentUsageCount(),
                discount.getMinimumOrderAmount() != null ?
                        discount.getMinimumOrderAmount().getAmount() : null,
                discount.getMaximumDiscountAmount() != null ?
                        discount.getMaximumDiscountAmount().getAmount() : null,
                discount.getActive(),
                discount.getCreatedAt(),
                discount.getUpdatedAt(),
                discount.getVersion()
        );
    }
}

