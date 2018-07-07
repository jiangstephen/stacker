package com.game.stacker.network.message;

import java.net.Socket;

public class LeaveGameMessage implements Message {

	private static final long serialVersionUID = 1L;
	
	private final String clientIdentifier;
	
	public LeaveGameMessage(String clientIdentifier){
		this.clientIdentifier = clientIdentifier;
	}

	@Override
	public String getClientIdentifier() {
		return clientIdentifier;
	}

	@Override
	public void accept(MessageHandler messageHandler, Socket socket) {
		messageHandler.handleMessage(this, socket);
	}

}
