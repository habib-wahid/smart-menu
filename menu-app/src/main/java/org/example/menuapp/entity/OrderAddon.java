package org.example.menuapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;


@Audited
@AuditTable(value = "order_addon_audit")
@Entity(name = "order_addon")
@Getter @Setter
public class OrderAddon {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_addon_seq")
    @SequenceGenerator(name = "order_addon_seq", sequenceName = "order_addon_sequence", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItem orderItem;

    @Column(name = "addon_id", nullable = false)
    private Long addonId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "price", nullable = false)
    private Double price;

}
