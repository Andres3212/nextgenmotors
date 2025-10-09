package org.example.nextgenmotors2.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private VehicleDatabase vehicleDatabase;

    // Crear una nueva reserva
    public Reservation createReservation(Reservation reservation) {
        // Verificar disponibilidad
        if (!isVehicleAvailable(reservation.getVehicleId(), reservation.getReservationDate())) {
            throw new RuntimeException("El vehículo no está disponible en la fecha seleccionada");
        }

        // Verificar que el vehículo existe
        Vehicle vehicle = vehicleDatabase.getVehicleById(reservation.getVehicleId());
        if (vehicle == null) {
            throw new RuntimeException("Vehículo no encontrado");
        }

        return reservationRepository.save(reservation);
    }

    // Verificar disponibilidad del vehículo
    public boolean isVehicleAvailable(Integer vehicleId, LocalDateTime dateTime) {
        // Verificar si ya existe una reserva para ese vehículo en esa fecha/hora
        return !reservationRepository.existsByVehicleIdAndReservationDateAndStatusNot(
                vehicleId, dateTime, ReservationStatus.CANCELLED);
    }

    // Obtener todas las reservas
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    // Obtener reserva por ID
    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    // Obtener reservas por usuario
    public List<Reservation> getReservationsByUser(String userEmail) {
        return reservationRepository.findByUserEmail(userEmail);
    }

    // Actualizar estado de reserva
    public Reservation updateReservationStatus(Long id, ReservationStatus status) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(id);
        if (reservationOpt.isPresent()) {
            Reservation reservation = reservationOpt.get();
            reservation.setStatus(status);
            return reservationRepository.save(reservation);
        }
        throw new RuntimeException("Reserva no encontrada");
    }

    // Cancelar reserva
    public Reservation cancelReservation(Long id) {
        return updateReservationStatus(id, ReservationStatus.CANCELLED);
    }

    // Obtener disponibilidad de vehículo para un rango de fechas
    public List<LocalDateTime> getAvailableSlots(Integer vehicleId, LocalDateTime startDate, LocalDateTime endDate) {
        // Implementación simple - en una aplicación real, esto sería más complejo
        // Retorna una lista de slots disponibles cada hora entre startDate y endDate
        // que no estén ocupados
        return List.of(); // Simplificado para este ejemplo
    }
}