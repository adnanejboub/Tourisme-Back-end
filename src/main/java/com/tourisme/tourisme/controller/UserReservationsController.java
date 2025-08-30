package com.tourisme.tourisme.controller;

import com.tourisme.tourisme.entities.Reservation;
import com.tourisme.tourisme.repository.ReservationRepository;
import com.tourisme.tourisme.service.UserMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users/me/reservations")
@PreAuthorize("isAuthenticated()")
public class UserReservationsController {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserMappingService userMappingService;

    @GetMapping
    public ResponseEntity<List<Reservation>> getMyReservations() {
        Long uid = userMappingService.getCurrentUserId();
        return ResponseEntity.ok(reservationRepository.findByUtilisateur_IdUtilisateur(uid));
    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
        reservation.setDateReservation(new Date());
        return ResponseEntity.ok(reservationRepository.save(reservation));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReservation(@PathVariable Long id) {
        return reservationRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReservation(@PathVariable Long id, @RequestBody Reservation reservation) {
        if (!reservationRepository.existsById(id)) return ResponseEntity.notFound().build();
        reservation.setIdReservation(id);
        return ResponseEntity.ok(reservationRepository.save(reservation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long id) {
        if (!reservationRepository.existsById(id)) return ResponseEntity.notFound().build();
        reservationRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Reservation deleted"));
    }
}









