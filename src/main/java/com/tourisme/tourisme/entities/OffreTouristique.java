package com.tourisme.tourisme.entities;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "offre_touristique")
public class OffreTouristique {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_offre")
    private Long idOffre;
    
    @Column(name = "nom_offre", nullable = false)
    private String nomOffre;
    
    @Column(name = "description_offre")
    private String descriptionOffre;
    
    @Column(name = "prix_offre")
    private Float prixOffre;
    
    @Column(name = "disponible")
    private Boolean disponible;
    
    @Column(name = "date_creation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    
    @Column(name = "date_validation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateValidation;
    
    @Column(name = "conditions_annulation")
    private String conditionsAnnulation;
    
    @Column(name = "horaire_ouverture")
    private String horaireOuverture;
    
    @Column(name = "horaire_fermeture")
    private String horaireFermeture;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "statut")
    @Enumerated(EnumType.STRING)
    private StatutOffre statut;
    
    // Relationships
    @ManyToOne
    @JoinColumn(name = "id_ville")
    private Ville ville;
    
    @ManyToMany
    @JoinTable(
        name = "offre_service",
        joinColumns = @JoinColumn(name = "id_offre"),
        inverseJoinColumns = @JoinColumn(name = "id_service")
    )
    private List<Service> services;
    
    @ManyToMany
    @JoinTable(
        name = "offre_produit",
        joinColumns = @JoinColumn(name = "id_offre"),
        inverseJoinColumns = @JoinColumn(name = "id_produit")
    )
    private List<Produit> produits;
    
    @ManyToOne
    @JoinColumn(name = "id_partenaire")
    private Partenaire partenaire;
    
    // Enum for offer status
    public enum StatutOffre {
        BROUILLON,
        EN_ATTENTE,
        VALIDEE,
        REJETEE,
        EXPIRED
    }
    
    // Constructors
    public OffreTouristique() {
        this.dateCreation = new Date();
        this.disponible = true;
        this.statut = StatutOffre.BROUILLON;
    }
    
    public OffreTouristique(String nomOffre, String descriptionOffre, Float prixOffre) {
        this.nomOffre = nomOffre;
        this.descriptionOffre = descriptionOffre;
        this.prixOffre = prixOffre;
        this.dateCreation = new Date();
        this.disponible = true;
        this.statut = StatutOffre.BROUILLON;
    }
    
    // Methods from UML
    public void validerOffre() {
        this.statut = StatutOffre.VALIDEE;
        this.dateValidation = new Date();
    }
    
    public void rejeterOffre() {
        this.statut = StatutOffre.REJETEE;
    }
    
    public Float calculerPrix() {
        // Calculate total price including services and products
        Float totalPrix = this.prixOffre;
        if (services != null) {
            for (Service service : services) {
                totalPrix += service.getPrixAnnulation();
            }
        }
        return totalPrix;
    }
    
    // Getters and Setters
    public Long getIdOffre() {
        return idOffre;
    }
    
    public void setIdOffre(Long idOffre) {
        this.idOffre = idOffre;
    }
    
    public String getNomOffre() {
        return nomOffre;
    }
    
    public void setNomOffre(String nomOffre) {
        this.nomOffre = nomOffre;
    }
    
    public String getDescriptionOffre() {
        return descriptionOffre;
    }
    
    public void setDescriptionOffre(String descriptionOffre) {
        this.descriptionOffre = descriptionOffre;
    }
    
    public Float getPrixOffre() {
        return prixOffre;
    }
    
    public void setPrixOffre(Float prixOffre) {
        this.prixOffre = prixOffre;
    }
    
    public Boolean getDisponible() {
        return disponible;
    }
    
    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }
    
    public Date getDateCreation() {
        return dateCreation;
    }
    
    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }
    
    public Date getDateValidation() {
        return dateValidation;
    }
    
    public void setDateValidation(Date dateValidation) {
        this.dateValidation = dateValidation;
    }
    
    public String getConditionsAnnulation() {
        return conditionsAnnulation;
    }
    
    public void setConditionsAnnulation(String conditionsAnnulation) {
        this.conditionsAnnulation = conditionsAnnulation;
    }
    
    public String getHoraireOuverture() {
        return horaireOuverture;
    }
    
    public void setHoraireOuverture(String horaireOuverture) {
        this.horaireOuverture = horaireOuverture;
    }
    
    public String getHoraireFermeture() {
        return horaireFermeture;
    }
    
    public void setHoraireFermeture(String horaireFermeture) {
        this.horaireFermeture = horaireFermeture;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public StatutOffre getStatut() {
        return statut;
    }
    
    public void setStatut(StatutOffre statut) {
        this.statut = statut;
    }
    
    public Ville getVille() {
        return ville;
    }
    
    public void setVille(Ville ville) {
        this.ville = ville;
    }
    
    public List<Service> getServices() {
        return services;
    }
    
    public void setServices(List<Service> services) {
        this.services = services;
    }
    
    public List<Produit> getProduits() {
        return produits;
    }
    
    public void setProduits(List<Produit> produits) {
        this.produits = produits;
    }
    
    public Partenaire getPartenaire() {
        return partenaire;
    }
    
    public void setPartenaire(Partenaire partenaire) {
        this.partenaire = partenaire;
    }
} 