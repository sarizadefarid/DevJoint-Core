package com.example.week1.repository;

import com.example.week1.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByTitleContainingIgnoreCaseAndPriceBetween(String title, Double minPrice, Double maxPrice);

    List<Product> findByCategoryIdAndPriceGreaterThanEqual(Long categoryId, Double minPrice);

    @Query("SELECT p FROM Product p WHERE LOWER(p.category.name) = LOWER(:categoryName) AND p.price <= :maxPrice")
    List<Product> filterByCategoryNameAndMaxPrice(@Param("categoryName") String categoryName,
            @Param("maxPrice") Double maxPrice);

    @Query(value = "SELECT * FROM products p WHERE p.price >= :minPrice AND p.price <= :maxPrice AND p.category_id = :catId", nativeQuery = true)
    List<Product> filterProductsNative(@Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("catId") Long catId);
}