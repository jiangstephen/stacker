package com.game.stacker.network.message;

import java.io.IOException;
import java.net.Socket;

import com.game.stacker.state.GameState;

public class ConnectMessage implements Message {

	private static final long serialVersionUID = 1L;
	
	private final String gamer;
	
	private final String clientIdentifier;
	
	private final GameState gameState;
	
	public ConnectMessage(String gamer, String clientIdentifier, GameState gameState){
		this.gamer = gamer;
		this.clientIdentifier = clientIdentifier;
		this.gameState = gameState;
	}

	public String getGamer() {
		return gamer;
	}
	
	@Override
	public String toString(){
		return "New Gamer ["+gamer+"] joined";
	}

	@Override
	public String getClientIdentifier() {
		return this.clientIdentifier;
	}

	public GameState getGameState() {
		return gameState;
	}

	@Override
	public void accept(MessageHandler messageHandler, Socket socket) throws IOException {
		messageHandler.handleMessage(this, socket);
	}
	
}
