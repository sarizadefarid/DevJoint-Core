package com.example.week1;

import com.example.week1.dto.category.CategoryRequest;
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
import org.springframework.web.server.ResponseStatusException;

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
    private CategoryRequest categoryRequest;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        categoryRequest = new CategoryRequest();
        categoryRequest.setName("Electronics");
    }

    @Test
    void create_shouldSaveAndReturnCategoryResponse() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryResponse result = categoryService.create(categoryRequest);

        assertNotNull(result);
        assertEquals("Electronics", result.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void findById_shouldReturnCategoryResponse_whenExists() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        CategoryResponse result = categoryService.findById(1L);

        assertNotNull(result);
        assertEquals("Electronics", result.getName());
    }

    @Test
    void findById_shouldThrowResponseStatusException_whenNotFound() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> categoryService.findById(99L)
        );

        assertTrue(exception.getReason().contains("Category not found"));
    }

    @Test
    void delete_shouldRemoveCategory_whenExists() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).delete(category);

        categoryService.delete(1L);

        verify(categoryRepository, times(1)).delete(category);
    }

    @Test
    void update_shouldModifyAndReturnCategoryResponse() {
        CategoryRequest updateRequest = new CategoryRequest();
        updateRequest.setName("Home Appliances");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryResponse result = categoryService.update(1L, updateRequest);

        assertNotNull(result);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }
}