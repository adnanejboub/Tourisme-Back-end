package com.tourisme.tourisme.service;

import com.tourisme.tourisme.entities.Activite;
import com.tourisme.tourisme.entities.Activite.CategorieActivite;
import com.tourisme.tourisme.repository.ActiviteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ActiviteService {
    
    @Autowired
    private ActiviteRepository activiteRepository;
    
    public List<Activite> getAllActivites() {
        return activiteRepository.findAll();
    }
    
    public Optional<Activite> getActiviteById(Long id) {
        return activiteRepository.findById(id);
    }
    
    public Activite saveActivite(Activite activite) {
        return activiteRepository.save(activite);
    }
    
    public void deleteActivite(Long id) {
        activiteRepository.deleteById(id);
    }
    
    public List<Activite> searchActivitesByName(String nom) {
        return activiteRepository.findByNomContainingIgnoreCase(nom);
    }
    
    public List<Activite> getActivitesByCategorie(CategorieActivite categorie) {
        return activiteRepository.findByCategorie(categorie);
    }
    
    public List<Activite> getActivitesBySaison(String saison) {
        return activiteRepository.findBySaison(saison);
    }
    
    public List<Activite> getActivitesByVille(Long villeId) {
        return activiteRepository.findByVilleId(villeId);
    }
    
    public List<Activite> getActivitesByMaxDuration(Integer maxDuree) {
        return activiteRepository.findByDureeMinimunLessThanEqual(maxDuree);
    }

    public List<Activite> getActivitesByPriceRange(Float minPrix, Float maxPrix) {
        return List.of();
    }

    /**
     * Get recommended activities
     */
    public List<Activite> getRecommendedActivites() {
        // TODO: Implement recommendation algorithm based on user preferences and popularity
        // For now, return activities with high ratings or popular categories
        List<Activite> allActivities = activiteRepository.findAll();
        
        // Return first 10 activities as recommendations
        return allActivities.stream()
            .limit(10)
            .collect(Collectors.toList());
    }

    /**
     * Get activities by category ID
     */
    public List<Activite> getActivitesByCategory(Long categoryId) {
        // TODO: Implement proper category mapping
        // For now, return all activities
        return activiteRepository.findAll();
    }
}