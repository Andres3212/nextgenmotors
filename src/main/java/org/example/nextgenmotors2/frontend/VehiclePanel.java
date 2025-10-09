package org.example.nextgenmotors2.frontend;

import org.example.nextgenmotors2.backend.Vehicle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;

public class VehiclePanel extends JPanel {
    private Vehicle vehicle;
    private ActionListener detailsListener;
    private ActionListener testDriveListener;

    public VehiclePanel(Vehicle vehicle, ActionListener detailsListener, ActionListener testDriveListener) {
        this.vehicle = vehicle;
        this.detailsListener = detailsListener;
        this.testDriveListener = testDriveListener;
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createRaisedBevelBorder());
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(300, 400));

        // Panel superior con imagen
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(new Color(230, 230, 230));
        imagePanel.setPreferredSize(new Dimension(300, 150));

        JLabel imageLabel;

        if (vehicle.getImagenUrl() != null && !vehicle.getImagenUrl().isEmpty()) {
            try {
                URL url = new URL(vehicle.getImagenUrl());
                ImageIcon originalIcon = new ImageIcon(url);
                Image scaledImg = originalIcon.getImage().getScaledInstance(300, 150, Image.SCALE_SMOOTH);
                imageLabel = new JLabel(new ImageIcon(scaledImg));
            } catch (Exception e) {
                imageLabel = new JLabel("📷 " + vehicle.getBrand() + " " + vehicle.getModel(), JLabel.CENTER);
                imageLabel.setFont(new Font("Arial", Font.BOLD, 14));
            }
        } else {
            imageLabel = new JLabel("📷 " + vehicle.getBrand() + " " + vehicle.getModel(), JLabel.CENTER);
            imageLabel.setFont(new Font("Arial", Font.BOLD, 14));
        }

        imagePanel.add(imageLabel, BorderLayout.CENTER);

        // Precio
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        JLabel priceLabel = new JLabel(formatter.format(vehicle.getPrice()));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 12));
        priceLabel.setForeground(Color.WHITE);
        priceLabel.setOpaque(true);
        priceLabel.setBackground(new Color(59, 130, 246));
        priceLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pricePanel.setOpaque(false);
        pricePanel.add(priceLabel);
        imagePanel.add(pricePanel, BorderLayout.NORTH);

        add(imagePanel, BorderLayout.NORTH);

        // Panel info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        infoPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(vehicle.getBrand() + " " + vehicle.getModel() + " " + vehicle.getYear());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(titleLabel);

        infoPanel.add(Box.createVerticalStrut(10));

        JTextArea descArea = new JTextArea(vehicle.getDescription());
        descArea.setWrapStyleWord(true);
        descArea.setLineWrap(true);
        descArea.setEditable(false);
        descArea.setOpaque(false);
        descArea.setFont(new Font("Arial", Font.PLAIN, 12));
        descArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(descArea);

        infoPanel.add(Box.createVerticalStrut(10));

        JPanel featuresPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        featuresPanel.setBackground(Color.WHITE);
        featuresPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (String feature : vehicle.getFeatures()) {
            JLabel featureLabel = new JLabel(feature);
            featureLabel.setFont(new Font("Arial", Font.PLAIN, 10));
            featureLabel.setOpaque(true);
            featureLabel.setBackground(new Color(243, 244, 246));
            featureLabel.setForeground(new Color(55, 65, 81));
            featureLabel.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
            featuresPanel.add(featureLabel);
        }

        infoPanel.add(featuresPanel);
        infoPanel.add(Box.createVerticalStrut(15));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton detailsButton = new JButton("Ver Detalles");
        detailsButton.setBackground(new Color(59, 130, 246));
        detailsButton.setForeground(Color.WHITE);
        detailsButton.setFocusPainted(false);
        detailsButton.setBorderPainted(false);
        detailsButton.setPreferredSize(new Dimension(120, 35));
        detailsButton.addActionListener(e -> detailsListener.actionPerformed(
                new java.awt.event.ActionEvent(this, 0, String.valueOf(vehicle.getId()))));

        JButton testButton = new JButton("Prueba");
        testButton.setBackground(new Color(16, 185, 129));
        testButton.setForeground(Color.WHITE);
        testButton.setFocusPainted(false);
        testButton.setBorderPainted(false);
        testButton.setPreferredSize(new Dimension(80, 35));
        testButton.addActionListener(e -> testDriveListener.actionPerformed(
                new java.awt.event.ActionEvent(this, 0, String.valueOf(vehicle.getId()))));

        buttonPanel.add(detailsButton);
        buttonPanel.add(testButton);

        infoPanel.add(buttonPanel);

        add(infoPanel, BorderLayout.CENTER);
    }

    public Vehicle getVehicle() {
        return vehicle;
    }
}