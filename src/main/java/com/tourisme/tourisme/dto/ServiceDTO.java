package com.tourisme.tourisme.dto;

import com.tourisme.tourisme.entities.Service.TypeService;
import java.util.List;

public class ServiceDTO {
    private Long idService;
    private TypeService typeService;
    private String fournisseurNom;
    private List<String> mediaUrls; // Just URLs, not full Media objects

    // Constructors
    public ServiceDTO() {}

    public ServiceDTO(Long idService, TypeService typeService) {
        this.idService = idService;
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

    public String getFournisseurNom() {
        return fournisseurNom;
    }

    public void setFournisseurNom(String fournisseurNom) {
        this.fournisseurNom = fournisseurNom;
    }

    public List<String> getMediaUrls() {
        return mediaUrls;
    }

    public void setMediaUrls(List<String> mediaUrls) {
        this.mediaUrls = mediaUrls;
    }
}
