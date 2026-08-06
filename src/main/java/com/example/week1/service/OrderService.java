package com.example.week1.service;

import com.example.week1.entity.Product;
import com.example.week1.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final ProductRepository productRepository;

    public OrderService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public void createOrderWithMultipleWrites(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        if (product.getPrice() == null) {
            throw new RuntimeException("Invalid product price");
        }

        productRepository.save(product);
    }
}