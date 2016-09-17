package com.mediacallz.server.model;

public interface IServerProxy {

	void handleMessageFromServer(MessageToClient msg, ConnectionToServer connectionToServer);
	void handleDisconnection(ConnectionToServer cts, String msg);
}
