package com.mediacallz.server.exceptions;

/**
 * Created by Mor on 14/11/2015.
 */
public class DownloadRequestFailedException extends Exception {

    public DownloadRequestFailedException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public DownloadRequestFailedException(String msg) {
        super(msg);
    }
}
