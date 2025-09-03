package com.tourisme.tourisme.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue("AGENCE_LOCATION")
public class AgenceLocation extends Utilisateur {
    
    // Les colonnes sont maintenant définies dans la classe parent Utilisateur
    // Cette classe hérite de ces propriétés
    
    // Relationships
    @ManyToOne
    @JoinColumn(name = "id_ville")
    @JsonBackReference("ville-agencesLocation")
    private Ville ville;
    
    @OneToMany(mappedBy = "agenceLocation", cascade = CascadeType.ALL)
    private List<Vehicule> vehicules;
    
    // Constructors
    public AgenceLocation() {
        super();
    }
    
    public AgenceLocation(String nom, String prenom, String email, String nomAgence) {
        super(nom, prenom, email);
        setNomAgence(nomAgence);
        setCertifie(false);
    }
    
    // Getters and Setters
    public Long getIdAgence() {
        return getIdUtilisateur(); // Use parent's ID
    }
    
    public void setIdAgence(Long idAgence) {
        setIdUtilisateur(idAgence); // Use parent's ID setter
    }
    
    public String getNomAgence() {
        return super.getNomAgence();
    }
    
    public void setNomAgence(String nomAgence) {
        super.setNomAgence(nomAgence);
    }
    
    public String getAdresseAgence() {
        return super.getAdresseAgence();
    }
    
    public void setAdresseAgence(String adresseAgence) {
        super.setAdresseAgence(adresseAgence);
    }
    
    public String getEmailAgence() {
        return super.getEmailAgence();
    }
    
    public void setEmailAgence(String emailAgence) {
        super.setEmailAgence(emailAgence);
    }
    
    public String getTelephoneAgence() {
        return super.getTelephoneAgence();
    }
    
    public void setTelephoneAgence(String telephoneAgence) {
        super.setTelephoneAgence(telephoneAgence);
    }
    
    public Boolean getCertifie() {
        return super.getCertifie();
    }
    
    public void setCertifie(Boolean certifie) {
        super.setCertifie(certifie);
    }
    
    public Ville getVille() {
        return ville;
    }
    
    public void setVille(Ville ville) {
        this.ville = ville;
    }
    
    public List<Vehicule> getVehicules() {
        return vehicules;
    }
    
    public void setVehicules(List<Vehicule> vehicules) {
        this.vehicules = vehicules;
    }
} 