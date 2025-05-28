package org.example.menuapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.menuapp.config.ConstantKeys;
import org.example.menuapp.dto.redis.OrderAddonSummary;
import org.example.menuapp.dto.redis.OrderItemSummary;
import org.example.menuapp.dto.redis.OrderSummary;
import org.example.menuapp.dto.request.*;
import org.example.menuapp.dto.response.OrderAddonResponse;
import org.example.menuapp.dto.response.OrderItemResponse;
import org.example.menuapp.dto.response.OrderResponse;
import org.example.menuapp.entity.*;
import org.example.menuapp.enums.OrderStatus;
import org.example.menuapp.error.custom_exceptions.OrderDeletionException;
import org.example.menuapp.error.custom_exceptions.SmUpdateNotAllowedException;
import org.example.menuapp.error.custom_exceptions.SmResourceNotFoundException;
import org.example.menuapp.error.messages.ExceptionMessages;
import org.example.menuapp.repository.OrderRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final AddonService addonService;
    private final ItemService itemService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public OrderService(OrderRepository orderRepository, AddonService addonService, ItemService itemService, RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.addonService = addonService;
        this.itemService = itemService;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public OrderResponse placeOrder(OrderRequest orderRequest) {
        Order order = getOrder(orderRequest);
        double totalItemPrice = 0.0;
        double totalAddonPrice = 0.0;

        for (OrderItemRequest orderItemRequest : orderRequest.getOrderItems()) {
            OrderItem orderItem = getOrderItem(orderItemRequest, null);
            totalItemPrice += orderItem.getTotalItemPrice();
            totalAddonPrice += orderItem.getTotalAddonPrice();
            order.addOrderItem(orderItem);
        }


        order.setTotalPrice(totalItemPrice + totalAddonPrice);
        order.setTotalItemPrice(totalItemPrice);
        order.setTotalAddonPrice(totalAddonPrice);
        Order response = orderRepository.save(order);
        try {
            cacheOrderDetails(response);
        } catch (Exception e) {
            log.error("Failed to cache order details", e);
        }
        return mapToOrderResponse(response);
    }


    @Transactional
    public OrderResponse updateOrder(Long orderId, OrderRequest orderRequest) {
        Order currentOrder = orderRepository.findOrderWithDetailsById(orderId)
                .orElseThrow(() -> new SmResourceNotFoundException(
                        String.format(ExceptionMessages.RESOURCE_NOT_FOUND, "Order", orderId)));

        if (!currentOrder.getOrderStatus().equals(OrderStatus.PLACED.getStatus())) {
            throw new SmUpdateNotAllowedException(ExceptionMessages.ORDER_PROCESSED);
        }
        removeItemsFromOrder(currentOrder, orderRequest);
        return mapToOrderResponse(updateOrderItem(currentOrder, orderRequest));
    }

    private Order updateOrderItem(Order currentOrder, OrderRequest orderRequest) {
        currentOrder.setTableNo(orderRequest.getTableNumber());
        double totalItemPrice = 0.0;
        double totalAddonPrice = 0.0;

        for (OrderItemRequest orderItem : orderRequest.getOrderItems()) {
            OrderItem currentOrderItem = findCurrentOrderItem(currentOrder, orderItem.getOrderItemId());
            Item item = itemService.getItemById(orderItem.getItemId());

            if (currentOrderItem == null) {
                currentOrderItem = getOrderItem(orderItem, item);
                currentOrder.addOrderItem(currentOrderItem);
            } else {
                updateExistingOrderItem(currentOrderItem, orderItem, item);
            }

            totalItemPrice += currentOrderItem.getTotalItemPrice();
            totalAddonPrice += currentOrderItem.getTotalAddonPrice();
        }

        currentOrder.setTotalItemPrice(totalItemPrice);
        currentOrder.setTotalAddonPrice(totalAddonPrice);
        currentOrder.setTotalPrice(totalItemPrice + totalAddonPrice);
        currentOrder.setUpdateTime(LocalDateTime.now());
        return orderRepository.save(currentOrder);
    }

    public void updateOrderStatus(Long orderId, StatusUpdateRequest statusUpdateRequest) {
        Order order = getOrderById(orderId);
        order.setOrderStatus(statusUpdateRequest.getStatus().getStatus());
        removeOrderFromCache(order);
        orderRepository.save(order);
    }

    private void removeOrderFromCache(Order order) {
        OrderSummary summary = getOrderSummary(order);
        List<Object> orders = redisTemplate.opsForList().range(ConstantKeys.ORDERS_KEY, 0, -1);
        if (orders != null) {
            for (int i = 0; i < orders.size(); i++) {
                OrderSummary orderSummary = objectMapper.convertValue(orders.get(i), OrderSummary.class);
                if (summary.getOrderId().equals(orderSummary.getOrderId())) {
                    redisTemplate.opsForList().remove(ConstantKeys.ORDERS_KEY, 1, orderSummary);
                }
            }
        }
       // redisTemplate.opsForList().remove(ConstantKeys.ORDERS_KEY, 0, summary);
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

    private Order getOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setUserId(orderRequest.getUserId());
        order.setOrderStatus(OrderStatus.PLACED.getStatus());
        order.setIsServed(false);
        order.setIsPaid(false);
        order.setTableNo(orderRequest.getTableNumber());
        order.setOrderTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        return order;
    }

    private OrderItem getOrderItem(OrderItemRequest orderItemRequest, Item requestItem) {
        // todo : need to get all the items at a time
        Item item = resolveItem(orderItemRequest, requestItem);
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setQuantity(orderItemRequest.getQuantity());
        orderItem.setTotalItemPrice(orderItemRequest.getQuantity() * item.getPrice());

        if (orderItemRequest.getOrderAddons() != null) {
           processAddons(orderItemRequest, orderItem);
        } else {
            orderItem.setTotalAddonPrice(0.0);
            orderItem.setTotalPrice(orderItem.getTotalItemPrice());
        }

        return orderItem;
    }

    private void processUpdateAddons(OrderItemRequest orderItem, OrderItem currentOrderItem) {
        double totalAddonPriceForItem = 0.0;
        for (OrderAddonRequest addonRequest : orderItem.getOrderAddons()) {
            OrderAddon currentOrderAddon = currentOrderItem.getOrderAddons().stream()
                    .filter(addon -> Objects.equals(addon.getId(), addonRequest.getOrderAddonId()))
                    .findFirst().orElse(null);

            log.info("Addon id : {}",  addonRequest.getOrderAddonId());
            AddOn addOn = addonService.getAddOnById(addonRequest.getAddonId());

            if (currentOrderAddon == null) {
                OrderAddon orderAddon = buildOrderAddon(addonRequest, addOn);
                totalAddonPriceForItem += orderAddon.getPrice();
                currentOrderItem.addOrderAddon(orderAddon);
            } else {
                currentOrderAddon.setQuantity(addonRequest.getQuantity());
                currentOrderAddon.setPrice(addonRequest.getQuantity() * addOn.getPrice());
                totalAddonPriceForItem += currentOrderAddon.getPrice();
            }
        }

        currentOrderItem.setTotalAddonPrice(totalAddonPriceForItem);
        currentOrderItem.setTotalPrice(totalAddonPriceForItem + currentOrderItem.getTotalItemPrice());
    }

    private void processAddons(OrderItemRequest orderItemRequest, OrderItem orderItem) {
        double totalAddonPrice = orderItemRequest.getOrderAddons()
                .stream()
                .mapToDouble(addonRequest -> {
                    OrderAddon orderAddon = createOrderAddon(addonRequest);
                    orderItem.addOrderAddon(orderAddon);
                    return orderAddon.getPrice();
                })
                .sum();

        orderItem.setTotalAddonPrice(totalAddonPrice);
        orderItem.setTotalPrice(totalAddonPrice + orderItem.getTotalItemPrice());
    }

    private OrderAddon createOrderAddon(OrderAddonRequest addonRequest) {
        AddOn addOn = addonService.getAddOnById(addonRequest.getAddonId());
        return buildOrderAddon(addonRequest, addOn);
    }

    private OrderAddon buildOrderAddon(OrderAddonRequest addonRequest, AddOn addOn) {
        return OrderAddon.builder()
                .addOn(addOn)
                .quantity(addonRequest.getQuantity())
                .price(addonRequest.getQuantity() * addOn.getPrice())
                .build();
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

    private OrderItem findCurrentOrderItem(Order currentOrder, Long orderItemId) {
        return currentOrder.getOrderItems()
                .stream().filter(item -> Objects.equals(item.getId(), orderItemId))
                .findFirst().orElse(null);
    }


    private void updateExistingOrderItem(OrderItem currentOrderItem, OrderItemRequest orderItem, Item item) {
        double currentItemTotalPrice = orderItem.getQuantity() * item.getPrice();
        currentOrderItem.setQuantity(orderItem.getQuantity());
        currentOrderItem.setTotalItemPrice(currentItemTotalPrice);

        if (orderItem.getOrderAddons() != null) {
            removeAddonsFromOrderItem(currentOrderItem, orderItem.getOrderAddons());
            processUpdateAddons(orderItem, currentOrderItem);
        } else if (orderItem.getOrderAddons() == null && currentOrderItem.getOrderAddons() != null) {
            currentOrderItem.setTotalAddonPrice(0.0);
            currentOrderItem.setTotalPrice(currentItemTotalPrice);
            currentOrderItem.getOrderAddons().clear();
        }
    }

    private Item resolveItem(OrderItemRequest orderItemRequest, Item requestItem) {
        return requestItem != null ? requestItem : itemService.getItemById(orderItemRequest.getItemId());
    }
    private List<OrderItemResponse> getOrderItemList(Set<OrderItem> orderItemList) {
        List<OrderItemResponse> orderItemResponseList = new ArrayList<>();
        orderItemList.forEach(orderItem -> orderItemResponseList.add(mapToOrderItemResponse(orderItem)));
        return orderItemResponseList;
    }

    private List<OrderAddonResponse> getOrderAddonList(Set<OrderAddon> orderAddonList) {
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
                .addonId(orderAddon.getAddOn().getId())
                .addonName(orderAddon.getAddOn().getName())
                .addonUnitPrice(orderAddon.getAddOn().getPrice())
                .quantity(orderAddon.getQuantity())
                .totalPrice(orderAddon.getPrice())
                .build();
    }

    private void removeItemsFromOrder(Order currentOrder, OrderRequest orderRequest) {
        Set<Long> currentOrderItemIds = currentOrder.getOrderItems()
                .stream().map(OrderItem::getId).collect(Collectors.toSet());

        Set<Long> currentOrderRequestItemIds = orderRequest.getOrderItems()
                .stream().map(OrderItemRequest::getOrderItemId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<Long> itemIdsNeedToDelete = new HashSet<>(currentOrderItemIds);
        itemIdsNeedToDelete.removeAll(currentOrderRequestItemIds);

        List<OrderItem> itemsNeedToDelete = new ArrayList<>();
        for (Long id : itemIdsNeedToDelete) {
            currentOrder.getOrderItems().stream()
                    .filter(orderItem -> orderItem.getId().equals(id))
                    .findFirst()
                    .ifPresent(itemsNeedToDelete::add);
        }

        currentOrder.removeOrderItem(itemsNeedToDelete);
    }

    private void removeAddonsFromOrderItem(OrderItem currentOrderItem, Set<OrderAddonRequest> orderAddonRequest) {
        Set<Long> currentOrderItemAddonIds = currentOrderItem.getOrderAddons()
                .stream().map(OrderAddon::getId)
                .collect(Collectors.toSet());

        if (currentOrderItemAddonIds.isEmpty())
            return;

        Set<Long> currentOrderItemAddonRequestIds = orderAddonRequest.stream()
                .map(OrderAddonRequest::getOrderAddonId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<Long> addonIdsToDelete = new HashSet<>(currentOrderItemAddonIds);
        addonIdsToDelete.removeAll(currentOrderItemAddonRequestIds);

        List<OrderAddon> addonsNeedToDelete = new ArrayList<>();
        for (Long id : addonIdsToDelete) {
            currentOrderItem.getOrderAddons().stream()
                    .filter(orderAddon -> orderAddon.getId().equals(id))
                    .findFirst()
                    .ifPresent(addonsNeedToDelete::add);
        }

        currentOrderItem.removeOrderAddon(addonsNeedToDelete);

    }

    public List<OrderResponse> getAllPendingOrders() {
        List<Order> allOrders =  orderRepository.findAllByOrderStatus(OrderStatus.PLACED.getStatus());
        return allOrders.stream().map(this::mapToOrderResponse).collect(Collectors.toList());
    }

    private void cacheOrderDetails(Order order) {
        OrderSummary orderSummary = getOrderSummary(order);

        redisTemplate.opsForList().leftPush(ConstantKeys.ORDERS_KEY, orderSummary);
    }

    private OrderSummary getOrderSummary(Order order) {
        return OrderSummary.builder()
                .orderId(order.getId())
                .userId(order.getUserId())
                .totalPrice(order.getTotalPrice())
                .orderStatus(order.getOrderStatus())
                .orderTime(order.getOrderTime())
                .orderItemSummaryList(getOrderItemSummeryList(order.getOrderItems()))
                .build();
    }
    private List<OrderItemSummary> getOrderItemSummeryList(Set<OrderItem> orderItems) {
        return orderItems
                .stream()
                .map(orderItem -> OrderItemSummary
                        .builder()
                        .orderItemId(orderItem.getId())
                        .itemId(orderItem.getItem().getId())
                        .itemName(orderItem.getItem().getName())
                        .quantity(orderItem.getQuantity())
                        .orderAddonSummaryList(getOrderAddonSummeryList(orderItem.getOrderAddons()))
                        .build())
                .toList();
    }

    private List<OrderAddonSummary> getOrderAddonSummeryList(Set<OrderAddon> orderAddons) {
        return orderAddons.stream().map(orderAddon -> OrderAddonSummary
                .builder()
                .orderAddonId(orderAddon.getId())
                .addonId(orderAddon.getAddOn().getId())
                .addonName(orderAddon.getAddOn().getName())
                .quantity(orderAddon.getQuantity())
                .build()).toList();
    }

    public List<OrderSummary> getAllOrdersSummary(Long page, Long size) {
        List<Object> summaries = redisTemplate.opsForList().range(ConstantKeys.ORDERS_KEY, page * size, (page + 1) * size - 1);
        if (summaries != null) {
            return summaries
                    .stream()
                    .map (obj -> objectMapper.convertValue(obj, OrderSummary.class))
                    .toList();
        }

        return Collections.emptyList();
    }
}
