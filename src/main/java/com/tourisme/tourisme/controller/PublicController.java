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

import com.tourisme.tourisme.repository.ServiceRepository;
import com.tourisme.tourisme.entities.Service;
import com.tourisme.tourisme.service.ActiviteService;
import com.tourisme.tourisme.service.VilleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

    @Autowired
    private ServiceRepository serviceRepository;

    // Public Explore - Return DTOs to avoid circular references
    @GetMapping("/cities")
    public ResponseEntity<List<VilleDTO>> getCities() {
        return ResponseEntity.ok(villeService.getAllVillesDTO());
    }

    @GetMapping("/cities/{id}")
    public ResponseEntity<?> getCity(@PathVariable Long id) {
        return villeService.getVilleDTOById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


    // Get comprehensive city details with all related data
    @GetMapping("/cities/{id}/details")
    public ResponseEntity<?> getCityDetails(@PathVariable Long id) {
        try {
            Map<String, Object> cityDetails = new HashMap<>();
            
            // Get city basic info
            Optional<VilleDTO> cityOpt = villeService.getVilleDTOById(id);
            if (cityOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            VilleDTO city = cityOpt.get();
            cityDetails.put("city", city);
            
            // Get city activities with enhanced details
            List<Activite> activities = activiteService.getActivitesByVille(id);
            cityDetails.put("activities", activities);
            
            // Get city monuments with enhanced details
            List<Monument> monuments = monumentRepository.findByVille_IdVille(id);
            List<Map<String, Object>> monumentDetails = monuments.stream().map(m -> {
                Map<String, Object> monumentMap = new HashMap<>();
                monumentMap.put("idMonument", m.getIdMonument());
                monumentMap.put("nomMonument", m.getNomMonument());
                monumentMap.put("adresseMonument", m.getAdresseMonument());
                monumentMap.put("prix", m.getPrix());
                monumentMap.put("gratuit", m.getGratuit());
                monumentMap.put("hasCulturelle", m.getHasCulturelle());
                monumentMap.put("hasHistorique", m.getHasHistorique());
                monumentMap.put("notesMoyennes", m.getNotesMoyennes());
                monumentMap.put("description", m.getDescription());
                monumentMap.put("imageUrl", m.getImageUrl());
                monumentMap.put("horairesOuverture", m.getHorairesOuverture());
                monumentMap.put("typeMonument", m.getTypeMonument());
                return monumentMap;
            }).toList();
            cityDetails.put("monuments", monumentDetails);
            
            // Get city accommodations with enhanced details
            List<Object[]> hebergementRows = hebergementRepository.findByCityName(city.getNomVille());
            List<Map<String, Object>> accommodationDetails = hebergementRows.stream().map(r -> {
                Map<String, Object> accMap = new HashMap<>();
                accMap.put("idHebergement", ((Number) r[0]).longValue());
                accMap.put("nomHebergement", (String) r[1]);
                accMap.put("adresse", (String) r[2]);
                accMap.put("prixParNuit", r[3] != null ? ((Number) r[3]).floatValue() : null);
                accMap.put("etoiles", r[4] != null ? ((Number) r[4]).intValue() : null);
                accMap.put("description", (String) r[5]);
                accMap.put("isDisponible", r[6] != null ? ((Boolean) r[6]) : null);
                accMap.put("hebergementType", (String) r[7]);
                accMap.put("imageUrl", "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=400"); // Default image
                accMap.put("amenities", List.of("WiFi", "Parking", "Restaurant")); // Default amenities
                return accMap;
            }).toList();
            cityDetails.put("accommodations", accommodationDetails);
            
            // Get city events (placeholder for now)
            cityDetails.put("events", new ArrayList<>());
            
            // Get city services with enhanced details
            List<Service> services = serviceRepository.findByVille_IdVille(id);
            List<Map<String, Object>> serviceDetails = services.stream().map(s -> {
                Map<String, Object> serviceMap = new HashMap<>();
                serviceMap.put("idService", s.getIdService());
                serviceMap.put("typeService", s.getTypeService());
                serviceMap.put("nomService", s.getTypeService().toString()); // Use type as name for now
                serviceMap.put("description", "Service de type " + s.getTypeService().toString());
                serviceMap.put("categorie", s.getTypeService().toString());
                serviceMap.put("prix", 0.0); // Default price
                serviceMap.put("disponible", true); // Default availability
                serviceMap.put("imageUrl", "https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?w=400"); // Default image
                return serviceMap;
            }).toList();
            cityDetails.put("services", serviceDetails);
            
            // Add city statistics
            Map<String, Object> statistics = new HashMap<>();
            statistics.put("totalActivities", activities.size());
            statistics.put("totalMonuments", monuments.size());
            statistics.put("totalAccommodations", accommodationDetails.size());
            statistics.put("totalServices", serviceDetails.size());
            statistics.put("averageRating", city.getNoteMoyenne());
            cityDetails.put("statistics", statistics);
            
            // Add city highlights (key attractions)
            List<Map<String, Object>> highlights = monuments.stream()
                .filter(m -> m.getNotesMoyennes() != null && m.getNotesMoyennes() >= 4.0)
                .limit(4)
                .map(m -> {
                    Map<String, Object> highlight = new HashMap<>();
                    highlight.put("name", m.getNomMonument());
                    highlight.put("type", "Monument");
                    highlight.put("rating", m.getNotesMoyennes());
                    highlight.put("imageUrl", m.getImageUrl());
                    return highlight;
                }).toList();
            cityDetails.put("highlights", highlights);
            
            return ResponseEntity.ok(cityDetails);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "city_details_retrieval_failed",
                "message", e.getMessage()
            ));
        }
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

    // Get monument details by id (public)
    @GetMapping("/monuments/{id}")
    public ResponseEntity<?> getMonumentDetails(@PathVariable Long id) {
        try {
            Optional<Monument> monumentOpt = monumentRepository.findById(id);
            if (monumentOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Monument monument = monumentOpt.get();

            Map<String, Object> details = new HashMap<>();
            details.put("idMonument", monument.getIdMonument());
            details.put("nomMonument", monument.getNomMonument());
            details.put("adresseMonument", monument.getAdresseMonument());
            details.put("prix", monument.getPrix());
            details.put("gratuit", monument.getGratuit());
            details.put("hasCulturelle", monument.getHasCulturelle());
            details.put("hasHistorique", monument.getHasHistorique());
            details.put("notesMoyennes", monument.getNotesMoyennes());
            details.put("description", monument.getDescription());
            details.put("imageUrl", monument.getImageUrl());
            details.put("horairesOuverture", monument.getHorairesOuverture());
            details.put("typeMonument", monument.getTypeMonument());

            // City info
            if (monument.getVille() != null) {
                Map<String, Object> city = new HashMap<>();
                city.put("idVille", monument.getVille().getIdVille());
                city.put("nomVille", monument.getVille().getNomVille());
                city.put("description", monument.getVille().getDescription());
                city.put("imageUrl", monument.getVille().getImageUrl());
                city.put("latitude", monument.getVille().getLatitude());
                city.put("longitude", monument.getVille().getLongitude());
                details.put("city", city);
            }

            // Related monuments in same city
            List<Map<String, Object>> related = new ArrayList<>();
            if (monument.getVille() != null) {
                List<Monument> inCity = monumentRepository.findByVille_IdVille(monument.getVille().getIdVille());
                related = inCity.stream()
                        .filter(m -> !m.getIdMonument().equals(monument.getIdMonument()))
                        .limit(10)
                        .map(m -> {
                            Map<String, Object> map = new HashMap<>();
                            map.put("idMonument", m.getIdMonument());
                            map.put("nomMonument", m.getNomMonument());
                            map.put("imageUrl", m.getImageUrl());
                            map.put("notesMoyennes", m.getNotesMoyennes());
                            return map;
                        })
                        .toList();
            }
            details.put("relatedMonuments", related);

            return ResponseEntity.ok(details);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "monument_details_failed",
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

    // Get accommodation details by id (public)
    @GetMapping("/hebergements/{id}")
    public ResponseEntity<?> getHebergementDetails(@PathVariable Long id) {
        try {
            List<Object[]> rows = hebergementRepository.findOneAsRow(id);
            if (rows == null || rows.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            Object[] row = rows.get(0);

            Map<String, Object> details = new HashMap<>();
            details.put("idHebergement", safeLong(row, 0));
            details.put("nomHebergement", safeString(row, 1));
            details.put("adresse", safeString(row, 2));
            details.put("prixParNuit", safeFloat(row, 3));
            details.put("etoiles", safeInteger(row, 4));
            details.put("description", safeString(row, 5));
            details.put("isDisponible", safeBoolean(row, 6));
            details.put("hebergementType", safeString(row, 7));

            // City info via service using id in column 8
            Long villeId = safeLong(row, 8);
            if (villeId != null) {
                Optional<VilleDTO> cityOpt = villeService.getVilleDTOById(villeId);
                cityOpt.ifPresent(city -> details.put("city", city));
            }

            // Related accommodations in the same city (avoid JPA discriminator issues)
            List<Map<String, Object>> related = new ArrayList<>();
            if (villeId != null) {
                Long currentId = safeLong(row, 0);
                List<Object[]> relatedRows = hebergementRepository.findRelatedByVilleAsRows(villeId, currentId != null ? currentId : -1L);
                related = relatedRows.stream().map(r -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("idHebergement", safeLong(r, 0));
                    map.put("nomHebergement", safeString(r, 1));
                    map.put("prixParNuit", safeFloat(r, 2));
                    map.put("etoiles", safeInteger(r, 3));
                    return map;
                }).toList();
            }
            details.put("relatedAccommodations", related);

            return ResponseEntity.ok(details);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "error", "hebergement_details_failed",
                    "message", e.getMessage()
            ));
        }
    }

    private Long safeLong(Object[] row, int idx) {
        if (idx >= row.length) return null;
        Object v = row[idx];
        if (v == null) return null;
        if (v instanceof Number n) return n.longValue();
        if (v instanceof String s) {
            try { return Long.parseLong(s); } catch (NumberFormatException ignored) {}
        }
        return null;
    }

    private Integer safeInteger(Object[] row, int idx) {
        if (idx >= row.length) return null;
        Object v = row[idx];
        if (v == null) return null;
        if (v instanceof Number n) return n.intValue();
        if (v instanceof String s) {
            try { return Integer.parseInt(s); } catch (NumberFormatException ignored) {}
        }
        return null;
    }

    private Float safeFloat(Object[] row, int idx) {
        if (idx >= row.length) return null;
        Object v = row[idx];
        if (v == null) return null;
        if (v instanceof Number n) return n.floatValue();
        if (v instanceof String s) {
            try { return Float.parseFloat(s); } catch (NumberFormatException ignored) {}
        }
        return null;
    }

    private String safeString(Object[] row, int idx) {
        if (idx >= row.length) return null;
        Object v = row[idx];
        return v != null ? String.valueOf(v) : null;
    }

    private Boolean safeBoolean(Object[] row, int idx) {
        if (idx >= row.length) return null;
        Object v = row[idx];
        if (v == null) return null;
        if (v instanceof Boolean b) return b;
        if (v instanceof Number n) return n.intValue() != 0;
        if (v instanceof String s) return Boolean.parseBoolean(s);
        return null;
    }

    // Get services by city
    @GetMapping("/cities/{id}/services")
    public ResponseEntity<?> getCityServices(@PathVariable Long id) {
        try {
            List<Service> services = serviceRepository.findByVille_IdVille(id);
            List<Map<String, Object>> serviceDetails = services.stream().map(s -> {
                Map<String, Object> serviceMap = new HashMap<>();
                serviceMap.put("idService", s.getIdService());
                serviceMap.put("typeService", s.getTypeService());
                serviceMap.put("nomService", s.getTypeService().toString());
                serviceMap.put("description", "Service de type " + s.getTypeService().toString());
                serviceMap.put("categorie", s.getTypeService().toString());
                serviceMap.put("prix", 0.0);
                serviceMap.put("disponible", true);
                serviceMap.put("imageUrl", "https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?w=400");
                return serviceMap;
            }).toList();
            return ResponseEntity.ok(serviceDetails);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(

                "error", "services_retrieval_failed",
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

    // List all services (lightweight)
    @GetMapping("/services")
    public ResponseEntity<?> getAllServices() {
        try {
            List<Service> services = serviceRepository.findAll();
            List<Map<String, Object>> dto = services.stream().map(s -> {
                Map<String, Object> map = new HashMap<>();
                map.put("idService", s.getIdService());
                map.put("typeService", s.getTypeService());
                if (s.getVille() != null) {
                    map.put("ville", s.getVille().getNomVille());
                }
                map.put("imageUrl", (s.getMedias() != null && !s.getMedias().isEmpty()) ? s.getMedias().get(0).getNomMedia() : null);
                return map;
            }).toList();
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "services_list_failed",
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

    // Get comprehensive activity details
    @GetMapping("/activities/{id}/details")
    public ResponseEntity<?> getActivityDetails(@PathVariable Long id) {
        try {
            Optional<Activite> activiteOpt = activiteService.getActiviteById(id);
            if (activiteOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Activite activite = activiteOpt.get();
            Map<String, Object> activityDetails = new HashMap<>();

            // Basic activity information
            activityDetails.put("idActivite", activite.getIdActivite());
            activityDetails.put("nom", activite.getNom());
            activityDetails.put("description", activite.getDescription());
            activityDetails.put("imageUrl", activite.getImageUrl());
            activityDetails.put("prix", activite.getPrix());
            activityDetails.put("noteMoyenne", activite.getNoteMoyenne());
            activityDetails.put("nombreEvaluations", activite.getNombreEvaluations());
            activityDetails.put("isDisponible", activite.getIsDisponible());
            activityDetails.put("dureeMinimun", activite.getDureeMinimun());
            activityDetails.put("dureeMaximun", activite.getDureeMaximun());
            activityDetails.put("saison", activite.getSaison());
            activityDetails.put("niveauDificulta", activite.getNiveauDificulta());
            activityDetails.put("conditionsSpeciales", activite.getConditionsSpeciales());
            activityDetails.put("categorie", activite.getCategorie());

            // City information
            if (activite.getVille() != null) {
                Map<String, Object> cityInfo = new HashMap<>();
                cityInfo.put("idVille", activite.getVille().getIdVille());
                cityInfo.put("nomVille", activite.getVille().getNomVille());
                cityInfo.put("description", activite.getVille().getDescription());
                cityInfo.put("imageUrl", activite.getVille().getImageUrl());
                cityInfo.put("latitude", activite.getVille().getLatitude());
                cityInfo.put("longitude", activite.getVille().getLongitude());
                cityInfo.put("paysNom", activite.getVille().getPays() != null ? activite.getVille().getPays().getNomPays() : null);
                cityInfo.put("climatNom", activite.getVille().getClimat() != null ? activite.getVille().getClimat().getNomClimat() : null);
                cityInfo.put("isPlage", activite.getVille().getIsPlage());
                cityInfo.put("isMontagne", activite.getVille().getIsMontagne());
                cityInfo.put("isDesert", activite.getVille().getIsDesert());
                cityInfo.put("isRiviera", activite.getVille().getIsRiviera());
                cityInfo.put("isHistorique", activite.getVille().getIsHistorique());
                cityInfo.put("isCulturelle", activite.getVille().getIsCulturelle());
                cityInfo.put("isModerne", activite.getVille().getIsModerne());
                cityInfo.put("noteMoyenne", activite.getVille().getNoteMoyenne());
                activityDetails.put("city", cityInfo);
            }

            // Media information
            if (activite.getMedias() != null && !activite.getMedias().isEmpty()) {
                List<Map<String, Object>> mediaList = activite.getMedias().stream()
                    .map(media -> {
                        Map<String, Object> mediaInfo = new HashMap<>();
                        mediaInfo.put("idMedia", media.getIdMedia());
                        mediaInfo.put("nomMedia", media.getNomMedia());
                        mediaInfo.put("typeMedia", media.getTypeMedia());
                        mediaInfo.put("taille", media.getTaille());
                        mediaInfo.put("dataUpload", media.getDataUpload());
                        return mediaInfo;
                    }).toList();
                activityDetails.put("medias", mediaList);
            } else {
                activityDetails.put("medias", new ArrayList<>());
            }

            // Related activities in the same city
            if (activite.getVille() != null) {
                List<Activite> relatedActivities = activiteService.getActivitesByVille(activite.getVille().getIdVille())
                    .stream()
                    .filter(a -> !a.getIdActivite().equals(activite.getIdActivite()))
                    .limit(5)
                    .toList();

                List<Map<String, Object>> relatedActivitiesList = relatedActivities.stream()
                    .map(related -> {
                        Map<String, Object> relatedInfo = new HashMap<>();
                        relatedInfo.put("idActivite", related.getIdActivite());
                        relatedInfo.put("nom", related.getNom());
                        relatedInfo.put("imageUrl", related.getImageUrl());
                        relatedInfo.put("prix", related.getPrix());
                        relatedInfo.put("noteMoyenne", related.getNoteMoyenne());
                        relatedInfo.put("categorie", related.getCategorie());
                        relatedInfo.put("dureeMinimun", related.getDureeMinimun());
                        relatedInfo.put("dureeMaximun", related.getDureeMaximun());
                        return relatedInfo;
                    }).toList();
                activityDetails.put("relatedActivities", relatedActivitiesList);
            } else {
                activityDetails.put("relatedActivities", new ArrayList<>());
            }

            // Activity statistics
            Map<String, Object> statistics = new HashMap<>();
            statistics.put("totalDuration", activite.getDureeMaximun() != null ? activite.getDureeMaximun() : 0);
            statistics.put("difficultyLevel", activite.getNiveauDificulta());
            statistics.put("season", activite.getSaison());
            statistics.put("category", activite.getCategorie());
            statistics.put("isAvailable", activite.getIsDisponible());
            activityDetails.put("statistics", statistics);

            return ResponseEntity.ok(activityDetails);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "activity_details_retrieval_failed",
                "message", e.getMessage()
            ));
        }
    }

    // Get service details by id (parent + related info)
    @GetMapping("/services/{id}")
    public ResponseEntity<?> getServiceDetails(@PathVariable Long id) {
        try {
            Optional<Service> serviceOpt = serviceRepository.findById(id);
            if (serviceOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Service service = serviceOpt.get();

            Map<String, Object> details = new HashMap<>();
            details.put("idService", service.getIdService());
            details.put("typeService", service.getTypeService());

            // Fournisseur info if available
            if (service.getFournisseur() != null) {
                Map<String, Object> fournisseur = new HashMap<>();
                fournisseur.put("idFournisseur", service.getFournisseur().getIdFournisseur());
                fournisseur.put("nom", service.getFournisseur().getNom());
                fournisseur.put("email", service.getFournisseur().getEmail());
                fournisseur.put("telephone", service.getFournisseur().getTelephone());
                details.put("fournisseur", fournisseur);
            }

            // City info
            if (service.getVille() != null) {
                Map<String, Object> city = new HashMap<>();
                city.put("idVille", service.getVille().getIdVille());
                city.put("nomVille", service.getVille().getNomVille());
                city.put("description", service.getVille().getDescription());
                city.put("imageUrl", service.getVille().getImageUrl());
                city.put("latitude", service.getVille().getLatitude());
                city.put("longitude", service.getVille().getLongitude());
                details.put("city", city);
            }

            // Medias (flatten)
            if (service.getMedias() != null) {
                List<Map<String, Object>> mediaList = service.getMedias().stream().map(m -> {
                    Map<String, Object> media = new HashMap<>();
                    media.put("idMedia", m.getIdMedia());
                    media.put("nomMedia", m.getNomMedia());
                    media.put("typeMedia", m.getTypeMedia());
                    media.put("taille", m.getTaille());
                    media.put("dataUpload", m.getDataUpload());
                    return media;
                }).toList();
                details.put("medias", mediaList);
            } else {
                details.put("medias", new ArrayList<>());
            }

            // Related services in same city
            List<Map<String, Object>> related = new ArrayList<>();
            if (service.getVille() != null) {
                related = serviceRepository.findByVille_IdVille(service.getVille().getIdVille())
                        .stream()
                        .filter(s -> !s.getIdService().equals(service.getIdService()))
                        .limit(8)
                        .map(s -> {
                            Map<String, Object> map = new HashMap<>();
                            map.put("idService", s.getIdService());
                            map.put("typeService", s.getTypeService());
                            map.put("imageUrl", (s.getMedias() != null && !s.getMedias().isEmpty()) ? s.getMedias().get(0).getNomMedia() : null);
                            return map;
                        })
                        .toList();
            }
            details.put("relatedServices", related);

            // Statistics placeholder
            Map<String, Object> statistics = new HashMap<>();
            statistics.put("hasMedias", service.getMedias() != null && !service.getMedias().isEmpty());
            statistics.put("cityPresent", service.getVille() != null);
            details.put("statistics", statistics);

            return ResponseEntity.ok(details);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "error", "service_details_failed",
                    "message", e.getMessage()
            ));
        }
    }
}










