package com.example.week1;

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
        category.setId(1L);
        category.setName("Electronics");
    }

    @Test
    void createCategory_shouldSaveAndReturnCategory() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category result = categoryService.createCategory(category);

        assertNotNull(result);
        assertEquals("Electronics", result.getName());
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void getCategoryById_shouldReturnCategory_whenExists() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Category result = categoryService.getCategoryById(1L);

        assertNotNull(result);
        assertEquals("Electronics", result.getName());
    }

    @Test
    void getCategoryById_shouldThrowException_whenNotFound() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> categoryService.getCategoryById(99L)
        );

        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void deleteCategory_shouldRemoveCategory_whenExists() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).delete(category);

        categoryService.deleteCategory(1L);

        verify(categoryRepository, times(1)).delete(category);
    }

    @Test
    void updateCategory_shouldModifyAndReturnCategory() {
        Category updated = new Category();
        updated.setName("Home Appliances");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category result = categoryService.updateCategory(1L, updated);

        assertNotNull(result);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }
}