package com.tourisme.tourisme.repository;

import com.tourisme.tourisme.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByUser_IdUtilisateurOrderByCreatedDateDesc(Long userId, Pageable pageable);
    Order findByOrderNumber(String orderNumber);
}
