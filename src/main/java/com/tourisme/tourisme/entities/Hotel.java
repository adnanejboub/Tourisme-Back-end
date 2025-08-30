package com.tourisme.tourisme.entities;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("HOTEL")
public class Hotel extends Hebergement {
    
    @Column(name = "nombre_chambres")
    private Integer nombreChambres;
    
    @Column(name = "etoiles")
    private Integer etoiles;
    
    @Column(name = "type_hotel")
    @Enumerated(EnumType.STRING)
    private TypeHotel typeHotel;
    
    @Column(name = "services_hotel")
    private String servicesHotel;
    
    @Column(name = "restaurant")
    private Boolean restaurant;
    
    @Column(name = "piscine")
    private Boolean piscine;
    
    @Column(name = "spa")
    private Boolean spa;
    
    @Column(name = "gym")
    private Boolean gym;
    
    // Enum for hotel type
    public enum TypeHotel {
        BOUTIQUE,
        RESORT,
        BUSINESS,
        FAMILY,
        LUXURY,
        BUDGET
    }
    
    // Constructors
    public Hotel() {
        super();
    }
    
    public Hotel(String nomHebergement, String adresse, Float prixParNuit, Integer etoiles,
                Integer nombreChambres, TypeHotel typeHotel) {
        super(nomHebergement, adresse, prixParNuit, etoiles);
        this.nombreChambres = nombreChambres;
        this.typeHotel = typeHotel;
    }
    
    // Methods from UML
    public Boolean verifierDisponibilite() {
        return getIsDisponible() && nombreChambres > 0;
    }
    
    public Integer obtenirNombreChambres() {
        return nombreChambres;
    }
    
    public Integer obtenirEtoiles() {
        return etoiles;
    }
    
    // Getters and Setters
    public Integer getNombreChambres() {
        return nombreChambres;
    }
    
    public void setNombreChambres(Integer nombreChambres) {
        this.nombreChambres = nombreChambres;
    }
    
    public Integer getEtoiles() {
        return etoiles;
    }
    
    public void setEtoiles(Integer etoiles) {
        this.etoiles = etoiles;
    }
    
    public TypeHotel getTypeHotel() {
        return typeHotel;
    }
    
    public void setTypeHotel(TypeHotel typeHotel) {
        this.typeHotel = typeHotel;
    }
    
    public String getServicesHotel() {
        return servicesHotel;
    }
    
    public void setServicesHotel(String servicesHotel) {
        this.servicesHotel = servicesHotel;
    }
    
    public Boolean getRestaurant() {
        return restaurant;
    }
    
    public void setRestaurant(Boolean restaurant) {
        this.restaurant = restaurant;
    }
    
    public Boolean getPiscine() {
        return piscine;
    }
    
    public void setPiscine(Boolean piscine) {
        this.piscine = piscine;
    }
    
    public Boolean getSpa() {
        return spa;
    }
    
    public void setSpa(Boolean spa) {
        this.spa = spa;
    }
    
    public Boolean getGym() {
        return gym;
    }
    
    public void setGym(Boolean gym) {
        this.gym = gym;
    }
} 