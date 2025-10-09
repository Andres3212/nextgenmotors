package org.example.nextgenmotors2.frontend;

import org.example.nextgenmotors2.backend.Vehicle;
import org.example.nextgenmotors2.backend.Reservation;
import org.example.nextgenmotors2.backend.ReservationType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ReservationDialog extends JDialog {
    private Vehicle vehicle;
    private ReservationType reservationType;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JSpinner dateSpinner;
    private JSpinner timeSpinner;

    private static final String API_BASE_URL = "http://localhost:8080/api/reservations";
    private HttpClient httpClient;
    private ObjectMapper objectMapper;

    public ReservationDialog(JFrame parent, Vehicle vehicle, ReservationType type) {
        super(parent, "Reservar Vehículo", true);
        this.vehicle = vehicle;
        this.reservationType = type;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());

        initializeComponents();
        setSize(500, 400);
        setLocationRelativeTo(parent);
    }

    private void initializeComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Título
        String title = reservationType == ReservationType.TEST_DRIVE ?
                "Agendar Prueba de Manejo" : "Reservar Vehículo para Compra";
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Información del vehículo
        JPanel vehiclePanel = new JPanel(new BorderLayout());
        vehiclePanel.setBackground(new Color(240, 240, 240));
        vehiclePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel vehicleInfo = new JLabel(vehicle.getBrand() + " " + vehicle.getModel() + " " + vehicle.getYear());
        vehicleInfo.setFont(new Font("Arial", Font.BOLD, 14));
        vehiclePanel.add(vehicleInfo, BorderLayout.CENTER);

        mainPanel.add(vehiclePanel, BorderLayout.NORTH);

        // Formulario
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);

        // Campos del formulario
        formPanel.add(new JLabel("Nombre completo:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("Teléfono:"));
        phoneField = new JTextField();
        formPanel.add(phoneField);

        formPanel.add(new JLabel("Fecha:"));
        // Configurar spinner de fecha (solo días futuros)
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1); // Mañana como fecha mínima
        Date minDate = calendar.getTime();
        calendar.add(Calendar.MONTH, 3); // 3 meses en el futuro como fecha máxima
        Date maxDate = calendar.getTime();

        SpinnerDateModel dateModel = new SpinnerDateModel(minDate, minDate, maxDate, Calendar.DAY_OF_MONTH);
        dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
        dateSpinner.setEditor(dateEditor);
        formPanel.add(dateSpinner);

        formPanel.add(new JLabel("Hora:"));
        // Configurar spinner de hora (horario laboral 9:00 - 18:00)
        Calendar timeCalendar = Calendar.getInstance();
        timeCalendar.set(Calendar.HOUR_OF_DAY, 9);
        timeCalendar.set(Calendar.MINUTE, 0);
        Date minTime = timeCalendar.getTime();

        timeCalendar.set(Calendar.HOUR_OF_DAY, 18);
        Date maxTime = timeCalendar.getTime();

        SpinnerDateModel timeModel = new SpinnerDateModel(minTime, minTime, maxTime, Calendar.HOUR_OF_DAY);
        timeSpinner = new JSpinner(timeModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        formPanel.add(timeSpinner);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton reserveButton = new JButton("Confirmar Reserva");
        reserveButton.setBackground(new Color(59, 130, 246));
        reserveButton.setForeground(Color.WHITE);
        reserveButton.setFocusPainted(false);
        reserveButton.setBorderPainted(false);
        reserveButton.setPreferredSize(new Dimension(150, 35));
        reserveButton.addActionListener(this::confirmReservation);

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.setBackground(new Color(107, 114, 128));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorderPainted(false);
        cancelButton.setPreferredSize(new Dimension(100, 35));
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(reserveButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void confirmReservation(ActionEvent e) {
        try {
            // Validar campos
            if (nameField.getText().trim().isEmpty() ||
                    emailField.getText().trim().isEmpty() ||
                    phoneField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Por favor complete todos los campos",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar email
            String email = emailField.getText().trim();
            if (!email.contains("@") || !email.contains(".")) {
                JOptionPane.showMessageDialog(this,
                        "Por favor ingrese un email válido",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Crear objeto Reservation
            Date selectedDate = (Date) dateSpinner.getValue();
            Date selectedTime = (Date) timeSpinner.getValue();

            // Combinar fecha y hora
            Calendar dateCalendar = Calendar.getInstance();
            dateCalendar.setTime(selectedDate);

            Calendar timeCalendar = Calendar.getInstance();
            timeCalendar.setTime(selectedTime);

            Calendar combinedCalendar = Calendar.getInstance();
            combinedCalendar.set(
                    dateCalendar.get(Calendar.YEAR),
                    dateCalendar.get(Calendar.MONTH),
                    dateCalendar.get(Calendar.DAY_OF_MONTH),
                    timeCalendar.get(Calendar.HOUR_OF_DAY),
                    timeCalendar.get(Calendar.MINUTE),
                    0
            );

            LocalDateTime reservationDateTime = combinedCalendar.getTime()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            Reservation reservation = new Reservation(
                    email,
                    nameField.getText().trim(),
                    phoneField.getText().trim(),
                    vehicle.getId(),
                    vehicle.getBrand(),
                    vehicle.getModel(),
                    reservationDateTime,
                    reservationType
            );

            // Enviar a la API
            String json = objectMapper.writeValueAsString(reservation);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String message = reservationType == ReservationType.TEST_DRIVE ?
                        "¡Prueba de manejo agendada exitosamente! Te contactaremos para confirmar." :
                        "¡Vehículo reservado exitosamente! Un asesor se comunicará contigo.";

                JOptionPane.showMessageDialog(this, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al realizar la reserva: " + response.body(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}