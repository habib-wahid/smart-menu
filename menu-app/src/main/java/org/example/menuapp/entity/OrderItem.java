package org.example.menuapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "order_item")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_item_seq")
    @SequenceGenerator(name = "order_item_seq", sequenceName = "order_item_sequence", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @Column(name = "total_addon_price")
    private Double totalAddonPrice;

    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderAddon> orderAddons = new ArrayList<>();

    public void addOrderAddon(OrderAddon orderAddon) {
        orderAddons.add(orderAddon);
        orderAddon.setOrderItem(this);
    }

    public void removeOrderAddon(OrderAddon orderAddon) {
        orderAddons.remove(orderAddon);
        orderAddon.setOrderItem(null);
    }

}
