package com.tourisme.tourisme.entities;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "touriste")
public class Touriste {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_touriste")
    private Long idTouriste;
    
    @Column(name = "nationalite")
    private String nationalite;
    
    @Column(name = "passeport")
    private String passeport;
    
    @Column(name = "date_entree")
    @Temporal(TemporalType.DATE)
    private Date dateEntree;
    
    @Column(name = "date_sortie")
    @Temporal(TemporalType.DATE)
    private Date dateSortie;
    
    @Column(name = "preferences")
    private String preferences;
    
    @Column(name = "niveau_langue")
    private String niveauLangue;
    
    @Column(name = "budget_max")
    private Float budgetMax;
    
    // Relationships
    @OneToOne
    @JoinColumn(name = "id_utilisateur")
    private Utilisateur utilisateur;
    
    @OneToMany(mappedBy = "touriste", cascade = CascadeType.ALL)
    private List<Sejour> sejours;
    
    @OneToMany(mappedBy = "touriste", cascade = CascadeType.ALL)
    private List<Avis> avis;
    
    // Constructors
    public Touriste() {}
    
    public Touriste(String nationalite, String passeport) {
        this.nationalite = nationalite;
        this.passeport = passeport;
    }
    
    // Methods from UML
    public void planifierSejour() {
        // Plan a new trip
    }
    
    public void evaluerExperience() {
        // Evaluate travel experience
    }
    
    public void partagerAvis() {
        // Share review/feedback
    }
    
    public Integer calculerDureeSejour() {
        if (dateEntree != null && dateSortie != null) {
            long diffInMillis = dateSortie.getTime() - dateEntree.getTime();
            return (int) (diffInMillis / (1000 * 60 * 60 * 24)); // Convert to days
        }
        return 0;
    }
    
    // Getters and Setters
    public Long getIdTouriste() {
        return idTouriste;
    }
    
    public void setIdTouriste(Long idTouriste) {
        this.idTouriste = idTouriste;
    }
    
    public String getNationalite() {
        return nationalite;
    }
    
    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }
    
    public String getPasseport() {
        return passeport;
    }
    
    public void setPasseport(String passeport) {
        this.passeport = passeport;
    }
    
    public Date getDateEntree() {
        return dateEntree;
    }
    
    public void setDateEntree(Date dateEntree) {
        this.dateEntree = dateEntree;
    }
    
    public Date getDateSortie() {
        return dateSortie;
    }
    
    public void setDateSortie(Date dateSortie) {
        this.dateSortie = dateSortie;
    }
    
    public String getPreferences() {
        return preferences;
    }
    
    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }
    
    public String getNiveauLangue() {
        return niveauLangue;
    }
    
    public void setNiveauLangue(String niveauLangue) {
        this.niveauLangue = niveauLangue;
    }
    
    public Float getBudgetMax() {
        return budgetMax;
    }
    
    public void setBudgetMax(Float budgetMax) {
        this.budgetMax = budgetMax;
    }
    
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }
    
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
    
    public List<Sejour> getSejours() {
        return sejours;
    }
    
    public void setSejours(List<Sejour> sejours) {
        this.sejours = sejours;
    }
    
    public List<Avis> getAvis() {
        return avis;
    }
    
    public void setAvis(List<Avis> avis) {
        this.avis = avis;
    }
} 