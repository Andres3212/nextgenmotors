package org.example.nextgenmotors2.backend.model.entity;

import java.util.List;
import java.util.Map;

public class Vehicle {
    private int id;
    private String brand;
    private String model;
    private int year;
    private double price;
    private List<String> features;
    private String description;
    private Map<String, String> specs;
    private String imagenUrl;

    // Constructor completo
    public Vehicle(int id, String brand, String model, int year, double price,
                   List<String> features, String description,
                   Map<String, String> specs, String imagenUrl) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.features = features;
        this.description = description;
        this.specs = specs;
        this.imagenUrl = imagenUrl;
    }

    // Constructor vacío para JPA
    public Vehicle() {
    }

    // Getters
    public int getId() { return id; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public double getPrice() { return price; }
    public List<String> getFeatures() { return features; }
    public String getDescription() { return description; }
    public Map<String, String> getSpecs() { return specs; }
    public String getImagenUrl() { return imagenUrl; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setBrand(String brand) { this.brand = brand; }
    public void setModel(String model) { this.model = model; }
    public void setYear(int year) { this.year = year; }
    public void setPrice(double price) { this.price = price; }
    public void setFeatures(List<String> features) { this.features = features; }
    public void setDescription(String description) { this.description = description; }
    public void setSpecs(Map<String, String> specs) { this.specs = specs; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    @Override
    public String toString() {
        return brand + " " + model + " " + year;
    }
}