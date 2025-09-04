package com.tourisme.tourisme.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "ville")
public class Ville {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ville")
    private Long idVille;
    
    @Column(name = "nom_ville", nullable = false)
    private String nomVille;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "latitude")
    private Float latitude;
    
    @Column(name = "longitude")
    private Float longitude;
    
    @Column(name = "all_inclusive")
    private Boolean allInclusive;
    
    @Column(name = "allouement")
    private String allouement;
    
    @Column(name = "note_moyenne")
    private Float noteMoyenne;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    // Boolean attributes for city characteristics
    @Column(name = "is_plage")
    private Boolean isPlage;
    
    @Column(name = "is_montagne")
    private Boolean isMontagne;
    
    @Column(name = "is_desert")
    private Boolean isDesert;
    
    @Column(name = "is_riviera")
    private Boolean isRiviera;
    
    @Column(name = "is_historique")
    private Boolean isHistorique;
    
    @Column(name = "is_culturelle")
    private Boolean isCulturelle;
    
    @Column(name = "is_moderne")
    private Boolean isModerne;
    
    @Column(name = "has_aeroport")
    private Boolean hasAeroport;
    
    @Column(name = "has_gare")
    private Boolean hasGare;
    
    @Column(name = "has_port")
    private Boolean hasPort;
    
    @Column(name = "has_plage")
    private Boolean hasPlage;
    
    @Column(name = "has_montagne")
    private Boolean hasMontagne;
    
    @Column(name = "has_desert")
    private Boolean hasDesert;
    
    @Column(name = "has_riviera")
    private Boolean hasRiviera;
    
    @Column(name = "has_historique")
    private Boolean hasHistorique;
    
    @Column(name = "has_culturelle")
    private Boolean hasCulturelle;
    
    @Column(name = "has_moderne")
    private Boolean hasModerne;
    
    // Relationships
    @ManyToOne
    @JoinColumn(name = "id_pays")
    private Pays pays;
    
    @ManyToOne
    @JoinColumn(name = "id_climat")
    private Climat climat;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_localisation")
    private Localisation localisation;
    
    @OneToMany(mappedBy = "ville", cascade = CascadeType.ALL)
    @JsonManagedReference("ville-specialites")
    private List<SpecialiteVille> specialites;
    
    @OneToMany(mappedBy = "ville", cascade = CascadeType.ALL)
    @JsonManagedReference("ville-monuments")
    private List<Monument> monuments;
    
    @OneToMany(mappedBy = "ville", cascade = CascadeType.ALL)
    @JsonManagedReference("ville-hebergements")
    private List<Hebergement> hebergements;
    
    @OneToMany(mappedBy = "ville", cascade = CascadeType.ALL)
    @JsonManagedReference("ville-aeroports")
    private List<Aeroport> aeroports;
    
    @OneToMany(mappedBy = "ville", cascade = CascadeType.ALL)
    @JsonManagedReference("ville-activites")
    private List<Activite> activites;
    
    @OneToMany(mappedBy = "ville", cascade = CascadeType.ALL)
    @JsonManagedReference("ville-services")
    private List<Service> services;
    
    @OneToMany(mappedBy = "ville", cascade = CascadeType.ALL)
    @JsonManagedReference("ville-agencesLocation")
    private List<AgenceLocation> agencesLocation;
    
    @OneToMany(mappedBy = "ville", cascade = CascadeType.ALL)
    @JsonManagedReference("ville-conseils")
    private List<Conseil> conseils;
    
    // Constructors
    public Ville() {}
    
    public Ville(String nomVille, String description) {
        this.nomVille = nomVille;
        this.description = description;
    }
    
    // Getters and Setters
    public Long getIdVille() {
        return idVille;
    }
    
    public void setIdVille(Long idVille) {
        this.idVille = idVille;
    }
    
    public String getNomVille() {
        return nomVille;
    }
    
    public void setNomVille(String nomVille) {
        this.nomVille = nomVille;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Float getLatitude() {
        return latitude;
    }
    
    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }
    
    public Float getLongitude() {
        return longitude;
    }
    
    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }
    
    public Boolean getAllInclusive() {
        return allInclusive;
    }
    
    public void setAllInclusive(Boolean allInclusive) {
        this.allInclusive = allInclusive;
    }
    
    public String getAllouement() {
        return allouement;
    }
    
    public void setAllouement(String allouement) {
        this.allouement = allouement;
    }
    
    public Float getNoteMoyenne() {
        return noteMoyenne;
    }
    
    public void setNoteMoyenne(Float noteMoyenne) {
        this.noteMoyenne = noteMoyenne;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public Boolean getIsPlage() {
        return isPlage;
    }
    
    public void setIsPlage(Boolean isPlage) {
        this.isPlage = isPlage;
    }
    
    public Boolean getIsMontagne() {
        return isMontagne;
    }
    
    public void setIsMontagne(Boolean isMontagne) {
        this.isMontagne = isMontagne;
    }
    
    public Boolean getIsDesert() {
        return isDesert;
    }
    
    public void setIsDesert(Boolean isDesert) {
        this.isDesert = isDesert;
    }
    
    public Boolean getIsRiviera() {
        return isRiviera;
    }
    
    public void setIsRiviera(Boolean isRiviera) {
        this.isRiviera = isRiviera;
    }
    
    public Boolean getIsHistorique() {
        return isHistorique;
    }
    
    public void setIsHistorique(Boolean isHistorique) {
        this.isHistorique = isHistorique;
    }
    
    public Boolean getIsCulturelle() {
        return isCulturelle;
    }
    
    public void setIsCulturelle(Boolean isCulturelle) {
        this.isCulturelle = isCulturelle;
    }
    
    public Boolean getIsModerne() {
        return isModerne;
    }
    
    public void setIsModerne(Boolean isModerne) {
        this.isModerne = isModerne;
    }
    
    public Boolean getHasAeroport() {
        return hasAeroport;
    }
    
    public void setHasAeroport(Boolean hasAeroport) {
        this.hasAeroport = hasAeroport;
    }
    
    public Boolean getHasGare() {
        return hasGare;
    }
    
    public void setHasGare(Boolean hasGare) {
        this.hasGare = hasGare;
    }
    
    public Boolean getHasPort() {
        return hasPort;
    }
    
    public void setHasPort(Boolean hasPort) {
        this.hasPort = hasPort;
    }
    
    public Boolean getHasPlage() {
        return hasPlage;
    }
    
    public void setHasPlage(Boolean hasPlage) {
        this.hasPlage = hasPlage;
    }
    
    public Boolean getHasMontagne() {
        return hasMontagne;
    }
    
    public void setHasMontagne(Boolean hasMontagne) {
        this.hasMontagne = hasMontagne;
    }
    
    public Boolean getHasDesert() {
        return hasDesert;
    }
    
    public void setHasDesert(Boolean hasDesert) {
        this.hasDesert = hasDesert;
    }
    
    public Boolean getHasRiviera() {
        return hasRiviera;
    }
    
    public void setHasRiviera(Boolean hasRiviera) {
        this.hasRiviera = hasRiviera;
    }
    
    public Boolean getHasHistorique() {
        return hasHistorique;
    }
    
    public void setHasHistorique(Boolean hasHistorique) {
        this.hasHistorique = hasHistorique;
    }
    
    public Boolean getHasCulturelle() {
        return hasCulturelle;
    }
    
    public void setHasCulturelle(Boolean hasCulturelle) {
        this.hasCulturelle = hasCulturelle;
    }
    
    public Boolean getHasModerne() {
        return hasModerne;
    }
    
    public void setHasModerne(Boolean hasModerne) {
        this.hasModerne = hasModerne;
    }
    
    public Pays getPays() {
        return pays;
    }
    
    public void setPays(Pays pays) {
        this.pays = pays;
    }
    
    public Climat getClimat() {
        return climat;
    }
    
    public void setClimat(Climat climat) {
        this.climat = climat;
    }
    
    public Localisation getLocalisation() {
        return localisation;
    }
    
    public void setLocalisation(Localisation localisation) {
        this.localisation = localisation;
    }
    
    public List<SpecialiteVille> getSpecialites() {
        return specialites;
    }
    
    public void setSpecialites(List<SpecialiteVille> specialites) {
        this.specialites = specialites;
    }
    
    public List<Monument> getMonuments() {
        return monuments;
    }
    
    public void setMonuments(List<Monument> monuments) {
        this.monuments = monuments;
    }
    
    public List<Hebergement> getHebergements() {
        return hebergements;
    }
    
    public void setHebergements(List<Hebergement> hebergements) {
        this.hebergements = hebergements;
    }
    
    public List<Aeroport> getAeroports() {
        return aeroports;
    }
    
    public void setAeroports(List<Aeroport> aeroports) {
        this.aeroports = aeroports;
    }
    
    public List<Activite> getActivites() {
        return activites;
    }
    
    public void setActivites(List<Activite> activites) {
        this.activites = activites;
    }
    
    public List<Service> getServices() {
        return services;
    }
    
    public void setServices(List<Service> services) {
        this.services = services;
    }
    
    public List<AgenceLocation> getAgencesLocation() {
        return agencesLocation;
    }
    
    public void setAgencesLocation(List<AgenceLocation> agencesLocation) {
        this.agencesLocation = agencesLocation;
    }
    
    public List<Conseil> getConseils() {
        return conseils;
    }
    
    public void setConseils(List<Conseil> conseils) {
        this.conseils = conseils;
    }
} 