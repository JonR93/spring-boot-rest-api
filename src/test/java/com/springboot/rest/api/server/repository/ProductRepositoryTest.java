package com.springboot.rest.api.server.repository;

import com.springboot.rest.api.server.entity.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setup(){
        product1 = Product.builder()
                .name("Something")
                .description("Testing")
                .build();

        product2 = Product.builder()
                .name("Something Else")
                .description("Testing")
                .build();
    }

    @Test
    void saveProduct(){
        Product savedProduct = productRepository.save(product1);
        Assertions.assertThat(savedProduct).isNotNull();
        Assertions.assertThat(savedProduct.getId()).isPositive();
    }

    @Test
    void findByName() {
        productRepository.save(product1);
        Product foundProduct = productRepository.findByName(product1.getName()).orElse(null);
        Assertions.assertThat(foundProduct).isNotNull();
        Assertions.assertThat(foundProduct.getName()).isEqualTo(product1.getName());
    }

    @Test
    void findAll(){
        productRepository.save(product1);
        productRepository.save(product2);
        List<Product> foundProducts = productRepository.findAll();
        Assertions.assertThat(foundProducts).hasSize(2);
    }
}
