package com.tourisme.tourisme.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO pour les requêtes d'authentification sociale
 */
public class SocialAuthRequest {
    
    @JsonProperty("provider")
    private String provider; // google, facebook, apple
    
    @JsonProperty("access_token")
    private String accessToken;
    
    @JsonProperty("id_token")
    private String idToken;
    
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("display_name")
    private String displayName;
    
    @JsonProperty("photo_url")
    private String photoUrl;

    // Constructeurs
    public SocialAuthRequest() {}

    public SocialAuthRequest(String provider, String accessToken, String idToken, 
                           String email, String displayName, String photoUrl) {
        this.provider = provider;
        this.accessToken = accessToken;
        this.idToken = idToken;
        this.email = email;
        this.displayName = displayName;
        this.photoUrl = photoUrl;
    }

    // Getters et Setters
    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
