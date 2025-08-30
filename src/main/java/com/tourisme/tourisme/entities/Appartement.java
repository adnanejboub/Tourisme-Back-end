package com.tourisme.tourisme.entities;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("APPARTEMENT")
public class Appartement extends Hebergement {
    
    @Column(name = "nom_proprietaire")
    private String nomProprietaire;
    
    @Column(name = "capacite_max")
    private Integer capaciteMax;
    
    @Column(name = "nombre_pieces")
    private Integer nombrePieces;
    
    @Column(name = "surface")
    private Float surface; // in square meters
    
    @Column(name = "etage")
    private Integer etage;
    
    @Column(name = "ascenseur")
    private Boolean ascenseur;
    
    @Column(name = "parking")
    private Boolean parking;
    
    @Column(name = "wifi")
    private Boolean wifi;
    
    @Column(name = "cuisine_equipee")
    private Boolean cuisineEquipee;
    
    // Constructors
    public Appartement() {
        super();
    }
    
    public Appartement(String nomHebergement, String adresse, Float prixParNuit, Integer etoiles,
                      String nomProprietaire, Integer capaciteMax) {
        super(nomHebergement, adresse, prixParNuit, etoiles);
        this.nomProprietaire = nomProprietaire;
        this.capaciteMax = capaciteMax;
    }
    
    // Methods from UML
    public String obtenirProprietaire() {
        return nomProprietaire;
    }
    
    public Integer obtenirCapaciteMax() {
        return capaciteMax;
    }
    
    public Boolean verifierDisponibilite() {
        return getIsDisponible() && capaciteMax > 0;
    }
    
    // Getters and Setters
    public String getNomProprietaire() {
        return nomProprietaire;
    }
    
    public void setNomProprietaire(String nomProprietaire) {
        this.nomProprietaire = nomProprietaire;
    }
    
    public Integer getCapaciteMax() {
        return capaciteMax;
    }
    
    public void setCapaciteMax(Integer capaciteMax) {
        this.capaciteMax = capaciteMax;
    }
    
    public Integer getNombrePieces() {
        return nombrePieces;
    }
    
    public void setNombrePieces(Integer nombrePieces) {
        this.nombrePieces = nombrePieces;
    }
    
    public Float getSurface() {
        return surface;
    }
    
    public void setSurface(Float surface) {
        this.surface = surface;
    }
    
    public Integer getEtage() {
        return etage;
    }
    
    public void setEtage(Integer etage) {
        this.etage = etage;
    }
    
    public Boolean getAscenseur() {
        return ascenseur;
    }
    
    public void setAscenseur(Boolean ascenseur) {
        this.ascenseur = ascenseur;
    }
    
    public Boolean getParking() {
        return parking;
    }
    
    public void setParking(Boolean parking) {
        this.parking = parking;
    }
    
    public Boolean getWifi() {
        return wifi;
    }
    
    public void setWifi(Boolean wifi) {
        this.wifi = wifi;
    }
    
    public Boolean getCuisineEquipee() {
        return cuisineEquipee;
    }
    
    public void setCuisineEquipee(Boolean cuisineEquipee) {
        this.cuisineEquipee = cuisineEquipee;
    }
} 