package com.tourisme.tourisme.controller;

import com.tourisme.tourisme.config.JwtTokenUtil;
import com.tourisme.tourisme.entities.Touriste;
import com.tourisme.tourisme.entities.Utilisateur;
import com.tourisme.tourisme.repository.TouristeRepository;
import com.tourisme.tourisme.repository.UtilisateurRepository;
import com.tourisme.tourisme.service.UserMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private TouristeRepository touristeRepository;

    @Autowired
    private UserMappingService userMappingService;

    @GetMapping("/profile")
    public ResponseEntity<?> getCurrentUserProfile() {
        Map<String, Object> profile = new HashMap<>();
        profile.put("username", jwtTokenUtil.getCurrentUsername());
        profile.put("email", jwtTokenUtil.getCurrentUserEmail());
        profile.put("roles", jwtTokenUtil.getCurrentUserRoles());

        userMappingService.getCurrentUser().ifPresent(u -> {
            profile.put("nom", u.getNom());
            profile.put("prenom", u.getPrenom());
            profile.put("telephone", u.getTelephone());
            profile.put("adresse", u.getAdresse());
        });

        return ResponseEntity.ok(profile);
    }

    @GetMapping("/admin-only")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> adminOnlyEndpoint() {
        return ResponseEntity.ok("This endpoint is only accessible to admin users");
    }

    @GetMapping("/user-only")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<?> userOnlyEndpoint() {
        return ResponseEntity.ok("This endpoint is only accessible to regular users");
    }

    @GetMapping("/token-info")
    public ResponseEntity<?> getTokenInfo() {
        return ResponseEntity.ok(jwtTokenUtil.getTokenClaims());
    }

    // Update profile (used by Flutter EditProfilePage)
    @PutMapping("/me")
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, Object> payload) {
        return userMappingService.getCurrentUser()
            .map(user -> {
                if (payload.containsKey("nom")) user.setNom(String.valueOf(payload.get("nom")));
                if (payload.containsKey("prenom")) user.setPrenom(String.valueOf(payload.get("prenom")));
                if (payload.containsKey("telephone")) user.setTelephone(String.valueOf(payload.get("telephone")));
                if (payload.containsKey("adresse")) user.setAdresse(String.valueOf(payload.get("adresse")));
                utilisateurRepository.save(user);
                return ResponseEntity.ok(Map.of("message", "Profile updated"));
            })
            .orElse(ResponseEntity.status(404).body(Map.of("error", "User not found")));
    }

    // Update app preferences (language, etc.) — minimal placeholder to satisfy Flutter
    @PutMapping("/me/preferences")
    public ResponseEntity<?> updatePreferences(@RequestBody Map<String, Object> payload) {
        // Persist per-user preferences if you have a table; for now echo back
        return ResponseEntity.ok(Map.of(
            "message", "Preferences updated",
            "preferences", payload
        ));
    }

    // Get user favorites
    @GetMapping("/favorites")
    public ResponseEntity<?> getUserFavorites() {
        try {
            Long userId = userMappingService.getCurrentUserId();
            // TODO: Implement favorites service
            return ResponseEntity.ok(Map.of(
                "message", "Favorites endpoint ready - implementation needed",
                "userId", userId,
                "favorites", new ArrayList<>()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "favorites_retrieval_failed",
                "message", e.getMessage()
            ));
        }
    }

    // Add to favorites
    @PostMapping("/favorites")
    public ResponseEntity<?> addToFavorites(@RequestBody Map<String, Object> payload) {
        try {
            Long userId = userMappingService.getCurrentUserId();
            String type = (String) payload.get("type"); // "city", "activity", "product"
            Long itemId = Long.valueOf(String.valueOf(payload.get("itemId")));
            
            // TODO: Implement favorites service
            return ResponseEntity.ok(Map.of(
                "message", "Added to favorites",
                "userId", userId,
                "type", type,
                "itemId", itemId
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "add_favorite_failed",
                "message", e.getMessage()
            ));
        }
    }

    // Remove from favorites
    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<?> removeFromFavorites(@PathVariable Long id) {
        try {
            Long userId = userMappingService.getCurrentUserId();
            // TODO: Implement favorites service
            return ResponseEntity.ok(Map.of(
                "message", "Removed from favorites",
                "userId", userId,
                "favoriteId", id
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "remove_favorite_failed",
                "message", e.getMessage()
            ));
        }
    }

    // Get user orders history
    @GetMapping("/orders")
    public ResponseEntity<?> getUserOrders() {
        try {
            Long userId = userMappingService.getCurrentUserId();
            // TODO: Implement orders service
            return ResponseEntity.ok(Map.of(
                "message", "Orders history endpoint ready - implementation needed",
                "userId", userId,
                "orders", new ArrayList<>()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "orders_retrieval_failed",
                "message", e.getMessage()
            ));
        }
    }
} 