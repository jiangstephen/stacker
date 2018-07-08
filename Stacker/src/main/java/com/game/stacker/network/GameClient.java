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

import javax.swing.JOptionPane;

import com.game.stacker.Stacker;
import com.game.stacker.StackerGame;
import com.game.stacker.network.message.ConnectMessage;
import com.game.stacker.network.message.Message;
import com.game.stacker.network.message.MessageHandler;
import com.game.stacker.network.message.MessageHandlerForClient;
import com.game.stacker.state.GameState;
import com.game.stacker.state.GameStateRecorder;

public class GameClient {
	
	private final Socket clientSocket ;
	
	private final Map<String, Stacker> stackersByName = new HashMap<String, Stacker>();
	
	private final String clientIdentifier;
	
	private final MessageHandler messageHandler;
	
	
	public GameClient(String gamer) {
		this.clientIdentifier = UUID.randomUUID().toString();
		this.messageHandler = new MessageHandlerForClient(stackersByName, clientIdentifier);
		try {
			
			this.clientSocket = new Socket("localhost", 48019);
			GameState gameState = new GameStateRecorder().saveGame(StackerGame.getInstance().getMainStacker());
			sentMessage(new ConnectMessage(gamer, clientIdentifier, gameState));
		} catch(Exception e){
			
			JOptionPane.showConfirmDialog(StackerGame.getInstance(), "There is no server", "Error", 
		            JOptionPane.CLOSED_OPTION);
			
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
						message.accept(messageHandler, clientSocket);
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
