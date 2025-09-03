package com.tourisme.tourisme.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "specialite_ville")
public class SpecialiteVille {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_specialite_ville")
    private Long idSpecialiteVille;
    
    @Column(name = "nom_specialite", nullable = false)
    private String nomSpecialite;
    
    @Column(name = "description")
    private String description;
    
    // Relationship with Ville
    @ManyToOne
    @JoinColumn(name = "id_ville")
    @JsonBackReference("ville-specialites")
    private Ville ville;
    
    // Constructors
    public SpecialiteVille() {}
    
    public SpecialiteVille(String nomSpecialite, String description) {
        this.nomSpecialite = nomSpecialite;
        this.description = description;
    }
    
    // Getters and Setters
    public Long getIdSpecialiteVille() {
        return idSpecialiteVille;
    }
    
    public void setIdSpecialiteVille(Long idSpecialiteVille) {
        this.idSpecialiteVille = idSpecialiteVille;
    }
    
    public String getNomSpecialite() {
        return nomSpecialite;
    }
    
    public void setNomSpecialite(String nomSpecialite) {
        this.nomSpecialite = nomSpecialite;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Ville getVille() {
        return ville;
    }
    
    public void setVille(Ville ville) {
        this.ville = ville;
    }
} 