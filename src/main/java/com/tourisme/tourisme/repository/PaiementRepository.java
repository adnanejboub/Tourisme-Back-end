package com.tourisme.tourisme.repository;

import com.tourisme.tourisme.entities.Paiement;
import com.tourisme.tourisme.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long> {
    
    List<Paiement> findByUtilisateur(Utilisateur utilisateur);
    
    List<Paiement> findByMethodePaiement(String methodePaiement);
    
    List<Paiement> findByDatePaiementBetween(Date startDate, Date endDate);
    
    @Query("SELECT SUM(p.montant) FROM Paiement p")
    Double getTotalRevenue();
    
    @Query("SELECT SUM(p.montant) FROM Paiement p WHERE p.datePaiement BETWEEN :startDate AND :endDate")
    Double getRevenueByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
    @Query("SELECT COUNT(p) FROM Paiement p WHERE p.datePaiement BETWEEN :startDate AND :endDate")
    Long getTransactionCountByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
    @Query("SELECT p.methodePaiement, COUNT(p), SUM(p.montant) FROM Paiement p GROUP BY p.methodePaiement")
    List<Object[]> getPaymentMethodStatistics();
}
