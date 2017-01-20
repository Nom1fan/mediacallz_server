package com.mediacallz.server.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;

/**
 * Created by Mor on 27/03/2016.
 */
@Component
public class ControlPanel extends JPanel {

    private final SendSmsPanel sendSmsPanel;

    private final SendPushPanel sendPushPanel;

    @Value("${server.port}")
    private int serverPort;

    @Autowired
    public ControlPanel(SendSmsPanel sendSmsPanel, SendPushPanel sendPushPanel) {
        this.sendSmsPanel = sendSmsPanel;
        this.sendPushPanel = sendPushPanel;
    }

    @PostConstruct
    public void init() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(getLblControlPanel());
        add(Box.createVerticalStrut(20));
        add(getTabPane());
        add(getServerListeningLabel());
    }

    private JTabbedPane getTabPane() {

        JTabbedPane tabPane = new JTabbedPane();
        tabPane.add("Sms Panel", sendSmsPanel);
        tabPane.add("Push Panel", sendPushPanel);
        return tabPane;
    }

    private JLabel getLblControlPanel() {

        JLabel lblControlPanel = new JLabel();
        lblControlPanel.setFont(new Font(null, Font.PLAIN, 20));
        lblControlPanel.setText("MediaCallz Server Control Panel");
        return lblControlPanel;
    }

    private JLabel getServerListeningLabel() {
        JLabel lblServerListening = new JLabel();
        lblServerListening.setFont(new Font(null, Font.PLAIN, 10));
        lblServerListening.setText("Server listening on port: " + serverPort + "...");
        return lblServerListening;
    }
}