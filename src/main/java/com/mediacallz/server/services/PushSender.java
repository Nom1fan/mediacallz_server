package com.mediacallz.server.services;


import com.mediacallz.server.model.PushEventKeys;
import com.mediacallz.server.model.push.PushData;

/**
 * Created by Mor on 25/07/2016.
 */
public interface PushSender {

    /**
     * Sending a push containing title, message and data
     *
     * @param deviceToken     The device token of the push will be sent to
     * @param pushEventAction The push event action. Valid values are in PushEventKeys class.
     * @param title           The title of the push notification
     * @param msg             The body of the push notification
     * @param pushEventData   The data payload to add to the push
     * @return true if push was successful, false otherwise
     * @see PushEventKeys
     */
    boolean sendPush(
            final String deviceToken,
            final String pushEventAction,
            String title,
            final String msg,
            final Object pushEventData);

    /**
     * Sending a push containing only data
     *
     * @param deviceToken     The device token of the push will be sent to
     * @param pushEventAction The push event action. Valid values are in PushEventKeys class.
     * @param pushEventData   The data payload to add to the push
     * @return true if push was successful, false otherwise
     * @see PushEventKeys
     */
    boolean sendPush(
            final String deviceToken,
            final String pushEventAction,
            final Object pushEventData);

    /**
     * Sending a push containing only title and message
     *
     * @param deviceToken     The device token of the push will be sent to
     * @param pushEventAction The push event action. Valid values are in PushEventKeys class
     * @param title           The title of the push notification
     * @param msg             The body of the push notification
     * @return true if push was successful, false otherwise
     * @see PushEventKeys
     */
    boolean sendPush(
            final String deviceToken,
            final String pushEventAction,
            String title,
            final String msg);
}
