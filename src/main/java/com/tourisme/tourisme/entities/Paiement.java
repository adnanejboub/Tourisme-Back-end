package com.tourisme.tourisme.entities;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "paiement")
public class Paiement {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paiement")
    private Long idPaiement;
    
    @Column(name = "methode_paiement")
    private String methodePaiement;
    
    @Column(name = "numero_transaction")
    private String numeroTransaction;
    
    @Column(name = "montant")
    private Float montant;
    
    @Column(name = "date_paiement")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePaiement;
    
    // Relationships
    @ManyToOne
    @JoinColumn(name = "id_utilisateur")
    private Utilisateur utilisateur;
    
    // Constructors
    public Paiement() {}
    
    public Paiement(String methodePaiement, String numeroTransaction, Float montant, Utilisateur utilisateur) {
        this.methodePaiement = methodePaiement;
        this.numeroTransaction = numeroTransaction;
        this.montant = montant;
        this.utilisateur = utilisateur;
        this.datePaiement = new Date();
    }
    
    // Getters and Setters
    public Long getIdPaiement() {
        return idPaiement;
    }
    
    public void setIdPaiement(Long idPaiement) {
        this.idPaiement = idPaiement;
    }
    
    public String getMethodePaiement() {
        return methodePaiement;
    }
    
    public void setMethodePaiement(String methodePaiement) {
        this.methodePaiement = methodePaiement;
    }
    
    public String getNumeroTransaction() {
        return numeroTransaction;
    }
    
    public void setNumeroTransaction(String numeroTransaction) {
        this.numeroTransaction = numeroTransaction;
    }
    
    public Float getMontant() {
        return montant;
    }
    
    public void setMontant(Float montant) {
        this.montant = montant;
    }
    
    public Date getDatePaiement() {
        return datePaiement;
    }
    
    public void setDatePaiement(Date datePaiement) {
        this.datePaiement = datePaiement;
    }
    
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }
    
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
} 