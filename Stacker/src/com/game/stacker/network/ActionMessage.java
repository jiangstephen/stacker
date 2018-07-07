package com.game.stacker.network;

import com.game.stacker.state.GameState;

public class ActionMessage implements Message {

	private static final long serialVersionUID = 1L;
	
	private final String gamer;
	
	private final String clientIdentifier;
	
	private final GameState gameState;
	
	public ActionMessage(String gamer, String clientIdentifier,  GameState gameState){
		this.gamer = gamer;
		this.clientIdentifier = clientIdentifier;
		this.gameState = gameState;
	}

	public String getGamer() {
		return gamer;
	}

	public String getClientIdentifier() {
		return clientIdentifier;
	}
	
	public GameState getGameState() {
		return gameState;
	}

	@Override
	public String toString(){
		return String.format("ActionMessage = [gamer: %s, clientIdentifier : %s]", gamer, clientIdentifier);
	}


}
