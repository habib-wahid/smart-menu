package org.example.menuapp.repository;

import org.example.menuapp.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select distinct o from customer_order o " +
    "join fetch o.orderItems oi " +
    "where o.id = :orderId ")
    Optional<Order> findOrderWithDetailsById(@Param("orderId") Long orderId);
}
