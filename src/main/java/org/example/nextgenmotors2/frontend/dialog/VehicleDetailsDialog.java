package org.example.nextgenmotors2.frontend.dialog;

import org.example.nextgenmotors2.backend.model.entity.Vehicle;
import org.example.nextgenmotors2.backend.model.enu.ReservationType;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class VehicleDetailsDialog extends JDialog {
    private Vehicle vehicle;

    public VehicleDetailsDialog(JFrame parent, Vehicle vehicle) {
        super(parent, "Detalles del Vehículo", true);
        this.vehicle = vehicle;
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        setSize(600, 500);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Título
        JLabel titleLabel = new JLabel(vehicle.getBrand() + " " + vehicle.getModel() + " " + vehicle.getYear());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Panel central con información
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centerPanel.setBackground(Color.WHITE);

        // Panel izquierdo - imagen
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(new Color(230, 230, 230));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel imageLabel = new JLabel("🚗 " + vehicle.getBrand() + " " + vehicle.getModel(), JLabel.CENTER);
        imageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        leftPanel.add(imageLabel, BorderLayout.CENTER);

        centerPanel.add(leftPanel);

        // Panel derecho - información
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);

        // Precio
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        JLabel priceLabel = new JLabel(formatter.format(vehicle.getPrice()));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 20));
        priceLabel.setForeground(new Color(59, 130, 246));
        priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(priceLabel);

        rightPanel.add(Box.createVerticalStrut(15));

        // Descripción
        JTextArea descArea = new JTextArea(vehicle.getDescription());
        descArea.setWrapStyleWord(true);
        descArea.setLineWrap(true);
        descArea.setEditable(false);
        descArea.setOpaque(false);
        descArea.setFont(new Font("Arial", Font.PLAIN, 14));
        descArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(descArea);

        rightPanel.add(Box.createVerticalStrut(20));

        // Especificaciones
        JLabel specsTitle = new JLabel("Especificaciones:");
        specsTitle.setFont(new Font("Arial", Font.BOLD, 16));
        specsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(specsTitle);

        rightPanel.add(Box.createVerticalStrut(10));

        for (Map.Entry<String, String> spec : vehicle.getSpecs().entrySet()) {
            JPanel specPanel = new JPanel(new BorderLayout());
            specPanel.setBackground(Color.WHITE);
            specPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

            JLabel specLabel = new JLabel(spec.getKey() + ":");
            specLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            specLabel.setForeground(new Color(107, 114, 128));

            JLabel specValue = new JLabel(spec.getValue());
            specValue.setFont(new Font("Arial", Font.BOLD, 12));

            specPanel.add(specLabel, BorderLayout.WEST);
            specPanel.add(specValue, BorderLayout.EAST);

            rightPanel.add(specPanel);
            rightPanel.add(Box.createVerticalStrut(5));
        }

        rightPanel.add(Box.createVerticalStrut(15));

        // Características
        JLabel featuresTitle = new JLabel("Características:");
        featuresTitle.setFont(new Font("Arial", Font.BOLD, 16));
        featuresTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(featuresTitle);

        rightPanel.add(Box.createVerticalStrut(10));

        JPanel featuresPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        featuresPanel.setBackground(Color.WHITE);
        featuresPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (String feature : vehicle.getFeatures()) {
            JLabel featureLabel = new JLabel(feature);
            featureLabel.setFont(new Font("Arial", Font.PLAIN, 11));
            featureLabel.setOpaque(true);
            featureLabel.setBackground(new Color(243, 244, 246));
            featureLabel.setForeground(new Color(55, 65, 81));
            featureLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            featuresPanel.add(featureLabel);
        }

        rightPanel.add(featuresPanel);

        centerPanel.add(rightPanel);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton testDriveButton = new JButton("Agendar Prueba de Manejo");
        testDriveButton.setBackground(new Color(16, 185, 129));
        testDriveButton.setForeground(Color.WHITE);
        testDriveButton.setFocusPainted(false);
        testDriveButton.setBorderPainted(false);
        testDriveButton.setPreferredSize(new Dimension(200, 40));
        testDriveButton.addActionListener(e -> {
            // Usar el nuevo diálogo de reserva
            ReservationDialog reservationDialog = new ReservationDialog(
                    (JFrame) this.getParent(), vehicle, ReservationType.TEST_DRIVE);
            reservationDialog.setVisible(true);
            dispose();
        });

        JButton buyButton = new JButton("Reservar para Compra");
        buyButton.setBackground(new Color(59, 130, 246));
        buyButton.setForeground(Color.WHITE);
        buyButton.setFocusPainted(false);
        buyButton.setBorderPainted(false);
        buyButton.setPreferredSize(new Dimension(180, 40));
        buyButton.addActionListener(e -> {
            // Usar el nuevo diálogo de reserva para compra
            ReservationDialog reservationDialog = new ReservationDialog(
                    (JFrame) this.getParent(), vehicle, ReservationType.RESERVATION);
            reservationDialog.setVisible(true);
            dispose();
        });

        JButton closeButton = new JButton("Cerrar");
        closeButton.setBackground(new Color(107, 114, 128));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.setPreferredSize(new Dimension(100, 40));
        closeButton.addActionListener(e -> dispose());

        buttonPanel.add(testDriveButton);
        buttonPanel.add(buyButton);
        buttonPanel.add(closeButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }
}