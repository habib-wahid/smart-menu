package org.example.menuapp.repository;

import org.example.menuapp.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select distinct o from customer_order o " +
    "join fetch o.orderItems oi " +
    "where o.id = :orderId ")
    Optional<Order> findOrderWitOrderItemsById(@Param("orderId") Long orderId);

    @EntityGraph(attributePaths = {
            "orderItems", "orderItems.item",
            "orderItems.orderAddons",
            "orderItems.orderAddons.addOn"})
    Page<Order> findAllByOrderStatus(String orderStatus, Pageable pageable);


    @EntityGraph(attributePaths = {
            "orderItems",
            "orderItems.orderAddons"})
    Page<Order> findAllByCustomerIdAndOrderStatus(Long customerId, String orderStatus, Pageable pageable);

    @EntityGraph(attributePaths = {"orderItems", "orderItems.orderAddons"})
    Optional<Order> findByCustomerIdAndId(Long customerId, Long orderId);

    @Modifying
    @Query(value = "UPDATE customer_order SET order_status =:status WHERE id = :orderId", nativeQuery = true)
    void updateOrderStatus(@Param("orderId") Long orderId, @Param("status") String status);
}
