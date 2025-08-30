package com.tourisme.tourisme.entities;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "media")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "media_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("MEDIA")
public class Media {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_media")
    private Long idMedia;
    
    @Column(name = "nom_media", nullable = false)
    private String nomMedia;
    
    @Column(name = "taille")
    private Long taille;
    
    @Column(name = "type_media")
    private String typeMedia;
    
    @Column(name = "data_upload")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataUpload;
    
    // Constructors
    public Media() {}
    
    public Media(String nomMedia, Long taille, String typeMedia) {
        this.nomMedia = nomMedia;
        this.taille = taille;
        this.typeMedia = typeMedia;
        this.dataUpload = new Date();
    }
    
    // Methods from UML
    public boolean hasChargementMedia() {
        return this.taille != null && this.taille > 0;
    }
    
    public void optimiserMedia() {
        // Placeholder for media optimization
    }
    
    // Getters and Setters
    public Long getIdMedia() {
        return idMedia;
    }
    
    public void setIdMedia(Long idMedia) {
        this.idMedia = idMedia;
    }
    
    public String getNomMedia() {
        return nomMedia;
    }
    
    public void setNomMedia(String nomMedia) {
        this.nomMedia = nomMedia;
    }
    
    public Long getTaille() {
        return taille;
    }
    
    public void setTaille(Long taille) {
        this.taille = taille;
    }
    
    public String getTypeMedia() {
        return typeMedia;
    }
    
    public void setTypeMedia(String typeMedia) {
        this.typeMedia = typeMedia;
    }
    
    public Date getDataUpload() {
        return dataUpload;
    }
    
    public void setDataUpload(Date dataUpload) {
        this.dataUpload = dataUpload;
    }
} 