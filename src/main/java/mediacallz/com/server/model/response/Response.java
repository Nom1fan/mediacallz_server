package mediacallz.com.server.model.response;

import mediacallz.com.server.model.ClientActionType;

import java.io.Serializable;

/* Generic message to the client, containing information and enables generic interface for client actions corresponding to the message
        * @author Mor
        *
        */
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

    public T getResult() {
        return result;
    }

    public ClientActionType getActionType() {
        return actionType;
    }

    @Override
    public String toString() {
        return "Response{" +
                "result=" + result +
                ", actionType=" + actionType +
                '}';
    }
}
