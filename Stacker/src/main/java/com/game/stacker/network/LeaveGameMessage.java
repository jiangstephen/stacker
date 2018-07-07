package com.game.stacker.network;

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

}
