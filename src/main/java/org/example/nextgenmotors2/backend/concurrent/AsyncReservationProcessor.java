package org.example.nextgenmotors2.backend.concurrent;

import org.example.nextgenmotors2.backend.model.entity.Reservation;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class AsyncReservationProcessor {
    private final ConcurrentHashMap<Long, ReentrantLock> reservationLocks;

    public AsyncReservationProcessor() {
        this.reservationLocks = new ConcurrentHashMap<>();
    }

    @Async("taskExecutor")
    public CompletableFuture<Reservation> processReservationAsync(Reservation reservation) {
        Long reservationId = reservation.getId();
        ReentrantLock lock = reservationLocks.computeIfAbsent(reservationId, k -> new ReentrantLock());

        try {
            lock.lock();
            // Simular procesamiento asíncrono
            Thread.sleep(100);
            return CompletableFuture.completedFuture(reservation);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return CompletableFuture.failedFuture(e);
        } finally {
            lock.unlock();
            reservationLocks.remove(reservationId);
        }
    }

    @Async("taskExecutor")
    public CompletableFuture<Boolean> validateAvailabilityAsync(Integer vehicleId, java.time.LocalDateTime dateTime) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(50); // Simular validación
                return Math.random() > 0.1; // 90% de disponibilidad
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        });
    }
}