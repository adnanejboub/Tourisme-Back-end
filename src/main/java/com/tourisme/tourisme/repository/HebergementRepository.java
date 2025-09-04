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

    @Query(value = "select id_hebergement, nom_hebergement, adresse, prix_par_nuit, etoiles, description, is_disponible, coalesce(nullif(hebergement_type,''),'HEBERGEMENT') as hebergement_type, id_ville from hebergement where id_hebergement = ?1", nativeQuery = true)
    List<Object[]> findOneAsRow(Long idHebergement);

    @Query(value = "select h.id_hebergement, h.nom_hebergement, h.adresse, h.prix_par_nuit, h.etoiles, h.description, h.is_disponible, coalesce(nullif(h.hebergement_type,''),'HEBERGEMENT') as hebergement_type, v.id_ville, v.nom_ville, v.description as ville_description, v.image_url, v.latitude, v.longitude from hebergement h left join ville v on h.id_ville = v.id_ville where h.id_hebergement = ?1", nativeQuery = true)
    Object[] findOneWithCityAsRow(Long idHebergement);

    @Query(value = "select id_hebergement, nom_hebergement, prix_par_nuit, etoiles from hebergement where id_ville = ?1 and id_hebergement <> ?2 and coalesce(nullif(hebergement_type,''),'HEBERGEMENT') in ('HEBERGEMENT','HOTEL','RIAD','APPARTEMENT') order by id_hebergement desc limit 8", nativeQuery = true)
    List<Object[]> findRelatedByVilleAsRows(Long idVille, Long excludeId);
}
