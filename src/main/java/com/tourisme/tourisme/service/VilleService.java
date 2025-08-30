package com.tourisme.tourisme.service;

import com.tourisme.tourisme.entities.Ville;
import com.tourisme.tourisme.repository.VilleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class VilleService {
    
    @Autowired
    private VilleRepository villeRepository;
    
    public List<Ville> getAllVilles() {
        return villeRepository.findAll();
    }
    
    public Optional<Ville> getVilleById(Long id) {
        return villeRepository.findById(id);
    }
    
    public Ville saveVille(Ville ville) {
        return villeRepository.save(ville);
    }
    
    public void deleteVille(Long id) {
        villeRepository.deleteById(id);
    }
    
    public List<Ville> searchVillesByName(String nomVille) {
        return villeRepository.findByNomVilleContainingIgnoreCase(nomVille);
    }
    
    public List<Ville> getVillesByPlage() {
        return villeRepository.findByIsPlageTrue();
    }
    
    public List<Ville> getVillesByMontagne() {
        return villeRepository.findByIsMontagneTrue();
    }
    
    public List<Ville> getVillesByDesert() {
        return villeRepository.findByIsDesertTrue();
    }
    
    public List<Ville> getVillesByHistorique() {
        return villeRepository.findByIsHistoriqueTrue();
    }
    
    public List<Ville> getVillesByCulturelle() {
        return villeRepository.findByIsCulturelleTrue();
    }
    
    public List<Ville> getVillesByModerne() {
        return villeRepository.findByIsModerneTrue();
    }
    
    public List<Ville> getVillesByPays(Long paysId) {
        return villeRepository.findByPaysId(paysId);
    }
    
    public List<Ville> getVillesByClimat(Long climatId) {
        return villeRepository.findByClimatId(climatId);
    }

    /**
     * Get popular cities (most visited/booked)
     */
    public List<Ville> getPopularVilles() {
        // TODO: Implement logic based on actual booking/visit data
        // For now, return cities with beach or historical characteristics
        List<Ville> popularVilles = new ArrayList<>();
        popularVilles.addAll(villeRepository.findByIsPlageTrue());
        popularVilles.addAll(villeRepository.findByIsHistoriqueTrue());
        
        // Remove duplicates and limit to top 10
        return popularVilles.stream()
            .distinct()
            .limit(10)
            .collect(Collectors.toList());
    }

    /**
     * Get recommended cities based on user preferences
     */
    public List<Ville> getRecommendedVilles() {
        // TODO: Implement recommendation algorithm based on user preferences
        // For now, return a mix of different city types
        List<Ville> recommendedVilles = new ArrayList<>();
        recommendedVilles.addAll(villeRepository.findByIsCulturelleTrue());
        recommendedVilles.addAll(villeRepository.findByIsModerneTrue());
        recommendedVilles.addAll(villeRepository.findByIsMontagneTrue());
        
        // Remove duplicates and limit to top 8
        return recommendedVilles.stream()
            .distinct()
            .limit(8)
            .collect(Collectors.toList());
    }
} 