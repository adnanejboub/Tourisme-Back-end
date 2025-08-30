package com.tourisme.tourisme.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;

import java.util.Arrays;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(authz -> authz
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // 💡 Preflight support
                .requestMatchers("/public/**", "/test/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/tourist/**").hasRole("TOURISTE")
                .requestMatchers("/api/events/**").permitAll() // Events are public
                .requestMatchers("/api/search/**").permitAll() // Search is public
                .requestMatchers("/auth/**").permitAll() // Tous les endpoints d'auth sont publics
                .requestMatchers("/api/**").authenticated()
                .anyRequest().authenticated()
            )
            // Configuration OAuth2 Resource Server pour la validation JWT
            // Note: Cette configuration s'applique à tous les endpoints
            // mais les endpoints /auth/** sont marqués comme permitAll()
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .jwkSetUri("http://localhost:8090/realms/tourisme/protocol/openid-connect/certs")
                    .jwtAuthenticationConverter(jwtAuthenticationConverter())
                )
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Allow all origins for development (be more restrictive in production)
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        
        // Allow all methods
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        
        // Allow all headers
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        // Expose headers
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type", "Access-Control-Allow-Origin"));
        
        // Allow credentials
        configuration.setAllowCredentials(true);
        
        // Set max age
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // apply globally

        return source;
    }
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakJwtGrantedAuthoritiesConverter());
        return jwtAuthenticationConverter;
    }

    /**
     * Custom converter to handle Keycloak's nested role structure
     */
    public static class KeycloakJwtGrantedAuthoritiesConverter implements 
            org.springframework.core.convert.converter.Converter<org.springframework.security.oauth2.jwt.Jwt, 
            java.util.Collection<org.springframework.security.core.GrantedAuthority>> {
        
        @Override
        public java.util.Collection<org.springframework.security.core.GrantedAuthority> convert(
                org.springframework.security.oauth2.jwt.Jwt jwt) {
            
            java.util.Collection<org.springframework.security.core.GrantedAuthority> authorities = 
                new java.util.ArrayList<>();
            
            // Extract realm_access.roles from JWT
            Object realmAccess = jwt.getClaim("realm_access");
            if (realmAccess instanceof java.util.Map) {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> realmAccessMap = (java.util.Map<String, Object>) realmAccess;
                Object roles = realmAccessMap.get("roles");
                
                if (roles instanceof java.util.List) {
                    @SuppressWarnings("unchecked")
                    java.util.List<String> rolesList = (java.util.List<String>) roles;
                    
                    for (String role : rolesList) {
                        // Skip default Keycloak roles
                        if (!role.startsWith("default-roles-") && 
                            !role.equals("offline_access") && 
                            !role.equals("uma_authorization")) {
                            authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
                        }
                    }
                }
            }
            
            return authorities;
        }
    }
}
