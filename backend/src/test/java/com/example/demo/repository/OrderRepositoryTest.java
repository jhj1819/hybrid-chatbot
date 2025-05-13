package com.example.demo.repository;

import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void testCreateAndFindOrder() {
        OrderItem item = OrderItem.builder()
                .productId("testProductId")
                .productName("테스트 상품")
                .quantity(2)
                .unitPrice(new BigDecimal("15000"))
                .build();

        Order order = Order.builder()
                .userId("testUserId")
                .orderItems(Collections.singletonList(item))
                .totalAmount(new BigDecimal("30000"))
                .status("paid")
                .shippingAddressId("testAddressId")
                .orderedAt(LocalDateTime.now())
                .paymentMethod("card")
                .build();

        orderRepository.save(order);

        List<Order> orders = orderRepository.findByUserId("testUserId");
        assertThat(orders).isNotEmpty();
        assertThat(orders.get(0).getTotalAmount()).isEqualTo(new BigDecimal("30000"));
    }
}