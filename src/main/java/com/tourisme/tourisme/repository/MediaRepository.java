package com.tourisme.tourisme.repository;

import com.tourisme.tourisme.entities.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
    // Basic CRUD operations are provided by JpaRepository
}
