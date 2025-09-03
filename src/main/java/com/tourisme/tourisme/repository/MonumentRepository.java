package com.tourisme.tourisme.repository;

import com.tourisme.tourisme.entities.Monument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MonumentRepository extends JpaRepository<Monument, Long> {
    List<Monument> findByVille_IdVille(Long idVille);
    List<Monument> findByNomMonumentContainingIgnoreCase(String q);
    
    @Query("SELECT m FROM Monument m JOIN m.ville v WHERE LOWER(v.nomVille) LIKE LOWER(CONCAT('%', :cityName, '%'))")
    List<Monument> findByCityName(@Param("cityName") String cityName);
}
