package com.example.week1;

import com.example.week1.dto.category.CategoryRequest; // layihənizdəki DTO paketi
import com.example.week1.dto.category.CategoryResponse;
import com.example.week1.entity.Category;
import com.example.week1.repository.CategoryRepository;
import com.example.week1.service.CategoryService;
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
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setName("Electronics");
    }

    @Test
    void createCategory_Success() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        // Əgər create metodu CategoryRequest qəbul edirsə:
        CategoryRequest request = new CategoryRequest();
        request.setName("Electronics");

        var created = categoryService.create(request);

        assertNotNull(created);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void getCategoryById_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        var found = categoryService.findById(1L);

        assertNotNull(found);
    }
}