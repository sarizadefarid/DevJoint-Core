package com.example.week1.repository;

import com.example.week1.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id); // Yeniləmə (Update) zamanı öz ID-sini nəzərə almamaq üçün
}