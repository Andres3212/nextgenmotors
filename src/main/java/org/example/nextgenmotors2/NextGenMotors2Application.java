package org.example.nextgenmotors2;


import org.example.nextgenmotors2.frontend.view.NextGenMotors;
import org.example.nextgenmotors2.frontend.view.SplashScreen;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;

@SpringBootApplication
public class NextGenMotors2Application implements ApplicationRunner {

    public static void main(String[] args) {
        // Configurar el look and feel del sistema antes de iniciar Spring Boot
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        SpringApplication.run(NextGenMotors2Application.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Mostrar SplashScreen y luego la aplicación principal
        SplashScreen splash = new SplashScreen(() -> {
            NextGenMotors app = new NextGenMotors();
            app.setVisible(true);
        });
        splash.showSplash();
    }
}