package com.tourisme.tourisme.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "localisation")
public class Localisation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_localisation")
    private Long idLocalisation;
    
    @Column(name = "latitude", nullable = false)
    private Float latitude;
    
    @Column(name = "longitude", nullable = false)
    private Float longitude;
    
    @Column(name = "region")
    private String region;
    
    // Constructors
    public Localisation() {}
    
    public Localisation(Float latitude, Float longitude, String region) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.region = region;
    }
    
    // Methods from UML
    public double calculDistance(Localisation autre) {
        // Calculate distance between two points using Haversine formula
        double lat1 = Math.toRadians(this.latitude);
        double lat2 = Math.toRadians(autre.latitude);
        double deltaLat = Math.toRadians(autre.latitude - this.latitude);
        double deltaLon = Math.toRadians(autre.longitude - this.longitude);
        
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                   Math.cos(lat1) * Math.cos(lat2) *
                   Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return 6371 * c; // Earth's radius in km
    }
    
    public void operation() {
        // Placeholder for operation method
    }
    
    // Getters and Setters
    public Long getIdLocalisation() {
        return idLocalisation;
    }
    
    public void setIdLocalisation(Long idLocalisation) {
        this.idLocalisation = idLocalisation;
    }
    
    public Float getLatitude() {
        return latitude;
    }
    
    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }
    
    public Float getLongitude() {
        return longitude;
    }
    
    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }
    
    public String getRegion() {
        return region;
    }
    
    public void setRegion(String region) {
        this.region = region;
    }
} 