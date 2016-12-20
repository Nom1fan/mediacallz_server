package mediacallz.com.server.model.response;

import lombok.Data;
import lombok.ToString;
import mediacallz.com.server.model.ClientActionType;

import java.io.Serializable;

/* Generic message to the client, containing information and enables generic interface for client actions corresponding to the message
        * @author Mor
        *
        */
@Data
@ToString
public class Response<T> implements Serializable {

    protected T result;
    protected ClientActionType actionType;

    public Response(ClientActionType actionType) {

        this.actionType = actionType;
    }

    public Response(ClientActionType actionType, T result) {

        this.result = result;
        this.actionType = actionType;
    }
}
