package com.example.demo.repository;

import com.example.demo.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testCreateAndFindProduct() {
        Product product = Product.builder()
                .name("테스트 상품")
                .description("상품 설명")
                .price(new BigDecimal("29900"))
                .stock(10)
                .createdBy("testUserId")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        productRepository.save(product);

        List<Product> products = productRepository.findByCreatedBy("testUserId");
        assertThat(products).isNotEmpty();
        assertThat(products.get(0).getName()).isEqualTo("테스트 상품");
    }
}