package com.example.week1.dto.product;

public record ProductResponse(Long id, String name, Double price, Long categoryId, String categoryName) {
}