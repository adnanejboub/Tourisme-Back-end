package com.tourisme.tourisme.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "climat")
public class Climat {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_climat")
    private Long idClimat;
    
    @Column(name = "nom_climat", nullable = false)
    private String nomClimat;
    
    // Constructors
    public Climat() {}
    
    public Climat(String nomClimat) {
        this.nomClimat = nomClimat;
    }
    
    // Getters and Setters
    public Long getIdClimat() {
        return idClimat;
    }
    
    public void setIdClimat(Long idClimat) {
        this.idClimat = idClimat;
    }
    
    public String getNomClimat() {
        return nomClimat;
    }
    
    public void setNomClimat(String nomClimat) {
        this.nomClimat = nomClimat;
    }
} 