package com.tourisme.tourisme.repository;

import com.tourisme.tourisme.entities.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    
    // Find services by city ID
    List<Service> findByVille_IdVille(Long idVille);
    
    // Find services by city name
    @Query("SELECT s FROM Service s JOIN s.ville v WHERE LOWER(v.nomVille) LIKE LOWER(CONCAT('%', :cityName, '%'))")
    List<Service> findByCityName(@Param("cityName") String cityName);
    
    // Find services by type
    List<Service> findByTypeService(Service.TypeService typeService);
    
    // Find services by city and type
    @Query("SELECT s FROM Service s WHERE s.ville.idVille = :cityId AND s.typeService = :typeService")
    List<Service> findByVilleAndType(@Param("cityId") Long cityId, @Param("typeService") Service.TypeService typeService);
}

