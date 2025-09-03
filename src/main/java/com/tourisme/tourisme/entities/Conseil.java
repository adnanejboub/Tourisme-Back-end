package com.tourisme.tourisme.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "conseil")
public class Conseil {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conseil")
    private Long idConseil;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type_conseil")
    private TypeConseil typeConseil;
    
    @Column(name = "description")
    private String description;
    
    // Relationship with Ville
    @ManyToOne
    @JoinColumn(name = "id_ville")
    @JsonBackReference("ville-conseils")
    private Ville ville;
    
    // Enum for type_conseil
    public enum TypeConseil {
        CRIMINALITE,
        NEGOCIATION,
        METEO
    }
    
    // Constructors
    public Conseil() {}
    
    public Conseil(TypeConseil typeConseil, String description) {
        this.typeConseil = typeConseil;
        this.description = description;
    }
    
    // Getters and Setters
    public Long getIdConseil() {
        return idConseil;
    }
    
    public void setIdConseil(Long idConseil) {
        this.idConseil = idConseil;
    }
    
    public TypeConseil getTypeConseil() {
        return typeConseil;
    }
    
    public void setTypeConseil(TypeConseil typeConseil) {
        this.typeConseil = typeConseil;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Ville getVille() {
        return ville;
    }
    
    public void setVille(Ville ville) {
        this.ville = ville;
    }
} 