package com.game.stacker.network;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import com.game.stacker.network.message.LeaveGameMessage;
import com.game.stacker.network.message.Message;
import com.game.stacker.network.message.MessageHandler;
import com.game.stacker.network.message.MessageHandlerForServer;

public class GameServer extends JFrame {

	private static final long serialVersionUID = 1L;

	private final static int SERVER_PORT = 48019;

	private final ServerSocket serverSocket;

	private Map<Socket, Gamer> gamersBySocket = new HashMap<>();
	
	private final MessageHandler messageHandler;

	JTextArea textArea = new JTextArea();
	
	JScrollPane scrollPane = new JScrollPane(textArea);  

	public GameServer() throws IOException {
		messageHandler = new MessageHandlerForServer(gamersBySocket);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		JScrollBar vertical = scrollPane.getVerticalScrollBar();
		vertical.setValue( vertical.getMaximum() );
		this.getContentPane().add(scrollPane);
		serverSocket = new ServerSocket(SERVER_PORT);
		appendMessage("The server is listening on the port " + SERVER_PORT);
		textArea.setBackground(Color.BLACK);
		textArea.setForeground(Color.GREEN);
		textArea.setFont(new Font("Arial", Font.PLAIN, 15));
	}

	private void showGUI() {
		this.setVisible(true);
		this.setSize(500, 300);
		this.setResizable(true);
		this.setTitle("Stacker Game Server");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void startServer() {
		showGUI();
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						Socket socket = serverSocket.accept();
						String hostAddress = socket.getInetAddress().getHostAddress();
						appendMessage("Client " + hostAddress + " connected to the server");
						startClientCommunication(socket);
					} catch (IOException e) {
						e.printStackTrace();
						System.exit(-1);
					}
				}
			}
		}).start();
	}

	private void startClientCommunication(final Socket socket) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						InputStream input = socket.getInputStream();
						ObjectInputStream ois = new ObjectInputStream(input);
						Message message = (Message) ois.readObject();
						appendMessage("Received the message " + message + " from client, and start broadcasting");
						message.accept(messageHandler, socket);
						for (Gamer gamer : gamersBySocket.values()) {
							new ObjectOutputStream(gamer.getSocket().getOutputStream()).writeObject(message);
						}
					} catch (IOException|ClassNotFoundException e) {
						Gamer gamer = gamersBySocket.get(socket);
						appendMessage(gamer.getGamerName() + " has left the party");
						gamersBySocket.remove(socket);
						for (Gamer otherGamer : gamersBySocket.values()) {
							try {
								new ObjectOutputStream(otherGamer.getSocket().getOutputStream()).writeObject(new LeaveGameMessage(gamer.getClientIdentifier()));
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}		
						return;
					} 
				}

			}
		}).start();
	}
	private void appendMessage(String message){
		textArea.append(message+"\n");
	}

	public static void main(String args[]) throws IOException {
		new GameServer().startServer();
	}

}
