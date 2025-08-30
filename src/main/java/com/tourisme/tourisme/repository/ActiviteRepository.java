package com.tourisme.tourisme.repository;

import com.tourisme.tourisme.entities.Activite;
import com.tourisme.tourisme.entities.Activite.CategorieActivite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActiviteRepository extends JpaRepository<Activite, Long> {
    
    List<Activite> findByNomContainingIgnoreCase(String nom);
    
    List<Activite> findByCategorie(CategorieActivite categorie);
    
    List<Activite> findBySaison(String saison);
    
    @Query("SELECT a FROM Activite a WHERE a.ville.idVille = :villeId")
    List<Activite> findByVilleId(@Param("villeId") Long villeId);
    
    @Query("SELECT a FROM Activite a WHERE a.dureeMinimun <= :maxDuree")
    List<Activite> findByDureeMinimunLessThanEqual(@Param("maxDuree") Integer maxDuree);
} 