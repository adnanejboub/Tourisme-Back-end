package com.tourisme.tourisme.entities;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "avis")
public class Avis {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_avis")
    private Long idAvis;
    
    @Column(name = "note")
    private Integer note;
    
    @Column(name = "commentaire")
    private String commentaire;
    
    @Column(name = "date_avis")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAvis;
    
    @Column(name = "verifie")
    private Boolean verifie;
    
    // Relationships
    @ManyToOne
    @JoinColumn(name = "id_touriste")
    private Touriste touriste;
    
    @ManyToMany
    @JoinTable(
        name = "avis_media",
        joinColumns = @JoinColumn(name = "id_avis"),
        inverseJoinColumns = @JoinColumn(name = "id_media")
    )
    private List<Media> medias;
    
    // Constructors
    public Avis() {}
    
    public Avis(Integer note, String commentaire, Touriste touriste) {
        this.note = note;
        this.commentaire = commentaire;
        this.touriste = touriste;
        this.dateAvis = new Date();
        this.verifie = false;
    }
    
    // Methods from UML
    public void modifierAvis(String nouveauCommentaire, Integer nouvelleNote) {
        this.commentaire = nouveauCommentaire;
        this.note = nouvelleNote;
        this.dateAvis = new Date();
    }
    
    // Getters and Setters
    public Long getIdAvis() {
        return idAvis;
    }
    
    public void setIdAvis(Long idAvis) {
        this.idAvis = idAvis;
    }
    
    public Integer getNote() {
        return note;
    }
    
    public void setNote(Integer note) {
        this.note = note;
    }
    
    public String getCommentaire() {
        return commentaire;
    }
    
    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
    
    public Date getDateAvis() {
        return dateAvis;
    }
    
    public void setDateAvis(Date dateAvis) {
        this.dateAvis = dateAvis;
    }
    
    public Boolean getVerifie() {
        return verifie;
    }
    
    public void setVerifie(Boolean verifie) {
        this.verifie = verifie;
    }
    
    public Touriste getTouriste() {
        return touriste;
    }
    
    public void setTouriste(Touriste touriste) {
        this.touriste = touriste;
    }
    
    public List<Media> getMedias() {
        return medias;
    }
    
    public void setMedias(List<Media> medias) {
        this.medias = medias;
    }
} 