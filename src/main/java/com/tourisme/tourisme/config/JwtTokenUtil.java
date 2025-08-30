package com.tourisme.tourisme.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JwtTokenUtil {

    /**
     * Get the current authenticated user's username from the JWT token
     */
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            return jwt.getSubject();
        }
        return null;
    }

    /**
     * Get the current authenticated user's email from the JWT token
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
     * Get the current authenticated user's roles from the JWT token
     */
    public String[] getCurrentUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
            if (realmAccess != null) {
                Object roles = realmAccess.get("roles");
                if (roles instanceof String[]) {
                    return (String[]) roles;
                }
            }
        }
        return new String[0];
    }

    /**
     * Check if the current user has a specific role
     */
    public boolean hasRole(String role) {
        String[] roles = getCurrentUserRoles();
        for (String userRole : roles) {
            if (userRole.equals(role)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the full JWT token claims
     */
    public Map<String, Object> getTokenClaims() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            return jwt.getClaims();
        }
        return null;
    }
} 