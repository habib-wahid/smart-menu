package org.example.menuapp.discount.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.example.menuapp.discount.domain.entity.DiscountAggregate;
import org.example.menuapp.discount.domain.repository.DiscountDomainRepository;
import org.example.menuapp.discount.infrastructure.persistence.entity.Discount;
import org.example.menuapp.discount.infrastructure.persistence.mapper.DiscountFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaDiscountDomainRepository implements DiscountDomainRepository {

    private final DiscountRepository discountRepository;
    private final DiscountFactory discountFactory;

    @Override
    public void save(DiscountAggregate discountAggregate) {
        Discount discount = discountFactory.toEntity(discountAggregate);
        discountRepository.save(discount);
    }

    @Override
    public Optional<DiscountAggregate> findById(Long id) {
        return discountRepository.findById(id)
                .map(discountFactory::toDomain);
    }

    @Override
    public Optional<DiscountAggregate> findByCode(String code) {
        return discountRepository.findByCode(code)
                .map(discountFactory::toDomain);
    }
}
