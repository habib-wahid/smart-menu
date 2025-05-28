package org.example.menuapp.controller;

import jakarta.validation.Valid;
import org.example.menuapp.dto.redis.OrderSummary;
import org.example.menuapp.dto.request.OrderRequest;
import org.example.menuapp.dto.request.StatusUpdateRequest;
import org.example.menuapp.dto.response.OrderResponse;
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


    @GetMapping("/all-order")
    public ResponseEntity<List<OrderResponse>> getAllPendingOrders() {
        List<OrderResponse> allOrders = orderService.getAllPendingOrders();
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
}
