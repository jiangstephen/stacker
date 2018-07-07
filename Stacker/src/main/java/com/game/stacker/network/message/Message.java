package com.game.stacker.network.message;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

public interface Message extends Serializable {
	
	String getClientIdentifier();
	
	void accept(MessageHandler messageHandler, Socket socket) throws IOException;

}
