package com.tourisme.tourisme.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "partenaire")
public class Partenaire {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_partenaire")
    private Long idPartenaire;
    
    @Column(name = "nom_partenaire", nullable = false)
    private String nomPartenaire;
    
    @Column(name = "verifier")
    private Boolean verifier;
    
    @Column(name = "type_partenaire")
    @Enumerated(EnumType.STRING)
    private TypePartenaire typePartenaire;
    
    @Column(name = "adresse")
    private String adresse;
    
    @Column(name = "telephone")
    private String telephone;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "site_web")
    private String siteWeb;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "actif")
    private Boolean actif;
    
    // Relationships
    @OneToOne
    @JoinColumn(name = "id_utilisateur")
    private Utilisateur utilisateur;
    
    @OneToMany(mappedBy = "partenaire", cascade = CascadeType.ALL)
    private List<OffreTouristique> offresTouristiques;
    
    // Enum for partner type
    public enum TypePartenaire {
        HOTEL,
        RESTAURANT,
        AGENCE_VOYAGE,
        TRANSPORT,
        ACTIVITE,
        BOUTIQUE,
        AUTRE
    }
    
    // Constructors
    public Partenaire() {
        this.verifier = false;
        this.actif = true;
    }
    
    public Partenaire(String nomPartenaire, TypePartenaire typePartenaire) {
        this.nomPartenaire = nomPartenaire;
        this.typePartenaire = typePartenaire;
        this.verifier = false;
        this.actif = true;
    }
    
    // Methods from UML
    public void verifierPartenaire() {
        this.verifier = true;
    }
    
    public void desactiverPartenaire() {
        this.actif = false;
    }
    
    public void activerPartenaire() {
        this.actif = true;
    }
    
    // Getters and Setters
    public Long getIdPartenaire() {
        return idPartenaire;
    }
    
    public void setIdPartenaire(Long idPartenaire) {
        this.idPartenaire = idPartenaire;
    }
    
    public String getNomPartenaire() {
        return nomPartenaire;
    }
    
    public void setNomPartenaire(String nomPartenaire) {
        this.nomPartenaire = nomPartenaire;
    }
    
    public Boolean getVerifier() {
        return verifier;
    }
    
    public void setVerifier(Boolean verifier) {
        this.verifier = verifier;
    }
    
    public TypePartenaire getTypePartenaire() {
        return typePartenaire;
    }
    
    public void setTypePartenaire(TypePartenaire typePartenaire) {
        this.typePartenaire = typePartenaire;
    }
    
    public String getAdresse() {
        return adresse;
    }
    
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    
    public String getTelephone() {
        return telephone;
    }
    
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getSiteWeb() {
        return siteWeb;
    }
    
    public void setSiteWeb(String siteWeb) {
        this.siteWeb = siteWeb;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Boolean getActif() {
        return actif;
    }
    
    public void setActif(Boolean actif) {
        this.actif = actif;
    }
    
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }
    
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
    
    public List<OffreTouristique> getOffresTouristiques() {
        return offresTouristiques;
    }
    
    public void setOffresTouristiques(List<OffreTouristique> offresTouristiques) {
        this.offresTouristiques = offresTouristiques;
    }
} 