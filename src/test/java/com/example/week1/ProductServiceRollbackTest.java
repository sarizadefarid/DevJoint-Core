package com.example.week1;

import com.example.week1.dto.product.ProductRequest;
import com.example.week1.entity.Category;
import com.example.week1.repository.CategoryRepository;
import com.example.week1.repository.ProductRepository;
import com.example.week1.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ProductServiceRollbackTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Long existingCategoryId;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();

        Category category = new Category("Electronics");
        Category savedCategory = categoryRepository.save(category);
        existingCategoryId = savedCategory.getId();
    }

    @Test
    void shouldRollbackTransactionWhenExceptionOccurs() {
        long initialProductCount = productRepository.count();

        ProductRequest invalidRequest = new ProductRequest();
        invalidRequest.setName("Laptop");
        invalidRequest.setPrice(100.0);
        invalidRequest.setCategoryId(null);

        assertThrows(RuntimeException.class, () -> {
            productService.create(invalidRequest);
        });

        long finalProductCount = productRepository.count();
        assertEquals(initialProductCount, finalProductCount, "Tranzaksiya rollback olunmalı və bazaya məhsul yazılmamalıdır");
    }

    @Test
    void shouldRollbackWhenCategoryNotFound() {
        long initialProductCount = productRepository.count();

        ProductRequest requestWithNonExistingCategory = new ProductRequest();
        requestWithNonExistingCategory.setName("Laptop");
        requestWithNonExistingCategory.setPrice(1500.0);
        requestWithNonExistingCategory.setCategoryId(999L);

        assertThrows(RuntimeException.class, () -> {
            productService.create(requestWithNonExistingCategory);
        });

        assertEquals(initialProductCount, productRepository.count(), "Kateqoriya tapılmadıqda tranzaksiya rollback olunmalıdır");
    }
}