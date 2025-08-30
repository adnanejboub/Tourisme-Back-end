package com.tourisme.tourisme.entities;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "vol")
public class Vol {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vol")
    private Long idVol;
    
    @Column(name = "numero_vol", nullable = false)
    private String numeroVol;
    
    @Column(name = "heure_depart")
    @Temporal(TemporalType.TIME)
    private Date heureDepart;
    
    @Column(name = "heure_arrivee")
    @Temporal(TemporalType.TIME)
    private Date heureArrivee;
    
    @Column(name = "prix")
    private Float prix;
    
    @Column(name = "duree")
    private Integer duree; // in minutes
    
    @Column(name = "taux_reduction")
    private Float tauxReduction;
    
    @Column(name = "date_expiration_reduction")
    @Temporal(TemporalType.DATE)
    private Date dateExpirationReduction;
    
    @Column(name = "disponible")
    private Boolean disponible;
    
    @Column(name = "statut")
    @Enumerated(EnumType.STRING)
    private StatutVol statut;
    
    // Relationships
    @ManyToOne
    @JoinColumn(name = "id_aeroport_depart")
    private Aeroport aeroportDepart;
    
    @ManyToOne
    @JoinColumn(name = "id_aeroport_arrivee")
    private Aeroport aeroportArrivee;
    
    @ManyToOne
    @JoinColumn(name = "id_compagnie_aerienne")
    private CompagnieAerienne compagnieAerienne;
    
    @ManyToMany(mappedBy = "vols")
    private List<Sejour> sejours;
    
    // Enum for flight status
    public enum StatutVol {
        PLANIFIE,
        EN_VOL,
        ARRIVE,
        RETARDE,
        ANNULE
    }
    
    // Constructors
    public Vol() {
        this.disponible = true;
        this.statut = StatutVol.PLANIFIE;
    }
    
    public Vol(String numeroVol, Date heureDepart, Date heureArrivee, Float prix) {
        this.numeroVol = numeroVol;
        this.heureDepart = heureDepart;
        this.heureArrivee = heureArrivee;
        this.prix = prix;
        this.disponible = true;
        this.statut = StatutVol.PLANIFIE;
    }
    
    // Methods from UML
    public Float calculerPrixAvecReduction() {
        if (tauxReduction != null && tauxReduction > 0) {
            return prix * (1 - tauxReduction);
        }
        return prix;
    }
    
    public Boolean estDisponible() {
        return disponible && statut != StatutVol.ANNULE;
    }
    
    public void reserver() {
        this.disponible = false;
    }
    
    public void annuler() {
        this.statut = StatutVol.ANNULE;
    }
    
    public Integer calculerDuree() {
        if (heureDepart != null && heureArrivee != null) {
            long diffInMillis = heureArrivee.getTime() - heureDepart.getTime();
            return (int) (diffInMillis / (1000 * 60)); // Convert to minutes
        }
        return duree;
    }
    
    // Getters and Setters
    public Long getIdVol() {
        return idVol;
    }
    
    public void setIdVol(Long idVol) {
        this.idVol = idVol;
    }
    
    public String getNumeroVol() {
        return numeroVol;
    }
    
    public void setNumeroVol(String numeroVol) {
        this.numeroVol = numeroVol;
    }
    
    public Date getHeureDepart() {
        return heureDepart;
    }
    
    public void setHeureDepart(Date heureDepart) {
        this.heureDepart = heureDepart;
    }
    
    public Date getHeureArrivee() {
        return heureArrivee;
    }
    
    public void setHeureArrivee(Date heureArrivee) {
        this.heureArrivee = heureArrivee;
    }
    
    public Float getPrix() {
        return prix;
    }
    
    public void setPrix(Float prix) {
        this.prix = prix;
    }
    
    public Integer getDuree() {
        return duree;
    }
    
    public void setDuree(Integer duree) {
        this.duree = duree;
    }
    
    public Float getTauxReduction() {
        return tauxReduction;
    }
    
    public void setTauxReduction(Float tauxReduction) {
        this.tauxReduction = tauxReduction;
    }
    
    public Date getDateExpirationReduction() {
        return dateExpirationReduction;
    }
    
    public void setDateExpirationReduction(Date dateExpirationReduction) {
        this.dateExpirationReduction = dateExpirationReduction;
    }
    
    public Boolean getDisponible() {
        return disponible;
    }
    
    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }
    
    public StatutVol getStatut() {
        return statut;
    }
    
    public void setStatut(StatutVol statut) {
        this.statut = statut;
    }
    
    public Aeroport getAeroportDepart() {
        return aeroportDepart;
    }
    
    public void setAeroportDepart(Aeroport aeroportDepart) {
        this.aeroportDepart = aeroportDepart;
    }
    
    public Aeroport getAeroportArrivee() {
        return aeroportArrivee;
    }
    
    public void setAeroportArrivee(Aeroport aeroportArrivee) {
        this.aeroportArrivee = aeroportArrivee;
    }
    
    public CompagnieAerienne getCompagnieAerienne() {
        return compagnieAerienne;
    }
    
    public void setCompagnieAerienne(CompagnieAerienne compagnieAerienne) {
        this.compagnieAerienne = compagnieAerienne;
    }
    
    public List<Sejour> getSejours() {
        return sejours;
    }
    
    public void setSejours(List<Sejour> sejours) {
        this.sejours = sejours;
    }
} 