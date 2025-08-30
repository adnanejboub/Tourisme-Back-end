package com.tourisme.tourisme.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "aeroport")
public class Aeroport {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aeroport")
    private Long idAeroport;
    
    @Column(name = "nom_aeroport", nullable = false)
    private String nomAeroport;
    
    @Column(name = "localisation")
    private String localisation;
    
    // Relationships
    @ManyToOne
    @JoinColumn(name = "id_ville")
    private Ville ville;
    
    // Constructors
    public Aeroport() {}
    
    public Aeroport(String nomAeroport, String localisation, Ville ville) {
        this.nomAeroport = nomAeroport;
        this.localisation = localisation;
        this.ville = ville;
    }
    
    // Getters and Setters
    public Long getIdAeroport() {
        return idAeroport;
    }
    
    public void setIdAeroport(Long idAeroport) {
        this.idAeroport = idAeroport;
    }
    
    public String getNomAeroport() {
        return nomAeroport;
    }
    
    public void setNomAeroport(String nomAeroport) {
        this.nomAeroport = nomAeroport;
    }
    
    public String getLocalisation() {
        return localisation;
    }
    
    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }
    
    public Ville getVille() {
        return ville;
    }
    
    public void setVille(Ville ville) {
        this.ville = ville;
    }
} 