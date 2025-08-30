package com.tourisme.tourisme.controller.admin;

import com.tourisme.tourisme.entities.*;
import com.tourisme.tourisme.repository.*;
import com.tourisme.tourisme.service.ActiviteService;
import com.tourisme.tourisme.service.KeycloakUserService;
import com.tourisme.tourisme.service.UserMappingService;
import com.tourisme.tourisme.service.VilleService;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private VilleService villeService;

    @Autowired
    private ActiviteService activiteService;

    @Autowired
    private UserMappingService userMappingService;

    @Autowired
    private KeycloakUserService keycloakUserService;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private PaiementRepository paiementRepository;

    @Autowired
    private PaysRepository paysRepository;

    @Autowired
    private OffreTouristiqueRepository offreTouristiqueRepository;

    @Autowired
    private HebergementRepository hebergementRepository;

    @Autowired
    private MonumentRepository monumentRepository;

    @Autowired
    private SejourRepository sejourRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private AvisRepository avisRepository;

    @Autowired
    private PartenaireRepository partenaireRepository;

    // 1. Gestion des utilisateurs (recherche, blocage, suppression, export CSV)
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        
        try {
           
            List<Utilisateur> localUsers = utilisateurRepository.findAll();
            Map<String, Object> response = new HashMap<>();
            response.put("users", localUsers);
            response.put("localUsersCount", localUsers.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to retrieve users");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @GetMapping("/users/search")
    public ResponseEntity<?> searchUsers(@RequestParam String query) {
        try {
            // Search in Keycloak
            List<UserRepresentation> keycloakUsers = keycloakUserService.searchKeycloakUsers(query);
            
            // Search in local database
            List<Utilisateur> localUsers = utilisateurRepository.findAll().stream()
                .filter(user -> 
                    (user.getNom() != null && user.getNom().toLowerCase().contains(query.toLowerCase())) ||
                    (user.getPrenom() != null && user.getPrenom().toLowerCase().contains(query.toLowerCase())) ||
                    (user.getEmail() != null && user.getEmail().toLowerCase().contains(query.toLowerCase()))
                )
                .collect(Collectors.toList());
            
            // Combine results
            List<Map<String, Object>> combinedResults = keycloakUsers.stream()
                .map(keycloakUser -> {
                    Map<String, Object> userMap = keycloakUserService.convertToUserMap(keycloakUser);
                    
                    Optional<Utilisateur> localUser = localUsers.stream()
                        .filter(u -> u.getEmail() != null && u.getEmail().equals(keycloakUser.getEmail()))
                        .findFirst();
                    
                    if (localUser.isPresent()) {
                        userMap.put("localUser", Map.of(
                            "id", localUser.get().getIdUtilisateur(),
                            "nom", localUser.get().getNom() != null ? localUser.get().getNom() : "",
                            "prenom", localUser.get().getPrenom() != null ? localUser.get().getPrenom() : "",
                            "role", localUser.get().getRole() != null ? localUser.get().getRole().getNomRole() : "USER"
                        ));
                    }
                    
                    return userMap;
                })
                .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("users", combinedResults);
            response.put("query", query);
            response.put("resultCount", combinedResults.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to search users");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @PostMapping("/users/{keycloakId}/block")
    public ResponseEntity<?> blockUser(@PathVariable String keycloakId) {
        try {
            // Toggle user enabled status in Keycloak
            UserRepresentation user = keycloakUserService.getKeycloakUserById(keycloakId);
            boolean newStatus = !user.isEnabled();
            keycloakUserService.toggleUserEnabled(keycloakId, newStatus);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", newStatus ? "User enabled successfully" : "User blocked successfully");
            response.put("userId", keycloakId);
            response.put("enabled", newStatus);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to toggle user status");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @DeleteMapping("/users/{keycloakId}")
    public ResponseEntity<?> deleteUser(@PathVariable String keycloakId) {
        try {
            // Get user info before deletion
            UserRepresentation user = keycloakUserService.getKeycloakUserById(keycloakId);
            String userEmail = user.getEmail();
            
            // Delete from Keycloak
            keycloakUserService.deleteKeycloakUser(keycloakId);
            
            // Delete from local database if exists
            if (userEmail != null) {
                Optional<Utilisateur> localUser = utilisateurRepository.findByEmail(userEmail);
                if (localUser.isPresent()) {
                    utilisateurRepository.delete(localUser.get());
                }
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User deleted successfully");
            response.put("deletedUserId", keycloakId);
            response.put("deletedEmail", userEmail);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to delete user");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @GetMapping("/users/export-csv")
    public ResponseEntity<?> exportUsersCSV() {
        try {
            // Get users from Keycloak
            List<UserRepresentation> keycloakUsers = keycloakUserService.getAllKeycloakUsers();
            List<Utilisateur> localUsers = utilisateurRepository.findAll();
            
            // Create CSV content
            StringWriter writer = new StringWriter();
            writer.append("Keycloak ID,Email,First Name,Last Name,Username,Enabled,Email Verified,Creation Date,Local ID,Phone,Address,Role\n");
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            for (UserRepresentation keycloakUser : keycloakUsers) {
                // Find corresponding local user
                Optional<Utilisateur> localUser = localUsers.stream()
                    .filter(u -> u.getEmail() != null && u.getEmail().equals(keycloakUser.getEmail()))
                    .findFirst();
                
                writer.append(escapeCSV(keycloakUser.getId())).append(",");
                writer.append(escapeCSV(keycloakUser.getEmail())).append(",");
                writer.append(escapeCSV(keycloakUser.getFirstName())).append(",");
                writer.append(escapeCSV(keycloakUser.getLastName())).append(",");
                writer.append(escapeCSV(keycloakUser.getUsername())).append(",");
                writer.append(keycloakUser.isEnabled() ? "Yes" : "No").append(",");
                writer.append(keycloakUser.isEmailVerified() ? "Yes" : "No").append(",");
                writer.append(keycloakUser.getCreatedTimestamp() != null ? 
                    dateFormat.format(new Date(keycloakUser.getCreatedTimestamp())) : "").append(",");
                
                if (localUser.isPresent()) {
                    Utilisateur u = localUser.get();
                    writer.append(u.getIdUtilisateur().toString()).append(",");
                    writer.append(escapeCSV(u.getTelephone())).append(",");
                    writer.append(escapeCSV(u.getAdresse())).append(",");
                    writer.append(u.getRole() != null ? escapeCSV(u.getRole().getNomRole()) : "").append("\n");
                } else {
                    writer.append(",,,\n");
                }
            }
            
            // Prepare file download
            byte[] csvBytes = writer.toString().getBytes("UTF-8");
            ByteArrayInputStream bis = new ByteArrayInputStream(csvBytes);
            
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users_export.csv");
            headers.add(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8");
            
            return ResponseEntity.ok()
                .headers(headers)
                .contentLength(csvBytes.length)
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(new InputStreamResource(bis));
                
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to export CSV");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    
    private String escapeCSV(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }



    // 3. Gestion des contenus: ajout / modification (activités, monuments, villes...)
    @GetMapping("/content/villes")
    public ResponseEntity<List<Ville>> getAllVilles() {
        return ResponseEntity.ok(villeService.getAllVilles());
    }

    @PostMapping("/content/villes")
    public ResponseEntity<Ville> createVille(@RequestBody Ville ville) {
        return ResponseEntity.ok(villeService.saveVille(ville));
    }

    @PutMapping("/content/villes/{id}")
    public ResponseEntity<Ville> updateVille(@PathVariable Long id, @RequestBody Ville ville) {
        ville.setIdVille(id);
        return ResponseEntity.ok(villeService.saveVille(ville));
    }

    @DeleteMapping("/content/villes/{id}")
    public ResponseEntity<?> deleteVille(@PathVariable Long id) {
        villeService.deleteVille(id);
        return ResponseEntity.ok("Ville deleted successfully");
    }

    @GetMapping("/content/activites")
    public ResponseEntity<List<Activite>> getAllActivites() {
        return ResponseEntity.ok(activiteService.getAllActivites());
    }

    @PostMapping("/content/activites")
    public ResponseEntity<Activite> createActivite(@RequestBody Activite activite) {
        return ResponseEntity.ok(activiteService.saveActivite(activite));
    }

    @PutMapping("/content/activites/{id}")
    public ResponseEntity<Activite> updateActivite(@PathVariable Long id, @RequestBody Activite activite) {
        activite.setIdActivite(id);
        return ResponseEntity.ok(activiteService.saveActivite(activite));
    }

    @DeleteMapping("/content/activites/{id}")
    public ResponseEntity<?> deleteActivite(@PathVariable Long id) {
        activiteService.deleteActivite(id);
        return ResponseEntity.ok("Activite deleted successfully");
    }

    // ===== CONTENT MANAGEMENT - PAYS =====
    @GetMapping("/content/pays")
    public ResponseEntity<List<Pays>> getAllPays() {
        return ResponseEntity.ok(paysRepository.findAll());
    }

    @GetMapping("/content/pays/{id}")
    public ResponseEntity<Pays> getPaysById(@PathVariable Long id) {
        return paysRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/content/pays")
    public ResponseEntity<Pays> createPays(@RequestBody Pays pays) {
        return ResponseEntity.ok(paysRepository.save(pays));
    }

    @PutMapping("/content/pays/{id}")
    public ResponseEntity<Pays> updatePays(@PathVariable Long id, @RequestBody Pays pays) {
        if (!paysRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        pays.setIdPays(id);
        return ResponseEntity.ok(paysRepository.save(pays));
    }

    @DeleteMapping("/content/pays/{id}")
    public ResponseEntity<?> deletePays(@PathVariable Long id) {
        if (!paysRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        paysRepository.deleteById(id);
        return ResponseEntity.ok("Pays deleted successfully");
    }

    // ===== OFFRE TOURISTIQUE MANAGEMENT =====
    @GetMapping("/offres")
    public ResponseEntity<List<OffreTouristique>> getAllOffres() {
        return ResponseEntity.ok(offreTouristiqueRepository.findAll());
    }

    @GetMapping("/offres/{id}")
    public ResponseEntity<OffreTouristique> getOffreById(@PathVariable Long id) {
        return offreTouristiqueRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/offres")
    public ResponseEntity<OffreTouristique> createOffre(@RequestBody OffreTouristique offre) {
        return ResponseEntity.ok(offreTouristiqueRepository.save(offre));
    }

    @PutMapping("/offres/{id}")
    public ResponseEntity<OffreTouristique> updateOffre(@PathVariable Long id, @RequestBody OffreTouristique offre) {
        if (!offreTouristiqueRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        offre.setIdOffre(id);
        return ResponseEntity.ok(offreTouristiqueRepository.save(offre));
    }

    @DeleteMapping("/offres/{id}")
    public ResponseEntity<?> deleteOffre(@PathVariable Long id) {
        if (!offreTouristiqueRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        offreTouristiqueRepository.deleteById(id);
        return ResponseEntity.ok("Offre deleted successfully");
    }

    // ===== HEBERGEMENT MANAGEMENT =====
    @GetMapping("/hebergements")
    public ResponseEntity<List<Hebergement>> getAllHebergements() {
        return ResponseEntity.ok(hebergementRepository.findAll());
    }

    @GetMapping("/hebergements/{id}")
    public ResponseEntity<Hebergement> getHebergementById(@PathVariable Long id) {
        return hebergementRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/hebergements")
    public ResponseEntity<Hebergement> createHebergement(@RequestBody Hebergement hebergement) {
        return ResponseEntity.ok(hebergementRepository.save(hebergement));
    }

    @PutMapping("/hebergements/{id}")
    public ResponseEntity<Hebergement> updateHebergement(@PathVariable Long id, @RequestBody Hebergement hebergement) {
        if (!hebergementRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        hebergement.setIdHebergement(id);
        return ResponseEntity.ok(hebergementRepository.save(hebergement));
    }

    @DeleteMapping("/hebergements/{id}")
    public ResponseEntity<?> deleteHebergement(@PathVariable Long id) {
        if (!hebergementRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        hebergementRepository.deleteById(id);
        return ResponseEntity.ok("Hebergement deleted successfully");
    }

    // ===== PRODUIT MANAGEMENT =====
    @GetMapping("/produits")
    public ResponseEntity<List<Produit>> getAllProduits() {
        return ResponseEntity.ok(produitRepository.findAll());
    }

    @GetMapping("/produits/{id}")
    public ResponseEntity<Produit> getProduitById(@PathVariable Long id) {
        return produitRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/produits")
    public ResponseEntity<Produit> createProduit(@RequestBody Produit produit) {
        return ResponseEntity.ok(produitRepository.save(produit));
    }

    @PutMapping("/produits/{id}")
    public ResponseEntity<Produit> updateProduit(@PathVariable Long id, @RequestBody Produit produit) {
        if (!produitRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        produit.setIdProduit(id);
        return ResponseEntity.ok(produitRepository.save(produit));
    }

    @DeleteMapping("/produits/{id}")
    public ResponseEntity<?> deleteProduit(@PathVariable Long id) {
        if (!produitRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        produitRepository.deleteById(id);
        return ResponseEntity.ok("Produit deleted successfully");
    }

    // ===== MONUMENT MANAGEMENT =====
    @GetMapping("/monuments")
    public ResponseEntity<List<Monument>> getAllMonuments() {
        return ResponseEntity.ok(monumentRepository.findAll());
    }

    @GetMapping("/monuments/{id}")
    public ResponseEntity<Monument> getMonumentById(@PathVariable Long id) {
        return monumentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/monuments")
    public ResponseEntity<Monument> createMonument(@RequestBody Monument monument) {
        return ResponseEntity.ok(monumentRepository.save(monument));
    }

    @PutMapping("/monuments/{id}")
    public ResponseEntity<Monument> updateMonument(@PathVariable Long id, @RequestBody Monument monument) {
        if (!monumentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        monument.setIdMonument(id);
        return ResponseEntity.ok(monumentRepository.save(monument));
    }

    @DeleteMapping("/monuments/{id}")
    public ResponseEntity<?> deleteMonument(@PathVariable Long id) {
        if (!monumentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        monumentRepository.deleteById(id);
        return ResponseEntity.ok("Monument deleted successfully");
    }

    // ===== SEJOUR MANAGEMENT =====
    @GetMapping("/sejours")
    public ResponseEntity<List<Sejour>> getAllSejours() {
        return ResponseEntity.ok(sejourRepository.findAll());
    }

    @GetMapping("/sejours/{id}")
    public ResponseEntity<Sejour> getSejourById(@PathVariable Long id) {
        return sejourRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/sejours")
    public ResponseEntity<Sejour> createSejour(@RequestBody Sejour sejour) {
        return ResponseEntity.ok(sejourRepository.save(sejour));
    }

    @PutMapping("/sejours/{id}")
    public ResponseEntity<Sejour> updateSejour(@PathVariable Long id, @RequestBody Sejour sejour) {
        if (!sejourRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        sejour.setIdSejour(id);
        return ResponseEntity.ok(sejourRepository.save(sejour));
    }

    @DeleteMapping("/sejours/{id}")
    public ResponseEntity<?> deleteSejour(@PathVariable Long id) {
        if (!sejourRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        sejourRepository.deleteById(id);
        return ResponseEntity.ok("Sejour deleted successfully");
    }

    // ===== RESERVATION MANAGEMENT =====
    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return ResponseEntity.ok(reservationRepository.findAll());
    }

    @GetMapping("/reservations/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        return reservationRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/reservations")
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
        return ResponseEntity.ok(reservationRepository.save(reservation));
    }

    @PutMapping("/reservations/{id}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable Long id, @RequestBody Reservation reservation) {
        if (!reservationRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        reservation.setIdReservation(id);
        return ResponseEntity.ok(reservationRepository.save(reservation));
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long id) {
        if (!reservationRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        reservationRepository.deleteById(id);
        return ResponseEntity.ok("Reservation deleted successfully");
    }

    // ===== PAIEMENT MANAGEMENT =====
    @GetMapping("/paiements")
    public ResponseEntity<List<Paiement>> getAllPaiements() {
        return ResponseEntity.ok(paiementRepository.findAll());
    }

    @GetMapping("/paiements/{id}")
    public ResponseEntity<Paiement> getPaiementById(@PathVariable Long id) {
        return paiementRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/paiements")
    public ResponseEntity<Paiement> createPaiement(@RequestBody Paiement paiement) {
        return ResponseEntity.ok(paiementRepository.save(paiement));
    }

    @PutMapping("/paiements/{id}")
    public ResponseEntity<Paiement> updatePaiement(@PathVariable Long id, @RequestBody Paiement paiement) {
        if (!paiementRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        paiement.setIdPaiement(id);
        return ResponseEntity.ok(paiementRepository.save(paiement));
    }

    @DeleteMapping("/paiements/{id}")
    public ResponseEntity<?> deletePaiement(@PathVariable Long id) {
        if (!paiementRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        paiementRepository.deleteById(id);
        return ResponseEntity.ok("Paiement deleted successfully");
    }

    // ===== MEDIA MANAGEMENT =====
    @GetMapping("/medias")
    public ResponseEntity<List<Media>> getAllMedias() {
        return ResponseEntity.ok(mediaRepository.findAll());
    }

    @GetMapping("/medias/{id}")
    public ResponseEntity<Media> getMediaById(@PathVariable Long id) {
        return mediaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/medias")
    public ResponseEntity<Media> createMedia(@RequestBody Media media) {
        return ResponseEntity.ok(mediaRepository.save(media));
    }

    @PutMapping("/medias/{id}")
    public ResponseEntity<Media> updateMedia(@PathVariable Long id, @RequestBody Media media) {
        if (!mediaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        media.setIdMedia(id);
        return ResponseEntity.ok(mediaRepository.save(media));
    }

    @DeleteMapping("/medias/{id}")
    public ResponseEntity<?> deleteMedia(@PathVariable Long id) {
        if (!mediaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        mediaRepository.deleteById(id);
        return ResponseEntity.ok("Media deleted successfully");
    }

    // ===== PARTENAIRE MANAGEMENT =====
    @GetMapping("/providers")
    public ResponseEntity<List<Partenaire>> getAllPartenaires() {
        return ResponseEntity.ok(partenaireRepository.findAll());
    }

    @GetMapping("/providers/{id}")
    public ResponseEntity<Partenaire> getPartenaireById(@PathVariable Long id) {
        return partenaireRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/providers")
    public ResponseEntity<Partenaire> createPartenaire(@RequestBody Partenaire partenaire) {
        return ResponseEntity.ok(partenaireRepository.save(partenaire));
    }

    @PutMapping("/providers/{id}")
    public ResponseEntity<Partenaire> updatePartenaire(@PathVariable Long id, @RequestBody Partenaire partenaire) {
        if (!partenaireRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        partenaire.setIdPartenaire(id);
        return ResponseEntity.ok(partenaireRepository.save(partenaire));
    }

    @DeleteMapping("/providers/{id}")
    public ResponseEntity<?> deletePartenaire(@PathVariable Long id) {
        if (!partenaireRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        partenaireRepository.deleteById(id);
        return ResponseEntity.ok("Partenaire deleted successfully");
    }

    @PostMapping("/providers/{id}/validate")
    public ResponseEntity<?> validateProvider(@PathVariable Long id) {
        return partenaireRepository.findById(id)
                .map(partenaire -> {
                    partenaire.setVerifier(true);
                    partenaireRepository.save(partenaire);
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "Provider validated successfully");
                    response.put("providerId", id);
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/providers/{id}/reject")
    public ResponseEntity<?> rejectProvider(@PathVariable Long id) {
        return partenaireRepository.findById(id)
                .map(partenaire -> {
                    partenaire.setVerifier(false);
                    partenaireRepository.save(partenaire);
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "Provider rejected successfully");
                    response.put("providerId", id);
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ===== AVIS MANAGEMENT =====
    @GetMapping("/avis")
    public ResponseEntity<List<Avis>> getAllAvis() {
        return ResponseEntity.ok(avisRepository.findAll());
    }

    @GetMapping("/avis/{id}")
    public ResponseEntity<Avis> getAvisById(@PathVariable Long id) {
        return avisRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/avis")
    public ResponseEntity<Avis> createAvis(@RequestBody Avis avis) {
        return ResponseEntity.ok(avisRepository.save(avis));
    }

    @PutMapping("/avis/{id}")
    public ResponseEntity<Avis> updateAvis(@PathVariable Long id, @RequestBody Avis avis) {
        if (!avisRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        avis.setIdAvis(id);
        return ResponseEntity.ok(avisRepository.save(avis));
    }

    @DeleteMapping("/avis/{id}")
    public ResponseEntity<?> deleteAvis(@PathVariable Long id) {
        if (!avisRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        avisRepository.deleteById(id);
        return ResponseEntity.ok("Avis deleted successfully");
    }

    @PostMapping("/avis/{id}/approve")
    public ResponseEntity<?> approveAvis(@PathVariable Long id) {
        return avisRepository.findById(id)
                .map(avis -> {
                    avis.setVerifie(true);
                    avisRepository.save(avis);
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "Avis approved successfully");
                    response.put("avisId", id);
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/avis/{id}/reject")
    public ResponseEntity<?> rejectAvis(@PathVariable Long id) {
        return avisRepository.findById(id)
                .map(avis -> {
                    avis.setVerifie(false);
                    avisRepository.save(avis);
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "Avis rejected successfully");
                    response.put("avisId", id);
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ===== TOKEN VALIDATION =====
    @GetMapping("/validate-token")
    public ResponseEntity<?> validateToken() {
        Map<String, Object> response = new HashMap<>();
        response.put("valid", true);
        response.put("message", "Token is valid");
        response.put("timestamp", new Date());
        return ResponseEntity.ok(response);
    }

    // 4. Gestion des produits boutique (prix, stock, visibilité)
    @GetMapping("/shop/products")
    public ResponseEntity<?> getAllProducts() {
        try {
            List<Produit> products = produitRepository.findAll();
            
            List<Map<String, Object>> productList = products.stream()
                .map(product -> {
                    Map<String, Object> productMap = new HashMap<>();
                    productMap.put("id", product.getIdProduit());
                    productMap.put("nom", product.getNom());
                    productMap.put("description", product.getDescription());
                    productMap.put("prix", product.getPrix());
                    productMap.put("prixUnitaire", product.getPrixUnitaire());
                    productMap.put("stockDisponible", product.getStockDisponible());
                    productMap.put("isDisponible", product.getIsDisponible());
                    productMap.put("origine", product.getOrigine());
                    productMap.put("authentique", product.getAuthentique());
                    productMap.put("exclusif", product.getExclusif());
                    productMap.put("image", product.getImage());
                    productMap.put("dateCreation", product.getDateCreation());
                    productMap.put("dateExpiration", product.getDateExpiration());
                    productMap.put("taille", product.getTaille());
                    productMap.put("poids", product.getPoids());
                    return productMap;
                })
                .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("products", productList);
            response.put("totalCount", products.size());
            response.put("availableCount", products.stream().mapToInt(p -> p.getIsDisponible() ? 1 : 0).sum());
            response.put("totalStock", produitRepository.getTotalStock());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to retrieve products");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @PutMapping("/shop/products/{id}/price")
    public ResponseEntity<?> updateProductPrice(@PathVariable Long id, @RequestParam Float newPrice) {
        try {
            Optional<Produit> productOpt = produitRepository.findById(id);
            if (!productOpt.isPresent()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Product not found");
                errorResponse.put("productId", id);
                return ResponseEntity.status(404).body(errorResponse);
            }
            
            Produit product = productOpt.get();
            Float oldPrice = product.getPrix();
            product.setPrix(newPrice);
            produitRepository.save(product);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Price updated successfully");
            response.put("productId", id);
            response.put("oldPrice", oldPrice);
            response.put("newPrice", newPrice);
            response.put("productName", product.getNom());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to update price");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @PutMapping("/shop/products/{id}/stock")
    public ResponseEntity<?> updateProductStock(@PathVariable Long id, @RequestParam Integer newStock) {
        try {
            Optional<Produit> productOpt = produitRepository.findById(id);
            if (!productOpt.isPresent()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Product not found");
                errorResponse.put("productId", id);
                return ResponseEntity.status(404).body(errorResponse);
            }
            
            Produit product = productOpt.get();
            Integer oldStock = product.getStockDisponible();
            product.setStockDisponible(newStock);
            produitRepository.save(product);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Stock updated successfully");
            response.put("productId", id);
            response.put("oldStock", oldStock);
            response.put("newStock", newStock);
            response.put("productName", product.getNom());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to update stock");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @PutMapping("/shop/products/{id}/visibility")
    public ResponseEntity<?> updateProductVisibility(@PathVariable Long id, @RequestParam Boolean visible) {
        try {
            Optional<Produit> productOpt = produitRepository.findById(id);
            if (!productOpt.isPresent()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Product not found");
                errorResponse.put("productId", id);
                return ResponseEntity.status(404).body(errorResponse);
            }
            
            Produit product = productOpt.get();
            Boolean oldVisibility = product.getIsDisponible();
            product.setIsDisponible(visible);
            produitRepository.save(product);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Visibility updated successfully");
            response.put("productId", id);
            response.put("oldVisibility", oldVisibility);
            response.put("newVisibility", visible);
            response.put("productName", product.getNom());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to update visibility");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    // 5. Consultation des statistiques (intégration + affichage dynamique)
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        try {
        Map<String, Object> stats = new HashMap<>();
            
            // User statistics
            int keycloakUsersCount = keycloakUserService.getUsersCount();
            long localUsersCount = utilisateurRepository.count();
            
            // Content statistics
            long totalVilles = villeService.getAllVilles().size();
            long totalActivites = activiteService.getAllActivites().size();
            
            // Product statistics
            long totalProducts = produitRepository.count();
            Long totalStock = produitRepository.getTotalStock();
            Long availableProducts = produitRepository.countAvailableProducts();
            
            // Revenue statistics
            Double totalRevenue = paiementRepository.getTotalRevenue();
            long totalTransactions = paiementRepository.count();
            
            stats.put("users", Map.of(
                "totalKeycloakUsers", keycloakUsersCount,
                "totalLocalUsers", localUsersCount
            ));
            
            stats.put("content", Map.of(
                "totalVilles", totalVilles,
                "totalActivites", totalActivites
            ));
            
            stats.put("products", Map.of(
                "totalProducts", totalProducts,
                "availableProducts", availableProducts != null ? availableProducts : 0,
                "totalStock", totalStock != null ? totalStock : 0
            ));
            
            stats.put("revenue", Map.of(
                "totalRevenue", totalRevenue != null ? totalRevenue : 0.0,
                "totalTransactions", totalTransactions
            ));
            
        return ResponseEntity.ok(stats);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to retrieve statistics");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @GetMapping("/statistics/users")
    public ResponseEntity<?> getUserStatistics() {
        try {
            Map<String, Object> userStats = new HashMap<>();
            
            // Get user count from Keycloak
            int totalKeycloakUsers = keycloakUserService.getUsersCount();
            
            // Get local user statistics
            List<Utilisateur> localUsers = utilisateurRepository.findAll();
            long totalLocalUsers = localUsers.size();
            
            // Calculate registration trends (example: last 30 days)
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -30);
            Date thirtyDaysAgo = calendar.getTime();
            
            long recentRegistrations = localUsers.stream()
                .filter(user -> user.getDateInscription() != null && user.getDateInscription().after(thirtyDaysAgo))
                .count();
            
            // Role distribution
            Map<String, Long> roleDistribution = localUsers.stream()
                .filter(user -> user.getRole() != null)
                .collect(Collectors.groupingBy(
                    user -> user.getRole().getNomRole(),
                    Collectors.counting()
                ));
            
            userStats.put("totalKeycloakUsers", totalKeycloakUsers);
            userStats.put("totalLocalUsers", totalLocalUsers);
            userStats.put("recentRegistrations", recentRegistrations);
            userStats.put("roleDistribution", roleDistribution);
            
            return ResponseEntity.ok(userStats);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to retrieve user statistics");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @GetMapping("/statistics/revenue")
    public ResponseEntity<?> getRevenueStatistics() {
        try {
            Map<String, Object> revenueStats = new HashMap<>();
            
            // Total revenue
            Double totalRevenue = paiementRepository.getTotalRevenue();
            
            // Revenue for current month
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date monthStart = calendar.getTime();
            Date now = new Date();
            
            Double monthlyRevenue = paiementRepository.getRevenueByDateRange(monthStart, now);
            Long monthlyTransactions = paiementRepository.getTransactionCountByDateRange(monthStart, now);
            
            // Revenue for last 7 days
            calendar.setTime(now);
            calendar.add(Calendar.DAY_OF_MONTH, -7);
            Date weekStart = calendar.getTime();
            
            Double weeklyRevenue = paiementRepository.getRevenueByDateRange(weekStart, now);
            Long weeklyTransactions = paiementRepository.getTransactionCountByDateRange(weekStart, now);
            
            // Payment method statistics
            List<Object[]> paymentMethodStats = paiementRepository.getPaymentMethodStatistics();
            Map<String, Map<String, Object>> paymentMethods = new HashMap<>();
            
            for (Object[] stat : paymentMethodStats) {
                String method = (String) stat[0];
                Long count = (Long) stat[1];
                Double amount = stat[2] instanceof Double ? (Double) stat[2] : ((Number) stat[2]).doubleValue();
                
                paymentMethods.put(method, Map.of(
                    "count", count,
                    "totalAmount", amount
                ));
            }
            
            revenueStats.put("totalRevenue", totalRevenue != null ? totalRevenue : 0.0);
            revenueStats.put("monthlyRevenue", monthlyRevenue != null ? monthlyRevenue : 0.0);
            revenueStats.put("weeklyRevenue", weeklyRevenue != null ? weeklyRevenue : 0.0);
            revenueStats.put("monthlyTransactions", monthlyTransactions != null ? monthlyTransactions : 0);
            revenueStats.put("weeklyTransactions", weeklyTransactions != null ? weeklyTransactions : 0);
            revenueStats.put("paymentMethodBreakdown", paymentMethods);
            
            return ResponseEntity.ok(revenueStats);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to retrieve revenue statistics");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    // 6. Gestion des transactions (historique paiements, remboursements, commissions)
    @GetMapping("/transactions")
    public ResponseEntity<?> getAllTransactions() {
        try {
            List<Paiement> transactions = paiementRepository.findAll();
            
            List<Map<String, Object>> transactionList = transactions.stream()
                .map(paiement -> {
                    Map<String, Object> transactionMap = new HashMap<>();
                    transactionMap.put("id", paiement.getIdPaiement());
                    transactionMap.put("numeroTransaction", paiement.getNumeroTransaction());
                    transactionMap.put("montant", paiement.getMontant());
                    transactionMap.put("methodePaiement", paiement.getMethodePaiement());
                    transactionMap.put("datePaiement", paiement.getDatePaiement());
                    
                    if (paiement.getUtilisateur() != null) {
                        transactionMap.put("utilisateur", Map.of(
                            "id", paiement.getUtilisateur().getIdUtilisateur(),
                            "nom", paiement.getUtilisateur().getNom(),
                            "prenom", paiement.getUtilisateur().getPrenom(),
                            "email", paiement.getUtilisateur().getEmail()
                        ));
                    }
                    
                    return transactionMap;
                })
                .sorted((t1, t2) -> {
                    Date date1 = (Date) t1.get("datePaiement");
                    Date date2 = (Date) t2.get("datePaiement");
                    return date2.compareTo(date1); // Sort by date descending
                })
                .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("transactions", transactionList);
            response.put("totalCount", transactions.size());
            response.put("totalAmount", transactions.stream().mapToDouble(p -> p.getMontant() != null ? p.getMontant() : 0.0).sum());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to retrieve transactions");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @GetMapping("/transactions/payments")
    public ResponseEntity<?> getPaymentHistory(@RequestParam(required = false) String methodePaiement,
                                              @RequestParam(required = false) String startDate,
                                              @RequestParam(required = false) String endDate) {
        try {
            List<Paiement> payments;
            
            if (startDate != null && endDate != null) {
                // Parse dates and filter
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date start = dateFormat.parse(startDate);
                Date end = dateFormat.parse(endDate);
                payments = paiementRepository.findByDatePaiementBetween(start, end);
            } else if (methodePaiement != null) {
                payments = paiementRepository.findByMethodePaiement(methodePaiement);
            } else {
                payments = paiementRepository.findAll();
            }
            
            List<Map<String, Object>> paymentHistory = payments.stream()
                .map(paiement -> {
                    Map<String, Object> paymentMap = new HashMap<>();
                    paymentMap.put("id", paiement.getIdPaiement());
                    paymentMap.put("numeroTransaction", paiement.getNumeroTransaction());
                    paymentMap.put("montant", paiement.getMontant());
                    paymentMap.put("methodePaiement", paiement.getMethodePaiement());
                    paymentMap.put("datePaiement", paiement.getDatePaiement());
                    
                    if (paiement.getUtilisateur() != null) {
                        paymentMap.put("client", Map.of(
                            "nom", paiement.getUtilisateur().getNom(),
                            "prenom", paiement.getUtilisateur().getPrenom(),
                            "email", paiement.getUtilisateur().getEmail()
                        ));
                    }
                    
                    return paymentMap;
                })
                .sorted((p1, p2) -> {
                    Date date1 = (Date) p1.get("datePaiement");
                    Date date2 = (Date) p2.get("datePaiement");
                    return date2.compareTo(date1);
                })
                .collect(Collectors.toList());
            
            // Payment method breakdown
            Map<String, Map<String, Object>> methodBreakdown = new HashMap<>();
            List<Object[]> paymentMethodStats = paiementRepository.getPaymentMethodStatistics();
            
            for (Object[] stat : paymentMethodStats) {
                String method = (String) stat[0];
                Long count = (Long) stat[1];
                Double amount = stat[2] instanceof Double ? (Double) stat[2] : ((Number) stat[2]).doubleValue();
                
                methodBreakdown.put(method, Map.of(
                    "transactionCount", count,
                    "totalAmount", amount
                ));
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("payments", paymentHistory);
            response.put("totalPayments", payments.size());
            response.put("totalAmount", payments.stream().mapToDouble(p -> p.getMontant() != null ? p.getMontant() : 0.0).sum());
            response.put("methodBreakdown", methodBreakdown);
            response.put("filters", Map.of(
                "methodePaiement", methodePaiement,
                "startDate", startDate,
                "endDate", endDate
            ));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to retrieve payment history");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @PostMapping("/transactions/{id}/refund")
    public ResponseEntity<?> processRefund(@PathVariable Long id, @RequestParam(required = false) String reason) {
        try {
            Optional<Paiement> paiementOpt = paiementRepository.findById(id);
            if (!paiementOpt.isPresent()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Transaction not found");
                errorResponse.put("transactionId", id);
                return ResponseEntity.status(404).body(errorResponse);
            }
            
            Paiement paiement = paiementOpt.get();
            
            // Create a refund record (negative transaction)
            Paiement refund = new Paiement();
            refund.setMontant(-paiement.getMontant()); // Negative amount for refund
            refund.setMethodePaiement("REFUND_" + paiement.getMethodePaiement());
            refund.setNumeroTransaction("REF_" + paiement.getNumeroTransaction());
            refund.setUtilisateur(paiement.getUtilisateur());
            refund.setDatePaiement(new Date());
            
            Paiement savedRefund = paiementRepository.save(refund);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Refund processed successfully");
            response.put("originalTransactionId", id);
            response.put("refundTransactionId", savedRefund.getIdPaiement());
            response.put("refundAmount", savedRefund.getMontant());
            response.put("refundDate", savedRefund.getDatePaiement());
            response.put("reason", reason != null ? reason : "Admin refund");
            
            if (paiement.getUtilisateur() != null) {
                response.put("refundedTo", Map.of(
                    "nom", paiement.getUtilisateur().getNom(),
                    "prenom", paiement.getUtilisateur().getPrenom(),
                    "email", paiement.getUtilisateur().getEmail()
                ));
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to process refund");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @GetMapping("/transactions/commissions")
    public ResponseEntity<?> getCommissions(@RequestParam(required = false) String startDate,
                                           @RequestParam(required = false) String endDate) {
        try {
            List<Paiement> transactions;
            
            if (startDate != null && endDate != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date start = dateFormat.parse(startDate);
                Date end = dateFormat.parse(endDate);
                transactions = paiementRepository.findByDatePaiementBetween(start, end);
            } else {
                // Get last 30 days by default
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, -30);
                Date thirtyDaysAgo = calendar.getTime();
                transactions = paiementRepository.findByDatePaiementBetween(thirtyDaysAgo, new Date());
            }
            
            // Calculate commissions (example: 5% of each transaction)
            double commissionRate = 0.05; // 5%
            
            List<Map<String, Object>> commissionDetails = transactions.stream()
                .filter(paiement -> paiement.getMontant() != null && paiement.getMontant() > 0) // Exclude refunds
                .map(paiement -> {
                    double commission = paiement.getMontant() * commissionRate;
                    
                    Map<String, Object> commissionMap = new HashMap<>();
                    commissionMap.put("transactionId", paiement.getIdPaiement());
                    commissionMap.put("transactionAmount", paiement.getMontant());
                    commissionMap.put("commissionAmount", commission);
                    commissionMap.put("commissionRate", commissionRate);
                    commissionMap.put("transactionDate", paiement.getDatePaiement());
                    commissionMap.put("paymentMethod", paiement.getMethodePaiement());
                    
                    if (paiement.getUtilisateur() != null) {
                        commissionMap.put("client", Map.of(
                            "nom", paiement.getUtilisateur().getNom(),
                            "email", paiement.getUtilisateur().getEmail()
                        ));
                    }
                    
                    return commissionMap;
                })
                .sorted((c1, c2) -> {
                    Date date1 = (Date) c1.get("transactionDate");
                    Date date2 = (Date) c2.get("transactionDate");
                    return date2.compareTo(date1);
                })
                .collect(Collectors.toList());
            
            double totalTransactionAmount = transactions.stream()
                .filter(paiement -> paiement.getMontant() != null && paiement.getMontant() > 0)
                .mapToDouble(Paiement::getMontant)
                .sum();
            
            double totalCommissions = totalTransactionAmount * commissionRate;
            
            Map<String, Object> response = new HashMap<>();
            response.put("commissions", commissionDetails);
            response.put("totalTransactions", transactions.size());
            response.put("totalTransactionAmount", totalTransactionAmount);
            response.put("totalCommissions", totalCommissions);
            response.put("commissionRate", commissionRate);
            response.put("period", Map.of(
                "startDate", startDate,
                "endDate", endDate
            ));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to calculate commissions");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
} 