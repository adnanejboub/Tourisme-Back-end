package com.tourisme.tourisme.dto;

public class VilleSummaryDTO {
    private Long idVille;
    private String nomVille;
    private String description;
    private Float latitude;
    private Float longitude;
    private String paysNom;
    private String climatNom;
    
    // Basic characteristics
    private Boolean isPlage;
    private Boolean isMontagne;
    private Boolean isDesert;
    private Boolean isRiviera;
    private Boolean isHistorique;
    private Boolean isCulturelle;
    private Boolean isModerne;

    // Constructors
    public VilleSummaryDTO() {}

    public VilleSummaryDTO(Long idVille, String nomVille, String description) {
        this.idVille = idVille;
        this.nomVille = nomVille;
        this.description = description;
    }

    // Getters and Setters
    public Long getIdVille() {
        return idVille;
    }

    public void setIdVille(Long idVille) {
        this.idVille = idVille;
    }

    public String getNomVille() {
        return nomVille;
    }

    public void setNomVille(String nomVille) {
        this.nomVille = nomVille;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getPaysNom() {
        return paysNom;
    }

    public void setPaysNom(String paysNom) {
        this.paysNom = paysNom;
    }

    public String getClimatNom() {
        return climatNom;
    }

    public void setClimatNom(String climatNom) {
        this.climatNom = climatNom;
    }

    public Boolean getIsPlage() {
        return isPlage;
    }

    public void setIsPlage(Boolean isPlage) {
        this.isPlage = isPlage;
    }

    public Boolean getIsMontagne() {
        return isMontagne;
    }

    public void setIsMontagne(Boolean isMontagne) {
        this.isMontagne = isMontagne;
    }

    public Boolean getIsDesert() {
        return isDesert;
    }

    public void setIsDesert(Boolean isDesert) {
        this.isDesert = isDesert;
    }

    public Boolean getIsRiviera() {
        return isRiviera;
    }

    public void setIsRiviera(Boolean isRiviera) {
        this.isRiviera = isRiviera;
    }

    public Boolean getIsHistorique() {
        return isHistorique;
    }

    public void setIsHistorique(Boolean isHistorique) {
        this.isHistorique = isHistorique;
    }

    public Boolean getIsCulturelle() {
        return isCulturelle;
    }

    public void setIsCulturelle(Boolean isCulturelle) {
        this.isCulturelle = isCulturelle;
    }

    public Boolean getIsModerne() {
        return isModerne;
    }

    public void setIsModerne(Boolean isModerne) {
        this.isModerne = isModerne;
    }
}
