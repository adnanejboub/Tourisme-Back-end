package com.tourisme.tourisme.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "hebergement")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "hebergement_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("HEBERGEMENT")
public class Hebergement {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_hebergement")
    private Long idHebergement;
    
    @Column(name = "nom_hebergement", nullable = false)
    private String nomHebergement;
    
    @Column(name = "adresse")
    private String adresse;
    
    @Column(name = "prix_par_nuit")
    private Float prixParNuit;
    
    @Column(name = "etoiles")
    private Integer etoiles;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "is_disponible")
    private Boolean isDisponible;
    
    // Relationships
    @ManyToOne
    @JoinColumn(name = "id_ville")
    private Ville ville;
    
    // Constructors
    public Hebergement() {}
    
    public Hebergement(String nomHebergement, String adresse, Float prixParNuit, Integer etoiles) {
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
    
    public Ville getVille() {
        return ville;
    }
    
    public void setVille(Ville ville) {
        this.ville = ville;
    }
} 