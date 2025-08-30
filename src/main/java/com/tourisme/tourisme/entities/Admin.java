package com.tourisme.tourisme.entities;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends Utilisateur {
    
    @Column(name = "cin", nullable = true, unique = true)
    private String cin;
    
    // Constructors
    public Admin() {}
    
    public Admin(String nom, String prenom, String email, String cin) {
        super(nom, prenom, email);
        this.cin = cin;
    }
    
    // Methods from UML
    public void gererUtilisateurs() {
        // Placeholder for user management
    }
    
    public void gererContenu() {
        // Placeholder for content management
    }
    
    public void gererBudgets() {
        // Placeholder for budget management
    }
    
    public void genererStatistiques() {
        // Placeholder for statistics generation
    }
    
    // Getters and Setters
    public Long getIdAdmin() {
        return getIdUtilisateur(); // Use parent's ID
    }
    
    public void setIdAdmin(Long idAdmin) {
        setIdUtilisateur(idAdmin); // Use parent's ID setter
    }
    
    public String getCin() {
        return cin;
    }
    
    public void setCin(String cin) {
        this.cin = cin;
    }
} 