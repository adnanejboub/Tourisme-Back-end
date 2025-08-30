package com.tourisme.tourisme.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "compagnie_aerienne")
public class CompagnieAerienne {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comp")
    private Long idComp;
    
    @Column(name = "nom_comp", nullable = false)
    private String nomComp;
    
    @Column(name = "tel")
    private String tel;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "adresse")
    private String adresse;
    
    @Column(name = "pays_origine")
    private String paysOrigine;
    
    @Column(name = "actif")
    private Boolean actif;
    
    // Relationships
    @OneToMany(mappedBy = "compagnieAerienne", cascade = CascadeType.ALL)
    private List<Vol> vols;
    
    // Constructors
    public CompagnieAerienne() {
        this.actif = true;
    }
    
    public CompagnieAerienne(String nomComp, String tel) {
        this.nomComp = nomComp;
        this.tel = tel;
        this.actif = true;
    }
    
    // Getters and Setters
    public Long getIdComp() {
        return idComp;
    }
    
    public void setIdComp(Long idComp) {
        this.idComp = idComp;
    }
    
    public String getNomComp() {
        return nomComp;
    }
    
    public void setNomComp(String nomComp) {
        this.nomComp = nomComp;
    }
    
    public String getTel() {
        return tel;
    }
    
    public void setTel(String tel) {
        this.tel = tel;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getAdresse() {
        return adresse;
    }
    
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    
    public String getPaysOrigine() {
        return paysOrigine;
    }
    
    public void setPaysOrigine(String paysOrigine) {
        this.paysOrigine = paysOrigine;
    }
    
    public Boolean getActif() {
        return actif;
    }
    
    public void setActif(Boolean actif) {
        this.actif = actif;
    }
    
    public List<Vol> getVols() {
        return vols;
    }
    
    public void setVols(List<Vol> vols) {
        this.vols = vols;
    }
} 