package org.example.menuapp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;


@Entity(name = "order_addon")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class OrderAddon {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_addon_seq")
    @SequenceGenerator(name = "order_addon_seq", sequenceName = "order_addon_sequence", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItem orderItem;

    @ManyToOne
    @JoinColumn(name = "addon_id", nullable = false)
    private AddOn addOn;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "price", nullable = false)
    private Double price;

}
