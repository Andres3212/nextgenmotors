package org.example.nextgenmotors2.frontend.reactive;

import org.example.nextgenmotors2.backend.model.entity.Vehicle;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public class VehicleDataStream {
    private final SubmissionPublisher<List<Vehicle>> publisher;
    private final List<Vehicle> currentVehicles;
    private Timer updateTimer;

    public VehicleDataStream() {
        this.publisher = new SubmissionPublisher<>();
        this.currentVehicles = new ArrayList<>();
    }

    public void startStream() {
        // Simular actualizaciones periódicas de datos
        updateTimer = new Timer(5000, e -> publishUpdate()); // Cada 5 segundos
        updateTimer.start();
    }

    public void stopStream() {
        if (updateTimer != null) {
            updateTimer.stop();
        }
        publisher.close();
    }

    public void updateVehicles(List<Vehicle> newVehicles) {
        this.currentVehicles.clear();
        this.currentVehicles.addAll(newVehicles);
        publishUpdate();
    }

    private void publishUpdate() {
        publisher.submit(new ArrayList<>(currentVehicles));
    }

    public void subscribe(Flow.Subscriber<List<Vehicle>> subscriber) {
        publisher.subscribe(subscriber);
    }
}