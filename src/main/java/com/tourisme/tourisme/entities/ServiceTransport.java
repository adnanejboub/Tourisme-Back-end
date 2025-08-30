package com.tourisme.tourisme.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "service_transport")
public class ServiceTransport {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_service_transport")
    private Long idServiceTransport;
    
    @Column(name = "type_service")
    @Enumerated(EnumType.STRING)
    private TypeServiceTransport typeService;
    
    @Column(name = "a_chauffeur")
    private Boolean aChauffeur;
    
    @Column(name = "a_guide")
    private Boolean aGuide;
    
    @Column(name = "prix_min")
    private Float prixMin;
    
    @Column(name = "prix_max")
    private Float prixMax;
    
    @Column(name = "nom")
    private String nom;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "disponible")
    private Boolean disponible;
    
    // Relationships
    @ManyToOne
    @JoinColumn(name = "id_agence_location")
    private AgenceLocation agenceLocation;
    
    @ManyToMany(mappedBy = "servicesTransport")
    private List<PlanningJournalier> planningsJournaliers;
    
    // Enum for transport service type
    public enum TypeServiceTransport {
        SEJOUR,
        JOURNALIER
    }
    
    // Constructors
    public ServiceTransport() {
        this.disponible = true;
    }
    
    public ServiceTransport(String nom, String description, TypeServiceTransport typeService) {
        this.nom = nom;
        this.description = description;
        this.typeService = typeService;
        this.disponible = true;
    }
    
    // Methods from UML
    public Float calculerPrix() {
        // Calculate price based on type and options
        Float prixBase = prixMin;
        if (aChauffeur) {
            prixBase += 50.0f; // Additional cost for driver
        }
        if (aGuide) {
            prixBase += 30.0f; // Additional cost for guide
        }
        return prixBase;
    }
    
    public Boolean estDisponible() {
        return disponible;
    }
    
    public void reserver() {
        // Mark as reserved/not available
        this.disponible = false;
    }
    
    public void liberer() {
        // Mark as available
        this.disponible = true;
    }
    
    // Getters and Setters
    public Long getIdServiceTransport() {
        return idServiceTransport;
    }
    
    public void setIdServiceTransport(Long idServiceTransport) {
        this.idServiceTransport = idServiceTransport;
    }
    
    public TypeServiceTransport getTypeService() {
        return typeService;
    }
    
    public void setTypeService(TypeServiceTransport typeService) {
        this.typeService = typeService;
    }
    
    public Boolean getAChauffeur() {
        return aChauffeur;
    }
    
    public void setAChauffeur(Boolean aChauffeur) {
        this.aChauffeur = aChauffeur;
    }
    
    public Boolean getAGuide() {
        return aGuide;
    }
    
    public void setAGuide(Boolean aGuide) {
        this.aGuide = aGuide;
    }
    
    public Float getPrixMin() {
        return prixMin;
    }
    
    public void setPrixMin(Float prixMin) {
        this.prixMin = prixMin;
    }
    
    public Float getPrixMax() {
        return prixMax;
    }
    
    public void setPrixMax(Float prixMax) {
        this.prixMax = prixMax;
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
    
    public Boolean getDisponible() {
        return disponible;
    }
    
    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }
    
    public AgenceLocation getAgenceLocation() {
        return agenceLocation;
    }
    
    public void setAgenceLocation(AgenceLocation agenceLocation) {
        this.agenceLocation = agenceLocation;
    }
    
    public List<PlanningJournalier> getPlanningsJournaliers() {
        return planningsJournaliers;
    }
    
    public void setPlanningsJournaliers(List<PlanningJournalier> planningsJournaliers) {
        this.planningsJournaliers = planningsJournaliers;
    }
} 