package org.example.menuapp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class OrderAddon {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_addon_seq")
    @SequenceGenerator(name = "order_addon_seq", sequenceName = "order_addon_sequence", allocationSize = 50)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItem orderItem;

    @ManyToOne
    @JoinColumn(name = "addon_id", nullable = false)
    private AddOn addOn;

    private Integer quantity;

    private Double price;

}
