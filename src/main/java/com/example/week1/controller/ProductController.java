package com.example.week1.controller;

import com.example.week1.dto.product.ProductRequest;
import com.example.week1.dto.product.ProductResponse;
import com.example.week1.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(@Valid @RequestBody ProductRequest request) {
        return productService.create(request);
    }

    @GetMapping
    public Page<ProductResponse> findAll(Pageable pageable) {
        return productService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ProductResponse findById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        return productService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }

    @GetMapping("/search")
    public List<ProductResponse> searchProducts(
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "0") Double minPrice,
            @RequestParam(required = false, defaultValue = "999999") Double maxPrice) {
        return productService.searchProducts(name, minPrice, maxPrice);
    }

    @GetMapping("/filter")
    public List<ProductResponse> filterByCategory(
            @RequestParam String categoryName,
            @RequestParam Double maxPrice) {
        return productService.filterByCategoryAndPrice(categoryName, maxPrice);
    }

    @GetMapping("/dynamic-search")
    public List<ProductResponse> dynamicSearch(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Long categoryId) {
        return productService.getDynamicProducts(title, minPrice, maxPrice, categoryId);
    }

    // N+1 Query Fix 1: JOIN FETCH endpoint
    @GetMapping("/with-category-fetch")
    public List<ProductResponse> findAllWithCategoryFetch() {
        return productService.findAllWithCategoryFetch();
    }

    // N+1 Query Fix 2: @EntityGraph endpoint
    @GetMapping("/with-category-graph")
    public List<ProductResponse> findAllWithCategoryEntityGraph() {
        return productService.findAllWithCategoryEntityGraph();
    }
}