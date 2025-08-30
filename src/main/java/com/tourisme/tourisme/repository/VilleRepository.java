package com.tourisme.tourisme.repository;

import com.tourisme.tourisme.entities.Ville;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VilleRepository extends JpaRepository<Ville, Long> {
    
    List<Ville> findByNomVilleContainingIgnoreCase(String nomVille);
    
    List<Ville> findByIsPlageTrue();
    
    List<Ville> findByIsMontagneTrue();
    
    List<Ville> findByIsDesertTrue();
    
    List<Ville> findByIsHistoriqueTrue();
    
    List<Ville> findByIsCulturelleTrue();
    
    List<Ville> findByIsModerneTrue();
    
    @Query("SELECT v FROM Ville v WHERE v.pays.idPays = :paysId")
    List<Ville> findByPaysId(@Param("paysId") Long paysId);
    
    @Query("SELECT v FROM Ville v WHERE v.climat.idClimat = :climatId")
    List<Ville> findByClimatId(@Param("climatId") Long climatId);
} 