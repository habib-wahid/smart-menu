package org.example.menuapp.controller;

import net.sf.jasperreports.engine.JRException;
import org.example.menuapp.dto.redis.OrderSummary;
import org.example.menuapp.dto.response.ApiResponse;
import org.example.menuapp.dto.response.CustomerOrderSummary;
import org.example.menuapp.dto.response.OrderResponse;
import org.example.menuapp.enums.OrderStatus;
import org.example.menuapp.service.OrderReportService;
import org.example.menuapp.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;


@RestController
public class OrderController {

    //todo: RBAC(stuff should only be able to update order status, customer profile management, real time admin dashboard, payment processing)
    private final OrderService orderService;
    //  private final SimpMessagingTemplate messagingTemplate;
    private final OrderReportService orderReportService;

    public OrderController(OrderService orderService, OrderReportService orderReportService) {
        this.orderService = orderService;
        this.orderReportService = orderReportService;
    }


    @GetMapping("/customers/{customerId}/orders/{orderId}")
    public ApiResponse<CustomerOrderSummary> getCustomerOrderDetails(
            @PathVariable Long customerId,
            @PathVariable Long orderId) {
        CustomerOrderSummary orderSummary = orderService.getOrderDetails(customerId, orderId);
        return ApiResponse.success("Order fetched Successfully", orderSummary);
    }

    @GetMapping("/customers/{customerId}/orders")
    public ApiResponse<List<CustomerOrderSummary>> getCustomerOrdersByStatus(
            @PathVariable Long customerId,
            @RequestParam(name = "orderStatus") OrderStatus orderStatus,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {
        List<CustomerOrderSummary> orderSummaries = orderService.getAllOrdersOfCustomer(customerId, orderStatus, page, size);
        return ApiResponse.success("Order Fetched Successfully", orderSummaries);
    }

    @GetMapping("/all-orders-summary")
    public ResponseEntity<List<OrderSummary>> getAllOrders(
            @RequestParam(value = "0") Long page,
            @RequestParam(value = "50") Long size
    ) {
       // List<OrderSummary> orderSummaries = orderService.getAllOrdersSummary(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(null);
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


    @GetMapping("/report")
    public String getOrderReport() throws JRException, IOException {
        return orderReportService.orderReport("pdf");
    }
}
