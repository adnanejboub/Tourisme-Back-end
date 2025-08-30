package com.tourisme.tourisme.service;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class KeycloakUserService {

    @Autowired
    private Keycloak keycloakAdminClient;

    @Value("${keycloak.realm:tourisme}")
    private String realm;

    /**
     * Get all users from Keycloak
     */
    public List<UserRepresentation> getAllKeycloakUsers() {
        RealmResource realmResource = keycloakAdminClient.realm(realm);
        UsersResource usersResource = realmResource.users();
        return usersResource.list();
    }

    /**
     * Search users in Keycloak by query
     */
    public List<UserRepresentation> searchKeycloakUsers(String query) {
        RealmResource realmResource = keycloakAdminClient.realm(realm);
        UsersResource usersResource = realmResource.users();
        return usersResource.search(query);
    }

    /**
     * Search users by email
     */
    public List<UserRepresentation> searchKeycloakUsersByEmail(String email) {
        RealmResource realmResource = keycloakAdminClient.realm(realm);
        UsersResource usersResource = realmResource.users();
        return usersResource.searchByEmail(email, true);
    }

    /**
     * Get user by ID from Keycloak
     */
    public UserRepresentation getKeycloakUserById(String userId) {
        RealmResource realmResource = keycloakAdminClient.realm(realm);
        UserResource userResource = realmResource.users().get(userId);
        return userResource.toRepresentation();
    }

    /**
     * Enable/Disable user in Keycloak
     */
    public void toggleUserEnabled(String userId, boolean enabled) {
        RealmResource realmResource = keycloakAdminClient.realm(realm);
        UserResource userResource = realmResource.users().get(userId);
        UserRepresentation user = userResource.toRepresentation();
        user.setEnabled(enabled);
        userResource.update(user);
    }

    /**
     * Delete user from Keycloak
     */
    public void deleteKeycloakUser(String userId) {
        RealmResource realmResource = keycloakAdminClient.realm(realm);
        realmResource.users().delete(userId);
    }

    /**
     * Get user roles from Keycloak
     */
    public List<String> getUserRoles(String userId) {
        RealmResource realmResource = keycloakAdminClient.realm(realm);
        UserResource userResource = realmResource.users().get(userId);
        return userResource.roles().realmLevel().listAll().stream()
                .map(role -> role.getName())
                .collect(Collectors.toList());
    }

    /**
     * Convert UserRepresentation to a simplified Map for API response
     */
    public Map<String, Object> convertToUserMap(UserRepresentation user) {
        return Map.of(
            "id", user.getId(),
            "username", user.getUsername() != null ? user.getUsername() : "",
            "email", user.getEmail() != null ? user.getEmail() : "",
            "firstName", user.getFirstName() != null ? user.getFirstName() : "",
            "lastName", user.getLastName() != null ? user.getLastName() : "",
            "enabled", user.isEnabled(),
            "emailVerified", user.isEmailVerified(),
            "createdTimestamp", user.getCreatedTimestamp() != null ? user.getCreatedTimestamp() : 0
        );
    }

    /**
     * Get users count from Keycloak
     */
    public int getUsersCount() {
        RealmResource realmResource = keycloakAdminClient.realm(realm);
        return realmResource.users().count();
    }
}
