package org.example.menuapp.controller;

import jakarta.validation.Valid;
import org.example.menuapp.dto.request.OrderRequest;
import org.example.menuapp.dto.request.StatusUpdateRequest;
import org.example.menuapp.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping
    public ResponseEntity<Void> placeOrder(@Valid @RequestBody OrderRequest orderRequest) {
        orderService.placeOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
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
