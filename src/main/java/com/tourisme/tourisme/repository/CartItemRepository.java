package com.tourisme.tourisme.repository;

import com.tourisme.tourisme.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser_IdUtilisateurOrderByCreatedDateDesc(Long userId);
    
    Optional<CartItem> findByUser_IdUtilisateurAndProduct_IdAndSelectedColorAndSelectedSize(
            Long userId, Long productId, String selectedColor, String selectedSize);
    
    void deleteByUser_IdUtilisateur(Long userId);
}