package com.tourisme.tourisme.repository;

import com.tourisme.tourisme.entities.OffreTouristique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OffreTouristiqueRepository extends JpaRepository<OffreTouristique, Long> {
    // Basic CRUD operations are provided by JpaRepository
}
