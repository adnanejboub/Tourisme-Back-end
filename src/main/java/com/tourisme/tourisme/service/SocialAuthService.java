package com.tourisme.tourisme.service;

import com.tourisme.tourisme.dto.SocialAuthRequest;
import com.tourisme.tourisme.dto.SocialAuthResponse;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.ws.rs.core.Response;
import java.util.*;

/**
 * Service pour l'authentification sociale
 */
@Service
public class SocialAuthService {

    @Value("${keycloak.auth-server-url}")
    private String keycloakServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Authentifie un utilisateur avec un fournisseur social
     */
    public SocialAuthResponse authenticateSocialUser(SocialAuthRequest request) throws Exception {
        // Vérifier le token social
        if (!validateSocialToken(request)) {
            throw new Exception("Token social invalide");
        }

        // Chercher l'utilisateur existant
        UserRepresentation existingUser = findUserByEmail(request.getEmail());
        
        if (existingUser != null) {
            // Utilisateur existant, générer un token Keycloak
            return generateKeycloakToken(existingUser.getUsername());
        } else {
            // Nouvel utilisateur, l'enregistrer d'abord
            return registerSocialUser(request);
        }
    }

    /**
     * Enregistre un nouvel utilisateur avec authentification sociale
     */
    public SocialAuthResponse registerSocialUser(SocialAuthRequest request) throws Exception {
        // Vérifier le token social
        if (!validateSocialToken(request)) {
            throw new Exception("Token social invalide");
        }

        // Créer l'utilisateur dans Keycloak
        String username = createUserInKeycloak(request);
        
        // Générer un token Keycloak
        return generateKeycloakToken(username);
    }

    /**
     * Valide le token social auprès du fournisseur
     */
    private boolean validateSocialToken(SocialAuthRequest request) {
        try {
            String provider = request.getProvider().toLowerCase();
            
            switch (provider) {
                case "google":
                    return validateGoogleToken(request.getAccessToken());
                case "facebook":
                    return validateFacebookToken(request.getAccessToken());
                case "apple":
                    return validateAppleToken(request.getIdToken());
                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Valide un token Google
     */
    private boolean validateGoogleToken(String accessToken) {
        try {
            String url = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + accessToken;
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Valide un token Facebook
     */
    private boolean validateFacebookToken(String accessToken) {
        try {
            String url = "https://graph.facebook.com/me?access_token=" + accessToken;
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Valide un token Apple (simplifié - en production, utiliser JWT validation)
     */
    private boolean validateAppleToken(String idToken) {
        // En production, valider le JWT Apple avec les clés publiques Apple
        return idToken != null && !idToken.isEmpty();
    }

    /**
     * Cherche un utilisateur par email dans Keycloak
     */
    private UserRepresentation findUserByEmail(String email) {
        try {
            Keycloak keycloak = getKeycloakAdminClient();
            RealmResource realmResource = keycloak.realm(realm);
            UsersResource usersResource = realmResource.users();
            
            List<UserRepresentation> users = usersResource.search(email, 0, 1);
            
            for (UserRepresentation user : users) {
                if (email.equalsIgnoreCase(user.getEmail())) {
                    return user;
                }
            }
            
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Crée un utilisateur dans Keycloak
     */
    private String createUserInKeycloak(SocialAuthRequest request) throws Exception {
        Keycloak keycloak = getKeycloakAdminClient();
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        // Générer un nom d'utilisateur unique
        String username = generateUsername(request.getEmail(), request.getDisplayName());
        
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(request.getEmail());
        user.setFirstName(extractFirstName(request.getDisplayName()));
        user.setLastName(extractLastName(request.getDisplayName()));
        user.setEnabled(true);
        user.setEmailVerified(true);

        // Créer l'utilisateur
        Response response = usersResource.create(user);
        
        if (response.getStatus() != 201) {
            throw new Exception("Erreur lors de la création de l'utilisateur dans Keycloak");
        }

        // Récupérer l'ID de l'utilisateur créé
        String userId = response.getLocation().getPath().substring(response.getLocation().getPath().lastIndexOf('/') + 1);
        
        // Ajouter une photo de profil si disponible
        if (request.getPhotoUrl() != null && !request.getPhotoUrl().isEmpty()) {
            UserResource userResource = usersResource.get(userId);
            Map<String, List<String>> attributes = new HashMap<>();
            attributes.put("picture", Arrays.asList(request.getPhotoUrl()));
            userResource.update(user);
        }

        return username;
    }

    /**
     * Génère un token Keycloak pour l'utilisateur
     */
    private SocialAuthResponse generateKeycloakToken(String username) throws Exception {
        String tokenUrl = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";
        
        Map<String, String> params = new HashMap<>();
        params.put("grant_type", "password");
        params.put("client_id", clientId);
        params.put("client_secret", clientSecret);
        params.put("username", username);
        params.put("password", "social_auth_user"); // Mot de passe temporaire pour les utilisateurs sociaux

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> tokenData = response.getBody();
            
            return new SocialAuthResponse(
                (String) tokenData.get("access_token"),
                (String) tokenData.get("refresh_token"),
                (Integer) tokenData.get("expires_in"),
                "Bearer",
                username,
                username, // email sera récupéré via le profil
                username
            );
        } else {
            throw new Exception("Erreur lors de la génération du token Keycloak");
        }
    }

    /**
     * Obtient le client admin Keycloak
     */
    private Keycloak getKeycloakAdminClient() {
        return KeycloakBuilder.builder()
                .serverUrl(keycloakServerUrl)
                .realm("master")
                .username("admin")
                .password("admin") // En production, utiliser des variables d'environnement
                .clientId("admin-cli")
                .build();
    }

    /**
     * Génère un nom d'utilisateur unique
     */
    private String generateUsername(String email, String displayName) {
        String baseUsername;
        
        if (displayName != null && !displayName.isEmpty()) {
            baseUsername = displayName.toLowerCase().replaceAll("[^a-z0-9]", "");
        } else {
            baseUsername = email.split("@")[0].toLowerCase().replaceAll("[^a-z0-9]", "");
        }
        
        // Ajouter un suffixe numérique si nécessaire
        String username = baseUsername;
        int counter = 1;
        
        while (userExists(username)) {
            username = baseUsername + counter;
            counter++;
        }
        
        return username;
    }

    /**
     * Vérifie si un utilisateur existe
     */
    private boolean userExists(String username) {
        try {
            Keycloak keycloak = getKeycloakAdminClient();
            RealmResource realmResource = keycloak.realm(realm);
            UsersResource usersResource = realmResource.users();
            
            List<UserRepresentation> users = usersResource.search(username, 0, 1);
            return !users.isEmpty() && username.equals(users.get(0).getUsername());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extrait le prénom du nom complet
     */
    private String extractFirstName(String displayName) {
        if (displayName == null || displayName.isEmpty()) {
            return "";
        }
        String[] parts = displayName.trim().split(" ");
        return parts[0];
    }

    /**
     * Extrait le nom de famille du nom complet
     */
    private String extractLastName(String displayName) {
        if (displayName == null || displayName.isEmpty()) {
            return "";
        }
        String[] parts = displayName.trim().split(" ");
        if (parts.length > 1) {
            return String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
        }
        return "";
    }
}
