package com.game.stacker.network.message;

import java.io.IOException;
import java.net.Socket;

import com.game.stacker.state.GameState;

public class TextMessage implements Message{

	private static final long serialVersionUID = 1L;
	private final String gamer;
	private final String clientIdentifier;
	private final String message;
	
	
	public TextMessage(String gamer, String clientIdentifier, String message){
		this.gamer = gamer;
		this.clientIdentifier = clientIdentifier;
		this.message = message;
	}

	@Override
	public String getClientIdentifier() {
		return this.clientIdentifier;
	}
	
	@Override
	public void accept(MessageHandler messageHandler, Socket socket) throws IOException {
		messageHandler.handleMessage(this, socket);
	}

	public String getGamer() {
		return gamer;
	}

	public String getMessage() {
		return message;
	}
	
	
	
}
