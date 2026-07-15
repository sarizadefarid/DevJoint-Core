package com.example.week1.service;

import com.example.week1.Category;
import com.example.week1.dto.category.CategoryRequest;
import com.example.week1.dto.category.CategoryResponse;
import com.example.week1.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponse create(CategoryRequest request) {
        categoryRepository.findByNameIgnoreCase(request.name())
                .ifPresent(category -> {
                    throw new ResponseStatusException(CONFLICT, "Category already exists");
                });

        Category category = new Category();
        category.setName(request.name());
        return toResponse(categoryRepository.save(category));
    }

    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public CategoryResponse findById(Long id) {
        return toResponse(getEntity(id));
    }

    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = getEntity(id);
        category.setName(request.name());
        return toResponse(categoryRepository.save(category));
    }

    public void delete(Long id) {
        Category category = getEntity(id);
        categoryRepository.delete(category);
    }

    public Category getEntity(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Category not found"));
    }

    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName());
    }
}