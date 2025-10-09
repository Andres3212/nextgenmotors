package org.example.nextgenmotors2.frontend;

import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JWindow {
    private JProgressBar progressBar;
    private Timer timer;
    private int progress = 0;
    private int carX = 0;

    public SplashScreen(Runnable onFinish) {
        JPanel content = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, Color.CYAN, getWidth(), getHeight(), Color.WHITE);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        JLabel title = new JLabel("NextGen Motors", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.BLUE);

        progressBar = new JProgressBar(0, 100) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int y = getY() - 25;
                g.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
                g.drawString("🚗", carX, y);
            }
        };
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(0, 150, 136));

        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        content.add(title, BorderLayout.CENTER);
        content.add(progressBar, BorderLayout.SOUTH);

        setContentPane(content);
        setSize(400, 200);
        setLocationRelativeTo(null);

        timer = new Timer(50, e -> {
            progress += 2;
            progressBar.setValue(progress);

            int barWidth = progressBar.getWidth();
            carX = (progress * (barWidth - 30)) / 100;
            progressBar.repaint();

            if (progress >= 100) {
                timer.stop();
                dispose();
                onFinish.run();
            }
        });
    }

    public void showSplash() {
        setVisible(true);
        timer.start();
    }
}