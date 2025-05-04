package org.example.menuapp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.menuapp.enums.OrderStatus;

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
    @SequenceGenerator(name = "order_seq", sequenceName = "order_sequence", allocationSize = 50)
    private Long id;

    private Long userId;
    private String orderStatus;
    private Double totalPrice;
    private Boolean isPaid;
    private Boolean isServed;
    private Integer tableNo;
    private LocalDateTime orderTime;
    private LocalDateTime updateTime;
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
