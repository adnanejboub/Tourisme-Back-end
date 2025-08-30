package com.tourisme.tourisme.service;

import com.tourisme.tourisme.dto.RegisterRequest;
import com.tourisme.tourisme.entities.Utilisateur;
import com.tourisme.tourisme.entities.Touriste;
import com.tourisme.tourisme.entities.Role;
import com.tourisme.tourisme.repository.UtilisateurRepository;
import com.tourisme.tourisme.repository.TouristeRepository;
import com.tourisme.tourisme.repository.RoleRepository;
import com.tourisme.tourisme.service.exception.DuplicateUserException;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserRegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationService.class);
    private static final String ROLE_NAME = "TOURISTE";

    @Autowired
    private Keycloak keycloak;

    @Autowired
    private Environment env;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private TouristeRepository touristeRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Inscription transactionnelle d'un nouvel utilisateur
     * Crée l'utilisateur dans la base locale ET dans Keycloak de manière atomique
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> registerUser(RegisterRequest request) throws Exception {
        logger.info("=== Début de l'inscription transactionnelle ===");
        logger.info("Email: {}, Username: {}", request.getEmail(), request.getUsername());

        String realm = getRealm();
        Utilisateur backendUser = null;
        Touriste createdTouriste = null;
        String keycloakUserId = null;

        try {
            // Vérification préalable de l'existence de l'utilisateur
            if (utilisateurRepository.existsByEmail(request.getEmail())) {
                throw new DuplicateUserException("Un utilisateur avec cet email existe déjà");
            }

            // Étape 1: Créer l'utilisateur dans la base locale (dans la transaction)
            logger.info("📝 Étape 1: Création de l'utilisateur dans la base locale");
            backendUser = createLocalUser(request);
            logger.info("✅ Utilisateur créé dans la base locale avec l'ID: {}", backendUser.getIdUtilisateur());

            // Étape 2: Créer l'entité Touriste
            logger.info("📝 Étape 2: Création de l'entité Touriste");
            createdTouriste = createTouristeEntity(backendUser);
            logger.info("✅ Entité Touriste créée avec l'ID: {}", createdTouriste.getIdTouriste());

            // Étape 3: Créer l'utilisateur dans Keycloak
            logger.info("📝 Étape 3: Création de l'utilisateur dans Keycloak");
            keycloakUserId = createKeycloakUser(request, realm);
            logger.info("✅ Utilisateur créé dans Keycloak avec l'ID: {}", keycloakUserId);

            // Étape 4: Attribuer le rôle dans Keycloak
            logger.info("📝 Étape 4: Attribution du rôle dans Keycloak");
            assignKeycloakRole(keycloakUserId, ROLE_NAME, realm);
            logger.info("✅ Rôle {} attribué avec succès", ROLE_NAME);

            // Succès - construire la réponse
            Map<String, Object> successResponse = buildSuccessResponse(request, keycloakUserId, backendUser, createdTouriste);
            logger.info("🎉 Inscription transactionnelle réussie pour l'utilisateur: {}", request.getEmail());
            
            return successResponse;

        } catch (Exception e) {
            logger.error("❌ Erreur lors de l'inscription transactionnelle: {}", e.getMessage(), e);
            
            // Nettoyage en cas d'échec
            cleanupOnFailure(keycloakUserId, realm, backendUser);
            
            // Relancer l'exception pour déclencher le rollback de la transaction
            throw e;
        }
    }

    /**
     * Créer l'utilisateur dans la base de données locale
     */
    private Utilisateur createLocalUser(RegisterRequest request) {
        // Récupérer ou créer le rôle TOURISTE
        Role touristeRole = roleRepository.findByNomRole(ROLE_NAME)
            .orElseGet(() -> {
                Role newRole = new Role();
                newRole.setNomRole(ROLE_NAME);
                newRole.setDescription("Role créé automatiquement pour " + ROLE_NAME);
                return roleRepository.save(newRole);
            });

        // Créer l'entité Utilisateur
        Utilisateur user = new Utilisateur();
        user.setNom(request.getLastName());
        user.setPrenom(request.getFirstName());
        user.setEmail(request.getEmail());
        
        // Encoder le mot de passe pour la base locale
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        user.setMotDePasse(encodedPassword);
        
        user.setDateInscription(new Date());
        
        // Gérer le champ CIN
        String cinValue = (request.getCin() != null && !request.getCin().isBlank()) 
            ? request.getCin() 
            : "N/A";
        user.setCin(cinValue);
        
        user.setRole(touristeRole);
        user.setPaiements(new java.util.ArrayList<>());
        
        return utilisateurRepository.save(user);
    }

    /**
     * Créer l'entité Touriste
     */
    private Touriste createTouristeEntity(Utilisateur utilisateur) {
        // Vérifier si un Touriste existe déjà pour cet utilisateur
        var existingTouriste = touristeRepository.findByUtilisateur(utilisateur);
        if (existingTouriste.isPresent()) {
            return existingTouriste.get();
        }

        Touriste touriste = new Touriste();
        touriste.setUtilisateur(utilisateur);
        touriste.setSejours(new java.util.ArrayList<>());
        touriste.setAvis(new java.util.ArrayList<>());
        
        return touristeRepository.save(touriste);
    }

    /**
     * Créer l'utilisateur dans Keycloak
     */
    private String createKeycloakUser(RegisterRequest request, String realm) throws Exception {
        UsersResource usersResource = keycloak.realm(realm).users();
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        
        String username = request.getUsername() != null && !request.getUsername().isBlank() 
            ? request.getUsername() 
            : request.getEmail();
        user.setUsername(username);
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmailVerified(false);

        // Configurer le mot de passe
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(request.getPassword());
        credential.setTemporary(false);
        user.setCredentials(Arrays.asList(credential));

        logger.info("Tentative de création de l'utilisateur {} dans Keycloak", username);
        
        jakarta.ws.rs.core.Response response = usersResource.create(user);
        logger.info("Réponse Keycloak - Status: {}", response.getStatus());
        
        if (response.getStatus() != 201) {
            String errorBody = response.readEntity(String.class);
            logger.error("❌ Échec de création dans Keycloak - Status: {}, Body: {}", response.getStatus(), errorBody);
            throw new RuntimeException("Failed to create user in Keycloak: " + errorBody);
        }

        String keycloakUserId = CreatedResponseUtil.getCreatedId(response);
        if (keycloakUserId == null) {
            throw new RuntimeException("Failed to retrieve Keycloak user ID");
        }
        
        return keycloakUserId;
    }

    /**
     * Attribuer un rôle à l'utilisateur dans Keycloak
     */
    private void assignKeycloakRole(String userId, String roleName, String realm) throws Exception {
        logger.info("🔐 Attribution du rôle {} à l'utilisateur {}", roleName, userId);
        
        try {
            // S'assurer que le rôle existe
            ensureRealmRoleExists(roleName, realm);
            
            // Récupérer l'ID du rôle
            var rolesResource = keycloak.realm(realm).roles();
            var role = rolesResource.get(roleName).toRepresentation();
            
            // Attribuer le rôle à l'utilisateur
            var userResource = keycloak.realm(realm).users().get(userId);
            userResource.roles().realmLevel().add(Collections.singletonList(role));
            
            logger.info("✅ Rôle {} attribué avec succès à l'utilisateur {}", roleName, userId);
        } catch (Exception e) {
            logger.error("❌ Échec de l'attribution du rôle {} à l'utilisateur {}: {}", roleName, userId, e.getMessage());
            throw new RuntimeException("Failed to assign role '" + roleName + "' to user '" + userId + "': " + e.getMessage());
        }
    }

    /**
     * S'assurer qu'un rôle existe dans Keycloak
     */
    private void ensureRealmRoleExists(String roleName, String realm) {
        logger.info("🔐 Vérification de l'existence du rôle {}", roleName);
        var realmResource = keycloak.realm(realm);
        var rolesResource = realmResource.roles();
        
        try {
            rolesResource.get(roleName).toRepresentation();
            logger.info("✅ Rôle {} existe déjà dans Keycloak", roleName);
            return;
        } catch (Exception notFound) {
            logger.info("📝 Rôle {} non trouvé, création en cours...", roleName);
            
            RoleRepresentation rr = new RoleRepresentation();
            rr.setName(roleName);
            rr.setComposite(false);
            rr.setClientRole(false);
            rr.setDescription("Role créé automatiquement pour " + roleName);
            
            try {
                rolesResource.create(rr);
                logger.info("✅ Rôle {} créé avec succès dans Keycloak", roleName);
                
                // Vérifier que le rôle existe maintenant
                rolesResource.get(roleName).toRepresentation();
                logger.info("✅ Vérification de l'existence du rôle {} réussie", roleName);
            } catch (Exception createEx) {
                logger.error("❌ Échec de la création du rôle {}: {}", roleName, createEx.getMessage());
                throw new RuntimeException("Failed to create realm role '" + roleName + "': " + createEx.getMessage());
            }
        }
    }

    /**
     * Nettoyage en cas d'échec
     */
    private void cleanupOnFailure(String keycloakUserId, String realm, Utilisateur backendUser) {
        // Supprimer l'utilisateur Keycloak s'il a été créé
        if (keycloakUserId != null) {
            try {
                keycloak.realm(realm).users().get(keycloakUserId).remove();
                logger.info("🧹 Utilisateur Keycloak supprimé lors du nettoyage");
            } catch (Exception e) {
                logger.warn("⚠️ Impossible de supprimer l'utilisateur Keycloak: {}", e.getMessage());
            }
        }
        
        // L'utilisateur local sera automatiquement supprimé par le rollback de la transaction
        if (backendUser != null) {
            logger.info("🧹 Utilisateur local sera supprimé par le rollback de la transaction");
        }
    }

    /**
     * Construire la réponse de succès
     */
    private Map<String, Object> buildSuccessResponse(RegisterRequest request, String keycloakUserId, 
                                                   Utilisateur backendUser, Touriste createdTouriste) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Tourist registration successful");
        response.put("keycloakUserId", keycloakUserId);
        response.put("backendUserId", backendUser.getIdUtilisateur());
        response.put("touristeId", createdTouriste != null ? createdTouriste.getIdTouriste() : null);
        response.put("role", ROLE_NAME);
        response.put("email", request.getEmail());
        response.put("username", request.getUsername() != null ? request.getUsername() : request.getEmail());
        response.put("firstName", request.getFirstName());
        response.put("lastName", request.getLastName());
        
        // Informations sur les champs optionnels
        response.put("profileCompletion", Map.of(
            "completed", false,
            "message", "Basic registration completed. You can complete your profile by adding: adresse, dateNaissance, telephone, cin, and other optional information.",
            "optionalFields", Arrays.asList("adresse", "dateNaissance", "telephone", "cin", "nationalite", "passeport", "preferences", "niveauLangue", "budgetMax")
        ));
        
        return response;
    }

    /**
     * Récupérer le realm depuis la configuration
     */
    private String getRealm() {
        String realm = env.getProperty("keycloak.realm");
        if (realm == null || realm.isBlank()) {
            throw new RuntimeException("Keycloak realm not configured");
        }
        return realm;
    }
}



