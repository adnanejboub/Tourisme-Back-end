package com.tourisme.tourisme.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO pour les réponses d'authentification sociale
 */
public class SocialAuthResponse {
    
    @JsonProperty("access_token")
    private String accessToken;
    
    @JsonProperty("refresh_token")
    private String refreshToken;
    
    @JsonProperty("expires_in")
    private int expiresIn;
    
    @JsonProperty("token_type")
    private String tokenType = "Bearer";
    
    @JsonProperty("user_id")
    private String userId;
    
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("username")
    private String username;

    // Constructeurs
    public SocialAuthResponse() {}

    public SocialAuthResponse(String accessToken, String refreshToken, int expiresIn, 
                            String tokenType, String userId, String email, String username) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.tokenType = tokenType;
        this.userId = userId;
        this.email = email;
        this.username = username;
    }

    // Getters et Setters
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
