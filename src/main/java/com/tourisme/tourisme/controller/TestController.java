package com.tourisme.tourisme.controller;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.lang.Math;

@RestController
@RequestMapping("/test")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = {"Content-Type", "Authorization"})
public class TestController {

    @GetMapping("/cors")
    public Map<String, Object> testCors() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "CORS is working!");
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Application is running");
        return response;
    }

    @PostMapping("/auth-test")
    public Map<String, Object> testAuth(@RequestHeader("Authorization") String authHeader) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Authentication test successful");
        response.put("authHeader", authHeader.substring(0, Math.min(50, authHeader.length())) + "...");
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    @GetMapping("/roles")
    public Map<String, Object> testRoles() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Roles test");
        response.put("timestamp", System.currentTimeMillis());
        
        // Get current authentication
        org.springframework.security.core.Authentication auth = 
            org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        
        if (auth != null && auth.getPrincipal() instanceof org.springframework.security.oauth2.jwt.Jwt) {
            org.springframework.security.oauth2.jwt.Jwt jwt = (org.springframework.security.oauth2.jwt.Jwt) auth.getPrincipal();
            
            response.put("principal", auth.getPrincipal().getClass().getSimpleName());
            response.put("authorities", auth.getAuthorities().stream()
                .map(Object::toString)
                .collect(java.util.stream.Collectors.toList()));
            response.put("authenticated", auth.isAuthenticated());
            
            // Debug JWT claims
            response.put("realm_access", jwt.getClaim("realm_access"));
            response.put("email", jwt.getClaimAsString("email"));
            response.put("subject", jwt.getSubject());
            
            // Extract roles manually for debugging
            Object realmAccess = jwt.getClaim("realm_access");
            if (realmAccess instanceof java.util.Map) {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> realmAccessMap = (java.util.Map<String, Object>) realmAccess;
                Object roles = realmAccessMap.get("roles");
                response.put("extracted_roles", roles);
            }
        } else {
            response.put("error", "No JWT authentication found");
        }
        
        return response;
    }
} 