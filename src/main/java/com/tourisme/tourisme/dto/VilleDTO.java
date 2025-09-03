package com.tourisme.tourisme.dto;

import java.util.List;

public class VilleDTO {
    private Long idVille;
    private String nomVille;
    private String description;
    private Float latitude;
    private Float longitude;
    private Boolean allInclusive;
    private String allouement;
    private Float noteMoyenne;
    
    // Boolean attributes for city characteristics
    private Boolean isPlage;
    private Boolean isMontagne;
    private Boolean isDesert;
    private Boolean isRiviera;
    private Boolean isHistorique;
    private Boolean isCulturelle;
    private Boolean isModerne;
    private Boolean hasAeroport;
    private Boolean hasGare;
    private Boolean hasPort;
    private Boolean hasPlage;
    private Boolean hasMontagne;
    private Boolean hasDesert;
    private Boolean hasRiviera;
    private Boolean hasHistorique;
    private Boolean hasCulturelle;
    private Boolean hasModerne;
    
    // Related information (avoiding circular references)
    private String paysNom;
    private String climatNom;
    private List<String> specialites;
    private List<MonumentDTO> monuments;
    private List<ActiviteDTO> activites;
    private List<HebergementDTO> hebergements;
    private List<AeroportDTO> aeroports;
    private List<ServiceDTO> services;

    // Constructors
    public VilleDTO() {}

    public VilleDTO(Long idVille, String nomVille, String description) {
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

    public Boolean getAllInclusive() {
        return allInclusive;
    }

    public void setAllInclusive(Boolean allInclusive) {
        this.allInclusive = allInclusive;
    }

    public String getAllouement() {
        return allouement;
    }

    public void setAllouement(String allouement) {
        this.allouement = allouement;
    }

    public Float getNoteMoyenne() {
        return noteMoyenne;
    }

    public void setNoteMoyenne(Float noteMoyenne) {
        this.noteMoyenne = noteMoyenne;
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

    public Boolean getHasAeroport() {
        return hasAeroport;
    }

    public void setHasAeroport(Boolean hasAeroport) {
        this.hasAeroport = hasAeroport;
    }

    public Boolean getHasGare() {
        return hasGare;
    }

    public void setHasGare(Boolean hasGare) {
        this.hasGare = hasGare;
    }

    public Boolean getHasPort() {
        return hasPort;
    }

    public void setHasPort(Boolean hasPort) {
        this.hasPort = hasPort;
    }

    public Boolean getHasPlage() {
        return hasPlage;
    }

    public void setHasPlage(Boolean hasPlage) {
        this.hasPlage = hasPlage;
    }

    public Boolean getHasMontagne() {
        return hasMontagne;
    }

    public void setHasMontagne(Boolean hasMontagne) {
        this.hasMontagne = hasMontagne;
    }

    public Boolean getHasDesert() {
        return hasDesert;
    }

    public void setHasDesert(Boolean hasDesert) {
        this.hasDesert = hasDesert;
    }

    public Boolean getHasRiviera() {
        return hasRiviera;
    }

    public void setHasRiviera(Boolean hasRiviera) {
        this.hasRiviera = hasRiviera;
    }

    public Boolean getHasHistorique() {
        return hasHistorique;
    }

    public void setHasHistorique(Boolean hasHistorique) {
        this.hasHistorique = hasHistorique;
    }

    public Boolean getHasCulturelle() {
        return hasCulturelle;
    }

    public void setHasCulturelle(Boolean hasCulturelle) {
        this.hasCulturelle = hasCulturelle;
    }

    public Boolean getHasModerne() {
        return hasModerne;
    }

    public void setHasModerne(Boolean hasModerne) {
        this.hasModerne = hasModerne;
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

    public List<String> getSpecialites() {
        return specialites;
    }

    public void setSpecialites(List<String> specialites) {
        this.specialites = specialites;
    }

    public List<MonumentDTO> getMonuments() {
        return monuments;
    }

    public void setMonuments(List<MonumentDTO> monuments) {
        this.monuments = monuments;
    }

    public List<ActiviteDTO> getActivites() {
        return activites;
    }

    public void setActivites(List<ActiviteDTO> activites) {
        this.activites = activites;
    }

    public List<HebergementDTO> getHebergements() {
        return hebergements;
    }

    public void setHebergements(List<HebergementDTO> hebergements) {
        this.hebergements = hebergements;
    }

    public List<AeroportDTO> getAeroports() {
        return aeroports;
    }

    public void setAeroports(List<AeroportDTO> aeroports) {
        this.aeroports = aeroports;
    }

    public List<ServiceDTO> getServices() {
        return services;
    }

    public void setServices(List<ServiceDTO> services) {
        this.services = services;
    }
} 