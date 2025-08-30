package com.tourisme.tourisme.entities;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("TRADITIONNEL")
public class ProduitTraditionnel extends Produit {
    
    @Column(name = "region_origine")
    private String regionOrigine;
    
    @Column(name = "certificat_authenticite")
    private Boolean certificatAuthenticite;
    
    @Column(name = "artisan")
    private String artisan;
    
    @Column(name = "technique_fabrication")
    private String techniqueFabrication;
    
    @Column(name = "materiaux")
    private String materiaux;
    
    @Column(name = "age_minimum")
    private Integer ageMinimum;
    
    // Constructors
    public ProduitTraditionnel() {
        super();
    }
    
    public ProduitTraditionnel(String nom, String description, Float prix, Integer stockDisponible, 
                              String regionOrigine, Boolean certificatAuthenticite) {
        super(nom, description, prix, stockDisponible);
        this.regionOrigine = regionOrigine;
        this.certificatAuthenticite = certificatAuthenticite;
    }
    
    // Methods from UML
    public Boolean verifierAuthenticite() {
        return certificatAuthenticite != null && certificatAuthenticite;
    }
    
    public String obtenirOrigine() {
        return regionOrigine;
    }
    
    // Getters and Setters
    public String getRegionOrigine() {
        return regionOrigine;
    }
    
    public void setRegionOrigine(String regionOrigine) {
        this.regionOrigine = regionOrigine;
    }
    
    public Boolean getCertificatAuthenticite() {
        return certificatAuthenticite;
    }
    
    public void setCertificatAuthenticite(Boolean certificatAuthenticite) {
        this.certificatAuthenticite = certificatAuthenticite;
    }
    
    public String getArtisan() {
        return artisan;
    }
    
    public void setArtisan(String artisan) {
        this.artisan = artisan;
    }
    
    public String getTechniqueFabrication() {
        return techniqueFabrication;
    }
    
    public void setTechniqueFabrication(String techniqueFabrication) {
        this.techniqueFabrication = techniqueFabrication;
    }
    
    public String getMateriaux() {
        return materiaux;
    }
    
    public void setMateriaux(String materiaux) {
        this.materiaux = materiaux;
    }
    
    public Integer getAgeMinimum() {
        return ageMinimum;
    }
    
    public void setAgeMinimum(Integer ageMinimum) {
        this.ageMinimum = ageMinimum;
    }
} 