package com.tourisme.tourisme.controller;

import com.tourisme.tourisme.dto.VilleDTO;
import com.tourisme.tourisme.entities.Activite;
import com.tourisme.tourisme.entities.Produit;
import com.tourisme.tourisme.entities.Ville;
import com.tourisme.tourisme.entities.Monument;
import com.tourisme.tourisme.dto.MonumentDTO;
import com.tourisme.tourisme.repository.MonumentRepository;
import com.tourisme.tourisme.entities.Hebergement;
import com.tourisme.tourisme.dto.HebergementDTO;
import com.tourisme.tourisme.repository.HebergementRepository;
import com.tourisme.tourisme.repository.ProduitRepository;
import com.tourisme.tourisme.service.ActiviteService;
import com.tourisme.tourisme.service.VilleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private VilleService villeService;

    @Autowired
    private ActiviteService activiteService;

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private MonumentRepository monumentRepository;

    @Autowired
    private HebergementRepository hebergementRepository;

    // Public Explore - Return DTOs to avoid circular references
    @GetMapping("/cities")
    public ResponseEntity<List<VilleDTO>> getCities() {
        return ResponseEntity.ok(villeService.getAllVillesDTO());
    }

    @GetMapping("/cities/{id}")
    public ResponseEntity<?> getCity(@PathVariable Long id) {
        return villeService.getVilleDTOById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/activities")
    public ResponseEntity<List<Activite>> getActivities() {
        return ResponseEntity.ok(activiteService.getAllActivites());
    }

    @GetMapping("/activities/{id}")
    public ResponseEntity<?> getActivity(@PathVariable Long id) {
        return activiteService.getActiviteById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Simple aggregated search (cities + activities by name contains)
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> search(@RequestParam("q") String q) {
        Map<String, Object> result = new HashMap<>();
        result.put("cities", villeService.searchVillesByNameDTO(q));
        result.put("activities", activiteService.searchActivitesByName(q));
        return ResponseEntity.ok(result);
    }

    // Public Products (catalog)
    @GetMapping("/products")
    public ResponseEntity<List<Produit>> getProducts(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "available", required = false) Boolean available,
            @RequestParam(value = "min", required = false) Float min,
            @RequestParam(value = "max", required = false) Float max
    ) {
        List<Produit> products;
        if (q != null && !q.isBlank()) {
            products = produitRepository.findByNomContainingIgnoreCase(q);
        } else if (available != null) {
            products = produitRepository.findByIsDisponible(available);
        } else if (min != null && max != null) {
            products = produitRepository.findByPrixBetween(min, max);
        } else {
            products = produitRepository.findAll();
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id) {
        return produitRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Get activities by city
    @GetMapping("/cities/{id}/activities")
    public ResponseEntity<?> getCityActivities(@PathVariable Long id) {
        try {
            List<Activite> activities = activiteService.getActivitesByVille(id);
            return ResponseEntity.ok(activities);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "activities_retrieval_failed",
                "message", e.getMessage()
            ));
        }
    }

    // Get monuments by city
    @GetMapping("/cities/{id}/monuments")
    public ResponseEntity<?> getCityMonuments(@PathVariable Long id) {
        try {
            List<Monument> monuments = monumentRepository.findByVille_IdVille(id);
            List<MonumentDTO> dto = monuments.stream().map(m -> {
                MonumentDTO d = new MonumentDTO();
                d.setIdMonument(m.getIdMonument());
                d.setNomMonument(m.getNomMonument());
                d.setAdresseMonument(m.getAdresseMonument());
                d.setPrix(m.getPrix());
                d.setGratuit(m.getGratuit());
                d.setHasCulturelle(m.getHasCulturelle());
                d.setHasHistorique(m.getHasHistorique());
                d.setNotesMoyennes(m.getNotesMoyennes());
                return d;
            }).toList();
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "monuments_retrieval_failed",
                "message", e.getMessage()
            ));
        }
    }

    // Search monuments by name (global)
    @GetMapping("/monuments/search")
    public ResponseEntity<?> searchMonuments(@RequestParam("q") String q) {
        try {
            List<Monument> monuments = monumentRepository.findByNomMonumentContainingIgnoreCase(q);
            List<MonumentDTO> dto = monuments.stream().map(m -> {
                MonumentDTO d = new MonumentDTO();
                d.setIdMonument(m.getIdMonument());
                d.setNomMonument(m.getNomMonument());
                d.setAdresseMonument(m.getAdresseMonument());
                d.setPrix(m.getPrix());
                d.setGratuit(m.getGratuit());
                d.setHasCulturelle(m.getHasCulturelle());
                d.setHasHistorique(m.getHasHistorique());
                d.setNotesMoyennes(m.getNotesMoyennes());
                return d;
            }).toList();
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "monuments_search_failed",
                "message", e.getMessage()
            ));
        }
    }

    // Search monuments by city name
    @GetMapping("/monuments/by-city")
    public ResponseEntity<?> getMonumentsByCity(@RequestParam("city") String cityName) {
        try {
            List<Monument> monuments = monumentRepository.findByCityName(cityName);
            List<MonumentDTO> dto = monuments.stream().map(m -> {
                MonumentDTO d = new MonumentDTO();
                d.setIdMonument(m.getIdMonument());
                d.setNomMonument(m.getNomMonument());
                d.setAdresseMonument(m.getAdresseMonument());
                d.setPrix(m.getPrix());
                d.setGratuit(m.getGratuit());
                d.setHasCulturelle(m.getHasCulturelle());
                d.setHasHistorique(m.getHasHistorique());
                d.setNotesMoyennes(m.getNotesMoyennes());
                return d;
            }).toList();
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "monuments_by_city_failed",
                "message", e.getMessage()
            ));
        }
    }

    // List all monuments
    @GetMapping("/monuments")
    public ResponseEntity<?> getAllMonuments() {
        try {
            List<Monument> monuments = monumentRepository.findAll();
            List<MonumentDTO> dto = monuments.stream().map(m -> {
                MonumentDTO d = new MonumentDTO();
                d.setIdMonument(m.getIdMonument());
                d.setNomMonument(m.getNomMonument());
                d.setAdresseMonument(m.getAdresseMonument());
                d.setPrix(m.getPrix());
                d.setGratuit(m.getGratuit());
                d.setHasCulturelle(m.getHasCulturelle());
                d.setHasHistorique(m.getHasHistorique());
                d.setNotesMoyennes(m.getNotesMoyennes());
                return d;
            }).toList();
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "monuments_list_failed",
                "message", e.getMessage()
            ));
        }
    }

    // List all accommodations (hébergements)
    @GetMapping("/hebergements")
    public ResponseEntity<?> getAllHebergements() {
        try {
            List<Object[]> rows = hebergementRepository.findAllAsRows();
            List<HebergementDTO> dto = rows.stream().map(r -> {
                HebergementDTO d = new HebergementDTO();
                d.setIdHebergement(((Number) r[0]).longValue());
                d.setNomHebergement((String) r[1]);
                d.setAdresse((String) r[2]);
                d.setPrixParNuit(r[3] != null ? ((Number) r[3]).floatValue() : null);
                d.setEtoiles(r[4] != null ? ((Number) r[4]).intValue() : null);
                d.setDescription((String) r[5]);
                d.setIsDisponible(r[6] != null ? ((Boolean) r[6]) : null);
                d.setHebergementType((String) r[7]);
                return d;
            }).toList();
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "hebergements_list_failed",
                "message", e.getMessage()
            ));
        }
    }

    // Search accommodations by name or city
    @GetMapping("/hebergements/search")
    public ResponseEntity<?> searchHebergements(@RequestParam("q") String q) {
        try {
            List<Object[]> rows = hebergementRepository.searchAsRows(q);
            List<HebergementDTO> dto = rows.stream().map(r -> {
                HebergementDTO d = new HebergementDTO();
                d.setIdHebergement(((Number) r[0]).longValue());
                d.setNomHebergement((String) r[1]);
                d.setAdresse((String) r[2]);
                d.setPrixParNuit(r[3] != null ? ((Number) r[3]).floatValue() : null);
                d.setEtoiles(r[4] != null ? ((Number) r[4]).intValue() : null);
                d.setDescription((String) r[5]);
                d.setIsDisponible(r[6] != null ? ((Boolean) r[6]) : null);
                d.setHebergementType((String) r[7]);
                return d;
            }).toList();
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "hebergements_search_failed",
                "message", e.getMessage()
            ));
        }
    }

    // Search accommodations by city name
    @GetMapping("/hebergements/by-city")
    public ResponseEntity<?> getHebergementsByCity(@RequestParam("city") String cityName) {
        try {
            List<Object[]> rows = hebergementRepository.findByCityName(cityName);
            List<HebergementDTO> dto = rows.stream().map(r -> {
                HebergementDTO d = new HebergementDTO();
                d.setIdHebergement(((Number) r[0]).longValue());
                d.setNomHebergement((String) r[1]);
                d.setAdresse((String) r[2]);
                d.setPrixParNuit(r[3] != null ? ((Number) r[3]).floatValue() : null);
                d.setEtoiles(r[4] != null ? ((Number) r[4]).intValue() : null);
                d.setDescription((String) r[5]);
                d.setIsDisponible(r[6] != null ? ((Boolean) r[6]) : null);
                d.setHebergementType((String) r[7]);
                return d;
            }).toList();
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "hebergements_by_city_failed",
                "message", e.getMessage()
            ));
        }
    }

    // Get events by city
    @GetMapping("/cities/{id}/events")
    public ResponseEntity<?> getCityEvents(@PathVariable Long id) {
        try {
            // TODO: Implement events service
            return ResponseEntity.ok(Map.of(
                "message", "Events endpoint ready - implementation needed",
                "cityId", id,
                "events", new ArrayList<>()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "events_retrieval_failed",
                "message", e.getMessage()
            ));
        }
    }

    // Get popular cities - Return DTOs
    @GetMapping("/cities/popular")
    public ResponseEntity<?> getPopularCities() {
        try {
            List<VilleDTO> popularCities = villeService.getPopularVillesDTO();
            return ResponseEntity.ok(popularCities);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "popular_cities_retrieval_failed",
                "message", e.getMessage()
            ));
        }
    }

    // Get recommended cities - Return DTOs
    @GetMapping("/cities/recommended")
    public ResponseEntity<?> getRecommendedCities() {
        try {
            List<VilleDTO> recommendedCities = villeService.getRecommendedVillesDTO();
            return ResponseEntity.ok(recommendedCities);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "recommended_cities_retrieval_failed",
                "message", e.getMessage()
            ));
        }
    }

    // Get recommended activities
    @GetMapping("/activities/recommended")
    public ResponseEntity<?> getRecommendedActivities() {
        try {
            List<Activite> recommendedActivities = activiteService.getRecommendedActivites();
            return ResponseEntity.ok(recommendedActivities);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "recommended_activities_retrieval_failed",
                "message", e.getMessage()
            ));
        }
    }

    // Get activities by category
    @GetMapping("/activities/by-category/{categoryId}")
    public ResponseEntity<?> getActivitiesByCategory(@PathVariable Long categoryId) {
        try {
            List<Activite> activities = activiteService.getActivitesByCategory(categoryId);
            return ResponseEntity.ok(activities);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "category_activities_retrieval_failed",
                "message", e.getMessage()
            ));
        }
    }

    // Add review to activity
    @PostMapping("/activities/{id}/reviews")
    public ResponseEntity<?> addActivityReview(@PathVariable Long id, @RequestBody Map<String, Object> review) {
        try {
            // TODO: Implement review service
            return ResponseEntity.ok(Map.of(
                "message", "Review added successfully",
                "activityId", id,
                "review", review
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "review_addition_failed",
                "message", e.getMessage()
            ));
        }
    }

    // Get reviews for activity
    @GetMapping("/activities/{id}/reviews")
    public ResponseEntity<?> getActivityReviews(@PathVariable Long id) {
        try {
            // TODO: Implement review service
            return ResponseEntity.ok(Map.of(
                "message", "Reviews endpoint ready - implementation needed",
                "activityId", id,
                "reviews", new ArrayList<>()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "reviews_retrieval_failed",
                "message", e.getMessage()
            ));
        }
    }
}









