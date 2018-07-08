package com.game.stacker.network.message;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

import com.game.stacker.Stacker;
import com.game.stacker.StackerGame;
import com.game.stacker.network.Gamer;
import com.game.stacker.state.GameStateRecorder;

public class MessageHandlerForClient implements MessageHandler{

	private final Map<String, Stacker> stackersByName ;
	
	private final String clientIdentifier;
	
	
	public MessageHandlerForClient(Map<String, Stacker> stackersByName, String clientIdentifier ){
		this.stackersByName = stackersByName;
		this.clientIdentifier = clientIdentifier;
	}
	
	
	@Override
	public void handleMessage(ActionMessage actionMessage, Socket socket) {
		if(isSelfMessageOriginated(actionMessage)){
			return;
		}
		stackersByName.get(actionMessage.getClientIdentifier()).restoreGame(actionMessage.getGameState(), true);
		
	}

	@Override
	public void handleMessage(ClientIntializationMessage clientIntializationMessage, Socket socket) {
		for(Gamer gamer: clientIntializationMessage.getGamers()){
			if(clientIdentifier.equals(gamer.getClientIdentifier())){
				System.out.println("Ignore myself from the ClientIntializationMessage");
			} else {
				Stacker otherStacker = new GameStateRecorder().restoreGame(gamer.getGameState(), true);
				stackersByName.put(gamer.getClientIdentifier(), otherStacker);
				StackerGame.getInstance().addStacker(otherStacker);
				otherStacker.start();
			}
		}
	}

	@Override
	public void handleMessage(LeaveGameMessage leaveGameMessage, Socket socket) {
		if(isSelfMessageOriginated(leaveGameMessage)){
			return;
		}
		Stacker stackerToRemove = stackersByName.remove(leaveGameMessage.getClientIdentifier());
		StackerGame.getInstance().removeStacker(stackerToRemove);
		
	}


	@Override
	public void handleMessage(ConnectMessage connectMessage, Socket socket) throws IOException {
		if(isSelfMessageOriginated(connectMessage)){
			return;
		}
		Stacker otherStacker = new GameStateRecorder().restoreGame(connectMessage.getGameState(), true);
		stackersByName.put(connectMessage.getClientIdentifier(), otherStacker);
		StackerGame.getInstance().addStacker(otherStacker);
		StackerGame.getInstance().pack();
		otherStacker.start();
	}
	
	private boolean isSelfMessageOriginated(Message message){
		return clientIdentifier.equals(message.getClientIdentifier());
	}


	@Override
	public void handleMessage(TextMessage textMessage, Socket socket) {
		String message = textMessage.getGamer() +" : " + textMessage.getMessage();
		StackerGame.getInstance().getChatPanel().appendMessage(message);
		
	}

}
