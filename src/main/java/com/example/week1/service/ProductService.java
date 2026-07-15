package com.example.week1.service;

import com.example.week1.Category;
import com.example.week1.Product;
import com.example.week1.dto.product.ProductRequest;
import com.example.week1.dto.product.ProductResponse;
import com.example.week1.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public ProductService(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    public ProductResponse create(ProductRequest request) {
        Product product = new Product();
        applyRequest(product, request);
        return toResponse(productRepository.save(product));
    }

    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public ProductResponse findById(Long id) {
        return toResponse(getEntity(id));
    }

    public ProductResponse update(Long id, ProductRequest request) {
        Product product = getEntity(id);
        applyRequest(product, request);
        return toResponse(productRepository.save(product));
    }

    public void delete(Long id) {
        Product product = getEntity(id);
        productRepository.delete(product);
    }

    private Product getEntity(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Product not found"));
    }

    private void applyRequest(Product product, ProductRequest request) {
        Category category = categoryService.getEntity(request.categoryId());
        product.setName(request.name());
        product.setPrice(request.price());
        product.setCategory(category);
    }

    private ProductResponse toResponse(Product product) {
        Category category = product.getCategory();
        Long categoryId = category != null ? category.getId() : null;
        String categoryName = category != null ? category.getName() : null;
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), categoryId, categoryName);
    }
}