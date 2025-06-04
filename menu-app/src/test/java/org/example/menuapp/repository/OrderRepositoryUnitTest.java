package org.example.menuapp.repository;

import org.example.menuapp.entity.Item;
import org.example.menuapp.entity.Order;
import org.example.menuapp.entity.OrderItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderRepositoryUnitTest {

    @Container
    static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("postgres")
            .withPassword("root");


    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void givenNewOrder_whenSaveOrder_thenSuccess() {

        Item item = new Item();
        item.setName("Burger");
        item.setPrice(10.0);
        item = itemRepository.save(item);

        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setQuantity(2);
        orderItem.setTotalItemPrice(20.0);
        orderItem.setTotalAddonPrice(2.0);
        orderItem.setTotalPrice(22.0);

        Order order = new Order();
        order.setUserId(1L);
        order.setOrderStatus("order placed");
        order.setTotalPrice(100.00);
        order.setTotalItemPrice(75.00);
        order.setTotalAddonPrice(25.00);
        order.setOrderItems(Set.of(orderItem));

        Order savedOrder = orderRepository.save(order);

        assertThat(savedOrder.getId()).isNotNull();
      //  assertThat(savedOrder.getId()).isEqualTo(1L) ;
    }


}
