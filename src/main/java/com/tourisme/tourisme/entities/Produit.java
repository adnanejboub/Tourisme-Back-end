package com.tourisme.tourisme.entities;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "produit")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "produit_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("PRODUIT")
public class Produit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produit")
    private Long idProduit;
    
    @Column(name = "nom", nullable = false)
    private String nom;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "prix")
    private Float prix;
    
    @Column(name = "origine")
    private String origine;
    
    @Column(name = "authentique")
    private Boolean authentique;
    
    @Column(name = "date_fabrication")
    @Temporal(TemporalType.DATE)
    private Date dateFabrication;
    
    @Column(name = "taille")
    private String taille;
    
    @Column(name = "stock_disponible")
    private Integer stockDisponible;
    
    @Column(name = "poids")
    private Float poids;
    
    @Column(name = "prix_unitaire")
    private Float prixUnitaire;
    
    @Column(name = "date_expiration")
    @Temporal(TemporalType.DATE)
    private Date dateExpiration;
    
    @Column(name = "exclusif")
    private Boolean exclusif;
    
    @Column(name = "image")
    private String image;
    
    @Column(name = "date_creation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    
    @Column(name = "is_disponible")
    private Boolean isDisponible;
    
    // Relationships
    @ManyToOne
    @JoinColumn(name = "id_fournisseur")
    private Fournisseur fournisseur;
    
    @ManyToMany
    @JoinTable(
        name = "produit_media",
        joinColumns = @JoinColumn(name = "id_produit"),
        inverseJoinColumns = @JoinColumn(name = "id_media")
    )
    private List<Media> medias;
    
    // Constructors
    public Produit() {}
    
    public Produit(String nom, String description, Float prix, Integer stockDisponible) {
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.stockDisponible = stockDisponible;
        this.dateCreation = new Date();
        this.isDisponible = true;
    }
    
    // Getters and Setters
    public Long getIdProduit() {
        return idProduit;
    }
    
    public void setIdProduit(Long idProduit) {
        this.idProduit = idProduit;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Float getPrix() {
        return prix;
    }
    
    public void setPrix(Float prix) {
        this.prix = prix;
    }
    
    public String getImage() {
        return image;
    }
    
    public void setImage(String image) {
        this.image = image;
    }
    
    public Integer getStockDisponible() {
        return stockDisponible;
    }
    
    public void setStockDisponible(Integer stockDisponible) {
        this.stockDisponible = stockDisponible;
    }
    
    public String getOrigine() {
        return origine;
    }
    
    public void setOrigine(String origine) {
        this.origine = origine;
    }
    
    public Boolean getAuthentique() {
        return authentique;
    }
    
    public void setAuthentique(Boolean authentique) {
        this.authentique = authentique;
    }
    
    public Date getDateFabrication() {
        return dateFabrication;
    }
    
    public void setDateFabrication(Date dateFabrication) {
        this.dateFabrication = dateFabrication;
    }
    
    public String getTaille() {
        return taille;
    }
    
    public void setTaille(String taille) {
        this.taille = taille;
    }
    
    public Float getPoids() {
        return poids;
    }
    
    public void setPoids(Float poids) {
        this.poids = poids;
    }
    
    public Float getPrixUnitaire() {
        return prixUnitaire;
    }
    
    public void setPrixUnitaire(Float prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }
    
    public Boolean getExclusif() {
        return exclusif;
    }
    
    public void setExclusif(Boolean exclusif) {
        this.exclusif = exclusif;
    }
    
    public Date getDateCreation() {
        return dateCreation;
    }
    
    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }
    
    public Date getDateExpiration() {
        return dateExpiration;
    }
    
    public void setDateExpiration(Date dateExpiration) {
        this.dateExpiration = dateExpiration;
    }
    
    public Boolean getIsDisponible() {
        return isDisponible;
    }
    
    public void setIsDisponible(Boolean isDisponible) {
        this.isDisponible = isDisponible;
    }
    
    public Fournisseur getFournisseur() {
        return fournisseur;
    }
    
    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }
    
    public List<Media> getMedias() {
        return medias;
    }
    
    public void setMedias(List<Media> medias) {
        this.medias = medias;
    }
} 