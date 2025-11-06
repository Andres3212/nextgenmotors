package org.example.nextgenmotors2.frontend.view;

import org.example.nextgenmotors2.backend.model.entity.Vehicle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

public class VehiclePanel extends JPanel {
    private Vehicle vehicle;
    private ActionListener detailsListener;
    private ActionListener testDriveListener;
    private static final ConcurrentHashMap<String, ImageIcon> imageCache = new ConcurrentHashMap<>();
    private static final ImageIcon DEFAULT_IMAGE = createDefaultImage();

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

        // Cargar imagen de forma asíncrona
        loadImageAsync();

        // ... el resto del código se mantiene igual
        JPanel imagePanel = createImagePanel();
        JPanel infoPanel = createInfoPanel();

        add(imagePanel, BorderLayout.NORTH);
        add(infoPanel, BorderLayout.CENTER);
    }

    private JPanel createImagePanel() {
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(new Color(230, 230, 230));
        imagePanel.setPreferredSize(new Dimension(300, 150));

        JLabel imageLabel = getCachedImageLabel();
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        // Panel de precio
        JPanel pricePanel = createPricePanel();
        imagePanel.add(pricePanel, BorderLayout.NORTH);

        return imagePanel;
    }

    private JLabel getCachedImageLabel() {
        String imageUrl = vehicle.getImagenUrl();
        ImageIcon imageIcon = imageCache.get(imageUrl);

        if (imageIcon == null) {
            // Usar imagen por defecto mientras carga
            imageIcon = DEFAULT_IMAGE;
            // Cargar en segundo plano
            loadImageInBackground(imageUrl);
        }

        return new JLabel(imageIcon);
    }

    private void loadImageInBackground(String imageUrl) {
        SwingWorker<ImageIcon, Void> worker = new SwingWorker<>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    try {
                        URL url = new URL(imageUrl);
                        ImageIcon originalIcon = new ImageIcon(url);
                        Image scaledImg = originalIcon.getImage().getScaledInstance(300, 150, Image.SCALE_SMOOTH);
                        return new ImageIcon(scaledImg);
                    } catch (Exception e) {
                        return DEFAULT_IMAGE;
                    }
                }
                return DEFAULT_IMAGE;
            }

            @Override
            protected void done() {
                try {
                    ImageIcon loadedIcon = get();
                    imageCache.put(imageUrl, loadedIcon);
                    // Actualizar la UI
                    repaint();
                } catch (Exception e) {
                    imageCache.put(imageUrl, DEFAULT_IMAGE);
                }
            }
        };
        worker.execute();
    }

    private void loadImageAsync() {
        // Precargar imagen en cache si no existe
        if (!imageCache.containsKey(vehicle.getImagenUrl())) {
            loadImageInBackground(vehicle.getImagenUrl());
        }
    }

    private JPanel createPricePanel() {
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
        return pricePanel;
    }

    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        infoPanel.setBackground(Color.WHITE);

        // Título
        JLabel titleLabel = new JLabel(vehicle.getBrand() + " " + vehicle.getModel() + " " + vehicle.getYear());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(titleLabel);

        infoPanel.add(Box.createVerticalStrut(10));

        // Descripción
        JTextArea descArea = new JTextArea(vehicle.getDescription());
        descArea.setWrapStyleWord(true);
        descArea.setLineWrap(true);
        descArea.setEditable(false);
        descArea.setOpaque(false);
        descArea.setFont(new Font("Arial", Font.PLAIN, 12));
        descArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(descArea);

        infoPanel.add(Box.createVerticalStrut(10));

        // Características
        JPanel featuresPanel = createFeaturesPanel();
        infoPanel.add(featuresPanel);

        infoPanel.add(Box.createVerticalStrut(15));

        // Botones
        JPanel buttonPanel = createButtonPanel();
        infoPanel.add(buttonPanel);

        return infoPanel;
    }

    private JPanel createFeaturesPanel() {
        JPanel featuresPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        featuresPanel.setBackground(Color.WHITE);
        featuresPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Mostrar solo las primeras 3 características para mejor rendimiento
        vehicle.getFeatures().stream()
                .limit(3)
                .forEach(feature -> {
                    JLabel featureLabel = new JLabel(feature);
                    featureLabel.setFont(new Font("Arial", Font.PLAIN, 10));
                    featureLabel.setOpaque(true);
                    featureLabel.setBackground(new Color(243, 244, 246));
                    featureLabel.setForeground(new Color(55, 65, 81));
                    featureLabel.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
                    featuresPanel.add(featureLabel);
                });

        return featuresPanel;
    }

    private JPanel createButtonPanel() {
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

        return buttonPanel;
    }

    private static ImageIcon createDefaultImage() {
        BufferedImage image = new BufferedImage(300, 150, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // Gradiente de fondo
        GradientPaint gp = new GradientPaint(0, 0, Color.LIGHT_GRAY, 300, 150, Color.GRAY);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, 300, 150);

        // Icono de auto
        g2d.setColor(Color.DARK_GRAY);
        g2d.setFont(new Font("Arial", Font.BOLD, 48));
        g2d.drawString("🚗", 120, 85);

        g2d.dispose();
        return new ImageIcon(image);
    }

    public Vehicle getVehicle() {
        return vehicle;
    }
}