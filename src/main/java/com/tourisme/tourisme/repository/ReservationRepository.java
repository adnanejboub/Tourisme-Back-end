package com.tourisme.tourisme.repository;

import com.tourisme.tourisme.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // Basic CRUD operations are provided by JpaRepository
    java.util.List<Reservation> findByUtilisateur_IdUtilisateur(Long idUtilisateur);
}
