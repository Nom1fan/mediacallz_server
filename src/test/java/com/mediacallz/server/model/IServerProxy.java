package com.mediacallz.server.model;

import com.mediacallz.server.client.ConnectionToServer;

public interface IServerProxy {

	void handleMessageFromServer(MessageToClient msg, ConnectionToServer connectionToServer);
	void handleDisconnection(ConnectionToServer cts, String msg);
}
