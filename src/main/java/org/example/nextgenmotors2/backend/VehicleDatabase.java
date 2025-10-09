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
                "https://toyota.scene7.com/is/image/toyota/CAM_MY23_0016_V002?fmt=jpg&fit=crop&qlt=90&wid=1200&hei=600"
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
                "https://www.honda.ca/-/media/Brands/Honda/VehiclePages/CR-V/2023/Overview/CRV23-hero_desktop.jpg"
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
                "https://www.bmwusa.com/content/dam/bmwusa/XModels/X3/2023/Overview/BMW-MY23-X3-Overview-01.jpg"
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
                "https://tesla-cdn.thron.com/delivery/public/image/tesla/6c2b04c9-7f4f-40b0-86f8-1fc62e3ed95f/bvlatuR/std/1200x628/lhd-model-3-social"
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
                "https://www.ford.com/cmslibs/content/dam/brand_ford/en_us/brand/resources/general/newvehicle/highlights/F150_22MY_LariatSport.jpg"
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
                "https://www.audi.com/content/dam/ci/en/models/a4/a4-sedan/my-2023/1920x1080-desktop-stage/1920x1080-desktop-stage-a4.jpg"
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