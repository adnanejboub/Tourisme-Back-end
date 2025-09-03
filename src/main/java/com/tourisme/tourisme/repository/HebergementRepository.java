package com.tourisme.tourisme.repository;

import com.tourisme.tourisme.entities.Hebergement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HebergementRepository extends JpaRepository<Hebergement, Long> {
    List<Hebergement> findByNomHebergementContainingIgnoreCase(String q);
    List<Hebergement> findByVille_IdVille(Long idVille);

    @Query(value = "select id_hebergement, nom_hebergement, adresse, prix_par_nuit, etoiles, description, is_disponible, hebergement_type from hebergement where coalesce(nullif(hebergement_type,''),'HEBERGEMENT') in ('HEBERGEMENT','HOTEL','RIAD','APPARTEMENT')", nativeQuery = true)
    List<Object[]> findAllAsRows();

    @Query(value = "select id_hebergement, nom_hebergement, adresse, prix_par_nuit, etoiles, description, is_disponible, hebergement_type from hebergement where lower(nom_hebergement) like concat('%', lower(?1), '%') and coalesce(nullif(hebergement_type,''),'HEBERGEMENT') in ('HEBERGEMENT','HOTEL','RIAD','APPARTEMENT')", nativeQuery = true)
    List<Object[]> searchAsRows(String q);

    @Query(value = "select h.id_hebergement, h.nom_hebergement, h.adresse, h.prix_par_nuit, h.etoiles, h.description, h.is_disponible, h.hebergement_type from hebergement h join ville v on h.id_ville = v.id_ville where lower(v.nom_ville) like concat('%', lower(?1), '%') and coalesce(nullif(h.hebergement_type,''),'HEBERGEMENT') in ('HEBERGEMENT','HOTEL','RIAD','APPARTEMENT')", nativeQuery = true)
    List<Object[]> findByCityName(String cityName);
}
