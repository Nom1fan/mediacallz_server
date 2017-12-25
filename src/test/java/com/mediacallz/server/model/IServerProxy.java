package com.mediacallz.server.model;

import com.mediacallz.server.client.ConnectionToServer;
import com.mediacallz.server.model.response.Response;

public interface IServerProxy {

	void handleMessageFromServer(Response msg, ConnectionToServer connectionToServer);
	void handleDisconnection(ConnectionToServer cts, String msg);
}
