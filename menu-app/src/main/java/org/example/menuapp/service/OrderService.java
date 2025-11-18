package org.example.menuapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.menuapp.config.ConstantKeys;
import org.example.menuapp.dto.redis.OrderAddonSummary;
import org.example.menuapp.dto.redis.OrderItemSummary;
import org.example.menuapp.dto.redis.OrderSummary;
import org.example.menuapp.dto.request.OrderAddonRequest;
import org.example.menuapp.dto.request.OrderItemRequest;
import org.example.menuapp.dto.request.OrderRequest;
import org.example.menuapp.dto.request.OrderStatusUpdateRequest;
import org.example.menuapp.dto.response.OrderAddonResponse;
import org.example.menuapp.dto.response.OrderItemResponse;
import org.example.menuapp.dto.response.OrderResponse;
import org.example.menuapp.entity.Addon;
import org.example.menuapp.entity.Item;
import org.example.menuapp.entity.Order;
import org.example.menuapp.entity.OrderAddon;
import org.example.menuapp.entity.OrderItem;
import org.example.menuapp.enums.OrderStatus;
import org.example.menuapp.error.custom_exceptions.OrderDeletionException;
import org.example.menuapp.error.custom_exceptions.SmResourceNotFoundException;
import org.example.menuapp.error.custom_exceptions.SmUpdateNotAllowedException;
import org.example.menuapp.error.messages.ExceptionMessages;
import org.example.menuapp.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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
    public void placeOrder(OrderRequest orderRequest) {
        Set<Item> itemSet = fetchAndValidateItems(orderRequest);
        Set<Addon> addonSet = fetchAndValidateAddons(orderRequest);
        Order order = getOrder(orderRequest);
        Set<OrderItem> orderItems = orderRequest.orderItems().stream()
                .map(orderItemRequest -> createOrderItem(orderItemRequest, itemSet, addonSet))
                .collect(Collectors.toSet());

        orderItems.forEach(order::addOrderItem);
        calculateAndSetTotals(order, orderItems);
        orderRepository.save(order);
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

    @Transactional
    public void updateOrderStatus(Long orderId, OrderStatusUpdateRequest orderStatusUpdateRequest) {
        Order order = getOrderById(orderId);
        order.setOrderStatus(orderStatusUpdateRequest.status().getStatus());
        order.setUpdateTime(LocalDateTime.now());
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
        order.setUserId(orderRequest.userId());
        order.setOrderStatus(OrderStatus.PLACED.getStatus());
        order.setIsServed(false);
        order.setIsPaid(false);
        order.setTableNo(orderRequest.tableNumber());
        order.setOrderTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        return order;
    }


    private OrderItem createOrderItem(OrderItemRequest orderItemRequest, Set<Item> itemSet, Set<Addon> addonSet) {
        Item currentItem = itemSet.stream()
                .filter(item -> Objects.equals(item.getId(), orderItemRequest.itemId()))
                .findFirst().orElseThrow(() -> new SmResourceNotFoundException("Item not found"));

        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(orderItemRequest.quantity());
        orderItem.setItemId(orderItemRequest.itemId());
        orderItem.setTotalItemPrice(orderItemRequest.quantity() * currentItem.getPrice());

        if (orderItemRequest.orderAddons() != null && !orderItemRequest.orderAddons().isEmpty()) {
            processAddons(orderItem, addonSet, orderItemRequest.orderAddons());
        } else {
            orderItem.setTotalAddonPrice(0.0);
            orderItem.setTotalPrice(orderItem.getTotalItemPrice());
        }

        return orderItem;
    }

    private Set<Item> fetchAndValidateItems(OrderRequest orderRequest) {
        Set<Long> orderItemIds = orderRequest.orderItems()
                .stream()
                .map(OrderItemRequest::itemId)
                .collect(Collectors.toSet());

        Set<Item> itemSet = itemService.getAllItemsByIds(orderItemIds);

        if (itemSet.size() != orderItemIds.size()) {
            throw new SmResourceNotFoundException(ExceptionMessages.SOME_ITEMS_ARE_NOT_FOUND);
        }

        return itemSet;
    }

    private Set<Addon> fetchAndValidateAddons(OrderRequest orderRequest) {
        Set<Long> orderAddonIds = orderRequest.orderItems()
                .stream()
                .filter(orderItem -> orderItem.orderAddons() != null)
                .flatMap(orderItem -> orderItem.orderAddons().stream())
                .map(OrderAddonRequest::addonId)
                .collect(Collectors.toSet());

        Set<Addon> addonSet = addonService.getAllAddonsByIds(orderAddonIds);

        if (addonSet.size() != orderAddonIds.size()) {
            throw new SmResourceNotFoundException(ExceptionMessages.SOME_ADDONS_ARE_NOT_FOUND);
        }

        return addonSet;
    }

    private void calculateAndSetTotals(Order order, Set<OrderItem> orderItems) {
        double totalItemPrice = orderItems.stream()
                .mapToDouble(OrderItem::getTotalItemPrice)
                .sum();
        double totalAddonPrice = orderItems.stream()
                .mapToDouble(OrderItem::getTotalAddonPrice)
                .sum();

        order.setTotalItemPrice(totalItemPrice);
        order.setTotalAddonPrice(totalAddonPrice);
        order.setTotalPrice(totalItemPrice + totalAddonPrice);
    }


    private Order updateOrderItem(Order currentOrder, OrderRequest orderRequest) {
        // currentOrder.setTableNo(orderRequest.getTableNumber());
        double totalItemPrice = 0.0;
        double totalAddonPrice = 0.0;

        for (OrderItemRequest orderItem : orderRequest.orderItems()) {
            OrderItem currentOrderItem = findCurrentOrderItem(currentOrder, orderItem.itemId());
            Item item = itemService.getItemById(orderItem.itemId());

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

    private OrderItem getOrderItem(OrderItemRequest orderItemRequest, Item requestItem) {
        // todo
        //  : need to get all the items at a time
        Item item = resolveItem(orderItemRequest, requestItem);
        OrderItem orderItem = new OrderItem();
        //   orderItem.setItem(item);
        // orderItem.setQuantity(orderItemRequest.getQuantity());
        //  orderItem.setTotalItemPrice(orderItemRequest.getQuantity() * item.getPrice());
//
//        if (orderItemRequest.getOrderAddons() != null) {
//           processAddons(orderItemRequest, orderItem);
//        } else {
//            orderItem.setTotalAddonPrice(0.0);
//            orderItem.setTotalPrice(orderItem.getTotalItemPrice());
//        }

        return orderItem;
    }

    private void processUpdateAddons(OrderItemRequest orderItem, OrderItem currentOrderItem) {
        double totalAddonPriceForItem = 0.0;
//        for (OrderAddonRequest addonRequest : orderItem.getOrderAddons()) {
//            OrderAddon currentOrderAddon = currentOrderItem.getOrderAddons().stream()
//                    .filter(addon -> Objects.equals(addon.getId(), addonRequest.getOrderAddonId()))
//                    .findFirst().orElse(null);
//
//            log.info("Addon id : {}",  addonRequest.getOrderAddonId());
//            Addon addOn = addonService.getAddOnById(addonRequest.getAddonId());
//
//            if (currentOrderAddon == null) {
//                OrderAddon orderAddon = buildOrderAddon(addonRequest, addOn);
//                totalAddonPriceForItem += orderAddon.getPrice();
//                currentOrderItem.addOrderAddon(orderAddon);
//            } else {
//                currentOrderAddon.setQuantity(addonRequest.getQuantity());
//                currentOrderAddon.setPrice(addonRequest.getQuantity() * addOn.getPrice());
//                totalAddonPriceForItem += currentOrderAddon.getPrice();
//            }
//        }

        currentOrderItem.setTotalAddonPrice(totalAddonPriceForItem);
        currentOrderItem.setTotalPrice(totalAddonPriceForItem + currentOrderItem.getTotalItemPrice());
    }

    private void processAddons(OrderItem orderItem, Set<Addon> addonSet, Set<OrderAddonRequest> addonRequests) {
        double totalAddonPrice = addonRequests
                .stream()
                .mapToDouble(addonRequest -> {

                    Addon currentAddon = addonSet.stream()
                            .filter(addon -> Objects.equals(addon.getId(), addonRequest.addonId()))
                            .findFirst()
                            .orElseThrow(() -> new SmResourceNotFoundException("Addon not found"));
                    double addonPrice = currentAddon.getPrice() * addonRequest.quantity();

                    OrderAddon orderAddon = new OrderAddon();
                    orderAddon.setAddonId(addonRequest.addonId());
                    orderAddon.setQuantity(addonRequest.quantity());
                    orderAddon.setPrice(addonPrice);

                    orderItem.addOrderAddon(orderAddon);
                    return addonPrice;
                })
                .sum();

        orderItem.setTotalAddonPrice(totalAddonPrice);
        orderItem.setTotalPrice(totalAddonPrice + orderItem.getTotalItemPrice());
    }

    private OrderAddon createOrderAddon(OrderAddonRequest addonRequest) {
        Addon addOn = addonService.getAddOnById(addonRequest.addonId());
        return buildOrderAddon(addonRequest, addOn);
    }

    private OrderAddon buildOrderAddon(OrderAddonRequest addonRequest, Addon addOn) {
        return null;
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
//        double currentItemTotalPrice = orderItem.getQuantity() * item.getPrice();
//        currentOrderItem.setQuantity(orderItem.getQuantity());
//        currentOrderItem.setTotalItemPrice(currentItemTotalPrice);
//
//        if (orderItem.getOrderAddons() != null) {
//            removeAddonsFromOrderItem(currentOrderItem, orderItem.getOrderAddons());
//            processUpdateAddons(orderItem, currentOrderItem);
//        } else if (orderItem.getOrderAddons() == null && currentOrderItem.getOrderAddons() != null) {
//            currentOrderItem.setTotalAddonPrice(0.0);
//            currentOrderItem.setTotalPrice(currentItemTotalPrice);
//            currentOrderItem.getOrderAddons().clear();
//        }
    }

    private Item resolveItem(OrderItemRequest orderItemRequest, Item requestItem) {
        return requestItem != null ? requestItem : itemService.getItemById(orderItemRequest.itemId());
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
                //  .itemId(orderItem.getItem().getId())
                //   .itemName(orderItem.getItem().getName())
                //   .itemUnitPrice(orderItem.getItem().getPrice())
                .quantity(orderItem.getQuantity())
                .totalPrice(orderItem.getTotalPrice())
                .orderAddons(getOrderAddonList(orderItem.getOrderAddons()))
                .build();
    }

    private OrderAddonResponse mapToOrderAddonResponse(OrderAddon orderAddon) {
        return OrderAddonResponse.builder()
                .orderAddonId(orderAddon.getId())
                .orderItemId(orderAddon.getOrderItem().getId())
                //  .addonId(orderAddon.getAddOn().getId())
                // .addonName(orderAddon.getAddOn().getName())
                //  .addonUnitPrice(orderAddon.getAddOn().getPrice())
                .quantity(orderAddon.getQuantity())
                .totalPrice(orderAddon.getPrice())
                .build();
    }

    private void removeItemsFromOrder(Order currentOrder, OrderRequest orderRequest) {
        Set<Long> currentOrderItemIds = currentOrder.getOrderItems()
                .stream().map(OrderItem::getId).collect(Collectors.toSet());

        Set<Long> currentOrderRequestItemIds = orderRequest.orderItems()
                .stream().map(OrderItemRequest::itemId)
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
                .map(OrderAddonRequest::addonId)
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

    public List<OrderResponse> getAllOrdersByStatus(OrderStatus orderStatus, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("orderTime").descending());
        Page<Order> allOrders = orderRepository.findAllByOrderStatus(orderStatus.getStatus(), pageable);
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
                        //  .itemId(orderItem.getItem().getId())
                        //  .itemName(orderItem.getItem().getName())
                        .quantity(orderItem.getQuantity())
                        .orderAddonSummaryList(getOrderAddonSummeryList(orderItem.getOrderAddons()))
                        .build())
                .toList();
    }

    private List<OrderAddonSummary> getOrderAddonSummeryList(Set<OrderAddon> orderAddons) {
        return orderAddons.stream().map(orderAddon -> OrderAddonSummary
                .builder()
                .orderAddonId(orderAddon.getId())
                //   .addonId(orderAddon.getAddOn().getId())
                //  .addonName(orderAddon.getAddOn().getName())
                .quantity(orderAddon.getQuantity())
                .build()).toList();
    }

    public List<OrderSummary> getAllOrdersSummary(Long page, Long size) {
        List<Object> summaries = redisTemplate.opsForList().range(ConstantKeys.ORDERS_KEY, page * size, (page + 1) * size - 1);
        if (summaries != null) {
            return summaries
                    .stream()
                    .map(obj -> objectMapper.convertValue(obj, OrderSummary.class))
                    .toList();
        }

        return Collections.emptyList();
    }

    public List<OrderSummary> getAllOrdersOfCustomer(Long customerId, OrderStatus orderStatus, Integer page, Integer size) {
        Pageable pageable = getOrderPageable(page, size);
        Page<Order> customerOrders = orderRepository.findAllByUserIdAndOrderStatus(customerId, orderStatus.getStatus(), pageable);
        return customerOrders.stream().map(this::getOrderSummary).toList();
    }

    private Pageable getOrderPageable(Integer page, Integer size) {
        return PageRequest.of(page, size, Sort.by("orderTime").descending());
    }
}
