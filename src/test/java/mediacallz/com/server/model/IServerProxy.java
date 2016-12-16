package mediacallz.com.server.model;

import mediacallz.com.server.client.ConnectionToServer;

public interface IServerProxy {

	void handleMessageFromServer(MessageToClient msg, ConnectionToServer connectionToServer);
	void handleDisconnection(ConnectionToServer cts, String msg);
}
