package com.tourisme.tourisme.repository;

import com.tourisme.tourisme.entities.Monument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonumentRepository extends JpaRepository<Monument, Long> {
    // Basic CRUD operations are provided by JpaRepository
}
