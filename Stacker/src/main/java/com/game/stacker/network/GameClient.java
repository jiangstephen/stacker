package com.game.stacker.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.game.stacker.Stacker;
import com.game.stacker.StackerGame;
import com.game.stacker.state.GameState;
import com.game.stacker.state.GameStateRecorder;

public class GameClient {
	
	private final Socket clientSocket ;
	
	private final Map<String, Stacker> stackersByName = new HashMap<String, Stacker>();
	
	private final String clientIdentifier;
	
	
	public GameClient(String gamer) {
		this.clientIdentifier = UUID.randomUUID().toString();
		try {
			
			this.clientSocket = new Socket("localhost", 48019);
			GameState gameState = new GameStateRecorder().saveGame(StackerGame.getInstance().getMainStacker());
			sentMessage(new ConnectMessage(gamer, clientIdentifier, gameState));
		} catch(Exception e){
			throw new RuntimeException("Unable to start a new game client ", e);
		}
	}
	
	//Read message from server
	public void startClient(){
		new Thread(new Runnable(){

			@Override
			public void run() {
				System.out.println("Start the client communication with server");
				while(true){
					
					try {
						InputStream input = clientSocket.getInputStream();
						ObjectInputStream ois = new ObjectInputStream(input);
						Message message = (Message)ois.readObject();
						System.out.println("Received a message from server " + message );
						if(message instanceof ClientIntializationMessage){
							ClientIntializationMessage clientInitMessage = (ClientIntializationMessage)message;
							for(Gamer gamer: clientInitMessage.getGamers()){
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
						if(message.getClientIdentifier().equals(clientIdentifier)){
							System.out.println("This message is originated from myself,  ignore");
							continue; 
						}
						if(message instanceof LeaveGameMessage){
							Stacker stackerToRemove = stackersByName.remove(message.getClientIdentifier());
							StackerGame.getInstance().removeStacker(stackerToRemove);
						}
						if(message instanceof ConnectMessage){
							ConnectMessage connectMessage = (ConnectMessage)message;
							Stacker otherStacker = new GameStateRecorder().restoreGame(connectMessage.getGameState(), true);
							stackersByName.put(connectMessage.getClientIdentifier(), otherStacker);
							StackerGame.getInstance().addStacker(otherStacker);
							StackerGame.getInstance().pack();
							otherStacker.start();
						}
						if(message instanceof ActionMessage){
							ActionMessage actionMessage = (ActionMessage)message;
							stackersByName.get(message.getClientIdentifier()).restoreGame(actionMessage.getGameState(), true);
						}

					} catch (Exception e) {
						throw new RuntimeException("Unable to accept the message from server, client exit " , e);
					}
				}
				
			}}).start();
	}
	
	//Sent message to Server
	public void sentMessage(Message message) {
		OutputStream output;
		try {
			output = clientSocket.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(output);
			oos.writeObject(message);
		} catch (IOException e) {
			throw new RuntimeException("Unable to send the message", e);
		}

	}

	public String getClientIdentifier() {
		return clientIdentifier;
	}
	
	
	
	

}
