package com.tourisme.tourisme.repository;

import com.tourisme.tourisme.entities.Pays;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaysRepository extends JpaRepository<Pays, Long> {
    // Basic CRUD operations are provided by JpaRepository
}
