package org.example.menuapp.service;

import org.example.menuapp.dto.request.OrderAddonRequest;
import org.example.menuapp.dto.request.OrderItemRequest;
import org.example.menuapp.dto.request.OrderRequest;
import org.example.menuapp.dto.request.StatusUpdateRequest;
import org.example.menuapp.dto.response.OrderAddonResponse;
import org.example.menuapp.dto.response.OrderItemResponse;
import org.example.menuapp.dto.response.OrderResponse;
import org.example.menuapp.entity.*;
import org.example.menuapp.enums.OrderStatus;
import org.example.menuapp.error.custom_exceptions.OrderDeletionException;
import org.example.menuapp.error.custom_exceptions.SmResourceNotFoundException;
import org.example.menuapp.error.messages.ExceptionMessages;
import org.example.menuapp.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final AddonService addonService;
    private final ItemService itemService;

    public OrderService(OrderRepository orderRepository, AddonService addonService, ItemService itemService) {
        this.orderRepository = orderRepository;
        this.addonService = addonService;
        this.itemService = itemService;
    }

    @Transactional
    public OrderResponse placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setUserId(orderRequest.getUserId());
        order.setOrderStatus(OrderStatus.PLACED.getStatus());
        order.setIsServed(false);
        order.setIsPaid(false);
        order.setTableNo(orderRequest.getTableNumber());
        order.setOrderTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());

        double totalItemPrice = 0.0;
        double totalAddonPrice = 0.0;

        for (OrderItemRequest orderItemRequest : orderRequest.getOrderItems()) {
            Item item = itemService.getItemById(orderItemRequest.getItemId());
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setQuantity(orderItemRequest.getQuantity());
            double currentItemTotalPrice = orderItemRequest.getQuantity() * item.getPrice();
            orderItem.setTotalItemPrice(currentItemTotalPrice);
            totalItemPrice += currentItemTotalPrice;

            if (orderItemRequest.getOrderAddons() != null) {
                double totalAddonPriceForItem = 0;
                for (OrderAddonRequest addonRequest : orderItemRequest.getOrderAddons()) {
                    AddOn addOn = addonService.getAddOnById(addonRequest.getAddonId());
                    OrderAddon orderAddon = OrderAddon.builder()
                            .addOn(addOn)
                            .quantity(addonRequest.getQuantity())
                            .price(addonRequest.getQuantity() * addOn.getPrice())
                            .build();
                    orderItem.addOrderAddon(orderAddon);
                    totalAddonPriceForItem += addonRequest.getQuantity() * addOn.getPrice();
                }
                totalAddonPrice += totalAddonPriceForItem;
                orderItem.setTotalAddonPrice(totalAddonPriceForItem);
                orderItem.setTotalPrice(currentItemTotalPrice + totalAddonPriceForItem);
            }
            order.addOrderItem(orderItem);
        }

        order.setTotalPrice(totalItemPrice + totalAddonPrice);
        order.setTotalItemPrice(totalItemPrice);
        order.setTotalAddonPrice(totalAddonPrice);
        Order response = orderRepository.save(order);
        return mapToOrderResponse(response);
    }

    public void updateOrderStatus(Long orderId, StatusUpdateRequest statusUpdateRequest) {
        Order order = getOrderById(orderId);
        order.setOrderStatus(statusUpdateRequest.getStatus().getStatus());
        orderRepository.save(order);
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        Order order = getOrderById(orderId);
        if (order.getOrderStatus().equals(OrderStatus.PROCESSING.getStatus())
            || order.getOrderStatus().equals(OrderStatus.COMPLETED.getStatus())
        ) {
            throw new OrderDeletionException(
                    String.format(ExceptionMessages.ORDER_NOT_ABLE_TO_DELETE, order.getOrderStatus()));
        }
        orderRepository.delete(order);
    }

    private Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new SmResourceNotFoundException(
                        String.format(ExceptionMessages.RESOURCE_NOT_FOUND, "Order", orderId)));
    }


    private OrderResponse mapToOrderResponse(Order order) {
        return OrderResponse.builder()
                .orderId(order.getId())
                .userId(order.getUserId())
                .orderStatus(order.getOrderStatus())
                .totalPrice(order.getTotalPrice())
                .isPaid(order.getIsPaid())
                .isServed(order.getIsServed())
                .tableNo(order.getTableNo())
                .orderTime(order.getOrderTime())
                .updateTime(order.getUpdateTime())
                .deliveryTime(order.getDeliveryTime())
                .orderItems(getOrderItemList(order.getOrderItems()))
                .build();
    }

    private List<OrderItemResponse> getOrderItemList(List<OrderItem> orderItemList) {
        List<OrderItemResponse> orderItemResponseList = new ArrayList<>();
        orderItemList.forEach(orderItem -> orderItemResponseList.add(mapToOrderItemResponse(orderItem)));
        return orderItemResponseList;
    }

    private List<OrderAddonResponse> getOrderAddonList(List<OrderAddon> orderAddonList) {
        List<OrderAddonResponse> orderAddonResponseList = new ArrayList<>();
        orderAddonList.forEach(orderAddon -> orderAddonResponseList.add(mapToOrderAddonResponse(orderAddon)));
        return orderAddonResponseList;
    }

    private OrderItemResponse mapToOrderItemResponse(OrderItem orderItem) {
        return OrderItemResponse.builder()
                .orderItemId(orderItem.getId())
                .orderId(orderItem.getOrder().getId())
                .itemId(orderItem.getItem().getId())
                .itemName(orderItem.getItem().getName())
                .itemUnitPrice(orderItem.getItem().getPrice())
                .quantity(orderItem.getQuantity())
                .totalPrice(orderItem.getTotalPrice())
                .orderAddons(getOrderAddonList(orderItem.getOrderAddons()))
                .build();
    }

    private OrderAddonResponse mapToOrderAddonResponse(OrderAddon orderAddon) {
        return OrderAddonResponse.builder()
                .orderAddonId(orderAddon.getId())
                .orderItemId(orderAddon.getOrderItem().getId())
                .addOnId(orderAddon.getAddOn().getId())
                .addOnName(orderAddon.getAddOn().getName())
                .addonUnitPrice(orderAddon.getAddOn().getPrice())
                .quantity(orderAddon.getQuantity())
                .totalPrice(orderAddon.getPrice())
                .build();
    }

}
