package com.mediacallz.server.ui;

import com.mediacallz.server.services.SmsSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Mor on 28/03/2016.
 */
@Component
public class SendSmsPanel extends SendPanel {

    private JTextField txtFieldSendTo;
    private JTextField txtFieldContent;

    private final SmsSender smsSender;

    private String dest;

    @Autowired
    public SendSmsPanel(SmsSender smsSender) {
        this.smsSender = smsSender;
    }

    @Override
    protected JLabel getLblMessageContent() {
        return new JLabel("SMS content:");
    }

    @Override
    protected JLabel getLblSendTo() {
        return new JLabel("Send SMS to:");
    }

    @Override
    protected JTextField getTxtFieldContent() {
        txtFieldContent = new JTextField();
        txtFieldContent.setToolTipText("Sms message...");
        return txtFieldContent;
    }

    @Override
    protected JTextField getTxtFieldSendTo() {
        txtFieldSendTo = new JTextField();
        txtFieldSendTo.setToolTipText("Send SMS to...");
        return txtFieldSendTo;
    }

    @Override
    protected JButton getBtnSend() {
        JButton btnSend = new JButton();
        btnSend.setText("Send");
        btnSend.addActionListener(e -> {
            dest = txtFieldSendTo.getText();
            String msg = txtFieldContent.getText();
            smsSender.sendSms(dest, msg);
            textArea.setText("Sending SMS to " + dest + "...");
        });

        return btnSend;
    }

    @Override
    protected JTextArea getStatusTextArea() {
        textArea = new JTextArea();
        textArea.setFont(new Font(null, Font.PLAIN, 10));
        textArea.setText("");
        return textArea;
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof SmsSender) {
            boolean success = (boolean) arg;
            if(success) {
                textArea.setText("Sms to " + dest + " was sent successfully!");
            }
            else {
                textArea.setText("Sms to " + dest + " failed!");
            }
        }
    }
}