package com.tourisme.tourisme.controller.tourist;

import com.tourisme.tourisme.entities.Activite;
import com.tourisme.tourisme.entities.Ville;
import com.tourisme.tourisme.service.ActiviteService;
import com.tourisme.tourisme.service.VilleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tourist")
@PreAuthorize("hasRole('TOURISTE')")
public class TouristController {

    @Autowired
    private VilleService villeService;

    @Autowired
    private ActiviteService activiteService;

    // Ville exploration
    @GetMapping("/villes")
    public ResponseEntity<List<Ville>> getAllVilles() {
        return ResponseEntity.ok(villeService.getAllVilles());
    }

    @GetMapping("/villes/{id}")
    public ResponseEntity<?> getVilleById(@PathVariable Long id) {
        return villeService.getVilleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/villes/search")
    public ResponseEntity<List<Ville>> searchVilles(@RequestParam String query) {
        return ResponseEntity.ok(villeService.searchVillesByName(query));
    }

    @GetMapping("/villes/plage")
    public ResponseEntity<List<Ville>> getVillesByPlage() {
        return ResponseEntity.ok(villeService.getVillesByPlage());
    }

    @GetMapping("/villes/montagne")
    public ResponseEntity<List<Ville>> getVillesByMontagne() {
        return ResponseEntity.ok(villeService.getVillesByMontagne());
    }

    @GetMapping("/villes/desert")
    public ResponseEntity<List<Ville>> getVillesByDesert() {
        return ResponseEntity.ok(villeService.getVillesByDesert());
    }

    @GetMapping("/villes/historique")
    public ResponseEntity<List<Ville>> getVillesByHistorique() {
        return ResponseEntity.ok(villeService.getVillesByHistorique());
    }

    @GetMapping("/villes/culturelle")
    public ResponseEntity<List<Ville>> getVillesByCulturelle() {
        return ResponseEntity.ok(villeService.getVillesByCulturelle());
    }

    @GetMapping("/villes/moderne")
    public ResponseEntity<List<Ville>> getVillesByModerne() {
        return ResponseEntity.ok(villeService.getVillesByModerne());
    }

    // Activite exploration
    @GetMapping("/activites")
    public ResponseEntity<List<Activite>> getAllActivites() {
        return ResponseEntity.ok(activiteService.getAllActivites());
    }

    @GetMapping("/activites/{id}")
    public ResponseEntity<?> getActiviteById(@PathVariable Long id) {
        return activiteService.getActiviteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/activites/search")
    public ResponseEntity<List<Activite>> searchActivites(@RequestParam String query) {
        return ResponseEntity.ok(activiteService.searchActivitesByName(query));
    }

    @GetMapping("/activites/ville/{villeId}")
    public ResponseEntity<List<Activite>> getActivitesByVille(@PathVariable Long villeId) {
        return ResponseEntity.ok(activiteService.getActivitesByVille(villeId));
    }

    @GetMapping("/activites/prix")
    public ResponseEntity<List<Activite>> getActivitesByPriceRange(
            @RequestParam Float minPrix, 
            @RequestParam Float maxPrix) {
        return ResponseEntity.ok(activiteService.getActivitesByPriceRange(minPrix, maxPrix));
    }

    @GetMapping("/activites/duree")
    public ResponseEntity<List<Activite>> getActivitesByMaxDuration(@RequestParam Integer maxDuree) {
        return ResponseEntity.ok(activiteService.getActivitesByMaxDuration(maxDuree));
    }

    @GetMapping("/activites/saison")
    public ResponseEntity<List<Activite>> getActivitesBySaison(@RequestParam String saison) {
        return ResponseEntity.ok(activiteService.getActivitesBySaison(saison));
    }

    // TODO: Add more tourist-specific endpoints for:
    // - Monument exploration
    // - Hebergement booking
    // - Transport services
    // - Product shopping
    // - Reviews and ratings
    // - Trip planning
} 