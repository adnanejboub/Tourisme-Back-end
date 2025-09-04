package com.tourisme.tourisme.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "activite")
public class Activite {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_activite")
    private Long idActivite;
    
    @Column(name = "nom", nullable = false)
    private String nom;
    
    @Column(name = "duree_minimun")
    private Integer dureeMinimun;
    
    @Column(name = "duree_maximun")
    private Integer dureeMaximun;
    
    @Column(name = "saison")
    private String saison;
    
    @Column(name = "niveau_dificulta")
    private String niveauDificulta;
    
    @Column(name = "conditions_speciales")
    private String conditionsSpeciales;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @Column(name = "prix")
    private Double prix;
    
    @Column(name = "note_moyenne")
    private Double noteMoyenne;
    
    @Column(name = "nombre_evaluations")
    private Integer nombreEvaluations;
    
    @Column(name = "is_disponible")
    private Boolean isDisponible;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "categorie")
    private CategorieActivite categorie;
    
    // Relationships
    @ManyToOne
    @JoinColumn(name = "id_ville")
    @JsonBackReference("ville-activites")
    private Ville ville;
    
    @ManyToMany
    @JoinTable(
        name = "activite_media",
        joinColumns = @JoinColumn(name = "id_activite"),
        inverseJoinColumns = @JoinColumn(name = "id_media")
    )
    private List<Media> medias;
    
    // Enum for category
    public enum CategorieActivite {
        TOURS,
        EVENEMENTS,
        ACTIVITES_PLEIN_AIR
    }
    
    // Constructors
    public Activite() {}
    
    public Activite(String nom, Integer dureeMinimun, Integer dureeMaximun, CategorieActivite categorie) {
        this.nom = nom;
        this.dureeMinimun = dureeMinimun;
        this.dureeMaximun = dureeMaximun;
        this.categorie = categorie;
    }
    
    // Getters and Setters
    public Long getIdActivite() {
        return idActivite;
    }
    
    public void setIdActivite(Long idActivite) {
        this.idActivite = idActivite;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public Integer getDureeMinimun() {
        return dureeMinimun;
    }
    
    public void setDureeMinimun(Integer dureeMinimun) {
        this.dureeMinimun = dureeMinimun;
    }
    
    public Integer getDureeMaximun() {
        return dureeMaximun;
    }
    
    public void setDureeMaximun(Integer dureeMaximun) {
        this.dureeMaximun = dureeMaximun;
    }
    
    public String getSaison() {
        return saison;
    }
    
    public void setSaison(String saison) {
        this.saison = saison;
    }
    
    public String getNiveauDificulta() {
        return niveauDificulta;
    }
    
    public void setNiveauDificulta(String niveauDificulta) {
        this.niveauDificulta = niveauDificulta;
    }
    
    public String getConditionsSpeciales() {
        return conditionsSpeciales;
    }
    
    public void setConditionsSpeciales(String conditionsSpeciales) {
        this.conditionsSpeciales = conditionsSpeciales;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public Double getPrix() {
        return prix;
    }
    
    public void setPrix(Double prix) {
        this.prix = prix;
    }
    
    public Double getNoteMoyenne() {
        return noteMoyenne;
    }
    
    public void setNoteMoyenne(Double noteMoyenne) {
        this.noteMoyenne = noteMoyenne;
    }
    
    public Integer getNombreEvaluations() {
        return nombreEvaluations;
    }
    
    public void setNombreEvaluations(Integer nombreEvaluations) {
        this.nombreEvaluations = nombreEvaluations;
    }
    
    public Boolean getIsDisponible() {
        return isDisponible;
    }
    
    public void setIsDisponible(Boolean isDisponible) {
        this.isDisponible = isDisponible;
    }
    
    public CategorieActivite getCategorie() {
        return categorie;
    }
    
    public void setCategorie(CategorieActivite categorie) {
        this.categorie = categorie;
    }
    
    public Ville getVille() {
        return ville;
    }
    
    public void setVille(Ville ville) {
        this.ville = ville;
    }
    
    public List<Media> getMedias() {
        return medias;
    }
    
    public void setMedias(List<Media> medias) {
        this.medias = medias;
    }
} 