package org.example.menuapp.discount.infrastructure.persistence.repository;

import org.example.menuapp.discount.infrastructure.persistence.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {

    Optional<Discount> findByCode(String code);

    /**
     * Find all active discounts
     */
    List<Discount> findByIsActiveTrue();

    /**
     * Check if discount code already exists
     */
    boolean existsByCode(String code);

    /**
     * Find all discounts sorted by creation date
     */
    List<Discount> findAllByOrderByCreatedAtDesc();
}
