package org.example.nextgenmotors2.backend.service.reactive;

import org.example.nextgenmotors2.backend.model.entity.Reservation;
import org.example.nextgenmotors2.backend.functional.monad.OptionalResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface ReactiveReservationService {
    Mono<OptionalResult<Reservation>> createReservationReactive(Reservation reservation);
    Flux<Reservation> getReservationsStream();
    Mono<Boolean> checkAvailabilityReactive(Integer vehicleId, LocalDateTime dateTime);
    Flux<Reservation> getReservationsByUserReactive(String userEmail);
    Mono<OptionalResult<Reservation>> updateReservationStatusReactive(Long id, String status);
}