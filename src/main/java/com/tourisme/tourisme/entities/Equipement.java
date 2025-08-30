package com.tourisme.tourisme.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "equipement")
public class Equipement {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_equipement")
    private Long idEquipement;
    
    @Column(name = "cad_vit_spa")
    private String cadVitSpa;
    
    @Column(name = "json_pieche")
    private String jsonPieche;
    
    @Column(name = "dimarisation")
    private String dimarisation;
    
    // Constructors
    public Equipement() {}
    
    public Equipement(String cadVitSpa, String jsonPieche, String dimarisation) {
        this.cadVitSpa = cadVitSpa;
        this.jsonPieche = jsonPieche;
        this.dimarisation = dimarisation;
    }
    
    // Getters and Setters
    public Long getIdEquipement() {
        return idEquipement;
    }
    
    public void setIdEquipement(Long idEquipement) {
        this.idEquipement = idEquipement;
    }
    
    public String getCadVitSpa() {
        return cadVitSpa;
    }
    
    public void setCadVitSpa(String cadVitSpa) {
        this.cadVitSpa = cadVitSpa;
    }
    
    public String getJsonPieche() {
        return jsonPieche;
    }
    
    public void setJsonPieche(String jsonPieche) {
        this.jsonPieche = jsonPieche;
    }
    
    public String getDimarisation() {
        return dimarisation;
    }
    
    public void setDimarisation(String dimarisation) {
        this.dimarisation = dimarisation;
    }
} 