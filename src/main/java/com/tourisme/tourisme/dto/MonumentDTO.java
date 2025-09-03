package com.tourisme.tourisme.dto;

import java.util.List;

public class MonumentDTO {
    private Long idMonument;
    private String nomMonument;
    private String adresseMonument;
    private Float prix;
    private Boolean gratuit;
    private String hasCulturelle;
    private String hasHistorique;
    private Float notesMoyennes;
    private List<String> mediaUrls; // Just URLs, not full Media objects

    // Constructors
    public MonumentDTO() {}

    public MonumentDTO(Long idMonument, String nomMonument, String adresseMonument, Float prix, Boolean gratuit) {
        this.idMonument = idMonument;
        this.nomMonument = nomMonument;
        this.adresseMonument = adresseMonument;
        this.prix = prix;
        this.gratuit = gratuit;
    }

    // Getters and Setters
    public Long getIdMonument() {
        return idMonument;
    }

    public void setIdMonument(Long idMonument) {
        this.idMonument = idMonument;
    }

    public String getNomMonument() {
        return nomMonument;
    }

    public void setNomMonument(String nomMonument) {
        this.nomMonument = nomMonument;
    }

    public String getAdresseMonument() {
        return adresseMonument;
    }

    public void setAdresseMonument(String adresseMonument) {
        this.adresseMonument = adresseMonument;
    }

    public Float getPrix() {
        return prix;
    }

    public void setPrix(Float prix) {
        this.prix = prix;
    }

    public Boolean getGratuit() {
        return gratuit;
    }

    public void setGratuit(Boolean gratuit) {
        this.gratuit = gratuit;
    }

    public String getHasCulturelle() {
        return hasCulturelle;
    }

    public void setHasCulturelle(String hasCulturelle) {
        this.hasCulturelle = hasCulturelle;
    }

    public String getHasHistorique() {
        return hasHistorique;
    }

    public void setHasHistorique(String hasHistorique) {
        this.hasHistorique = hasHistorique;
    }

    public Float getNotesMoyennes() {
        return notesMoyennes;
    }

    public void setNotesMoyennes(Float notesMoyennes) {
        this.notesMoyennes = notesMoyennes;
    }

    public List<String> getMediaUrls() {
        return mediaUrls;
    }

    public void setMediaUrls(List<String> mediaUrls) {
        this.mediaUrls = mediaUrls;
    }
}
