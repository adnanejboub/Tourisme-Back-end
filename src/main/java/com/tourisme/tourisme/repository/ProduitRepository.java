package com.tourisme.tourisme.repository;

import com.tourisme.tourisme.entities.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {
    
    List<Produit> findByNomContainingIgnoreCase(String nom);
    
    List<Produit> findByIsDisponible(Boolean isDisponible);
    
    List<Produit> findByPrixBetween(Float minPrix, Float maxPrix);
    
    @Query("SELECT COUNT(p) FROM Produit p WHERE p.isDisponible = true")
    Long countAvailableProducts();
    
    @Query("SELECT SUM(p.stockDisponible) FROM Produit p WHERE p.isDisponible = true")
    Long getTotalStock();
}
