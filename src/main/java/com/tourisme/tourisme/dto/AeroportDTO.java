package com.tourisme.tourisme.dto;

public class AeroportDTO {
    private Long idAeroport;
    private String nomAeroport;
    private String localisation;

    // Constructors
    public AeroportDTO() {}

    public AeroportDTO(Long idAeroport, String nomAeroport, String localisation) {
        this.idAeroport = idAeroport;
        this.nomAeroport = nomAeroport;
        this.localisation = localisation;
    }

    // Getters and Setters
    public Long getIdAeroport() {
        return idAeroport;
    }

    public void setIdAeroport(Long idAeroport) {
        this.idAeroport = idAeroport;
    }

    public String getNomAeroport() {
        return nomAeroport;
    }

    public void setNomAeroport(String nomAeroport) {
        this.nomAeroport = nomAeroport;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }
}
