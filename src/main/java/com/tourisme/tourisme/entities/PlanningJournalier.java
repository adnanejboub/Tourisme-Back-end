package com.tourisme.tourisme.entities;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "planning_journalier")
public class PlanningJournalier {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_planning")
    private Long idPlanning;
    
    @Column(name = "date_planning")
    @Temporal(TemporalType.DATE)
    private Date datePlanning;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "duree")
    private Integer duree; // in minutes
    
    @Column(name = "statut")
    @Enumerated(EnumType.STRING)
    private StatutPlanning statut;
    
    // Relationships
    @ManyToOne
    @JoinColumn(name = "id_sejour")
    private Sejour sejour;
    
    @ManyToMany
    @JoinTable(
        name = "planning_activite",
        joinColumns = @JoinColumn(name = "id_planning"),
        inverseJoinColumns = @JoinColumn(name = "id_activite")
    )
    private List<Activite> activites;
    
    @ManyToMany
    @JoinTable(
        name = "planning_service_transport",
        joinColumns = @JoinColumn(name = "id_planning"),
        inverseJoinColumns = @JoinColumn(name = "id_service_transport")
    )
    private List<ServiceTransport> servicesTransport;
    
    // Enum for planning status
    public enum StatutPlanning {
        BROUILLON,
        CONFIRME,
        EN_COURS,
        TERMINE,
        ANNULE
    }
    
    // Constructors
    public PlanningJournalier() {
        this.statut = StatutPlanning.BROUILLON;
    }
    
    public PlanningJournalier(Date datePlanning, String description) {
        this.datePlanning = datePlanning;
        this.description = description;
        this.statut = StatutPlanning.BROUILLON;
    }
    
    // Methods from UML
    public void ajouterActivite(Activite activite) {
        if (activites != null) {
            activites.add(activite);
        }
    }
    
    public void modifierPlanning(String nouvelleDescription) {
        this.description = nouvelleDescription;
    }
    
    public void confirmerPlanning() {
        this.statut = StatutPlanning.CONFIRME;
    }
    
    public Integer calculerDureeTotale() {
        Integer dureeTotale = 0;
        if (activites != null) {
            for (Activite activite : activites) {
                dureeTotale += activite.getDureeMinimun();
            }
        }
        return dureeTotale;
    }
    
    // Getters and Setters
    public Long getIdPlanning() {
        return idPlanning;
    }
    
    public void setIdPlanning(Long idPlanning) {
        this.idPlanning = idPlanning;
    }
    
    public Date getDatePlanning() {
        return datePlanning;
    }
    
    public void setDatePlanning(Date datePlanning) {
        this.datePlanning = datePlanning;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getDuree() {
        return duree;
    }
    
    public void setDuree(Integer duree) {
        this.duree = duree;
    }
    
    public StatutPlanning getStatut() {
        return statut;
    }
    
    public void setStatut(StatutPlanning statut) {
        this.statut = statut;
    }
    
    public Sejour getSejour() {
        return sejour;
    }
    
    public void setSejour(Sejour sejour) {
        this.sejour = sejour;
    }
    
    public List<Activite> getActivites() {
        return activites;
    }
    
    public void setActivites(List<Activite> activites) {
        this.activites = activites;
    }
    
    public List<ServiceTransport> getServicesTransport() {
        return servicesTransport;
    }
    
    public void setServicesTransport(List<ServiceTransport> servicesTransport) {
        this.servicesTransport = servicesTransport;
    }
} 