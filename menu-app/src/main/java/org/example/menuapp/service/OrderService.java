package org.example.menuapp.service;

import jakarta.validation.Valid;
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
import org.example.menuapp.error.custom_exceptions.SmOrderProcessedException;
import org.example.menuapp.error.custom_exceptions.SmResourceNotFoundException;
import org.example.menuapp.error.messages.ExceptionMessages;
import org.example.menuapp.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

            // todo : need to get all the items at a time

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

    @Transactional
    public OrderResponse updateOrder(Long orderId, OrderRequest orderRequest) {
        Order currentOrder = orderRepository.findOrderWithDetailsById(orderId)
                .orElseThrow(() -> new SmResourceNotFoundException(
                        String.format(ExceptionMessages.RESOURCE_NOT_FOUND, "Order", orderId)));

        if (!currentOrder.getOrderStatus().equals(OrderStatus.PLACED.getStatus())) {
            throw new SmOrderProcessedException(ExceptionMessages.ORDER_PROCESSED);
        }
        removeItemsFromOrder(currentOrder, orderRequest);
        updateOrderItem(currentOrder, orderRequest);

        return null;
    }

    private void updateOrderItem(Order currentOrder, OrderRequest orderRequest) {

        currentOrder.setTableNo(orderRequest.getTableNumber());
        double totalItemPrice = 0.0;
        double totalAddonPrice = 0.0;

        for (OrderItemRequest orderItem : orderRequest.getOrderItems()) {
            OrderItem currentOrderItem = currentOrder.getOrderItems()
                    .stream().filter(item -> item.getId().equals(orderItem.getOrderItemId()))
                    .findFirst().orElse(null);

            Item item = itemService.getItemById(orderItem.getItemId());


            if (currentOrderItem == null) {
                currentOrderItem = new OrderItem();
                currentOrderItem.setItem(item);
                currentOrderItem.setQuantity(orderItem.getQuantity());
                double currentItemTotalPrice = orderItem.getQuantity() * item.getPrice();
                currentOrderItem.setTotalItemPrice(currentItemTotalPrice);
                totalItemPrice += currentItemTotalPrice;

                if (orderItem.getOrderAddons() != null) {
                    double totalAddonPriceForItem = 0;
                    for (OrderAddonRequest addonRequest : orderItem.getOrderAddons()) {
                        AddOn addOn = addonService.getAddOnById(addonRequest.getAddonId());
                        OrderAddon orderAddon = OrderAddon.builder()
                                .addOn(addOn)
                                .quantity(addonRequest.getQuantity())
                                .price(addonRequest.getQuantity() * addOn.getPrice())
                                .build();
                        totalAddonPriceForItem += addonRequest.getQuantity() * addOn.getPrice();
                        currentOrderItem.addOrderAddon(orderAddon);
                    }

                    totalAddonPrice += totalAddonPriceForItem;
                    currentOrderItem.setTotalAddonPrice(totalAddonPriceForItem);
                    currentOrderItem.setTotalPrice(currentItemTotalPrice + totalAddonPriceForItem);
                }
                currentOrder.addOrderItem(currentOrderItem);
            } else {
                currentOrderItem.setQuantity(orderItem.getQuantity());
                double currentItemTotalPrice = orderItem.getQuantity() * item.getPrice();
                currentOrderItem.setTotalItemPrice(currentItemTotalPrice);
                totalItemPrice += currentItemTotalPrice;

                if (orderItem.getOrderAddons() != null) {

                    List<OrderAddon> currentOrderItemAddons = currentOrderItem.getOrderAddons();
                    double totalAddonPriceForItem = 0;
                    removeAddonsFromOrderItem(currentOrderItem, currentOrderItemAddons, orderItem.getOrderAddons());

                    for (OrderAddonRequest addonRequest : orderItem.getOrderAddons()) {

                        OrderAddon currentOrderAddon = currentOrderItemAddons.stream()
                                .filter(addon -> addon.getId().equals(addonRequest.getOrderAddonId()))
                                .findFirst().orElse(null);

                        AddOn addOn = addonService.getAddOnById(addonRequest.getAddonId());

                        if (currentOrderAddon == null) {
                            OrderAddon orderAddon = OrderAddon.builder()
                                    .addOn(addOn)
                                    .quantity(addonRequest.getQuantity())
                                    .price(addonRequest.getQuantity() * addOn.getPrice())
                                    .build();
                            currentOrderItem.addOrderAddon(orderAddon);
                            totalAddonPriceForItem += addonRequest.getQuantity() * addOn.getPrice();
                        } else {
                            currentOrderAddon.setQuantity(addonRequest.getQuantity());
                            currentOrderAddon.setPrice(addonRequest.getQuantity() * addOn.getPrice());
                            totalAddonPriceForItem += addonRequest.getQuantity() * addOn.getPrice();
                        }

                        totalAddonPrice += totalAddonPriceForItem;
                    }
                    currentOrderItem.setTotalAddonPrice(totalAddonPriceForItem);
                    currentOrderItem.setTotalPrice(totalItemPrice + totalAddonPriceForItem);
                }
            }
        }

        currentOrder.setTotalItemPrice(totalItemPrice);
        currentOrder.setTotalAddonPrice(totalAddonPrice);
        currentOrder.setTotalPrice(totalItemPrice + totalAddonPrice);
        orderRepository.save(currentOrder);
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

    private void removeItemsFromOrder(Order currentOrder, OrderRequest orderRequest) {
        Set<Long> currentOrderItemIds = currentOrder.getOrderItems()
                .stream().map(OrderItem::getId).collect(Collectors.toSet());

        Set<Long> currentOrderRequestItemIds = orderRequest.getOrderItems()
                .stream().map(OrderItemRequest::getOrderItemId).collect(Collectors.toSet());

        Set<Long> deletedItemIds = new HashSet<>(currentOrderItemIds);
        deletedItemIds.removeAll(currentOrderRequestItemIds);

        List<OrderItem> itemsNeedToDelete = new ArrayList<>();
        for (Long id : deletedItemIds) {
            currentOrder.getOrderItems().stream()
                    .filter(orderItem -> orderItem.getId().equals(id))
                    .findFirst()
                    .ifPresent(itemsNeedToDelete::add);
        }

        currentOrder.removeOrderItem(itemsNeedToDelete);
    }

    private void removeAddonsFromOrderItem(OrderItem currentOrderItem, List<OrderAddon> currentOrderItemAddons, Set<OrderAddonRequest> orderAddonRequest) {
        Set<Long> currentOrderItemAddonIds = currentOrderItemAddons
                .stream().map(OrderAddon::getId).collect(Collectors.toSet());

        Set<Long> currentOrderItemAddonRequestIds = orderAddonRequest.stream()
                .map(OrderAddonRequest::getOrderAddonId)
                .collect(Collectors.toSet());

        Set<Long> addonIdsToDelete = new HashSet<>(currentOrderItemAddonIds);
        addonIdsToDelete.removeAll(currentOrderItemAddonRequestIds);

        List<OrderAddon> addonsNeedToDelete = new ArrayList<>();
        for (Long id : addonIdsToDelete) {
            currentOrderItemAddons.stream()
                    .filter(orderAddon -> orderAddon.getId().equals(id))
                    .findFirst()
                    .ifPresent(addonsNeedToDelete::add);
        }

        currentOrderItem.removeOrderAddon(addonsNeedToDelete);

    }
}
