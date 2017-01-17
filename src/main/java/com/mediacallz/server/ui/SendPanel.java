package com.mediacallz.server.ui;

import com.mediacallz.server.database.dbo.UserDBO;
import com.mediacallz.server.model.PushEventKeys;
import com.sun.deploy.panel.JSmartTextArea;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * Created by Mor on 1/17/2017.
 */
@Component
public abstract class SendPanel extends JPanel {

    protected JTextField txtFieldSendTo;

    protected JTextField txtFieldContent;

    protected JSmartTextArea jSmartTextAreaStatus;

    @PostConstruct
    public void init() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel getSmsCodePanel = new JPanel();
        getSmsCodePanel.setLayout(new BoxLayout(getSmsCodePanel, BoxLayout.Y_AXIS));

        JPanel sendToPanel = new JPanel();
        sendToPanel.setLayout(new BoxLayout(sendToPanel, BoxLayout.Y_AXIS));
        sendToPanel.add(getLblSendTo());
        sendToPanel.add(getTxtFieldSendTo());

        JPanel msgContentPanel = new JPanel();
        msgContentPanel.setLayout(new BoxLayout(msgContentPanel, BoxLayout.Y_AXIS));
        msgContentPanel.add(getLblMessageContent());
        msgContentPanel.add(getTxtFieldContent());

        JPanel btnSendPanel = new JPanel();
        btnSendPanel.setLayout(new BoxLayout(btnSendPanel, BoxLayout.X_AXIS));
        btnSendPanel.add(Box.createHorizontalStrut(50));
        btnSendPanel.add(getBtnSend());

        add(Box.createVerticalStrut(20));
        add(sendToPanel);
        add(Box.createVerticalStrut(20));
        add(msgContentPanel);
        add(Box.createVerticalStrut(10));
        add(btnSendPanel);
        add(getStatusTextArea());
    }

    protected JLabel getLblMessageContent() {
        return new JLabel();
    }

    protected JLabel getLblSendTo() {
       return new JLabel();
    }

    protected JTextField getTxtFieldContent() {
        txtFieldContent = new JTextField();
        return txtFieldContent;
    }

    protected JTextField getTxtFieldSendTo() {
        txtFieldSendTo = new JTextField();
        return txtFieldSendTo;
    }

    protected JButton getBtnSend() {
        return new JButton();
    }

    protected JSmartTextArea getStatusTextArea() {
        jSmartTextAreaStatus = new JSmartTextArea();
        return jSmartTextAreaStatus;
    }

}
