package com.tourisme.tourisme.entities;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("IMAGE")
public class Image extends Media {
    
    @Column(name = "largeur")
    private Integer largeur;
    
    @Column(name = "hauteur")
    private Integer hauteur;
    
    // Constructors
    public Image() {}
    
    public Image(String nomMedia, Long taille, String typeMedia, Integer largeur, Integer hauteur) {
        super(nomMedia, taille, typeMedia);
        this.largeur = largeur;
        this.hauteur = hauteur;
    }
    
    // Getters and Setters
    public Integer getLargeur() {
        return largeur;
    }
    
    public void setLargeur(Integer largeur) {
        this.largeur = largeur;
    }
    
    public Integer getHauteur() {
        return hauteur;
    }
    
    public void setHauteur(Integer hauteur) {
        this.hauteur = hauteur;
    }
} 