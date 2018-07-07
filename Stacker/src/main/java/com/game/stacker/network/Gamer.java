package com.game.stacker.network;

import java.io.Serializable;
import java.net.Socket;

import com.game.stacker.state.GameState;

public class Gamer implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final String clientIdentifier;
	
	private final transient Socket socket;
	
	private final String gamerName;
	
	private GameState gameState;
	
	public Gamer(String clientIdentifier, Socket socket, String gamerName, GameState gameState){
		this.clientIdentifier = clientIdentifier;
		this.socket = socket;
		this.gamerName = gamerName;
		this.gameState = gameState;
	}

	public String getClientIdentifier() {
		return clientIdentifier;
	}

	public Socket getSocket() {
		return socket;
	}


	public String getGamerName() {
		return gamerName;
	}

	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}


}
