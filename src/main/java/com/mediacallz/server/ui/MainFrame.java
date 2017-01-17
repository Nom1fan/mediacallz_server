package com.mediacallz.server.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * Created by Mor on 27/03/2016.
 */
@Component
public class MainFrame extends JFrame {

    private final ControlPanel controlPanel;

    @Autowired
    public MainFrame(ControlPanel controlPanel) {
        this.controlPanel = controlPanel;
    }

    @PostConstruct
    public void init() {
        setContentPane(controlPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(true);
        setVisible(true);
        pack();
    }
}