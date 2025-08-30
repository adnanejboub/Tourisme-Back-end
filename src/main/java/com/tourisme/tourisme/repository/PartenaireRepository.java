package com.tourisme.tourisme.repository;

import com.tourisme.tourisme.entities.Partenaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartenaireRepository extends JpaRepository<Partenaire, Long> {
    // Basic CRUD operations are provided by JpaRepository
}
