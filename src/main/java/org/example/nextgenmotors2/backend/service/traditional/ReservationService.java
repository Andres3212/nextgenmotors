package org.example.nextgenmotors2.backend.service.traditional;

import org.example.nextgenmotors2.backend.model.entity.Reservation;
import org.example.nextgenmotors2.backend.model.enu.ReservationStatus;
import org.example.nextgenmotors2.backend.model.entity.Vehicle;
import org.example.nextgenmotors2.backend.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
        import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private VehicleDatabase vehicleDatabase;

    // Cache para disponibilidad
    private final Map<String, Boolean> availabilityCache = new ConcurrentHashMap<>();
    private final Map<Long, Reservation> reservationCache = new ConcurrentHashMap<>();

    // Crear una nueva reserva
    public Reservation createReservation(Reservation reservation) {
        // Limpiar cache de disponibilidad
        availabilityCache.clear();

        // Verificar disponibilidad de forma optimizada
        if (!isVehicleAvailable(reservation.getVehicleId(), reservation.getReservationDate())) {
            throw new RuntimeException("El vehículo no está disponible en la fecha seleccionada");
        }

        // Verificar que el vehículo existe (usando cache)
        Vehicle vehicle = vehicleDatabase.getVehicleById(reservation.getVehicleId());
        if (vehicle == null) {
            throw new RuntimeException("Vehículo no encontrado");
        }

        Reservation saved = reservationRepository.save(reservation);
        // Actualizar cache
        reservationCache.put(saved.getId(), saved);
        return saved;
    }

    // Verificar disponibilidad del vehículo - OPTIMIZADO
    public boolean isVehicleAvailable(Integer vehicleId, LocalDateTime dateTime) {
        String cacheKey = vehicleId + "|" + dateTime.toString();

        // Verificar cache primero
        Boolean cached = availabilityCache.get(cacheKey);
        if (cached != null) {
            return cached;
        }

        // Si no está en cache, calcular
        List<Reservation> reservations = reservationRepository.findAll();
        boolean available = reservations.stream()
                .filter(reservation -> reservation.getVehicleId().equals(vehicleId))
                .filter(reservation -> reservation.getReservationDate().equals(dateTime))
                .noneMatch(reservation -> !reservation.getStatus().equals(ReservationStatus.CANCELLED));

        // Guardar en cache
        availabilityCache.put(cacheKey, available);
        return available;
    }

    // Obtener todas las reservas - CON CACHE
    public List<Reservation> getAllReservations() {
        if (reservationCache.size() == reservationRepository.count()) {
            return new ArrayList<>(reservationCache.values());
        }

        // Si el cache no está completo, cargar desde BD y actualizar cache
        List<Reservation> reservations = reservationRepository.findAll();
        reservationCache.clear();
        reservations.forEach(res -> reservationCache.put(res.getId(), res));
        return reservations;
    }

    // Obtener reserva por ID - CON CACHE
    public Optional<Reservation> getReservationById(Long id) {
        // Buscar en cache primero
        Reservation cached = reservationCache.get(id);
        if (cached != null) {
            return Optional.of(cached);
        }

        // Si no está en cache, buscar en BD
        Optional<Reservation> reservation = reservationRepository.findById(id);
        reservation.ifPresent(res -> reservationCache.put(res.getId(), res));
        return reservation;
    }

    // Obtener reservas por usuario - OPTIMIZADO
    public List<Reservation> getReservationsByUser(String userEmail) {
        return reservationRepository.findByUserEmail(userEmail);
    }

    // Actualizar estado de reserva
    public Reservation updateReservationStatus(Long id, ReservationStatus status) {
        Optional<Reservation> reservationOpt = getReservationById(id); // Usa cache
        if (reservationOpt.isPresent()) {
            Reservation reservation = reservationOpt.get();
            reservation.setStatus(status);
            Reservation updated = reservationRepository.save(reservation);
            // Actualizar cache
            reservationCache.put(updated.getId(), updated);
            availabilityCache.clear(); // Limpiar cache de disponibilidad
            return updated;
        }
        throw new RuntimeException("Reserva no encontrada");
    }

    // Cancelar reserva
    public Reservation cancelReservation(Long id) {
        return updateReservationStatus(id, ReservationStatus.CANCELLED);
    }

    // Método asíncrono para verificación de disponibilidad
    @Async
    public CompletableFuture<Boolean> checkAvailabilityAsync(Integer vehicleId, LocalDateTime dateTime) {
        return CompletableFuture.completedFuture(isVehicleAvailable(vehicleId, dateTime));
    }
}