package com.tourisme.tourisme.controller;

import com.tourisme.tourisme.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/profile")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    /**
     * Update user profile with optional fields
     * @param profileData Map containing profile fields to update
     * @return Updated profile information
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, Object> profileData) {
        try {
            // Get current user from JWT token
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !(authentication.getPrincipal() instanceof Jwt)) {
                return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
            }

            Jwt jwt = (Jwt) authentication.getPrincipal();
            String email = jwt.getClaimAsString("email");
            
            if (email == null) {
                return ResponseEntity.status(400).body(Map.of("error", "Email not found in token"));
            }

            // Update profile using service
            var updatedUser = userProfileService.updateUserProfile(email, profileData);
            
            return ResponseEntity.ok(Map.of(
                "message", "Profile updated successfully",
                "user", updatedUser
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "profile_update_failed",
                "message", e.getMessage()
            ));
        }
    }

    /**
     * Get profile completion status for current user
     * @return Profile completion information
     */
    @GetMapping("/completion")
    public ResponseEntity<?> getProfileCompletion() {
        try {
            // Get current user from JWT token
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !(authentication.getPrincipal() instanceof Jwt)) {
                return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
            }

            Jwt jwt = (Jwt) authentication.getPrincipal();
            String email = jwt.getClaimAsString("email");
            
            if (email == null) {
                return ResponseEntity.status(400).body(Map.of("error", "Email not found in token"));
            }

            // Get profile completion using service
            var completionData = userProfileService.getProfileCompletion(email);
            
            return ResponseEntity.ok(completionData);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "profile_completion_failed",
                "message", e.getMessage()
            ));
        }
    }

    /**
     * Get available profile fields that can be updated
     * @return List of available profile fields
     */
    @GetMapping("/fields")
    public ResponseEntity<?> getAvailableProfileFields() {
        Map<String, Object> availableFields = Map.of(
            "utilisateur", Map.of(
                "telephone", "Phone number",
                "adresse", "Address",
                "dateNaissance", "Birth date (format: yyyy-MM-dd)",
                "cin", "Identity card number"
            ),
            "touriste", Map.of(
                "nationalite", "Nationality",
                "passeport", "Passport number",
                "preferences", "Travel preferences",
                "niveauLangue", "Language level",
                "budgetMax", "Maximum budget (number)"
            ),
            "note", "All fields are optional and can be updated after initial registration"
        );

        return ResponseEntity.ok(availableFields);
    }
}
