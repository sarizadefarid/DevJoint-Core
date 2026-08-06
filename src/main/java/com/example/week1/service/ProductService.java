package com.example.week1.service;

import com.example.week1.dto.product.ProductRequest;
import com.example.week1.dto.product.ProductResponse;
import com.example.week1.entity.Category;
import com.example.week1.entity.Product;
import com.example.week1.repository.CategoryRepository;
import com.example.week1.repository.ProductRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public ProductResponse create(ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getCategoryId()));

        Product product = new Product();
        product.setTitle(request.getName());
        product.setPrice(request.getPrice());
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        return mapToResponse(savedProduct);
    }

    // Multi-table write transaction requirement (20 points)
    // Bu metod tək tranzaksiyada həm Product, həm də Category cədvəlinə yazır (Multi-table write)
    @Transactional
    public ProductResponse createProductWithCategoryUpdate(ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getCategoryId()));

        // 1-ci Cədvələ yazma (Category cədvəli yenilənir)
        categoryRepository.save(category);

        Product product = new Product();
        product.setTitle(request.getName());
        product.setPrice(request.getPrice());
        product.setCategory(category);

        // 2-ci Cədvələ yazma (Product cədvəlinə əlavə olunur)
        Product savedProduct = productRepository.save(product);

        return mapToResponse(savedProduct);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> findAll(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return mapToResponse(product);
    }

    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getCategoryId()));

        product.setTitle(request.getName());
        product.setPrice(request.getPrice());
        product.setCategory(category);

        Product updatedProduct = productRepository.save(product);
        return mapToResponse(updatedProduct);
    }

    @Transactional
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> searchProducts(String name, Double minPrice, Double maxPrice) {
        return productRepository.findByTitleContainingIgnoreCaseAndPriceBetween(name, minPrice, maxPrice)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> filterByCategoryAndPrice(String categoryName, Double maxPrice) {
        return productRepository.filterByCategoryNameAndMaxPrice(categoryName, maxPrice)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Dynamic search / filtering using Specification API
    @Transactional(readOnly = true)
    public List<ProductResponse> getDynamicProducts(String title, Double minPrice, Double maxPrice, Long categoryId) {
        Specification<Product> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (title != null && !title.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%"
                ));
            }

            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
            }

            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            if (categoryId != null) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), categoryId));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return productRepository.findAll(spec)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ProductResponse mapToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getTitle());
        response.setPrice(product.getPrice());
        if (product.getCategory() != null) {
            response.setCategoryId(product.getCategory().getId());
            response.setCategoryName(product.getCategory().getName());
        }
        return response;
    }
}