package org.example.nextgenmotors2.backend.reactive;

import org.example.nextgenmotors2.backend.model.entity.Vehicle;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class VehicleFluxProcessor {
    private final Sinks.Many<Vehicle> vehicleSink;
    private final ConcurrentHashMap<Integer, Vehicle> vehicleCache;
    private final Flux<Vehicle> vehicleFlux;

    public VehicleFluxProcessor() {
        this.vehicleSink = Sinks.many().multicast().onBackpressureBuffer();
        this.vehicleCache = new ConcurrentHashMap<>();
        this.vehicleFlux = vehicleSink.asFlux()
                .cache(Duration.ofMinutes(10))
                .onBackpressureLatest();
    }

    @PostConstruct
    public void init() {
        // Procesamiento reactivo en background
        vehicleFlux
                .groupBy(Vehicle::getBrand)
                .subscribe(groupedFlux ->
                        groupedFlux
                                .sample(Duration.ofSeconds(1))
                                .subscribe(vehicle ->
                                        vehicleCache.put(vehicle.getId(), vehicle)
                                )
                );
    }

    public void emitVehicle(Vehicle vehicle) {
        vehicleSink.tryEmitNext(vehicle);
    }

    public Flux<Vehicle> getVehicleStream() {
        return vehicleFlux;
    }

    public Flux<Vehicle> getVehiclesByBrand(String brand) {
        return vehicleFlux.filter(vehicle -> brand.equals(vehicle.getBrand()));
    }
}