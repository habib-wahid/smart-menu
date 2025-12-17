package org.example.menuapp.discount.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.menuapp.discount.application.dto.ApplyDiscountRequest;
import org.example.menuapp.discount.application.dto.ApplyDiscountResponse;
import org.example.menuapp.discount.application.dto.CreateDiscountRequest;
import org.example.menuapp.discount.application.dto.DiscountListResponse;
import org.example.menuapp.discount.application.dto.DiscountResponse;
import org.example.menuapp.discount.application.dto.UpdateDiscountRequest;
import org.example.menuapp.discount.application.service.ApplyDiscountService;
import org.example.menuapp.discount.application.service.DiscountCrudService;
import org.example.menuapp.dto.response.ApiResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for Discount APIs
 * DDD: Presentation Layer - Handles HTTP requests and responses
 */
@RestController
@RequestMapping("/discounts")
@RequiredArgsConstructor
@Slf4j
public class DiscountController {

    private final DiscountCrudService discountCrudService;
    private final ApplyDiscountService applyDiscountService;

    /**
     * CREATE - Create a new discount
     * POST /api/discounts
     */
    @PostMapping
    public ApiResponse<DiscountResponse> createDiscount(@Valid @RequestBody CreateDiscountRequest request) {

        log.info("POST /api/discounts - Creating new discount with code: {}", request.code());

        DiscountResponse response = discountCrudService.createDiscount(request);
        return ApiResponse.success("Successfully created discount", response);

    }

    /**
     * READ - Get discount by ID
     * GET /api/discounts/{id}
     */
    @GetMapping("/{id}")
    public ApiResponse<DiscountResponse> getDiscountById(@PathVariable Long id) {
        log.info("GET /api/discounts/{} - Fetching discount by ID", id);

        return ApiResponse.success("Fetched Discount response successfully", discountCrudService.getDiscountById(id));

    }

    /**
     * READ - Get discount by code
     * GET /api/discounts/code/{code}
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<DiscountResponse> getDiscountByCode(@PathVariable String code) {
        log.info("GET /api/discounts/code/{} - Fetching discount by code", code);

        try {
            DiscountResponse response = discountCrudService.getDiscountByCode(code);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Discount not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * READ - Get all discounts with pagination
     * GET /api/discounts?page=0&size=10&sort=code,desc
     */
    @GetMapping
    public ResponseEntity<DiscountListResponse> getAllDiscounts(
            @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable) {
        log.info("GET /api/discounts - Fetching all discounts with pagination");

        try {
            DiscountListResponse response = discountCrudService.getAllDiscounts(pageable);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to fetch discounts: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * READ - Get all active discounts
     * GET /api/discounts/active
     */
    @GetMapping("/active")
    public ResponseEntity<?> getActiveDiscounts() {
        log.info("GET /api/discounts/active - Fetching all active discounts");

        try {
            var response = discountCrudService.getActiveDiscounts();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to fetch active discounts: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * UPDATE - Update an existing discount
     * PUT /api/discounts/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<DiscountResponse> updateDiscount(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDiscountRequest request) {
        log.info("PUT /api/discounts/{} - Updating discount", id);

        try {
            DiscountResponse response = discountCrudService.updateDiscount(id, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Failed to update discount: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * ACTION - Apply discount to order
     * POST /api/discounts/apply
     */
    @PostMapping("/apply")
    public ResponseEntity<ApplyDiscountResponse> applyDiscount(
            @Valid @RequestBody ApplyDiscountRequest request) {
        log.info("POST /api/discounts/apply - Applying discount code: {}", request.discountCode());

        try {
            ApplyDiscountResponse response = applyDiscountService.applyDiscount(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Failed to apply discount: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error applying discount: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * ACTION - Deactivate discount
     * PUT /api/discounts/{id}/deactivate
     */
    @PutMapping("/{id}/deactivate")
    public ApiResponse<String> deactivateDiscount(@PathVariable Long id) {
        log.info("PUT /api/discounts/{}/deactivate - Deactivating discount", id);
        discountCrudService.deactivateDiscount(id);
        return ApiResponse.success("Discount Deactivated Successfully");

    }

    /**
     * ACTION - Activate discount
     * PUT /api/discounts/{id}/activate
     */
    @PutMapping("/{id}/activate")
    public ApiResponse<String> activateDiscount(@PathVariable Long id) {
        log.info("PUT /api/discounts/{}/activate - Activating discount", id);

        discountCrudService.activateDiscount(id);
        return ApiResponse.success("Discount Activated Successfully");
    }

    /**
     * DELETE - Delete discount
     * DELETE /api/discounts/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiscount(@PathVariable Long id) {
        log.info("DELETE /api/discounts/{} - Deleting discount", id);

        try {
            discountCrudService.deleteDiscount(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Discount not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}

