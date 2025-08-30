package com.tourisme.tourisme.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "monument")
public class Monument {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_monument")
    private Long idMonument;
    
    @Column(name = "nom_monument", nullable = false)
    private String nomMonument;
    
    @Column(name = "adresse_monument")
    private String adresseMonument;
    
    @Column(name = "prix")
    private Float prix;
    
    @Column(name = "gratuit")
    private Boolean gratuit;
    
    @Column(name = "has_culturelle")
    private String hasCulturelle;
    
    @Column(name = "has_historique")
    private String hasHistorique;
    
    @Column(name = "notes_moyennes")
    private Float notesMoyennes;
    
    // Relationships
    @ManyToOne
    @JoinColumn(name = "id_ville")
    private Ville ville;
    
    @ManyToMany
    @JoinTable(
        name = "monument_media",
        joinColumns = @JoinColumn(name = "id_monument"),
        inverseJoinColumns = @JoinColumn(name = "id_media")
    )
    private List<Media> medias;
    
    // Constructors
    public Monument() {}
    
    public Monument(String nomMonument, String adresseMonument, Float prix, Boolean gratuit) {
        this.nomMonument = nomMonument;
        this.adresseMonument = adresseMonument;
        this.prix = prix;
        this.gratuit = gratuit;
    }
    
    // Methods from UML
    public Float calculTarif() {
        return this.gratuit ? 0.0f : this.prix;
    }
    
    public String obtenirHoraires() {
        // Placeholder for getting opening hours
        return "9:00 - 18:00";
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
    
    public Ville getVille() {
        return ville;
    }
    
    public void setVille(Ville ville) {
        this.ville = ville;
    }
    
    public List<Media> getMedias() {
        return medias;
    }
    
    public void setMedias(List<Media> medias) {
        this.medias = medias;
    }
} 