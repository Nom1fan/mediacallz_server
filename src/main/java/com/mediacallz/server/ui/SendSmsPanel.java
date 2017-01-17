package com.mediacallz.server.ui;

import com.mediacallz.server.services.SmsSender;
import com.sun.deploy.panel.JSmartTextArea;
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
public class SendSmsPanel extends SendPanel implements Observer {

    private JTextField txtFieldSendTo;
    private JTextField txtFieldContent;

    private final SmsSender smsSender;

    private String dest;

    @Autowired
    public SendSmsPanel(SmsSender smsSender) {
        this.smsSender = smsSender;
    }

    @Autowired
    public void registerToObservables(List<Observable> observables) {
        for (Observable observable : observables) {
            observable.addObserver(this);
        }
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
            jSmartTextAreaStatus.setText("Sending SMS to " + dest + "...");
        });

        return btnSend;
    }

    @Override
    protected JSmartTextArea getStatusTextArea() {
        jSmartTextAreaStatus = new JSmartTextArea();
        jSmartTextAreaStatus.setFont(new Font(null, Font.PLAIN, 10));
        jSmartTextAreaStatus.setText("");
        return jSmartTextAreaStatus;
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof SmsSender) {
            boolean success = (boolean) arg;
            if(success) {
                jSmartTextAreaStatus.setText("Sms to " + dest + " was sent successfully!");
            }
            else {
                jSmartTextAreaStatus.setText("Sms to " + dest + " failed!");
            }
        }
    }
}