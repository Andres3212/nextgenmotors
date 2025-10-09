package org.example.nextgenmotors2.frontend;

import org.example.nextgenmotors2.backend.Vehicle;
import org.example.nextgenmotors2.backend.VehicleDatabase;
import org.example.nextgenmotors2.backend.ReservationType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class NextGenMotors extends JFrame {
    private VehicleDatabase database;
    private JTextField searchField;
    private JComboBox<String> priceFilter;
    private JPanel vehicleContainer;
    private JScrollPane mainScrollPane;

    public NextGenMotors() {
        setTitle("NextGen Motors");
        setSize(850, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        database = new VehicleDatabase();
        initializeComponents();
        loadVehicles(database.getAllVehicles());
    }

    private void initializeComponents() {
        setTitle("NextGenMotors - Concesionario Premium");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel searchPanel = createSearchPanel();
        mainPanel.add(searchPanel, BorderLayout.CENTER);

        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        mainScrollPane = new JScrollPane(mainPanel);
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        setContentPane(mainScrollPane);

        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel("NextGenMotors");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(59, 130, 246));

        JLabel subtitleLabel = new JLabel("Concesionario Premium");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(107, 114, 128));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createHorizontalStrut(10));
        titlePanel.add(subtitleLabel);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        JPanel heroPanel = new JPanel();
        heroPanel.setLayout(new BoxLayout(heroPanel, BoxLayout.Y_AXIS));
        heroPanel.setBackground(new Color(59, 130, 246));
        heroPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel heroTitle = new JLabel("Encuentra tu Auto Ideal");
        heroTitle.setFont(new Font("Arial", Font.BOLD, 32));
        heroTitle.setForeground(Color.WHITE);
        heroTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel heroSubtitle = new JLabel("Los mejores vehículos con la mejor atención al cliente");
        heroSubtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        heroSubtitle.setForeground(Color.WHITE);
        heroSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        heroPanel.add(heroTitle);
        heroPanel.add(Box.createVerticalStrut(10));
        heroPanel.add(heroSubtitle);

        JPanel heroContainer = new JPanel(new BorderLayout());
        heroContainer.add(headerPanel, BorderLayout.NORTH);
        heroContainer.add(heroPanel, BorderLayout.CENTER);

        return heroContainer;
    }

    private JPanel createSearchPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(249, 250, 251));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel searchContainer = new JPanel(new BorderLayout());
        searchContainer.setBackground(Color.WHITE);
        searchContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel catalogTitle = new JLabel("Nuestro Catálogo");
        catalogTitle.setFont(new Font("Arial", Font.BOLD, 24));
        catalogTitle.setHorizontalAlignment(SwingConstants.CENTER);
        catalogTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel filtersPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        filtersPanel.setBackground(Color.WHITE);

        JPanel searchFieldPanel = new JPanel(new BorderLayout());
        searchFieldPanel.setBackground(Color.WHITE);
        JLabel searchLabel = new JLabel("Buscar vehículo");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 12));
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(0, 35));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        searchFieldPanel.add(searchLabel, BorderLayout.NORTH);
        searchFieldPanel.add(Box.createVerticalStrut(5), BorderLayout.CENTER);
        searchFieldPanel.add(searchField, BorderLayout.SOUTH);

        JPanel pricePanel = new JPanel(new BorderLayout());
        pricePanel.setBackground(Color.WHITE);
        JLabel priceLabel = new JLabel("Filtrar por precio");
        priceLabel.setFont(new Font("Arial", Font.BOLD, 12));

        String[] priceRanges = {
                "Todos los precios",
                "$0 - $20,000",
                "$20,000 - $40,000",
                "$40,000 - $60,000",
                "$60,000+"
        };
        priceFilter = new JComboBox<>(priceRanges);
        priceFilter.setPreferredSize(new Dimension(0, 35));

        pricePanel.add(priceLabel, BorderLayout.NORTH);
        pricePanel.add(Box.createVerticalStrut(5), BorderLayout.CENTER);
        pricePanel.add(priceFilter, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);

        JButton clearButton = new JButton("Limpiar Filtros");
        clearButton.setBackground(new Color(107, 114, 128));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        clearButton.setBorderPainted(false);
        clearButton.setPreferredSize(new Dimension(120, 35));
        clearButton.addActionListener(e -> clearFilters());

        buttonPanel.add(clearButton);

        filtersPanel.add(searchFieldPanel);
        filtersPanel.add(pricePanel);
        filtersPanel.add(buttonPanel);

        searchContainer.add(catalogTitle, BorderLayout.NORTH);
        searchContainer.add(filtersPanel, BorderLayout.CENTER);

        mainPanel.add(searchContainer, BorderLayout.NORTH);

        vehicleContainer = new JPanel();
        vehicleContainer.setLayout(new GridLayout(0, 3, 20, 20));
        vehicleContainer.setBackground(new Color(249, 250, 251));

        mainPanel.add(vehicleContainer, BorderLayout.CENTER);

        searchField.addActionListener(e -> performSearch());
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { performSearch(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { performSearch(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { performSearch(); }
        });

        priceFilter.addActionListener(e -> performSearch());

        return mainPanel;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new GridLayout(1, 3, 40, 0));
        footerPanel.setBackground(new Color(55, 65, 81));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JPanel companyPanel = new JPanel();
        companyPanel.setLayout(new BoxLayout(companyPanel, BoxLayout.Y_AXIS));
        companyPanel.setBackground(new Color(55, 65, 81));

        JLabel companyTitle = new JLabel("NextGenMotors");
        companyTitle.setFont(new Font("Arial", Font.BOLD, 18));
        companyTitle.setForeground(Color.WHITE);
        companyTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea companyDesc = new JTextArea("Tu concesionario de confianza con los mejores vehículos del mercado.");
        companyDesc.setWrapStyleWord(true);
        companyDesc.setLineWrap(true);
        companyDesc.setEditable(false);
        companyDesc.setOpaque(false);
        companyDesc.setForeground(new Color(209, 213, 219));
        companyDesc.setFont(new Font("Arial", Font.PLAIN, 12));
        companyDesc.setAlignmentX(Component.LEFT_ALIGNMENT);

        companyPanel.add(companyTitle);
        companyPanel.add(Box.createVerticalStrut(10));
        companyPanel.add(companyDesc);

        JPanel contactPanel = new JPanel();
        contactPanel.setLayout(new BoxLayout(contactPanel, BoxLayout.Y_AXIS));
        contactPanel.setBackground(new Color(55, 65, 81));

        JLabel contactTitle = new JLabel("Contacto");
        contactTitle.setFont(new Font("Arial", Font.BOLD, 16));
        contactTitle.setForeground(Color.WHITE);
        contactTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel phone = new JLabel("📞 (555) 123-4567");
        phone.setForeground(new Color(209, 213, 219));
        phone.setFont(new Font("Arial", Font.PLAIN, 12));
        phone.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel email = new JLabel("📧 info@nextgenmotors.com");
        email.setForeground(new Color(209, 213, 219));
        email.setFont(new Font("Arial", Font.PLAIN, 12));
        email.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel address = new JLabel("📍 Av. Principal 123, Ciudad");
        address.setForeground(new Color(209, 213, 219));
        address.setFont(new Font("Arial", Font.PLAIN, 12));
        address.setAlignmentX(Component.LEFT_ALIGNMENT);

        contactPanel.add(contactTitle);
        contactPanel.add(Box.createVerticalStrut(10));
        contactPanel.add(phone);
        contactPanel.add(Box.createVerticalStrut(5));
        contactPanel.add(email);
        contactPanel.add(Box.createVerticalStrut(5));
        contactPanel.add(address);

        JPanel hoursPanel = new JPanel();
        hoursPanel.setLayout(new BoxLayout(hoursPanel, BoxLayout.Y_AXIS));
        hoursPanel.setBackground(new Color(55, 65, 81));

        JLabel hoursTitle = new JLabel("Horarios");
        hoursTitle.setFont(new Font("Arial", Font.BOLD, 16));
        hoursTitle.setForeground(Color.WHITE);
        hoursTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel weekdays = new JLabel("Lun - Vie: 9:00 AM - 7:00 PM");
        weekdays.setForeground(new Color(209, 213, 219));
        weekdays.setFont(new Font("Arial", Font.PLAIN, 12));
        weekdays.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel saturday = new JLabel("Sáb: 9:00 AM - 5:00 PM");
        saturday.setForeground(new Color(209, 213, 219));
        saturday.setFont(new Font("Arial", Font.PLAIN, 12));
        saturday.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sunday = new JLabel("Dom: Cerrado");
        sunday.setForeground(new Color(209, 213, 219));
        sunday.setFont(new Font("Arial", Font.PLAIN, 12));
        sunday.setAlignmentX(Component.LEFT_ALIGNMENT);

        hoursPanel.add(hoursTitle);
        hoursPanel.add(Box.createVerticalStrut(10));
        hoursPanel.add(weekdays);
        hoursPanel.add(Box.createVerticalStrut(5));
        hoursPanel.add(saturday);
        hoursPanel.add(Box.createVerticalStrut(5));
        hoursPanel.add(sunday);

        footerPanel.add(companyPanel);
        footerPanel.add(contactPanel);
        footerPanel.add(hoursPanel);

        return footerPanel;
    }

    private void loadVehicles(List<Vehicle> vehicles) {
        vehicleContainer.removeAll();

        if (vehicles.isEmpty()) {
            JPanel noResultsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            noResultsPanel.setBackground(new Color(249, 250, 251));

            JLabel noResultsLabel = new JLabel("No se encontraron vehículos que coincidan con tu búsqueda.");
            noResultsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            noResultsLabel.setForeground(new Color(107, 114, 128));

            noResultsPanel.add(noResultsLabel);
            vehicleContainer.add(noResultsPanel);
        } else {
            for (Vehicle vehicle : vehicles) {
                VehiclePanel panel = new VehiclePanel(
                        vehicle,
                        this::showVehicleDetails,
                        this::scheduleQuickTestDrive
                );
                vehicleContainer.add(panel);
            }
        }

        vehicleContainer.revalidate();
        vehicleContainer.repaint();
    }

    private void performSearch() {
        String searchTerm = searchField.getText().trim();
        String selectedPriceRange = (String) priceFilter.getSelectedItem();

        List<Vehicle> filtered = database.searchVehicles(searchTerm, selectedPriceRange);
        loadVehicles(filtered);
    }

    private void clearFilters() {
        searchField.setText("");
        priceFilter.setSelectedIndex(0);
        loadVehicles(database.getAllVehicles());
    }

    private void showVehicleDetails(ActionEvent e) {
        try {
            int vehicleId = Integer.parseInt(e.getActionCommand());
            Vehicle vehicle = database.getVehicleById(vehicleId);

            if (vehicle != null) {
                VehicleDetailsDialog dialog = new VehicleDetailsDialog(this, vehicle);
                dialog.setVisible(true);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar los detalles del vehículo.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void scheduleQuickTestDrive(ActionEvent e) {
        try {
            int vehicleId = Integer.parseInt(e.getActionCommand());
            Vehicle vehicle = database.getVehicleById(vehicleId);

            if (vehicle != null) {
                ReservationDialog dialog = new ReservationDialog(this, vehicle, ReservationType.TEST_DRIVE);
                dialog.setVisible(true);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error al procesar la solicitud.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para reservar vehículo (puede ser usado en el futuro)
    private void reserveVehicle(ActionEvent e) {
        try {
            int vehicleId = Integer.parseInt(e.getActionCommand());
            Vehicle vehicle = database.getVehicleById(vehicleId);

            if (vehicle != null) {
                ReservationDialog dialog = new ReservationDialog(this, vehicle, ReservationType.RESERVATION);
                dialog.setVisible(true);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error al procesar la solicitud.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}