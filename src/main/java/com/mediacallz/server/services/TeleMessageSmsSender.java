package com.mediacallz.server.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Observable;

/**
 * Created by Mor on 28/03/2016.
 */


/**
 * Created by Mor on 28/03/2016.
 */
@Service
@Slf4j
public class TeleMessageSmsSender extends Observable implements SmsSender, Runnable {

    @Value(value = "${sms.username}")
    private String smsUser;

    @Value(value = "${sms.password}")
    private String smsPassword;

    @Value(value = "${sms.url}")
    private String smsUrl;

    private String dest;

    private String msg;

    @Override
    public void sendSms(final String dest, final String msg) {
        this.dest = dest;
        this.msg = msg;
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        boolean success = true;
        try {
            sendSmsGET(smsUrl, smsUser, smsPassword, dest, msg);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Failed to send SMS to [User]:" + dest + ". [Message]:" + msg + ". [Exception]:" + (e.getMessage() != null ? e.getMessage() : e));
            success = false;
        }
        setChanged();
        notifyObservers(success);
    }

    private void sendSmsGET(String url, String userId, String password, String to, String text) throws Exception {

        RequestBuilder builder = RequestBuilder.get().setUri(url);
        builder.addParameter("userid", userId);
        builder.addParameter("password", password);
        builder.addParameter("to", to);
        builder.addParameter("text", text);
        log.info("Sending SMS to [User]:" + to);
        HttpResponse response = HttpClients.createDefault().execute(builder.build());

        if (response != null && response.getStatusLine().getStatusCode() == 200) {
            //response.getEntity().writeTo(System.out);
            log.info("SMS GET Response: 200 OK");
        } else if (response != null) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (String line; (line = reader.readLine()) != null; ) {
                sb.append(line).append("\n");
            }

            log.error("SMS GET failed. [Response String]:" + sb.toString());
            throw new Exception("SMS GET failed. [Status]:" + response.getStatusLine().getStatusCode() + ". [Message Id]:" + sb.toString());

        } else {

            log.error("SMS GET failed. Response  is NULL");
            throw new Exception("SMS GET failed. Response  is NULL");

        }
    }
}