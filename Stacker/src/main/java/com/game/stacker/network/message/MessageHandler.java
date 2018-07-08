package com.game.stacker.network.message;

import java.io.IOException;
import java.net.Socket;

public interface MessageHandler {
	
	void handleMessage(ActionMessage message, Socket socket);

	void handleMessage(ClientIntializationMessage clientIntializationMessage, Socket socket);

	void handleMessage(ConnectMessage connectMessage, Socket socket) throws IOException;

	void handleMessage(LeaveGameMessage leaveGameMessage, Socket socket);
	
	void handleMessage(TextMessage textMessage, Socket socket);

}
