package com.tourisme.tourisme.controller.auth;

import com.tourisme.tourisme.dto.SocialAuthRequest;
import com.tourisme.tourisme.dto.SocialAuthResponse;
import com.tourisme.tourisme.service.SocialAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Contrôleur pour l'authentification sociale (Google, Facebook, Apple)
 */
@RestController
@RequestMapping("/auth/social")
@CrossOrigin(origins = "*")
public class SocialAuthController {

    @Autowired
    private SocialAuthService socialAuthService;

    /**
     * Connexion avec authentification sociale
     */
    @PostMapping("/login")
    public ResponseEntity<?> socialLogin(@RequestBody SocialAuthRequest request) {
        try {
            SocialAuthResponse response = socialAuthService.authenticateSocialUser(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "SOCIAL_AUTH_FAILED",
                "message", e.getMessage()
            ));
        }
    }

    /**
     * Enregistrement avec authentification sociale
     */
    @PostMapping("/register")
    public ResponseEntity<?> socialRegister(@RequestBody SocialAuthRequest request) {
        try {
            SocialAuthResponse response = socialAuthService.registerSocialUser(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "SOCIAL_REGISTER_FAILED",
                "message", e.getMessage()
            ));
        }
    }
}
