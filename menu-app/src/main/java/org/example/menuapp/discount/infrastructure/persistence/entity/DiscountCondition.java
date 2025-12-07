package org.example.menuapp.discount.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "discount_condition")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiscountCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "discount_condition_seq")
    @SequenceGenerator(name = "discount_condition_seq", sequenceName = "discount_condition_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "condition_type", nullable = false)
    private String conditionType;

    @Column(name = "condition_value", nullable = false)
    private String conditionValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id")
    private Discount discount;
}
