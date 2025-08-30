package com.tourisme.tourisme.repository;

import com.tourisme.tourisme.entities.Avis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvisRepository extends JpaRepository<Avis, Long> {
    // Basic CRUD operations are provided by JpaRepository
}
