package com.game.stacker.network;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;

import com.game.stacker.Stacker;
import com.game.stacker.StackerGame;
import com.game.stacker.network.message.TextMessage;

public class ChatPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private JTextArea messageBox; 
	private JTextArea inputBox; 
	private JButton sendButton; 
	private BorderLayout mainLayout;
	private BorderLayout sendLayout;
	private JPanel sendMessagePanel;
	private JScrollPane scrollPane;
	private final Stacker stacker;
	
	
	public ChatPanel(final Stacker stacker) {
		this.stacker = stacker;
		messageBox = new JTextArea();
		messageBox.setEditable(false);
		messageBox.setPreferredSize(new Dimension(300, 200));
		inputBox = new JTextArea("Message");
		inputBox.setPreferredSize(new Dimension(250, 20));
		inputBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		messageBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		sendButton = new JButton("Send");
		mainLayout = new BorderLayout();
		sendMessagePanel = new JPanel();
		this.scrollPane = new  JScrollPane(messageBox);
		this.setLayout(mainLayout);
		this.add(scrollPane, BorderLayout.NORTH);
		sendLayout = new BorderLayout();
		sendMessagePanel.setLayout(sendLayout);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		sendMessagePanel.add(inputBox, BorderLayout.WEST);
		sendMessagePanel.add(sendButton, BorderLayout.EAST);
		this.add(sendMessagePanel, BorderLayout.SOUTH);
		this.repaint();
		this.setVisible(true);
		sendButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String gamer = StackerGame.getInstance().getMainStacker().getGamer();
				String clientIdentifier = StackerGame.getInstance().getGameClient().getClientIdentifier();
				String message = inputBox.getText();
				TextMessage textMesasge = new TextMessage(gamer, clientIdentifier, message);
				StackerGame.getInstance().getGameClient().sentMessage(textMesasge);
				stacker.requestFocus();
			}
			
		});
		inputBox.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==10){
					inputBox.setText("Messasge");
					stacker.requestFocus();
				}
				if(e.getKeyCode() == 9){
					stacker.requestFocus();
				}
				
			}
		});
	}
	
	public void startChat(){
		inputBox.requestFocus();
	}
	public void appendMessage(String message){
		messageBox.append(message + "\n");
	}

}

