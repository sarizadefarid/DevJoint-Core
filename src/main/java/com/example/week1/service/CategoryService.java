package com.example.week1.service;

import com.example.week1.dto.category.CategoryRequest;
import com.example.week1.dto.category.CategoryResponse;
import com.example.week1.entity.Category;
import com.example.week1.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponse create(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new ResponseStatusException(CONFLICT, "Category with name '" + request.getName() + "' already exists");
        }
        Category category = new Category();
        applyRequest(category, request);
        return toResponse(categoryRepository.save(category));
    }

    public Page<CategoryResponse> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(this::toResponse);
    }

    public CategoryResponse findById(Long id) {
        return toResponse(getEntity(id));
    }

    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = getEntity(id);

        if (categoryRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new ResponseStatusException(CONFLICT, "Category with name '" + request.getName() + "' already exists");
        }

        applyRequest(category, request);
        return toResponse(categoryRepository.save(category));
    }

    public void delete(Long id) {
        Category category = getEntity(id);
        categoryRepository.delete(category);
    }

    public Category getEntity(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Category not found with id: " + id));
    }

    private void applyRequest(Category category, CategoryRequest request) {
        category.setName(request.getName());
    }

    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName());
    }
}