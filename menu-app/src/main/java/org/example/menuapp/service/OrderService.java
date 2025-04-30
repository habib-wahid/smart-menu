package org.example.menuapp.service;

import org.example.menuapp.dto.request.OrderAddonRequest;
import org.example.menuapp.dto.request.OrderItemRequest;
import org.example.menuapp.dto.request.OrderRequest;
import org.example.menuapp.entity.*;
import org.example.menuapp.enums.OrderStatus;
import org.example.menuapp.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setUserId(orderRequest.getUserId());
        order.setOrderStatus(OrderStatus.PLACED);
        order.setTotalPrice(orderRequest.getTotalPrice());
        order.setIsServed(false);
        order.setIsPaid(false);
        order.setTableNo(orderRequest.getTableNumber());
        order.setOrderTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());


        double totalPrice = 0.0;

        for (OrderItemRequest orderItemRequest : orderRequest.getOrderItems()) {
            Item item = itemService.getItemById(orderItemRequest.getItemId());
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setQuantity(orderItemRequest.getQuantity());
            orderItem.setPrice(item.getPrice() * orderItemRequest.getQuantity());
            double itemTotalPrice = orderItem.getPrice();
            for (OrderAddonRequest addonRequest : orderItemRequest.getOrderAddons()) {
                AddOn addOn = addonService.getAddOnById(addonRequest.getAddonId());
                OrderAddon orderAddon = OrderAddon.builder()
                        .addOn(addOn)
                        .quantity(addonRequest.getQuantity())
                        .price(addonRequest.getQuantity() * addOn.getPrice())
                        .build();
                orderItem.addOrderAddon(orderAddon);
                itemTotalPrice += orderAddon.getPrice();
            }

            orderItem.setPrice(itemTotalPrice);
            order.addOrderItem(orderItem);
            totalPrice += itemTotalPrice;
        }

        order.setTotalPrice(totalPrice);
        orderRepository.save(order);

    }
}
