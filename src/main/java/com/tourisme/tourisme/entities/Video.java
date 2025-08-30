package com.tourisme.tourisme.entities;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("VIDEO")
public class Video extends Media {
    
    @Column(name = "duree")
    private Integer duree;
    
    @Column(name = "qualite")
    private String qualite;
    
    // Constructors
    public Video() {}
    
    public Video(String nomMedia, Long taille, String typeMedia, Integer duree, String qualite) {
        super(nomMedia, taille, typeMedia);
        this.duree = duree;
        this.qualite = qualite;
    }
    
    // Getters and Setters
    public Integer getDuree() {
        return duree;
    }
    
    public void setDuree(Integer duree) {
        this.duree = duree;
    }
    
    public String getQualite() {
        return qualite;
    }
    
    public void setQualite(String qualite) {
        this.qualite = qualite;
    }
} 