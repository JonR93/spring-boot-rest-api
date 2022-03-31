package com.springboot.rest.api.server.repository;

import com.springboot.rest.api.server.entity.Category;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    private Category category1;
    private Category category2;

    @BeforeEach
    void setup(){
        category1 = Category.builder()
                .name("Something")
                .build();

        category2 = Category.builder()
                .name("Something Else")
                .build();
    }

    @Test
    void saveCategory(){
        Category savedCategory = categoryRepository.save(category1);
        Assertions.assertThat(savedCategory).isNotNull();
        Assertions.assertThat(savedCategory.getId()).isPositive();
    }

    @Test
    void findByName() {
        categoryRepository.save(category1);
        Category foundCategory = categoryRepository.findByName(category1.getName()).orElse(null);
        Assertions.assertThat(foundCategory).isNotNull();
        Assertions.assertThat(foundCategory.getName()).isEqualTo(category1.getName());
    }

    @Test
    void findAll(){
        categoryRepository.save(category1);
        categoryRepository.save(category2);
        List<Category> foundCategories = categoryRepository.findAll();
        Assertions.assertThat(foundCategories).hasSize(2);
    }
}
