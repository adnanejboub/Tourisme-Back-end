package com.tourisme.tourisme.entities;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@DiscriminatorValue("FOURNISSEUR")
public class Fournisseur extends Utilisateur {
    
    // Les colonnes sont maintenant définies dans la classe parent Utilisateur
    // Cette classe hérite de ces propriétés
    
    // Relationships
    @OneToMany(mappedBy = "fournisseur", cascade = CascadeType.ALL)
    private List<Produit> produits;
    
    @OneToMany(mappedBy = "fournisseur", cascade = CascadeType.ALL)
    private List<Service> services;
    
    // Constructors
    public Fournisseur() {
        super();
    }
    
    public Fournisseur(String nom, String prenom, String email, String nomFournisseur) {
        super(nom, prenom, email);
        setNomFournisseur(nomFournisseur);
        setDatePartenariat(new Date());
    }
    
    // Getters and Setters
    public Long getIdFournisseur() {
        return getIdUtilisateur(); // Use parent's ID
    }
    
    public void setIdFournisseur(Long idFournisseur) {
        setIdUtilisateur(idFournisseur); // Use parent's ID setter
    }
    
    public String getNomFournisseur() {
        return super.getNomFournisseur();
    }
    
    public void setNomFournisseur(String nomFournisseur) {
        super.setNomFournisseur(nomFournisseur);
    }
    
    public String getAdresseFournisseur() {
        return super.getAdresseFournisseur();
    }
    
    public void setAdresseFournisseur(String adresseFournisseur) {
        super.setAdresseFournisseur(adresseFournisseur);
    }
    
    public String getTelephoneFournisseur() {
        return super.getTelephoneFournisseur();
    }
    
    public void setTelephoneFournisseur(String telephoneFournisseur) {
        super.setTelephoneFournisseur(telephoneFournisseur);
    }
    
    public String getSiteWeb() {
        return super.getSiteWeb();
    }
    
    public void setSiteWeb(String siteWeb) {
        super.setSiteWeb(siteWeb);
    }
    
    public String getEmailFournisseur() {
        return super.getEmailFournisseur();
    }
    
    public void setEmailFournisseur(String emailFournisseur) {
        super.setEmailFournisseur(emailFournisseur);
    }
    
    public Date getDatePartenariat() {
        return super.getDatePartenariat();
    }
    
    public void setDatePartenariat(Date datePartenariat) {
        super.setDatePartenariat(datePartenariat);
    }
    
    public Float getCommission() {
        return super.getCommission();
    }
    
    public void setCommission(Float commission) {
        super.setCommission(commission);
    }
    
    public List<Produit> getProduits() {
        return produits;
    }
    
    public void setProduits(List<Produit> produits) {
        this.produits = produits;
    }
    
    public List<Service> getServices() {
        return services;
    }
    
    public void setServices(List<Service> services) {
        this.services = services;
    }
} 