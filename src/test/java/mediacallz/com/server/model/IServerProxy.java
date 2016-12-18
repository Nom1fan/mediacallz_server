package mediacallz.com.server.model;

import mediacallz.com.server.client.ConnectionToServer;
import mediacallz.com.server.model.response.Response;

public interface IServerProxy {

	void handleMessageFromServer(Response msg, ConnectionToServer connectionToServer);
	void handleDisconnection(ConnectionToServer cts, String msg);
}
