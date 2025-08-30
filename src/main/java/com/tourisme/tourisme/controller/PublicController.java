package com.tourisme.tourisme.controller;

import com.tourisme.tourisme.entities.Activite;
import com.tourisme.tourisme.entities.Produit;
import com.tourisme.tourisme.entities.Ville;
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

    // Public Explore
    @GetMapping("/cities")
    public ResponseEntity<List<Ville>> getCities() {
        return ResponseEntity.ok(villeService.getAllVilles());
    }

    @GetMapping("/cities/{id}")
    public ResponseEntity<?> getCity(@PathVariable Long id) {
        return villeService.getVilleById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
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
        result.put("cities", villeService.searchVillesByName(q));
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

    // Get attractions by city (monuments, etc.)
    @GetMapping("/cities/{id}/attractions")
    public ResponseEntity<?> getCityAttractions(@PathVariable Long id) {
        try {
            // TODO: Implement attractions service
            return ResponseEntity.ok(Map.of(
                "message", "Attractions endpoint ready - implementation needed",
                "cityId", id,
                "attractions", new ArrayList<>()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "attractions_retrieval_failed",
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

    // Get popular cities
    @GetMapping("/cities/popular")
    public ResponseEntity<?> getPopularCities() {
        try {
            List<Ville> popularCities = villeService.getPopularVilles();
            return ResponseEntity.ok(popularCities);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "popular_cities_retrieval_failed",
                "message", e.getMessage()
            ));
        }
    }

    // Get recommended cities
    @GetMapping("/cities/recommended")
    public ResponseEntity<?> getRecommendedCities() {
        try {
            List<Ville> recommendedCities = villeService.getRecommendedVilles();
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









