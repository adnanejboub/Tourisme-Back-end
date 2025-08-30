package com.tourisme.tourisme.entities;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("RIAD")
public class Riad extends Hebergement {
    
    @Column(name = "est_traditionnel")
    private Boolean estTraditionnel;
    
    @Column(name = "nombre_chambres")
    private Integer nombreChambres;
    
    @Column(name = "patio")
    private Boolean patio;
    
    @Column(name = "terrasse")
    private Boolean terrasse;
    
    @Column(name = "cuisine_traditionnelle")
    private Boolean cuisineTraditionnelle;
    
    @Column(name = "style_architectural")
    private String styleArchitectural;
    
    @Column(name = "annee_construction")
    private Integer anneeConstruction;
    
    // Constructors
    public Riad() {
        super();
    }
    
    public Riad(String nomHebergement, String adresse, Float prixParNuit, Integer etoiles,
               Boolean estTraditionnel, Integer nombreChambres) {
        super(nomHebergement, adresse, prixParNuit, etoiles);
        this.estTraditionnel = estTraditionnel;
        this.nombreChambres = nombreChambres;
    }
    
    // Methods from UML
    public Boolean estTraditionnel() {
        return estTraditionnel;
    }
    
    public Integer obtenirNombreChambres() {
        return nombreChambres;
    }
    
    public Boolean verifierDisponibilite() {
        return getIsDisponible() && nombreChambres > 0;
    }
    
    // Getters and Setters
    public Boolean getEstTraditionnel() {
        return estTraditionnel;
    }
    
    public void setEstTraditionnel(Boolean estTraditionnel) {
        this.estTraditionnel = estTraditionnel;
    }
    
    public Integer getNombreChambres() {
        return nombreChambres;
    }
    
    public void setNombreChambres(Integer nombreChambres) {
        this.nombreChambres = nombreChambres;
    }
    
    public Boolean getPatio() {
        return patio;
    }
    
    public void setPatio(Boolean patio) {
        this.patio = patio;
    }
    
    public Boolean getTerrasse() {
        return terrasse;
    }
    
    public void setTerrasse(Boolean terrasse) {
        this.terrasse = terrasse;
    }
    
    public Boolean getCuisineTraditionnelle() {
        return cuisineTraditionnelle;
    }
    
    public void setCuisineTraditionnelle(Boolean cuisineTraditionnelle) {
        this.cuisineTraditionnelle = cuisineTraditionnelle;
    }
    
    public String getStyleArchitectural() {
        return styleArchitectural;
    }
    
    public void setStyleArchitectural(String styleArchitectural) {
        this.styleArchitectural = styleArchitectural;
    }
    
    public Integer getAnneeConstruction() {
        return anneeConstruction;
    }
    
    public void setAnneeConstruction(Integer anneeConstruction) {
        this.anneeConstruction = anneeConstruction;
    }
} 