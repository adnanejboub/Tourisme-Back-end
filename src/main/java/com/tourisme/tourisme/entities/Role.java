package com.tourisme.tourisme.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "role")
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_role")
    private Integer idRole;
    
    @Column(name = "nom_role", nullable = false)
    private String nomRole;
    
    @Column(name = "description")
    private String description;
    
    // Constructors
    public Role() {}
    
    public Role(String nomRole, String description) {
        this.nomRole = nomRole;
        this.description = description;
    }
    
    // Getters and Setters
    public Integer getIdRole() {
        return idRole;
    }
    
    public void setIdRole(Integer idRole) {
        this.idRole = idRole;
    }
    
    public String getNomRole() {
        return nomRole;
    }
    
    public void setNomRole(String nomRole) {
        this.nomRole = nomRole;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
} 