package com.game.stacker.network.message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Map;

import com.game.stacker.network.Gamer;

public class MessageHandlerForServer implements MessageHandler {
	
	private final Map<Socket, Gamer> gamersBySocket;
	
	public MessageHandlerForServer(Map<Socket, Gamer> gamersBySocket){
		this.gamersBySocket = gamersBySocket;
	}

	@Override
	public void handleMessage(ActionMessage message, Socket socket) {
		gamersBySocket.get(socket).setGameState(message.getGameState());
	}

	@Override
	public void handleMessage(ClientIntializationMessage clientIntializationMessage, Socket socket) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleMessage(ConnectMessage connectMessage, Socket socket) throws IOException {
		gamersBySocket.put(socket, new Gamer(connectMessage.getClientIdentifier(), socket, connectMessage.getGamer(), connectMessage.getGameState()));
		Message clientInitializationMessage = new ClientIntializationMessage(connectMessage.getClientIdentifier(), new HashSet<>(gamersBySocket.values()));
		new ObjectOutputStream(socket.getOutputStream()).writeObject(clientInitializationMessage);
	}

	@Override
	public void handleMessage(LeaveGameMessage leaveGameMessage, Socket socket) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleMessage(TextMessage textMessage, Socket socket) {
		// TODO Auto-generated method stub
		
	}

}
