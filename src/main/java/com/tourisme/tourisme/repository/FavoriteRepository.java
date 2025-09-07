package com.tourisme.tourisme.repository;

import com.tourisme.tourisme.entities.Favorite;
import com.tourisme.tourisme.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUtilisateurOrderByCreatedAtDesc(Utilisateur utilisateur);
    Optional<Favorite> findByUtilisateurAndItemTypeAndItemId(Utilisateur utilisateur, String itemType, Long itemId);
}



