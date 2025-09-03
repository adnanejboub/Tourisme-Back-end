package com.tourisme.tourisme.service;

import com.tourisme.tourisme.dto.VilleDTO;
import com.tourisme.tourisme.dto.VilleSummaryDTO;
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
    
    public List<VilleDTO> getAllVillesDTO() {
        List<Ville> villes = villeRepository.findAll();
        return toVilleDTOList(villes);
    }
    
    public Optional<Ville> getVilleById(Long id) {
        return villeRepository.findById(id);
    }
    
    public Optional<VilleDTO> getVilleDTOById(Long id) {
        Optional<Ville> ville = villeRepository.findById(id);
        return ville.map(this::toVilleDTO);
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
    
    public List<VilleDTO> searchVillesByNameDTO(String nomVille) {
        List<Ville> villes = villeRepository.findByNomVilleContainingIgnoreCase(nomVille);
        return toVilleDTOList(villes);
    }
    
    public List<Ville> getVillesByPlage() {
        return villeRepository.findByIsPlageTrue();
    }
    
    public List<VilleDTO> getVillesByPlageDTO() {
        List<Ville> villes = villeRepository.findByIsPlageTrue();
        return toVilleDTOList(villes);
    }
    
    public List<Ville> getVillesByMontagne() {
        return villeRepository.findByIsMontagneTrue();
    }
    
    public List<VilleDTO> getVillesByMontagneDTO() {
        List<Ville> villes = villeRepository.findByIsMontagneTrue();
        return toVilleDTOList(villes);
    }
    
    public List<Ville> getVillesByDesert() {
        return villeRepository.findByIsDesertTrue();
    }
    
    public List<VilleDTO> getVillesByDesertDTO() {
        List<Ville> villes = villeRepository.findByIsDesertTrue();
        return toVilleDTOList(villes);
    }
    
    public List<Ville> getVillesByHistorique() {
        return villeRepository.findByIsHistoriqueTrue();
    }
    
    public List<VilleDTO> getVillesByHistoriqueDTO() {
        List<Ville> villes = villeRepository.findByIsHistoriqueTrue();
        return toVilleDTOList(villes);
    }
    
    public List<Ville> getVillesByCulturelle() {
        return villeRepository.findByIsCulturelleTrue();
    }
    
    public List<VilleDTO> getVillesByCulturelleDTO() {
        List<Ville> villes = villeRepository.findByIsCulturelleTrue();
        return toVilleDTOList(villes);
    }
    
    public List<Ville> getVillesByModerne() {
        return villeRepository.findByIsModerneTrue();
    }
    
    public List<VilleDTO> getVillesByModerneDTO() {
        List<Ville> villes = villeRepository.findByIsModerneTrue();
        return toVilleDTOList(villes);
    }
    
    public List<Ville> getVillesByPays(Long paysId) {
        return villeRepository.findByPaysId(paysId);
    }
    
    public List<VilleDTO> getVillesByPaysDTO(Long paysId) {
        List<Ville> villes = villeRepository.findByPaysId(paysId);
        return toVilleDTOList(villes);
    }
    
    public List<Ville> getVillesByClimat(Long climatId) {
        return villeRepository.findByClimatId(climatId);
    }
    
    public List<VilleDTO> getVillesByClimatDTO(Long climatId) {
        List<Ville> villes = villeRepository.findByClimatId(climatId);
        return toVilleDTOList(villes);
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
    
    public List<VilleDTO> getPopularVillesDTO() {
        List<Ville> popularVilles = getPopularVilles();
        return toVilleDTOList(popularVilles);
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
    
    public List<VilleDTO> getRecommendedVillesDTO() {
        List<Ville> recommendedVilles = getRecommendedVilles();
        return toVilleDTOList(recommendedVilles);
    }
    
    // Summary DTO methods for lightweight city information
    public List<VilleSummaryDTO> getAllVillesSummaryDTO() {
        List<Ville> villes = villeRepository.findAll();
        return toVilleSummaryDTOList(villes);
    }
    
    public List<VilleSummaryDTO> searchVillesByNameSummaryDTO(String nomVille) {
        List<Ville> villes = villeRepository.findByNomVilleContainingIgnoreCase(nomVille);
        return toVilleSummaryDTOList(villes);
    }
    
    public List<VilleSummaryDTO> getPopularVillesSummaryDTO() {
        List<Ville> popularVilles = getPopularVilles();
        return toVilleSummaryDTOList(popularVilles);
    }
    
    public List<VilleSummaryDTO> getRecommendedVillesSummaryDTO() {
        List<Ville> recommendedVilles = getRecommendedVilles();
        return toVilleSummaryDTOList(recommendedVilles);
    }
    
    // Private mapping methods
    private VilleDTO toVilleDTO(Ville ville) {
        if (ville == null) {
            return null;
        }
        
        VilleDTO dto = new VilleDTO();
        dto.setIdVille(ville.getIdVille());
        dto.setNomVille(ville.getNomVille());
        dto.setDescription(ville.getDescription());
        dto.setLatitude(ville.getLatitude());
        dto.setLongitude(ville.getLongitude());
        dto.setAllInclusive(ville.getAllInclusive());
        dto.setAllouement(ville.getAllouement());
        
        // Boolean characteristics
        dto.setIsPlage(ville.getIsPlage());
        dto.setIsMontagne(ville.getIsMontagne());
        dto.setIsDesert(ville.getIsDesert());
        dto.setIsRiviera(ville.getIsRiviera());
        dto.setIsHistorique(ville.getIsHistorique());
        dto.setIsCulturelle(ville.getIsCulturelle());
        dto.setIsModerne(ville.getIsModerne());
        
        // Has characteristics
        dto.setHasAeroport(ville.getHasAeroport());
        dto.setHasGare(ville.getHasGare());
        dto.setHasPort(ville.getHasPort());
        dto.setHasPlage(ville.getHasPlage());
        dto.setHasMontagne(ville.getHasMontagne());
        dto.setHasDesert(ville.getHasDesert());
        dto.setHasRiviera(ville.getHasRiviera());
        dto.setHasHistorique(ville.getHasHistorique());
        dto.setHasCulturelle(ville.getHasCulturelle());
        dto.setHasModerne(ville.getHasModerne());
        
        // Related information
        if (ville.getPays() != null) {
            dto.setPaysNom(ville.getPays().getNomPays());
        }
        if (ville.getClimat() != null) {
            dto.setClimatNom(ville.getClimat().getNomClimat());
        }
        
        // Note: For complex relationships like specialites, monuments, etc.
        // you might want to implement separate mapping logic or leave them null
        // to avoid circular references and performance issues
        
        return dto;
    }
    
    private List<VilleDTO> toVilleDTOList(List<Ville> villes) {
        if (villes == null) {
            return new ArrayList<>();
        }
        return villes.stream()
            .map(this::toVilleDTO)
            .collect(Collectors.toList());
    }
    
    private VilleSummaryDTO toVilleSummaryDTO(Ville ville) {
        if (ville == null) {
            return null;
        }
        
        VilleSummaryDTO dto = new VilleSummaryDTO();
        dto.setIdVille(ville.getIdVille());
        dto.setNomVille(ville.getNomVille());
        dto.setDescription(ville.getDescription());
        dto.setLatitude(ville.getLatitude());
        dto.setLongitude(ville.getLongitude());
        
        // Basic characteristics
        dto.setIsPlage(ville.getIsPlage());
        dto.setIsMontagne(ville.getIsMontagne());
        dto.setIsDesert(ville.getIsDesert());
        dto.setIsRiviera(ville.getIsRiviera());
        dto.setIsHistorique(ville.getIsHistorique());
        dto.setIsCulturelle(ville.getIsCulturelle());
        dto.setIsModerne(ville.getIsModerne());
        
        // Related information
        if (ville.getPays() != null) {
            dto.setPaysNom(ville.getPays().getNomPays());
        }
        if (ville.getClimat() != null) {
            dto.setClimatNom(ville.getClimat().getNomClimat());
        }
        
        return dto;
    }
    
    private List<VilleSummaryDTO> toVilleSummaryDTOList(List<Ville> villes) {
        if (villes == null) {
            return new ArrayList<>();
        }
        return villes.stream()
            .map(this::toVilleSummaryDTO)
            .collect(Collectors.toList());
    }
} 