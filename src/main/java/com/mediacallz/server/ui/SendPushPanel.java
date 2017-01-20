package com.mediacallz.server.ui;

import com.mediacallz.server.database.Dao;
import com.mediacallz.server.database.dbo.UserDBO;
import com.mediacallz.server.model.PushEventKeys;
import com.mediacallz.server.model.dto.UserDTO;
import com.mediacallz.server.services.PushSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Observable;

/**
 * Created by Mor on 28/03/2016.
 */
@Component
public class SendPushPanel extends SendPanel implements Runnable {

    private final Dao dao;

    private final PushSender pushSender;

    private JTextField txtFieldSendTo;
    private JTextField txtFieldContent;

    private String dest;
    private String msg;
    private UserDBO userRecord;

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
        JButton btnSend = new JButton();
        btnSend.setText("Send");
        btnSend.addActionListener(e -> {
            dest = txtFieldSendTo.getText();
            msg = txtFieldContent.getText();

            textArea.setText("Sending push to: " + dest + "...");

            try {
                userRecord = dao.getUserRecord(dest);
                if(userRecord != null) {
                    new Thread(this).start();
                }
                else {
                    textArea.setText("User " + dest + " does not exist");
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });

        return btnSend;
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof PushSender) {
            boolean success = (boolean) arg;
            String statusText = "Push to " + dest + " failed!";

            if (success) {
                statusText = "Push to " + dest + " was sent successfully!";
            }
            textArea.setText(statusText);
        }
    }

    @Override
    public void run() {
        pushSender.sendPush(userRecord.getToken(), PushEventKeys.SHOW_MESSAGE, "Notification", msg);
    }

}