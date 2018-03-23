package com.mediacallz.server.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.gson.Gson;
import com.mediacallz.server.model.push.PushEventKeys;
import com.mediacallz.server.model.push.PushResponse;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;


/**
 * Created by Mor on 15/09/2015.
 */
@Service
@Slf4j
public class FireBasePushSender extends Observable implements PushSender {

    @Value("${push.url}")
    private String pushUrl;

    @Value("${service.account.path}")
    private String serviceAccountPath;

    private final Gson gson;

    @Getter
    private PushResponse pushResponse;

    @Autowired
    public FireBasePushSender(Gson gson) {
        this.gson = gson;
    }

    @Override
    public boolean sendPush(
            final String deviceToken,
            final String pushEventAction,
            String title,
            final String msg,
            final Object pushEventData) {

        setChanged();

        if (isValidToken(deviceToken)) {
            return false;
        }

        try {
            PushMessage pushMessage = createPushMessage(deviceToken, pushEventAction, title, msg, pushEventData);

            pushData(gson.toJson(pushMessage));

        } catch (Exception e) {
            return handleFailure(deviceToken, e);
        }

        notifyObservers(true);
        return true;
    }

    @Override
    public boolean sendPush(
            final String deviceToken,
            final String pushEventAction,
            final Object pushEventData) {

        setChanged();

        if (isValidToken(deviceToken)) {
            return false;
        }

        try {
            PushMessage pushMessage = createPushMessage(deviceToken, pushEventAction, pushEventData);

            pushData(gson.toJson(pushMessage));

        } catch (Exception e) {
            return handleFailure(deviceToken, e);
        }

        notifyObservers(true);
        return true;
    }

    @Override
    public boolean sendPush(
            final String deviceToken,
            final String pushEventAction,
            String title,
            final String msg) {

        setChanged();

        if (isValidToken(deviceToken)) {
            return false;
        }

        try {
            PushMessage pushMessage = createPushMessage(deviceToken, pushEventAction, title, msg);
            pushData(gson.toJson(pushMessage));

        } catch (Exception e) {
            return handleFailure(deviceToken, e);
        }

        notifyObservers(true);
        return true;
    }

    private boolean handleFailure(String deviceToken, Exception e) {
        e.printStackTrace();
        log.error("Failed to send push to token:" + deviceToken + ". Exception:" + e.getMessage());
        notifyObservers(false);
        return false;
    }

    private boolean isValidToken(String deviceToken) {
        if (StringUtils.isEmpty(deviceToken)) {
            log.error("Invalid device token. Aborting push send");
            notifyObservers(false);
            return true;
        }
        return false;
    }

    private PushMessage createPushMessage(String deviceToken, String pushEventAction, String title, String msg) {
        return createPushMessage(deviceToken, pushEventAction, title, msg, null);
    }

    private PushMessage createPushMessage(String deviceToken, String pushEventAction, Object pushEventData) {
        return createPushMessage(deviceToken, pushEventAction, null, null, pushEventData);
    }

    private PushMessage createPushMessage(String deviceToken, String pushEventAction, String title, String msg, Object pushEventData) {
        PushMessage.Message message = new PushMessage.Message();

        if (title != null && msg != null) {
            PushMessage.Message.Notification notification = new PushMessage.Message.Notification();
            notification.setTitle(title);
            notification.setBody(msg);
            message.setNotification(notification);
        }

        message.setToken(deviceToken);
        message.setData(new HashMap<String, String>() {{
            put(PushEventKeys.PUSH_EVENT_ACTION, pushEventAction);
            put(PushEventKeys.PUSH_EVENT_DATA, gson.toJson(pushEventData));
        }});

        return new PushMessage(message);
    }

    private void pushData(String postData) throws Exception {
        log.info("Sending push data:" + postData);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response;
        HttpEntity entity;
        String responseString = null;
        HttpPost httpPost = new HttpPost(pushUrl);
        httpPost.addHeader("Authorization", "Bearer " + getAccessToken());
        httpPost.addHeader("Content-Type", "application/json");
        StringEntity reqEntity = new StringEntity(postData, ContentType.APPLICATION_JSON);
        httpPost.setEntity(reqEntity);
        response = httpClient.execute(httpPost);
        try {
            entity = response.getEntity();
            if (entity != null) {
                responseString = EntityUtils.toString(response.getEntity());
            }
            int statusCode = response.getStatusLine().getStatusCode();
            pushResponse = new PushResponse(statusCode);
            if (statusCode != HttpStatus.SC_OK) {
                String reason = response.getStatusLine().getReasonPhrase();
                pushResponse.setReason(reason);
                throw new Exception("Push POST failed. Status code:" + statusCode + ". Reason:" + reason);
            }
            log.info("Push response:" + responseString);

        } finally {
            response.close();
        }
    }

    private String getAccessToken() throws IOException {
        GoogleCredential googleCredential = GoogleCredential
                .fromStream(getServiceAccountInputStream())
                .createScoped(Collections.singletonList("https://www.googleapis.com/auth/firebase.messaging"));
        googleCredential.refreshToken();
        return googleCredential.getAccessToken();
    }

    private InputStream getServiceAccountInputStream() {
        File file = new File(serviceAccountPath);
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Couldn't find service-account.json");
        }
    }

    private void printServiceAccountJson() throws IOException {
        InputStream is = getServiceAccountInputStream();
        String str;
        StringBuilder buf = new StringBuilder();

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        while ((str = reader.readLine()) != null) {
            buf.append(str).append("\n");
        }

        log.info("Contents of service-account.json:\n");
        log.info(buf.toString());
    }


    @Data
    @RequiredArgsConstructor
    private static class PushMessage {

        private final Message message;

        @Data
        private static class Message {
            private String token;
            private Map<String, String> data;
            private Map<String, String> android = new HashMap<String, String>() {{
                put("priority", "high");
            }};
            private Notification notification;

            @Data
            private static class Notification {
                private String body;
                private String title;
            }
        }
    }
}
