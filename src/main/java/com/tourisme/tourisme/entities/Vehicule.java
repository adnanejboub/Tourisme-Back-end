package com.tourisme.tourisme.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "vehicule")
public class Vehicule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehicule")
    private Long idVehicule;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type_vehicule")
    private TypeVehicule typeVehicule;
    
    @Column(name = "marque")
    private String marque;
    
    @Column(name = "modele")
    private String modele;
    
    @Column(name = "nombre_places")
    private Integer nombrePlaces;
    
    @Column(name = "prix_par_jour")
    private Float prixParJour;
    
    // Relationships
    @ManyToOne
    @JoinColumn(name = "id_agence_location")
    private AgenceLocation agenceLocation;
    
    @ManyToMany
    @JoinTable(
        name = "vehicule_media",
        joinColumns = @JoinColumn(name = "id_vehicule"),
        inverseJoinColumns = @JoinColumn(name = "id_media")
    )
    private List<Media> medias;
    
    // Enum for type_vehicule
    public enum TypeVehicule {
        VOITURE,
        MOTO,
        TROTINETTE_ELECTRIQUE,
        VELO_ELECTRIQUE,
        VELO
    }
    
    // Constructors
    public Vehicule() {}
    
    public Vehicule(TypeVehicule typeVehicule, String marque, String modele, Integer nombrePlaces, Float prixParJour) {
        this.typeVehicule = typeVehicule;
        this.marque = marque;
        this.modele = modele;
        this.nombrePlaces = nombrePlaces;
        this.prixParJour = prixParJour;
    }
    
    // Getters and Setters
    public Long getIdVehicule() {
        return idVehicule;
    }
    
    public void setIdVehicule(Long idVehicule) {
        this.idVehicule = idVehicule;
    }
    
    public TypeVehicule getTypeVehicule() {
        return typeVehicule;
    }
    
    public void setTypeVehicule(TypeVehicule typeVehicule) {
        this.typeVehicule = typeVehicule;
    }
    
    public String getMarque() {
        return marque;
    }
    
    public void setMarque(String marque) {
        this.marque = marque;
    }
    
    public String getModele() {
        return modele;
    }
    
    public void setModele(String modele) {
        this.modele = modele;
    }
    
    public Integer getNombrePlaces() {
        return nombrePlaces;
    }
    
    public void setNombrePlaces(Integer nombrePlaces) {
        this.nombrePlaces = nombrePlaces;
    }
    
    public Float getPrixParJour() {
        return prixParJour;
    }
    
    public void setPrixParJour(Float prixParJour) {
        this.prixParJour = prixParJour;
    }
    
    public AgenceLocation getAgenceLocation() {
        return agenceLocation;
    }
    
    public void setAgenceLocation(AgenceLocation agenceLocation) {
        this.agenceLocation = agenceLocation;
    }
    
    public List<Media> getMedias() {
        return medias;
    }
    
    public void setMedias(List<Media> medias) {
        this.medias = medias;
    }
} 