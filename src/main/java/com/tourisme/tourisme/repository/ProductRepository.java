package com.tourisme.tourisme.repository;

import com.tourisme.tourisme.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    //what is Page??
    Page<Product> findByIsActiveTrueOrderByCreatedDateDesc(Pageable pageable);

    Page<Product> findByCategoryIdAndIsActiveTrueOrderByCreatedDateDesc(Long categoryId, Pageable pageable);

    List<Product> findByIsFeaturedTrueAndIsActiveTrueOrderByCreatedDateDesc();

    @Query("SELECT p FROM Product p WHERE p.isActive = true AND " +
            "(LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Product> searchProducts(@Param("query") String query, Pageable pageable);
}
