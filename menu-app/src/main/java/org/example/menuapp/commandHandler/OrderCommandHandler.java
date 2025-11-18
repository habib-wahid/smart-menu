package org.example.menuapp.commandHandler;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.menuapp.dto.request.OrderRequest;
import org.example.menuapp.dto.request.OrderStatusUpdateRequest;
import org.example.menuapp.dto.response.ApiResponse;
import org.example.menuapp.dto.response.OrderResponse;
import org.example.menuapp.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class OrderCommandHandler {

    private final OrderService orderService;

    @PostMapping("/place-order")
    public ApiResponse<String> placeOrder(@Valid @RequestBody OrderRequest orderRequest) {
        orderService.placeOrder(orderRequest);
        return ApiResponse.success("Order placed successfully");
    }

    @PatchMapping("/orders/{orderId}/status")
    public ApiResponse<String> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody OrderStatusUpdateRequest orderStatusUpdateRequest) {
        orderService.updateOrderStatus(orderId, orderStatusUpdateRequest);
        return ApiResponse.success("Order status updated successfully");
    }


    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponse> updateOrder(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.updateOrder(orderId, orderRequest);
        return ResponseEntity.status(HttpStatus.OK).body(orderResponse);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Map<String, String>> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok(Map.of("message", "Order deleted"));
    }
}
