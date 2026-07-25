package com.example.week1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.week1.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}