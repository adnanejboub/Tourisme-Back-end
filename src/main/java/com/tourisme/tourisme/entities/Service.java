package com.tourisme.tourisme.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "service")
public class Service {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_service")
    private Long idService;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type_service")
    private TypeService typeService;
    
    // Relationships
    @ManyToOne
    @JoinColumn(name = "id_ville")
    @JsonBackReference("ville-services")
    private Ville ville;
    
    @ManyToOne
    @JoinColumn(name = "id_fournisseur")
    private Fournisseur fournisseur;
    
    @ManyToMany
    @JoinTable(
        name = "service_media",
        joinColumns = @JoinColumn(name = "id_service"),
        inverseJoinColumns = @JoinColumn(name = "id_media")
    )
    private List<Media> medias;

    public Float getPrixAnnulation() {
        return 0.0F;
    }

    // Enum for type_service
    public enum TypeService {
        CAFE,
        RESTAURANT,
        MAGASIN
    }
    
    // Constructors
    public Service() {}
    
    public Service(TypeService typeService) {
        this.typeService = typeService;
    }
    
    // Getters and Setters
    public Long getIdService() {
        return idService;
    }
    
    public void setIdService(Long idService) {
        this.idService = idService;
    }
    
    public TypeService getTypeService() {
        return typeService;
    }
    
    public void setTypeService(TypeService typeService) {
        this.typeService = typeService;
    }
    
    public Ville getVille() {
        return ville;
    }
    
    public void setVille(Ville ville) {
        this.ville = ville;
    }
    
    public Fournisseur getFournisseur() {
        return fournisseur;
    }
    
    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }
    
    public List<Media> getMedias() {
        return medias;
    }
    
    public void setMedias(List<Media> medias) {
        this.medias = medias;
    }
} 