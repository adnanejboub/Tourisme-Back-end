package com.tourisme.tourisme.repository;

import com.tourisme.tourisme.entities.Sejour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SejourRepository extends JpaRepository<Sejour, Long> {
    // Basic CRUD operations are provided by JpaRepository
}
