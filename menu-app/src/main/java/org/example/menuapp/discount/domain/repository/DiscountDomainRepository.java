package org.example.menuapp.discount.domain.repository;

import org.example.menuapp.discount.domain.entity.DiscountAggregate;

import java.util.Optional;

public interface DiscountDomainRepository {

    void save(DiscountAggregate discountAggregate);
    Optional<DiscountAggregate> findById(Long id);
    Optional<DiscountAggregate> findByCode(String code);
}
