package com.tourisme.tourisme.repository;

import com.tourisme.tourisme.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByIsActiveTrueOrderByTitleAsc();

    @Query("SELECT c FROM Category c WHERE c.isActive = true AND c.title LIKE %:title%")
    List<Category> findByTitleContainingAndIsActive(String title);
}