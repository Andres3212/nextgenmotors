package org.example.nextgenmotors2.backend.service.reactive;

import org.example.nextgenmotors2.backend.model.entity.Reservation;
import org.example.nextgenmotors2.backend.model.enu.ReservationStatus;
import org.example.nextgenmotors2.backend.functional.monad.OptionalResult;
import org.example.nextgenmotors2.backend.repository.ReservationRepository;
import org.example.nextgenmotors2.backend.concurrent.AsyncReservationProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class ReactiveReservationServiceImpl implements ReactiveReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private AsyncReservationProcessor asyncProcessor;

    @Override
    public Mono<OptionalResult<Reservation>> createReservationReactive(Reservation reservation) {
        return Mono.fromCallable(() -> reservation)
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(res -> checkAvailabilityReactive(res.getVehicleId(), res.getReservationDate())
                        .flatMap(available -> {
                            if (available) {
                                return Mono.fromFuture(asyncProcessor.processReservationAsync(res))
                                        .map(OptionalResult::success);
                            } else {
                                return Mono.just(OptionalResult.<Reservation>failure(
                                        "Vehículo no disponible en la fecha seleccionada"));
                            }
                        })
                )
                .timeout(Duration.ofSeconds(5))
                .onErrorReturn(OptionalResult.failure("Timeout en la creación de reserva"));
    }

    @Override
    public Flux<Reservation> getReservationsStream() {
        return Flux.fromIterable(reservationRepository.findAll())
                .delayElements(Duration.ofMillis(100))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Boolean> checkAvailabilityReactive(Integer vehicleId, LocalDateTime dateTime) {
        return Mono.fromFuture(asyncProcessor.validateAvailabilityAsync(vehicleId, dateTime))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Flux<Reservation> getReservationsByUserReactive(String userEmail) {
        return Flux.fromIterable(reservationRepository.findByUserEmail(userEmail))
                .subscribeOn(Schedulers.boundedElastic())
                .filter(reservation -> !reservation.getStatus().equals(ReservationStatus.CANCELLED));
    }

    @Override
    public Mono<OptionalResult<Reservation>> updateReservationStatusReactive(Long id, String status) {
        return Mono.fromCallable(() -> reservationRepository.findById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(optionalReservation -> {
                    if (optionalReservation.isPresent()) {
                        Reservation reservation = optionalReservation.get();
                        reservation.setStatus(ReservationStatus.valueOf(status));
                        return Mono.fromCallable(() -> reservationRepository.save(reservation))
                                .map(OptionalResult::success);
                    } else {
                        return Mono.just(OptionalResult.<Reservation>failure("Reserva no encontrada"));
                    }
                });
    }
}