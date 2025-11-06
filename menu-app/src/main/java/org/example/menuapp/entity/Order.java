package org.example.menuapp.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Audited
@AuditTable(value = "customer_order_audit")
@Entity(name = "customer_order")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq")
    @SequenceGenerator(name = "order_seq", sequenceName = "order_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "order_status", nullable = false)
    private String orderStatus;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @Column(name = "total_item_price", nullable = false)
    private Double totalItemPrice;

    @Column(name = "total_addon_price")
    private Double totalAddonPrice;

    @Column(name = "paid")
    private Boolean isPaid;

    @Column(name = "served")
    private Boolean isServed;

    @Column(name = "table_no")
    private Integer tableNo;

    @Column(name = "order_time")
    private LocalDateTime orderTime;

    @Column(name = "order_update_time")
    private LocalDateTime updateTime;

    @Column(name = "order_delivery_time")
    private LocalDateTime deliveryTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> orderItems = new HashSet<>();


    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void removeOrderItem(List<OrderItem> items) {
        orderItems.removeAll(items);
        items.forEach(item -> item.setOrder(null));
    }

}
