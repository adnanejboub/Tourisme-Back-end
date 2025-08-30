package com.tourisme.tourisme.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "pays")
public class Pays {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pays")
    private Long idPays;
    
    @Column(name = "nom_pays", nullable = false)
    private String nomPays;
    
    // Constructors
    public Pays() {}
    
    public Pays(String nomPays) {
        this.nomPays = nomPays;
    }
    
    // Getters and Setters
    public Long getIdPays() {
        return idPays;
    }
    
    public void setIdPays(Long idPays) {
        this.idPays = idPays;
    }
    
    public String getNomPays() {
        return nomPays;
    }
    
    public void setNomPays(String nomPays) {
        this.nomPays = nomPays;
    }
} 