package org.example.nextgenmotors2.backend.repository;

import org.example.nextgenmotors2.backend.model.enu.ReservationStatus;
import org.example.nextgenmotors2.backend.model.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Encontrar reservas por vehículo y fecha
    List<Reservation> findByVehicleIdAndReservationDateBetween(Integer vehicleId,
                                                               LocalDateTime start,
                                                               LocalDateTime end);

    // Encontrar reservas por email de usuario
    List<Reservation> findByUserEmail(String userEmail);

    // Encontrar reservas por estado
    List<Reservation> findByStatus(ReservationStatus status);

    // Verificar disponibilidad de vehículo en una fecha específica
    boolean existsByVehicleIdAndReservationDateAndStatusNot(Integer vehicleId,
                                                            LocalDateTime reservationDate,
                                                            ReservationStatus status);
}