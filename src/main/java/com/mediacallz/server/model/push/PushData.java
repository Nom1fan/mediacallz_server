package com.mediacallz.server.model.push;

import java.io.Serializable;

/**
 * Created by Mor on 12/31/2016.
 */
public interface PushData extends Serializable {

    class PushEventKeys {

        public static final String PUSH_EVENT_ACTION = "pushEventAction";
        public static final String PUSH_EVENT_DATA = "pushEventData";
        public static final String PENDING_DOWNLOAD = "PENDING_DOWNLOAD";
        public static final String CLEAR_MEDIA = "CLEAR_MEDIA";
        public static final String CLEAR_SUCCESS = "CLEAR_SUCCESS";
        public static final String SHOW_MESSAGE = "SHOW_MESSAGE";
        public static final String SHOW_ERROR = "SHOW_ERROR";
        public static final String TRANSFER_SUCCESS = "TRANSFER_SUCCESS";
        public static final String PUSH_DATA = "alert";
        public static final String PUSH_DATA_EXTRA = "PUSH_DATA_EXTRA";

    }
}
