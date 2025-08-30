package com.tourisme.tourisme.service;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class KeycloakHealthService {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakHealthService.class);

    @Autowired
    private Keycloak keycloak;

    @Autowired
    private Environment env;

    /**
     * Vérifier la santé générale de Keycloak
     */
    public Map<String, Object> checkKeycloakHealth() {
        Map<String, Object> healthReport = new HashMap<>();
        
        try {
            // Vérifier la connexion à Keycloak
            healthReport.put("connection", checkConnection());
            
            // Vérifier la configuration du realm
            healthReport.put("realm", checkRealmConfiguration());
            
            // Vérifier les permissions du client
            healthReport.put("client", checkClientPermissions());
            
            // Vérifier les rôles
            healthReport.put("roles", checkRoles());
            
            // Vérifier la capacité de créer des utilisateurs
            healthReport.put("userCreation", checkUserCreationCapability());
            
            healthReport.put("overallStatus", "healthy");
            
        } catch (Exception e) {
            logger.error("❌ Erreur lors de la vérification de la santé de Keycloak: {}", e.getMessage(), e);
            healthReport.put("overallStatus", "unhealthy");
            healthReport.put("error", e.getMessage());
        }
        
        return healthReport;
    }

    /**
     * Vérifier la connexion à Keycloak
     */
    private Map<String, Object> checkConnection() {
        Map<String, Object> connectionStatus = new HashMap<>();
        
        try {
            String serverUrl = env.getProperty("keycloak.auth-server-url");
            String realm = env.getProperty("keycloak.realm");
            String adminUsername = env.getProperty("keycloak.admin.username");
            
            connectionStatus.put("serverUrl", serverUrl);
            connectionStatus.put("realm", realm);
            connectionStatus.put("adminUsername", adminUsername);
            
            // Tester la connexion en récupérant les informations du realm
            RealmResource realmResource = keycloak.realm(realm);
            RealmRepresentation realmInfo = realmResource.toRepresentation();
            
            connectionStatus.put("status", "connected");
            connectionStatus.put("realmName", realmInfo.getRealm());
            connectionStatus.put("realmEnabled", realmInfo.isEnabled());
            
            logger.info("✅ Connexion Keycloak réussie - Realm: {}", realmInfo.getRealm());
            
        } catch (Exception e) {
            connectionStatus.put("status", "failed");
            connectionStatus.put("error", e.getMessage());
            logger.error("❌ Échec de la connexion Keycloak: {}", e.getMessage());
        }
        
        return connectionStatus;
    }

    /**
     * Vérifier la configuration du realm
     */
    private Map<String, Object> checkRealmConfiguration() {
        Map<String, Object> realmStatus = new HashMap<>();
        
        try {
            String realm = env.getProperty("keycloak.realm");
            RealmResource realmResource = keycloak.realm(realm);
            RealmRepresentation realmInfo = realmResource.toRepresentation();
            
            realmStatus.put("realmName", realmInfo.getRealm());
            realmStatus.put("enabled", realmInfo.isEnabled());
            realmStatus.put("registrationAllowed", realmInfo.isRegistrationAllowed());
            realmStatus.put("resetPasswordAllowed", realmInfo.isResetPasswordAllowed());
            realmStatus.put("rememberMe", realmInfo.isRememberMe());
            realmStatus.put("verifyEmail", realmInfo.isVerifyEmail());
            realmStatus.put("loginWithEmailAllowed", realmInfo.isLoginWithEmailAllowed());
            realmStatus.put("duplicateEmailsAllowed", realmInfo.isDuplicateEmailsAllowed());
            
            logger.info("✅ Configuration du realm vérifiée");
            
        } catch (Exception e) {
            realmStatus.put("error", e.getMessage());
            logger.error("❌ Erreur lors de la vérification de la configuration du realm: {}", e.getMessage());
        }
        
        return realmStatus;
    }

    /**
     * Vérifier les permissions du client
     */
    private Map<String, Object> checkClientPermissions() {
        Map<String, Object> clientStatus = new HashMap<>();
        
        try {
            String realm = env.getProperty("keycloak.realm");
            String clientId = env.getProperty("keycloak.resource");
            String clientSecret = env.getProperty("keycloak.credentials.secret");
            
            clientStatus.put("clientId", clientId);
            clientStatus.put("hasSecret", clientSecret != null && !clientSecret.isBlank());
            
            // Vérifier si le client existe
            var clientsResource = keycloak.realm(realm).clients();
            var client = clientsResource.findByClientId(clientId).stream().findFirst();
            
            if (client.isPresent()) {
                var clientRepresentation = client.get();
                clientStatus.put("clientExists", true);
                clientStatus.put("clientEnabled", clientRepresentation.isEnabled());
                clientStatus.put("clientId", clientRepresentation.getClientId());
                clientStatus.put("protocol", clientRepresentation.getProtocol());
                
                logger.info("✅ Client Keycloak trouvé: {}", clientRepresentation.getClientId());
            } else {
                clientStatus.put("clientExists", false);
                clientStatus.put("error", "Client not found");
                logger.warn("⚠️ Client Keycloak non trouvé: {}", clientId);
            }
            
        } catch (Exception e) {
            clientStatus.put("error", e.getMessage());
            logger.error("❌ Erreur lors de la vérification du client: {}", e.getMessage());
        }
        
        return clientStatus;
    }

    /**
     * Vérifier les rôles existants
     */
    private Map<String, Object> checkRoles() {
        Map<String, Object> rolesStatus = new HashMap<>();
        
        try {
            String realm = env.getProperty("keycloak.realm");
            RolesResource rolesResource = keycloak.realm(realm).roles();
            List<RoleRepresentation> roles = rolesResource.list();
            
            rolesStatus.put("totalRoles", roles.size());
            rolesStatus.put("roles", roles.stream().map(RoleRepresentation::getName).toList());
            
            // Vérifier si le rôle TOURISTE existe
            boolean touristeRoleExists = roles.stream()
                .anyMatch(role -> "TOURISTE".equals(role.getName()));
            
            rolesStatus.put("touristeRoleExists", touristeRoleExists);
            
            if (touristeRoleExists) {
                logger.info("✅ Rôle TOURISTE trouvé dans Keycloak");
            } else {
                logger.warn("⚠️ Rôle TOURISTE non trouvé dans Keycloak");
            }
            
        } catch (Exception e) {
            rolesStatus.put("error", e.getMessage());
            logger.error("❌ Erreur lors de la vérification des rôles: {}", e.getMessage());
        }
        
        return rolesStatus;
    }

    /**
     * Vérifier la capacité de créer des utilisateurs
     */
    private Map<String, Object> checkUserCreationCapability() {
        Map<String, Object> userCreationStatus = new HashMap<>();
        
        try {
            String realm = env.getProperty("keycloak.realm");
            UsersResource usersResource = keycloak.realm(realm).users();
            
            // Compter les utilisateurs existants
            List<UserRepresentation> users = usersResource.list();
            userCreationStatus.put("existingUsersCount", users.size());
            
            // Vérifier si on peut accéder aux utilisateurs
            userCreationStatus.put("canAccessUsers", true);
            
            logger.info("✅ Capacité de création d'utilisateurs vérifiée - {} utilisateurs existants", users.size());
            
        } catch (Exception e) {
            userCreationStatus.put("canAccessUsers", false);
            userCreationStatus.put("error", e.getMessage());
            logger.error("❌ Erreur lors de la vérification de la capacité de création d'utilisateurs: {}", e.getMessage());
        }
        
        return userCreationStatus;
    }

    /**
     * Test de création d'utilisateur temporaire (pour diagnostic)
     */
    public Map<String, Object> testUserCreation(String testUsername, String testEmail) {
        Map<String, Object> testResult = new HashMap<>();
        
        try {
            String realm = env.getProperty("keycloak.realm");
            UsersResource usersResource = keycloak.realm(realm).users();
            
            // Créer un utilisateur de test
            UserRepresentation testUser = new UserRepresentation();
            testUser.setEnabled(true);
            testUser.setUsername(testUsername);
            testUser.setEmail(testEmail);
            testUser.setFirstName("Test");
            testUser.setLastName("User");
            testUser.setEmailVerified(false);
            
            // Créer l'utilisateur
            var response = usersResource.create(testUser);
            
            if (response.getStatus() == 201) {
                String userId = org.keycloak.admin.client.CreatedResponseUtil.getCreatedId(response);
                testResult.put("status", "success");
                testResult.put("userId", userId);
                
                // Supprimer l'utilisateur de test
                usersResource.get(userId).remove();
                testResult.put("cleanup", "success");
                
                logger.info("✅ Test de création d'utilisateur réussi");
            } else {
                String errorBody = response.readEntity(String.class);
                testResult.put("status", "failed");
                testResult.put("error", errorBody);
                testResult.put("statusCode", response.getStatus());
                
                logger.error("❌ Test de création d'utilisateur échoué: {}", errorBody);
            }
            
        } catch (Exception e) {
            testResult.put("status", "error");
            testResult.put("error", e.getMessage());
            logger.error("❌ Erreur lors du test de création d'utilisateur: {}", e.getMessage());
        }
        
        return testResult;
    }
}



