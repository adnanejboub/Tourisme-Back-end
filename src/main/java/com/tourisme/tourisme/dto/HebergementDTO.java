package com.tourisme.tourisme.dto;

public class HebergementDTO {
    private Long idHebergement;
    private String nomHebergement;
    private String adresse;
    private Float prixParNuit;
    private Integer etoiles;
    private String description;
    private Boolean isDisponible;
    private String hebergementType; // To distinguish between different types

    // Constructors
    public HebergementDTO() {}

    public HebergementDTO(Long idHebergement, String nomHebergement, String adresse, Float prixParNuit, Integer etoiles) {
        this.idHebergement = idHebergement;
        this.nomHebergement = nomHebergement;
        this.adresse = adresse;
        this.prixParNuit = prixParNuit;
        this.etoiles = etoiles;
        this.isDisponible = true;
    }

    // Getters and Setters
    public Long getIdHebergement() {
        return idHebergement;
    }

    public void setIdHebergement(Long idHebergement) {
        this.idHebergement = idHebergement;
    }

    public String getNomHebergement() {
        return nomHebergement;
    }

    public void setNomHebergement(String nomHebergement) {
        this.nomHebergement = nomHebergement;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Float getPrixParNuit() {
        return prixParNuit;
    }

    public void setPrixParNuit(Float prixParNuit) {
        this.prixParNuit = prixParNuit;
    }

    public Integer getEtoiles() {
        return etoiles;
    }

    public void setEtoiles(Integer etoiles) {
        this.etoiles = etoiles;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsDisponible() {
        return isDisponible;
    }

    public void setIsDisponible(Boolean isDisponible) {
        this.isDisponible = isDisponible;
    }

    public String getHebergementType() {
        return hebergementType;
    }

    public void setHebergementType(String hebergementType) {
        this.hebergementType = hebergementType;
    }
}
