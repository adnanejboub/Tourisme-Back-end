package com.tourisme.tourisme.entities;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@DiscriminatorValue("MARQUE")
public class ProduitMarque extends Produit {
    
    @Column(name = "marque")
    private String marque;
    
    @Column(name = "modele")
    private String modele;
    
    @Column(name = "description_marque")
    private String descriptionMarque;
    
    @Column(name = "date_expiration_marque")
    @Temporal(TemporalType.DATE)
    private Date dateExpirationMarque;
    
    @Column(name = "taille_disponible")
    private String tailleDisponible;
    
    @Column(name = "couleur")
    private String couleur;
    
    @Column(name = "garantie")
    private String garantie;
    
    @Column(name = "certification_qualite")
    private Boolean certificationQualite;
    
    // Constructors
    public ProduitMarque() {
        super();
    }
    
    public ProduitMarque(String nom, String description, Float prix, Integer stockDisponible, 
                        String marque, String modele) {
        super(nom, description, prix, stockDisponible);
        this.marque = marque;
        this.modele = modele;
    }
    
    // Methods from UML
    public Boolean verifierGarantie() {
        if (dateExpirationMarque != null) {
            return new Date().before(dateExpirationMarque);
        }
        return false;
    }
    
    public String obtenirMarque() {
        return marque;
    }
    
    public String obtenirModele() {
        return modele;
    }
    
    // Getters and Setters
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
    
    public String getDescriptionMarque() {
        return descriptionMarque;
    }
    
    public void setDescriptionMarque(String descriptionMarque) {
        this.descriptionMarque = descriptionMarque;
    }
    
    public Date getDateExpirationMarque() {
        return dateExpirationMarque;
    }
    
    public void setDateExpirationMarque(Date dateExpirationMarque) {
        this.dateExpirationMarque = dateExpirationMarque;
    }
    
    public String getTailleDisponible() {
        return tailleDisponible;
    }
    
    public void setTailleDisponible(String tailleDisponible) {
        this.tailleDisponible = tailleDisponible;
    }
    
    public String getCouleur() {
        return couleur;
    }
    
    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }
    
    public String getGarantie() {
        return garantie;
    }
    
    public void setGarantie(String garantie) {
        this.garantie = garantie;
    }
    
    public Boolean getCertificationQualite() {
        return certificationQualite;
    }
    
    public void setCertificationQualite(Boolean certificationQualite) {
        this.certificationQualite = certificationQualite;
    }
} 