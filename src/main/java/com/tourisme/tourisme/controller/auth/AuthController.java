package com.tourisme.tourisme.controller.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tourisme.tourisme.dto.RegisterRequest;
import com.tourisme.tourisme.service.UserMappingService;
import com.tourisme.tourisme.service.UserRegistrationService;
import com.tourisme.tourisme.service.exception.DuplicateUserException;
import com.tourisme.tourisme.service.KeycloakHealthService;
import com.tourisme.tourisme.entities.Utilisateur;
import com.tourisme.tourisme.entities.Touriste;
import com.tourisme.tourisme.entities.Role;
import com.tourisme.tourisme.repository.UtilisateurRepository;
import com.tourisme.tourisme.repository.TouristeRepository;
import com.tourisme.tourisme.repository.RoleRepository;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.keycloak.admin.client.CreatedResponseUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private Keycloak keycloak;

    @Autowired
    private Environment env;

    @Autowired
    private UserMappingService userMappingService;

    @Autowired
    private UserRegistrationService userRegistrationService;

    @Autowired
    private KeycloakHealthService keycloakHealthService;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private TouristeRepository touristeRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Register a new tourist user
     * Uses the UserRegistrationService for transactional registration
     * Creates user in local database first, then in Keycloak
     * If Keycloak fails, the local transaction is rolled back
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        logger.info("=== Début de l'inscription utilisateur ===");
        logger.info("Email: {}, Username: {}", request.getEmail(), request.getUsername());
        
        try {
            // Utiliser le service d'inscription transactionnelle
            Map<String, Object> result = userRegistrationService.registerUser(request);
            return ResponseEntity.status(201).body(result);
            
        } catch (DuplicateUserException e) {
            logger.warn("⚠️ Conflit d'inscription: {}", e.getMessage());
            return ResponseEntity.status(409).body(Map.of(
                "error", "conflict",
                "message", e.getMessage()
            ));
        } catch (IllegalArgumentException e) {
            logger.warn("⚠️ Erreur de validation: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                "error", "validation_error",
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            logger.error("❌ Erreur lors de l'inscription: {}", e.getMessage(), e);
            
            // Provide more specific error information
            String errorMessage = "Registration failed";
            if (e.getMessage() != null) {
                if (e.getMessage().contains("Keycloak")) {
                    errorMessage = "Authentication service error: " + e.getMessage();
                } else if (e.getMessage().contains("database")) {
                    errorMessage = "Database error: " + e.getMessage();
                } else {
                    errorMessage = e.getMessage();
                }
            }
            
            return ResponseEntity.status(500).body(Map.of(
                "error", "registration_failed",
                "message", errorMessage,
                "details", e.getClass().getSimpleName()
            ));
        }
    }

    /**
     * Username/Password login proxy to Keycloak token endpoint
     * Returns access_token (and refresh_token) for the mobile app
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {

            String authServer = env.getProperty("keycloak.auth-server-url");
            String realm = env.getProperty("keycloak.realm");
            String clientId = env.getProperty("keycloak.resource");
            String clientSecret = env.getProperty("keycloak.credentials.secret");

            String identifier = (request.getIdentifier() != null && !request.getIdentifier().isBlank())
                ? request.getIdentifier()
                : request.getUsername();

            // First attempt: try as-is (Keycloak can accept email if enabled)
            Map<String, Object> token = requestToken(authServer, realm, clientId, clientSecret, identifier, request.getPassword());
            if (token != null) {
                return ResponseEntity.ok(token);
            }

            // If failed and identifier looks like an email, resolve username via Keycloak admin and retry
            if (identifier != null && identifier.contains("@")) {
                try {
                    var users = keycloak.realm(realm).users().search(identifier, 0, 10);
                    var match = users.stream()
                        .filter(u -> identifier.equalsIgnoreCase(u.getEmail()))
                        .findFirst();
                    if (match.isPresent()) {
                        String resolvedUsername = match.get().getUsername();
                        Map<String, Object> token2 = requestToken(authServer, realm, clientId, clientSecret, resolvedUsername, request.getPassword());
                        if (token2 != null) {
                            return ResponseEntity.ok(token2);
                        }
                    }
                } catch (Exception ignored) {
                    // fall through to unauthorized
                }
            }

            return ResponseEntity.status(401).body(Map.of(
                "error", "Unauthorized",
                "message", "Invalid username/email or password"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "login_failed",
                "message", e.getMessage()
            ));
        }
    }

    private Map<String, Object> requestToken(String authServer, String realm, String clientId, String clientSecret,
                                             String username, String password) throws JsonProcessingException {
        try {
            String tokenUrl = String.format("%s/realms/%s/protocol/openid-connect/token", authServer, realm);
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
            form.add("grant_type", "password");
            form.add("client_id", clientId);
            if (clientSecret != null && !clientSecret.isBlank()) {
                form.add("client_secret", clientSecret);
            }
            form.add("username", username);
            form.add("password", password);

            org.springframework.http.HttpEntity<MultiValueMap<String, String>> httpEntity =
                new org.springframework.http.HttpEntity<>(form, headers);

            String kcResponse = restTemplate.postForObject(tokenUrl, httpEntity, String.class);
            ObjectMapper mapper = new ObjectMapper();
            @SuppressWarnings("unchecked")
            Map<String, Object> body = mapper.readValue(kcResponse, Map.class);
            return body;
        } catch (HttpClientErrorException e) {
            return null; // unauthorized or bad request
        } catch (Exception e) {
            throw e;
        }
    }

    // Simple DTO for login request
    static class LoginRequest {
        private String username; // backward compatibility
        private String identifier; // username or email
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getIdentifier() { return identifier; }
        public void setIdentifier(String identifier) { this.identifier = identifier; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        @Override
        public String toString() {
            return "LoginRequest{" +
                    "username='" + username + '\'' +
                    ", identifier='" + identifier + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }

    /**
     * Endpoint to demonstrate token verification and user synchronization
     * This shows how the backend automatically verifies tokens and maps Keycloak users
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();

            // Extract information from JWT token
            String email = jwt.getClaimAsString("email");
            String firstName = jwt.getClaimAsString("given_name");
            String lastName = jwt.getClaimAsString("family_name");
            String keycloakId = jwt.getSubject();
            String realmAccess = jwt.getClaimAsString("realm_access");

            // Get or create backend user
            var backendUser = userMappingService.getCurrentUser();

            Map<String, Object> response = new HashMap<>();
            response.put("keycloakId", keycloakId);
            response.put("email", email);
            response.put("firstName", firstName);
            response.put("lastName", lastName);
            response.put("realmAccess", realmAccess);
            response.put("backendUser", backendUser.orElse(null));
            response.put("tokenVerified", true);

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(401).body(Map.of("error", "No valid token found"));
    }

    // Optional: Proxy logout to Keycloak (mobile can also hit KC end-session directly)
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // Frontend should clear tokens. Optionally implement Keycloak refresh token revocation here.
        return ResponseEntity.ok(Map.of("message", "Logged out"));
    }

    /**
     * Refresh access token using refresh_token via Keycloak token endpoint
     */
    @PostMapping("/token/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> body) {
        try {
            String refreshToken = body.get("refreshToken");
            if (refreshToken == null || refreshToken.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "validation_error",
                    "message", "refreshToken is required"
                ));
            }

            String authServer = env.getProperty("keycloak.auth-server-url");
            String realm = env.getProperty("keycloak.realm");
            String clientId = env.getProperty("keycloak.resource");
            String clientSecret = env.getProperty("keycloak.credentials.secret");

            String tokenUrl = String.format("%s/realms/%s/protocol/openid-connect/token", authServer, realm);
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
            form.add("grant_type", "refresh_token");
            form.add("client_id", clientId);
            if (clientSecret != null && !clientSecret.isBlank()) {
                form.add("client_secret", clientSecret);
            }
            form.add("refresh_token", refreshToken);

            org.springframework.http.HttpEntity<MultiValueMap<String, String>> httpEntity =
                new org.springframework.http.HttpEntity<>(form, headers);

            String kcResponse = restTemplate.postForObject(tokenUrl, httpEntity, String.class);
            ObjectMapper mapper = new ObjectMapper();
            @SuppressWarnings("unchecked")
            Map<String, Object> tokenBody = mapper.readValue(kcResponse, Map.class);
            return ResponseEntity.ok(tokenBody);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode().value()).body(Map.of(
                "error", "refresh_failed",
                "message", "Invalid refresh token"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "refresh_failed",
                "message", e.getMessage()
            ));
        }
    }

    /**
     * Trigger forgot password email via Keycloak required action
     */
    @PostMapping("/password/forgot")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body) {
        try {
            String email = body.get("email");
            if (email == null || email.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "validation_error",
                    "message", "email is required"
                ));
            }

            String realm = env.getProperty("keycloak.realm");
            UsersResource usersResource = keycloak.realm(realm).users();
            List<UserRepresentation> matches = usersResource.search(email, 0, 10);
            UserRepresentation user = matches.stream()
                .filter(u -> email.equalsIgnoreCase(u.getEmail()))
                .findFirst().orElse(null);

            if (user == null) {
                return ResponseEntity.status(404).body(Map.of(
                    "error", "not_found",
                    "message", "No user with this email"
                ));
            }

            usersResource.get(user.getId()).executeActionsEmail(List.of("UPDATE_PASSWORD"));
            return ResponseEntity.ok(Map.of("message", "Password reset email sent"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "forgot_password_failed",
                "message", e.getMessage()
            ));
        }
    }

    /**
     * Direct password reset by admin (used if app chooses direct reset)
     */
    @PostMapping("/password/reset")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
        try {
            String username = body.get("username");
            String newPassword = body.get("newPassword");
            if (username == null || username.isBlank() || newPassword == null || newPassword.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "validation_error",
                    "message", "username and newPassword are required"
                ));
            }

            String realm = env.getProperty("keycloak.realm");
            UsersResource usersResource = keycloak.realm(realm).users();
            List<UserRepresentation> matches = usersResource.search(username, 0, 10);
            UserRepresentation user = matches.stream()
                .filter(u -> username.equalsIgnoreCase(u.getUsername()) || username.equalsIgnoreCase(u.getEmail()))
                .findFirst().orElse(null);

            if (user == null) {
                return ResponseEntity.status(404).body(Map.of(
                    "error", "not_found",
                    "message", "User not found"
                ));
            }

            CredentialRepresentation cred = new CredentialRepresentation();
            cred.setTemporary(false);
            cred.setType(CredentialRepresentation.PASSWORD);
            cred.setValue(newPassword);
            usersResource.get(user.getId()).resetPassword(cred);

            // Also update local DB password hash if exists
            utilisateurRepository.findByEmail(user.getEmail()).ifPresent(u -> {
                try {
                    u.setMotDePasse(passwordEncoder.encode(newPassword));
                    utilisateurRepository.save(u);
                } catch (Exception ignored) {}
            });

            return ResponseEntity.ok(Map.of("message", "Password updated"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "reset_password_failed",
                "message", e.getMessage()
            ));
        }
    }

    /**
     * Resend email verification link via Keycloak
     */
    @PostMapping("/email/resend-verification")
    public ResponseEntity<?> resendEmailVerification(@RequestBody Map<String, String> body) {
        try {
            String email = body.get("email");
            if (email == null || email.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "validation_error",
                    "message", "email is required"
                ));
            }

            String realm = env.getProperty("keycloak.realm");
            UsersResource usersResource = keycloak.realm(realm).users();
            List<UserRepresentation> matches = usersResource.search(email, 0, 10);
            UserRepresentation user = matches.stream()
                .filter(u -> email.equalsIgnoreCase(u.getEmail()))
                .findFirst().orElse(null);

            if (user == null) {
                return ResponseEntity.status(404).body(Map.of(
                    "error", "not_found",
                    "message", "No user with this email"
                ));
            }

            usersResource.get(user.getId()).executeActionsEmail(List.of("VERIFY_EMAIL"));
            return ResponseEntity.ok(Map.of("message", "Verification email sent"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "resend_verification_failed",
                "message", e.getMessage()
            ));
        }
    }

    /**
     * Verify email address
     */
    @PostMapping("/email/verify")
    public ResponseEntity<?> verifyEmail(@RequestBody Map<String, String> body) {
        try {
            String email = body.get("email");
            String token = body.get("token");
            
            if (email == null || email.isBlank() || token == null || token.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "validation_error",
                    "message", "email and token are required"
                ));
            }

            String realm = env.getProperty("keycloak.realm");
            UsersResource usersResource = keycloak.realm(realm).users();
            List<UserRepresentation> matches = usersResource.search(email, 0, 10);
            UserRepresentation user = matches.stream()
                .filter(u -> email.equalsIgnoreCase(u.getEmail()))
                .findFirst().orElse(null);

            if (user == null) {
                return ResponseEntity.status(404).body(Map.of(
                    "error", "not_found",
                    "message", "No user with this email"
                ));
            }

            // In a real implementation, you would verify the token
            // For now, we'll just mark the email as verified
            user.setEmailVerified(true);
            usersResource.get(user.getId()).update(user);

            return ResponseEntity.ok(Map.of("message", "Email verified successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "email_verification_failed",
                "message", e.getMessage()
            ));
        }
    }

    /**
     * Change password for authenticated user
     */
    @PostMapping("/password/change")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> body) {
        try {
            String currentPassword = body.get("currentPassword");
            String newPassword = body.get("newPassword");
            
            if (currentPassword == null || currentPassword.isBlank() || 
                newPassword == null || newPassword.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "validation_error",
                    "message", "currentPassword and newPassword are required"
                ));
            }

            // Get current user from JWT
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !(authentication.getPrincipal() instanceof Jwt)) {
                return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
            }

            Jwt jwt = (Jwt) authentication.getPrincipal();
            String email = jwt.getClaimAsString("email");
            
            if (email == null) {
                return ResponseEntity.status(400).body(Map.of("error", "Email not found in token"));
            }

            String realm = env.getProperty("keycloak.realm");
            UsersResource usersResource = keycloak.realm(realm).users();
            List<UserRepresentation> matches = usersResource.search(email, 0, 10);
            UserRepresentation user = matches.stream()
                .filter(u -> email.equalsIgnoreCase(u.getEmail()))
                .findFirst().orElse(null);

            if (user == null) {
                return ResponseEntity.status(404).body(Map.of(
                    "error", "not_found",
                    "message", "User not found"
                ));
            }

            // Update password in Keycloak
            CredentialRepresentation cred = new CredentialRepresentation();
            cred.setTemporary(false);
            cred.setType(CredentialRepresentation.PASSWORD);
            cred.setValue(newPassword);
            usersResource.get(user.getId()).resetPassword(cred);

            // Also update local DB password hash if exists
            utilisateurRepository.findByEmail(user.getEmail()).ifPresent(u -> {
                try {
                    u.setMotDePasse(passwordEncoder.encode(newPassword));
                    utilisateurRepository.save(u);
                } catch (Exception ignored) {}
            });

            return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "password_change_failed",
                "message", e.getMessage()
            ));
        }
    }

    /**
     * Endpoint de diagnostic pour vérifier la santé de Keycloak
     * Utile pour diagnostiquer les problèmes de configuration
     */
    @GetMapping("/health/keycloak")
    public ResponseEntity<?> checkKeycloakHealth() {
        logger.info("🔍 Vérification de la santé de Keycloak");
        Map<String, Object> healthReport = keycloakHealthService.checkKeycloakHealth();
        return ResponseEntity.ok(healthReport);
    }

    /**
     * Test de création d'utilisateur temporaire pour diagnostic
     */
    @PostMapping("/test/user-creation")
    public ResponseEntity<?> testUserCreation(@RequestParam String username, @RequestParam String email) {
        logger.info("🧪 Test de création d'utilisateur - Username: {}, Email: {}", username, email);
        Map<String, Object> testResult = keycloakHealthService.testUserCreation(username, email);
        return ResponseEntity.ok(testResult);
    }

    /**
     * Simple test endpoint to verify connection
     */
    @GetMapping("/test/connection")
    public ResponseEntity<?> testConnection() {
        logger.info("🔗 Test de connexion depuis l'application Flutter");
        return ResponseEntity.ok(Map.of(
            "message", "Connection successful",
            "timestamp", new java.util.Date(),
            "status", "OK"
        ));
    }

    /**
     * Endpoint pour vérifier si un utilisateur existe déjà (utile pour le frontend)
     */
    @GetMapping("/check-user-exists")
    public ResponseEntity<?> checkUserExists(@RequestParam(required = false) String email, 
                                           @RequestParam(required = false) String username) {
        try {
            if ((email == null || email.isBlank()) && (username == null || username.isBlank())) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "validation_error",
                    "message", "email ou username requis"
                ));
            }

            Map<String, Object> result = new HashMap<>();
            
            // Vérifier dans la base locale
            boolean existsInLocal = false;
            if (email != null && !email.isBlank()) {
                existsInLocal = utilisateurRepository.existsByEmail(email);
            }
            
            // Vérifier dans Keycloak
            boolean existsInKeycloak = false;
            String realm = env.getProperty("keycloak.realm");
            if (realm != null) {
                try {
                    UsersResource usersResource = keycloak.realm(realm).users();
                    
                    if (email != null && !email.isBlank()) {
                        List<UserRepresentation> usersByEmail = usersResource.searchByEmail(email, true);
                        existsInKeycloak = usersByEmail.stream()
                            .anyMatch(user -> email.equalsIgnoreCase(user.getEmail()));
                    }
                    
                    if (!existsInKeycloak && username != null && !username.isBlank()) {
                        List<UserRepresentation> usersByUsername = usersResource.search(username, 0, 10);
                        existsInKeycloak = usersByUsername.stream()
                            .anyMatch(user -> username.equalsIgnoreCase(user.getUsername()));
                    }
                } catch (Exception e) {
                    logger.warn("Erreur lors de la vérification Keycloak: {}", e.getMessage());
                }
            }
            
            result.put("email", email);
            result.put("username", username);
            result.put("existsInLocal", existsInLocal);
            result.put("existsInKeycloak", existsInKeycloak);
            result.put("canRegister", !existsInLocal && !existsInKeycloak);
            
            if (existsInLocal || existsInKeycloak) {
                result.put("message", "Utilisateur déjà existant");
                return ResponseEntity.status(409).body(result);
            } else {
                result.put("message", "Utilisateur disponible");
                return ResponseEntity.ok(result);
            }
            
        } catch (Exception e) {
            logger.error("Erreur lors de la vérification de l'utilisateur: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of(
                "error", "check_failed",
                "message", e.getMessage()
            ));
        }
    }


}