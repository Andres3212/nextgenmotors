package org.example.nextgenmotors2.backend;

import java.util.*;

public class VehicleDatabase {
    private List<Vehicle> vehicles;

    public VehicleDatabase() {
        initializeVehicles();
    }

    private void initializeVehicles() {
        vehicles = new ArrayList<>();

        // Toyota Camry
        Map<String, String> camrySpecs = new HashMap<>();
        camrySpecs.put("Motor", "2.5L 4-cilindros + Motor eléctrico");
        camrySpecs.put("Transmisión", "CVT Automática");
        camrySpecs.put("Combustible", "Híbrido");
        camrySpecs.put("Rendimiento", "52 MPG ciudad");

        vehicles.add(new Vehicle(1, "Toyota", "Camry", 2023, 28500,
                Arrays.asList("Automático", "4 puertas", "Híbrido", "Cámara trasera"),
                "Sedán confiable y eficiente en combustible con tecnología híbrida avanzada.",
                camrySpecs,
                "https://fuelcarmagazine.com/wp-content/uploads/2022/05/Toyota-Camry-2023.jpg"
        ));

        // Honda CR-V
        Map<String, String> crvSpecs = new HashMap<>();
        crvSpecs.put("Motor", "1.5L Turbo 4-cilindros");
        crvSpecs.put("Transmisión", "CVT Automática");
        crvSpecs.put("Combustible", "Gasolina");
        crvSpecs.put("Rendimiento", "31 MPG combinado");

        vehicles.add(new Vehicle(2, "Honda", "CR-V", 2023, 32000,
                Arrays.asList("AWD", "SUV", "Turbo", "Honda Sensing"),
                "SUV compacta perfecta para familias con excelente espacio y seguridad.",
                crvSpecs,
                "https://revistaelconocedor.net/wp-content/uploads/2023/03/honda-crv-2023-1.jpg"
        ));

        // BMW X3
        Map<String, String> x3Specs = new HashMap<>();
        x3Specs.put("Motor", "2.0L TwinPower Turbo");
        x3Specs.put("Transmisión", "8-velocidades automática");
        x3Specs.put("Combustible", "Gasolina Premium");
        x3Specs.put("Rendimiento", "26 MPG combinado");

        vehicles.add(new Vehicle(3, "BMW", "X3", 2023, 45000,
                Arrays.asList("xDrive", "Cuero", "Navegación", "Techo panorámico"),
                "SUV de lujo con rendimiento deportivo y tecnología premium.",
                x3Specs,
                "https://images.topgear.com.ph/topgear/images/2022/08/15/bmw-x3-2023-ph-launch-02-1660558853.jpg"
        ));

        // Tesla Model 3
        Map<String, String> model3Specs = new HashMap<>();
        model3Specs.put("Motor", "Motor eléctrico dual");
        model3Specs.put("Transmisión", "Automática de velocidad única");
        model3Specs.put("Combustible", "100% Eléctrico");
        model3Specs.put("Rendimiento", "358 millas de autonomía");

        vehicles.add(new Vehicle(4, "Tesla", "Model 3", 2023, 42000,
                Arrays.asList("Eléctrico", "Autopilot", "Supercargador", "OTA Updates"),
                "Sedán eléctrico con tecnología autónoma y cero emisiones.",
                model3Specs,
                "https://fuelcarmagazine.com/wp-content/uploads/2023/09/Tesla-Model-3-2024-actualizacion.jpg"
        ));

        // Ford F-150
        Map<String, String> f150Specs = new HashMap<>();
        f150Specs.put("Motor", "3.5L V6 EcoBoost");
        f150Specs.put("Transmisión", "10-velocidades automática");
        f150Specs.put("Combustible", "Gasolina");
        f150Specs.put("Rendimiento", "24 MPG carretera");

        vehicles.add(new Vehicle(5, "Ford", "F-150", 2023, 38000,
                Arrays.asList("4x4", "Pickup", "Remolque", "SYNC 4"),
                "La pickup más vendida de América con capacidad de trabajo superior.",
                f150Specs,
                "https://loscoches.com/wp-content/uploads/2020/01/gran-tecnologia-ford-f150-segura.jpg"
        ));

        // Audi A4
        Map<String, String> a4Specs = new HashMap<>();
        a4Specs.put("Motor", "2.0L TFSI Turbo");
        a4Specs.put("Transmisión", "7-velocidades S tronic");
        a4Specs.put("Combustible", "Gasolina Premium");
        a4Specs.put("Rendimiento", "31 MPG combinado");

        vehicles.add(new Vehicle(6, "Audi", "A4", 2023, 39000,
                Arrays.asList("Quattro", "Virtual Cockpit", "Bang & Olufsen", "Asientos deportivos"),
                "Sedán de lujo alemán con diseño elegante y tecnología avanzada.",
                a4Specs,
                "https://hips.hearstapps.com/hmg-prod/images/medium-6802-audia4-1654633069.jpg?crop=1.00xw:0.846xh;0,0.154xh&resize=1200:*"
        ));
    }

    public List<Vehicle> getAllVehicles() {
        return new ArrayList<>(vehicles);
    }

    public Vehicle getVehicleById(int id) {
        return vehicles.stream()
                .filter(v -> v.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Vehicle> searchVehicles(String searchTerm, String priceRange) {
        List<Vehicle> filtered = new ArrayList<>();

        for (Vehicle vehicle : vehicles) {
            boolean matchesSearch = searchTerm.isEmpty() ||
                    vehicle.getBrand().toLowerCase().contains(searchTerm.toLowerCase()) ||
                    vehicle.getModel().toLowerCase().contains(searchTerm.toLowerCase()) ||
                    vehicle.getFeatures().stream()
                            .anyMatch(f -> f.toLowerCase().contains(searchTerm.toLowerCase()));

            boolean matchesPrice = true;
            if (!priceRange.isEmpty() && !priceRange.equals("Todos los precios")) {
                matchesPrice = checkPriceRange(vehicle.getPrice(), priceRange);
            }

            if (matchesSearch && matchesPrice) {
                filtered.add(vehicle);
            }
        }

        return filtered;
    }

    private boolean checkPriceRange(double price, String range) {
        switch (range) {
            case "$0 - $20,000":
                return price <= 20000;
            case "$20,000 - $40,000":
                return price >= 20000 && price <= 40000;
            case "$40,000 - $60,000":
                return price >= 40000 && price <= 60000;
            case "$60,000+":
                return price >= 60000;
            default:
                return true;
        }
    }
}