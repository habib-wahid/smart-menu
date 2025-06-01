package org.example.menuapp.controller;

import jakarta.validation.Valid;
import org.example.menuapp.dto.redis.OrderSummary;
import org.example.menuapp.dto.request.OrderRequest;
import org.example.menuapp.dto.request.StatusUpdateRequest;
import org.example.menuapp.dto.response.OrderResponse;
import org.example.menuapp.enums.OrderStatus;
import org.example.menuapp.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;



@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    //todo need two apis. 1. Get all orders of a customer based on placed, processing, finish. 2. Get order details of a customer order


    @GetMapping("/all-order-order-status")
    public ResponseEntity<List<OrderResponse>> getAllPendingOrders(
            @RequestParam(name = "orderStatus") OrderStatus orderStatus,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        List<OrderResponse> allOrders = orderService.getAllOrdersByStatus(orderStatus, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(allOrders);
    }

    @GetMapping("/all-orders-summary")
    public ResponseEntity<List<OrderSummary>> getAllOrders(
            @RequestParam(value = "0") Long page,
            @RequestParam(value = "50") Long size
    ) {
       List<OrderSummary> orderSummaries = orderService.getAllOrdersSummary(page, size);
       return ResponseEntity.status(HttpStatus.OK).body(orderSummaries);
    }

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@Valid @RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.placeOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponse> updateOrder(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.updateOrder(orderId, orderRequest);
        return ResponseEntity.status(HttpStatus.OK).body(orderResponse);
    }

    @PatchMapping("/{orderId}/status")
    public void updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody StatusUpdateRequest statusUpdateRequest) {
        orderService.updateOrderStatus(orderId, statusUpdateRequest);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Map<String, String>> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok(Map.of("message", "Order deleted"));
    }

    @GetMapping("/customer-orders")
    public ResponseEntity<List<OrderSummary>> getCustomerOrdersByStatus(
            @RequestParam(name = "customerId") Long customerId,
            @RequestParam(name = "orderStatus") OrderStatus orderStatus,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {
        List<OrderSummary> orderSummaries = orderService.getAllOrdersOfCustomer(customerId, orderStatus, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(orderSummaries);
    }
}
