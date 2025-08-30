package com.tourisme.tourisme.service;

import com.tourisme.tourisme.entities.Role;
import com.tourisme.tourisme.entities.Utilisateur;
import com.tourisme.tourisme.repository.RoleRepository;
import com.tourisme.tourisme.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserMappingService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Extract user information from the current JWT token
     * This method is called automatically by Spring Security for each authenticated request
     */
    public Optional<Utilisateur> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            String email = jwt.getClaimAsString("email");
            
            if (email != null) {
                return utilisateurRepository.findByEmail(email);
            }
        }
        
        return Optional.empty();
    }

    /**
     * Get the current user's email from JWT token
     */
    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            return jwt.getClaimAsString("email");
        }
        
        return null;
    }

    /**
     * Get the current user's Keycloak ID from JWT token
     */
    public String getCurrentUserKeycloakId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            return jwt.getSubject(); // This is the Keycloak user ID
        }
        
        return null;
    }

    /**
     * Sync a Keycloak user to the backend database
     * This is called when a user first accesses the backend after authentication
     */
    public Utilisateur syncKeycloakUser(String keycloakId, String email, String firstName, String lastName, String roleName) {
        // Check if user already exists in backend
        Optional<Utilisateur> existingUser = utilisateurRepository.findByEmail(email);
        
        if (existingUser.isPresent()) {
            // Update existing user if needed
            Utilisateur user = existingUser.get();
            user.setNom(lastName);
            user.setPrenom(firstName);
            return utilisateurRepository.save(user);
        } else {
            // Create new user in backend
            Utilisateur newUser = new Utilisateur();
            newUser.setNom(lastName);
            newUser.setPrenom(firstName);
            newUser.setEmail(email);
            
            // Set role based on Keycloak role
            Role role = roleRepository.findByNomRole(roleName)
                    .orElseGet(() -> createDefaultRole(roleName));
            newUser.setRole(role);
            
            return utilisateurRepository.save(newUser);
        }
    }

    /**
     * Create a default role if it doesn't exist
     */
    private Role createDefaultRole(String roleName) {
        Role role = new Role();
        role.setNomRole(roleName);
        role.setDescription("Role créé automatiquement pour " + roleName);
        return roleRepository.save(role);
    }

    /**
     * Check if the current user has a specific role
     */
    public boolean hasRole(String roleName) {
        Optional<Utilisateur> user = getCurrentUser();
        return user.isPresent() && user.get().getRole() != null && 
               user.get().getRole().getNomRole().equals(roleName);
    }

    /**
     * Get user ID from current authentication
     */
    public Long getCurrentUserId() {
        Optional<Utilisateur> user = getCurrentUser();
        return user.map(Utilisateur::getIdUtilisateur).orElse(null);
    }
} 