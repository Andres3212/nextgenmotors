package org.example.nextgenmotors2.backend.distributed.cache;

import org.example.nextgenmotors2.backend.model.entity.Vehicle;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class VehicleCacheManager {

    @Cacheable(value = "vehicles", key = "#id")
    public Optional<Vehicle> getVehicleById(Integer id) {
        // Este método será cacheado automáticamente
        return Optional.empty(); // La implementación real irá en el servicio
    }

    @Cacheable(value = "vehicles", key = "'all'")
    public List<Vehicle> getAllVehicles() {
        // Cache para toda la lista de vehículos
        return List.of();
    }

    @Cacheable(value = "availability", key = "{#vehicleId, #dateTime}")
    public Boolean checkAvailability(Integer vehicleId, java.time.LocalDateTime dateTime) {
        // Cache para verificaciones de disponibilidad
        return null;
    }
}