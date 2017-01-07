package com.mediacallz.server.model.response;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/* Generic message to the client, containing information and enables generic interface for client actions corresponding to the message
        * @author Mor
        *
        */
@Data
@ToString
public class Response<T> implements Serializable {

    protected T result;

    public Response(T result) {

        this.result = result;

    }
}
