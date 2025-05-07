package org.example.menuapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "customer_order")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
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
    private List<OrderItem> orderItems = new ArrayList<>();


    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void removeOrderItem(OrderItem orderItem) {
        orderItems.remove(orderItem);
        orderItem.setOrder(null);
    }

}
