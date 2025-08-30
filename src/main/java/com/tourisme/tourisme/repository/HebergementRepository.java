package com.tourisme.tourisme.repository;

import com.tourisme.tourisme.entities.Hebergement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HebergementRepository extends JpaRepository<Hebergement, Long> {
    // Basic CRUD operations are provided by JpaRepository
}
