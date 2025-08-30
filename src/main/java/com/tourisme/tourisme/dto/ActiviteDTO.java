package com.tourisme.tourisme.dto;

import com.tourisme.tourisme.entities.Activite.CategorieActivite;

public class ActiviteDTO {
    private Long idActivite;
    private String nom;
    private Integer dureeMinimun;
    private Integer dureeMaximun;
    private String saison;
    private String niveauDificulta;
    private String conditionsSpeciales;
    private CategorieActivite categorie;
    private String villeNom;

    // Constructors
    public ActiviteDTO() {}

    public ActiviteDTO(Long idActivite, String nom, Integer dureeMinimun, Integer dureeMaximun, CategorieActivite categorie) {
        this.idActivite = idActivite;
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

    public CategorieActivite getCategorie() {
        return categorie;
    }

    public void setCategorie(CategorieActivite categorie) {
        this.categorie = categorie;
    }

    public String getVilleNom() {
        return villeNom;
    }

    public void setVilleNom(String villeNom) {
        this.villeNom = villeNom;
    }
} 