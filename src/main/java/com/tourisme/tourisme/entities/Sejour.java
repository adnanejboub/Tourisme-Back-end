package com.tourisme.tourisme.entities;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "sejour")
public class Sejour {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sejour")
    private Long idSejour;
    
    @Column(name = "nom_sejour", nullable = false)
    private String nomSejour;
    
    @Column(name = "date_debut")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    
    @Column(name = "date_fin")
    @Temporal(TemporalType.DATE)
    private Date dateFin;
    
    @Column(name = "budget_total")
    private Float budgetTotal;
    
    @Column(name = "nombre_personnes")
    private Integer nombrePersonnes;
    
    // Relationships
    @ManyToOne
    @JoinColumn(name = "id_touriste")
    private Touriste touriste;
    
    @OneToOne(mappedBy = "sejour", cascade = CascadeType.ALL)
    private Reservation reservation;
    
    @ManyToMany
    @JoinTable(
        name = "sejour_hebergement",
        joinColumns = @JoinColumn(name = "id_sejour"),
        inverseJoinColumns = @JoinColumn(name = "id_hebergement")
    )
    private List<Hebergement> hebergements;
    
    @ManyToMany
    @JoinTable(
        name = "sejour_ville",
        joinColumns = @JoinColumn(name = "id_sejour"),
        inverseJoinColumns = @JoinColumn(name = "id_ville")
    )
    private List<Ville> villes;
    
    @OneToMany(mappedBy = "sejour", cascade = CascadeType.ALL)
    private List<PlanningJournalier> planningsJournaliers;
    
    @ManyToMany
    @JoinTable(
        name = "sejour_vol",
        joinColumns = @JoinColumn(name = "id_sejour"),
        inverseJoinColumns = @JoinColumn(name = "id_vol")
    )
    private List<Vol> vols;
    
    // Constructors
    public Sejour() {}
    
    public Sejour(String nomSejour, Date dateDebut, Date dateFin, Integer nombrePersonnes) {
        this.nomSejour = nomSejour;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.nombrePersonnes = nombrePersonnes;
    }
    
    // Methods from UML
    public Float calculerBudget() {
        // Calculate total budget for the stay
        Float totalBudget = 0.0f;
        if (hebergements != null) {
            for (Hebergement hebergement : hebergements) {
                totalBudget += hebergement.getPrixParNuit();
            }
        }
        return totalBudget;
    }
    
    public void optimiserItineraire() {
        // Optimize the travel itinerary
    }
    
    public void genererPlanning() {
        // Generate daily planning
    }
    
    // Getters and Setters
    public Long getIdSejour() {
        return idSejour;
    }
    
    public void setIdSejour(Long idSejour) {
        this.idSejour = idSejour;
    }
    
    public String getNomSejour() {
        return nomSejour;
    }
    
    public void setNomSejour(String nomSejour) {
        this.nomSejour = nomSejour;
    }
    
    public Date getDateDebut() {
        return dateDebut;
    }
    
    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }
    
    public Date getDateFin() {
        return dateFin;
    }
    
    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }
    
    public Float getBudgetTotal() {
        return budgetTotal;
    }
    
    public void setBudgetTotal(Float budgetTotal) {
        this.budgetTotal = budgetTotal;
    }
    
    public Integer getNombrePersonnes() {
        return nombrePersonnes;
    }
    
    public void setNombrePersonnes(Integer nombrePersonnes) {
        this.nombrePersonnes = nombrePersonnes;
    }
    
    public Touriste getTouriste() {
        return touriste;
    }
    
    public void setTouriste(Touriste touriste) {
        this.touriste = touriste;
    }
    
    public Reservation getReservation() {
        return reservation;
    }
    
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
    
    public List<Hebergement> getHebergements() {
        return hebergements;
    }
    
    public void setHebergements(List<Hebergement> hebergements) {
        this.hebergements = hebergements;
    }
    
    public List<Ville> getVilles() {
        return villes;
    }
    
    public void setVilles(List<Ville> villes) {
        this.villes = villes;
    }
    
    public List<PlanningJournalier> getPlanningsJournaliers() {
        return planningsJournaliers;
    }
    
    public void setPlanningsJournaliers(List<PlanningJournalier> planningsJournaliers) {
        this.planningsJournaliers = planningsJournaliers;
    }
    
    public List<Vol> getVols() {
        return vols;
    }
    
    public void setVols(List<Vol> vols) {
        this.vols = vols;
    }
} 