package com.game.stacker.network;

import java.util.Set;

public class ClientIntializationMessage implements Message{

	private static final long serialVersionUID = 1L;
	
	private final Set<Gamer> gamers;
	
	private final String clientIdentifier;
	
	public ClientIntializationMessage(String clientIdentifier, Set<Gamer> gamers) {
		this.clientIdentifier = clientIdentifier;
		this.gamers = gamers;
	}

	@Override
	public String getClientIdentifier() {
		return clientIdentifier;
	}

	public Set<Gamer> getGamers() {
		return gamers;
	}
	

}
