package com.example.week1;

import com.example.week1.dto.product.ProductRequest;
import com.example.week1.dto.product.ProductResponse;
import com.example.week1.entity.Category;
import com.example.week1.entity.Product;
import com.example.week1.repository.ProductRepository;
import com.example.week1.service.ProductService;
import com.example.week1.service.CategoryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductService productService;

    private Category category;
    private Product product;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setPrice(1500.0);
        product.setCategory(category);
    }

    private ProductRequest buildRequest(String name, Double price, Long categoryId) {
        ProductRequest request = new ProductRequest();
        request.setName(name);
        request.setPrice(price);
        request.setCategoryId(categoryId);
        return request;
    }

    @Test
    void create_shouldSaveAndReturnProduct() {
        ProductRequest request = buildRequest("Laptop", 1500.0, 1L);

        // getCategoryById ƏVƏZİNƏ getEntity mock-lanır:
        when(categoryService.getEntity(1L)).thenReturn(category);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponse response = productService.create(request);

        assertNotNull(response);
        assertEquals("Laptop", response.getName());
        assertEquals(1500.0, response.getPrice());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void findById_shouldReturnProduct_whenExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponse response = productService.findById(1L);

        assertNotNull(response);
        assertEquals("Laptop", response.getName());
    }

    @Test
    void findById_shouldThrowException_whenNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> productService.findById(99L)
        );

        assertEquals(404, exception.getStatusCode().value());
    }

    @Test
    void delete_shouldRemoveProduct_whenExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).delete(product);

        productService.delete(1L);

        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void update_shouldModifyAndReturnProduct() {
        ProductRequest request = buildRequest("Laptop Pro", 1800.0, 1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        // getCategoryById ƏVƏZİNƏ getEntity mock-lanır:
        when(categoryService.getEntity(1L)).thenReturn(category);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponse response = productService.update(1L, request);

        assertNotNull(response);
        verify(productRepository, times(1)).save(any(Product.class));
    }
}