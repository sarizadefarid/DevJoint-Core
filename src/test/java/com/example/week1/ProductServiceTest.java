package com.example.week1;

import com.example.week1.dto.product.ProductRequest;
import com.example.week1.dto.product.ProductResponse;
import com.example.week1.entity.Category;
import com.example.week1.entity.Product;
import com.example.week1.repository.CategoryRepository;
import com.example.week1.repository.ProductRepository;
import com.example.week1.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    private Category category;
    private Product product;
    private ProductRequest request;

    @BeforeEach
    void setUp() {
        category = new Category("Electronics");
        
        product = new Product();
        product.setTitle("Laptop");
        product.setPrice(1200.0);
        product.setCategory(category);

        request = new ProductRequest();
        request.setName("Laptop");
        request.setPrice(1200.0);
        request.setCategoryId(1L);
    }

    @Test
    void createProduct_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponse created = productService.create(request);

        assertNotNull(created);
        assertEquals("Laptop", created.getName());
        assertEquals(1200.0, created.getPrice());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void getProductById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponse found = productService.findById(1L);

        assertNotNull(found);
        assertEquals("Laptop", found.getName());
    }

    @Test
    void createProduct_ThrowsException_WhenCategoryNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            productService.create(request);
        });
    }
}