package com.game.stacker.network.message;

import java.net.Socket;
import java.util.Set;

import com.game.stacker.network.Gamer;

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

	@Override
	public void accept(MessageHandler messageHandler, Socket socket) {
		messageHandler.handleMessage(this, socket);
	}
	

}
