package com.tourisme.tourisme.repository;

import com.tourisme.tourisme.entities.Touriste;
import com.tourisme.tourisme.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TouristeRepository extends JpaRepository<Touriste, Long> {
    Optional<Touriste> findByUtilisateur(Utilisateur utilisateur);
}




