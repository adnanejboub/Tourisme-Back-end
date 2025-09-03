package com.tourisme.tourisme.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "utilisateur")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("UTILISATEUR")
public class Utilisateur {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_utilisateur")
    private Long idUtilisateur;
    
    @Column(name = "nom", nullable = false)
    private String nom;
    
    @Column(name = "prenom", nullable = false)
    private String prenom;
        
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Nullable
    @Column(name = "telephone" )
    private String telephone;

    @Nullable
    @Column(name = "adresse")
    private String adresse;

    @Nullable
    @Column(name = "date_naissance")
    @Temporal(TemporalType.DATE)
    private Date dateNaissance;

    @Nullable
    @Column(name = "date_inscription")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateInscription;
    
    @Column(name = "mot_de_passe" ,nullable = false)
    private String motDePasse;
    
    // Champ cin avec valeur par défaut pour éviter les contraintes NOT NULL
    @Column(name = "cin", nullable = true)
    private String cin = "N/A"; // Valeur par défaut pour les utilisateurs non-admin
    
    // Colonnes des sous-classes - marquées comme nullable pour éviter les erreurs
    @Column(name = "nom_agence", nullable = true)
    private String nomAgence;
    
    @Column(name = "adresse_agence", nullable = true)
    private String adresseAgence;
    
    @Column(name = "email_agence", nullable = true)
    private String emailAgence;
    
    @Column(name = "telephone_agence", nullable = true)
    private String telephoneAgence;
    
    @Column(name = "certifie", nullable = true)
    private Boolean certifie;
    
    @Column(name = "nom_fournisseur", nullable = true)
    private String nomFournisseur;
    
    @Column(name = "adresse_fournisseur", nullable = true)
    private String adresseFournisseur;
    
    @Column(name = "telephone_fournisseur", nullable = true)
    private String telephoneFournisseur;
    
    @Column(name = "site_web", nullable = true)
    private String siteWeb;
    
    @Column(name = "email_fournisseur", nullable = true)
    private String emailFournisseur;
    
    @Column(name = "date_partenariat", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date datePartenariat;
    
    @Column(name = "commission", nullable = true)
    private Float commission;
    
    // Relationships
    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    @JsonManagedReference("utilisateur-paiements")
    private List<Paiement> paiements;
    
    @ManyToOne
    @JoinColumn(name = "id_role")
    @JsonIgnore
    private Role role;
    
    // Constructors
    public Utilisateur() {
        this.cin = "N/A"; // Initialisation par défaut
        this.dateInscription = new Date(); // Initialisation automatique
    }
    
    public Utilisateur(String nom, String prenom, String email) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.dateInscription = new Date();
        this.cin = "N/A"; // Valeur par défaut
    }
    
    public Utilisateur(String nom, String prenom, String email, String motDePasse) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.dateInscription = new Date();
        this.cin = "N/A"; // Valeur par défaut
    }
    
    public Utilisateur(String nom, String prenom, String email, String motDePasse, Role role) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
        this.dateInscription = new Date();
        this.cin = "N/A"; // Valeur par défaut
    }
    
    // Methods from UML
    public void sInscrire() {
        // Placeholder for registration method
    }
    
    public void sAuthentifier() {
        // Placeholder for authentication method
    }
    
    public void analyserStatistiques() {
        // Placeholder for statistics analysis
    }
    
    public Float obtenirRevenus() {
        // Placeholder for revenue calculation
        return 0.0f;
    }
    
    // Getters and Setters
    public Long getIdUtilisateur() {
        return idUtilisateur;
    }
    
    public void setIdUtilisateur(Long idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getPrenom() {
        return prenom;
    }
    
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTelephone() {
        return telephone;
    }
    
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
    public String getAdresse() {
        return adresse;
    }
    
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    
    public Date getDateNaissance() {
        return dateNaissance;
    }
    
    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }
    
    public Date getDateInscription() {
        return dateInscription;
    }
    
    public void setDateInscription(Date dateInscription) {
        this.dateInscription = dateInscription;
    }
    
    public String getMotDePasse() {
        return motDePasse;
    }
    
    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
    
    public String getCin() {
        return cin;
    }
    
    public void setCin(String cin) {
        this.cin = cin;
    }
    
    public List<Paiement> getPaiements() {
        return paiements;
    }
    
    public void setPaiements(List<Paiement> paiements) {
        this.paiements = paiements;
    }
    
    public Role getRole() {
        return role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }
    
    // Getters and Setters pour les colonnes des sous-classes
    public String getNomAgence() {
        return nomAgence;
    }
    
    public void setNomAgence(String nomAgence) {
        this.nomAgence = nomAgence;
    }
    
    public String getAdresseAgence() {
        return adresseAgence;
    }
    
    public void setAdresseAgence(String adresseAgence) {
        this.adresseAgence = adresseAgence;
    }
    
    public String getEmailAgence() {
        return emailAgence;
    }
    
    public void setEmailAgence(String emailAgence) {
        this.emailAgence = emailAgence;
    }
    
    public String getTelephoneAgence() {
        return telephoneAgence;
    }
    
    public void setTelephoneAgence(String telephoneAgence) {
        this.telephoneAgence = telephoneAgence;
    }
    
    public Boolean getCertifie() {
        return certifie;
    }
    
    public void setCertifie(Boolean certifie) {
        this.certifie = certifie;
    }
    
    public String getNomFournisseur() {
        return nomFournisseur;
    }
    
    public void setNomFournisseur(String nomFournisseur) {
        this.nomFournisseur = nomFournisseur;
    }
    
    public String getAdresseFournisseur() {
        return adresseFournisseur;
    }
    
    public void setAdresseFournisseur(String adresseFournisseur) {
        this.adresseFournisseur = adresseFournisseur;
    }
    
    public String getTelephoneFournisseur() {
        return telephoneFournisseur;
    }
    
    public void setTelephoneFournisseur(String telephoneFournisseur) {
        this.telephoneFournisseur = telephoneFournisseur;
    }
    
    public String getSiteWeb() {
        return siteWeb;
    }
    
    public void setSiteWeb(String siteWeb) {
        this.siteWeb = siteWeb;
    }
    
    public String getEmailFournisseur() {
        return emailFournisseur;
    }
    
    public void setEmailFournisseur(String emailFournisseur) {
        this.emailFournisseur = emailFournisseur;
    }
    
    public Date getDatePartenariat() {
        return datePartenariat;
    }
    
    public void setDatePartenariat(Date datePartenariat) {
        this.datePartenariat = datePartenariat;
    }
    
    public Float getCommission() {
        return commission;
    }
    
    public void setCommission(Float commission) {
        this.commission = commission;
    }
} 