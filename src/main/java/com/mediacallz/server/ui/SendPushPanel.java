package com.mediacallz.server.ui;

import com.mediacallz.server.database.Dao;
import com.mediacallz.server.database.dbo.UserDBO;
import com.mediacallz.server.model.PushEventKeys;
import com.mediacallz.server.services.PushSender;
import com.sun.deploy.panel.JSmartTextArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Created by Mor on 28/03/2016.
 */
@Component
public class SendPushPanel extends SendPanel {

    private final Dao dao;

    private final PushSender pushSender;

    private JTextField txtFieldSendTo;
    private JTextField txtFieldContent;
    private JSmartTextArea jSmartTextAreaStatus;

    @Autowired
    public SendPushPanel(Dao dao, PushSender pushSender) {
        this.dao = dao;
        this.pushSender = pushSender;
    }

    @Override
    protected JLabel getLblMessageContent() {
        return new JLabel("Push content:");
    }

    @Override
    protected JLabel getLblSendTo() {
        return new JLabel("Send push to:");
    }

    @Override
    protected JTextField getTxtFieldContent() {
        txtFieldContent = new JTextField();
        txtFieldContent.setToolTipText("Push message...");
        return txtFieldContent;
    }

    @Override
    protected JTextField getTxtFieldSendTo() {
        txtFieldSendTo = new JTextField();
        txtFieldSendTo.setToolTipText("Send push to...");
        return txtFieldSendTo;
    }

    @Override
    protected JButton getBtnSend() {
        JButton _btnSend = new JButton();
        _btnSend.setText("Send");
        _btnSend.addActionListener(e -> {
            String dest = txtFieldSendTo.getText();
            String msg = txtFieldContent.getText();

            String statusText = "Push to " + dest + " failed!";
            try {
                UserDBO userRecord = dao.getUserRecord(dest);
                if(userRecord != null) {
                    boolean success = pushSender.sendPush(userRecord.getToken(), PushEventKeys.SHOW_MESSAGE, "Notification", msg);
                    if (success) {
                        statusText = "Push to " + dest + " was sent successfully!";
                    }
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            jSmartTextAreaStatus.setText(statusText);
        });

        return _btnSend;
    }
}