package com.tourisme.tourisme.entities;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reservation")
public class Reservation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reservation")
    private Long idReservation;
    
    @Column(name = "date_reservation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateReservation;
    
    @Column(name = "date_debut")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    
    @Column(name = "date_fin")
    @Temporal(TemporalType.DATE)
    private Date dateFin;
    
    @Column(name = "nombre_personnes")
    private Integer nombrePersonnes;
    
    @Column(name = "prix_total")
    private Float prixTotal;
    
    @Column(name = "statut")
    @Enumerated(EnumType.STRING)
    private StatutReservation statut;
    
    // Relationships
    @OneToOne
    @JoinColumn(name = "id_sejour")
    private Sejour sejour;
    
    @ManyToOne
    @JoinColumn(name = "id_utilisateur")
    private Utilisateur utilisateur;
    
    // Enum for reservation status
    public enum StatutReservation {
        EN_ATTENTE,
        CONFIRMEE,
        ANNULEE,
        TERMINEE
    }
    
    // Constructors
    public Reservation() {
        this.dateReservation = new Date();
        this.statut = StatutReservation.EN_ATTENTE;
    }
    
    public Reservation(Date dateDebut, Date dateFin, Integer nombrePersonnes) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.nombrePersonnes = nombrePersonnes;
        this.dateReservation = new Date();
        this.statut = StatutReservation.EN_ATTENTE;
    }
    
    // Getters and Setters
    public Long getIdReservation() {
        return idReservation;
    }
    
    public void setIdReservation(Long idReservation) {
        this.idReservation = idReservation;
    }
    
    public Date getDateReservation() {
        return dateReservation;
    }
    
    public void setDateReservation(Date dateReservation) {
        this.dateReservation = dateReservation;
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
    
    public Integer getNombrePersonnes() {
        return nombrePersonnes;
    }
    
    public void setNombrePersonnes(Integer nombrePersonnes) {
        this.nombrePersonnes = nombrePersonnes;
    }
    
    public Float getPrixTotal() {
        return prixTotal;
    }
    
    public void setPrixTotal(Float prixTotal) {
        this.prixTotal = prixTotal;
    }
    
    public StatutReservation getStatut() {
        return statut;
    }
    
    public void setStatut(StatutReservation statut) {
        this.statut = statut;
    }
    
    public Sejour getSejour() {
        return sejour;
    }
    
    public void setSejour(Sejour sejour) {
        this.sejour = sejour;
    }
    
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }
    
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
} 