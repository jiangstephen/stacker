package com.game.stacker;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.game.stacker.network.ChatPanel;
import com.game.stacker.network.GameClient;


public class StackerGame extends javax.swing.JFrame implements KeyListener {

	private static final long serialVersionUID = 1L;
	
	private JPanel mainPanel = new JPanel();
	
	private JPanel stackerPanel = new JPanel();
	
	private BorderLayout mainLayout = new BorderLayout();
	
	private GridLayout stackerLayout = new GridLayout(1, 3);
	
	private List<Stacker> stackers = new ArrayList<Stacker>();
	
	private Stacker mainStacker;
	
	private ControlMenu controlMenu;
	
	private ChatPanel chatPanel;

	private static StackerGame instance;
	
	
	private StackerGame(String name) {
		super(name);
		setResizable(false);
		stackerPanel.setLayout(stackerLayout);
	}
	
	public synchronized static StackerGame getInstance(){
		if(instance == null){
			instance = new StackerGame("Stacker");
		}
		return instance;
	}
	

	private void initialize(String userName) {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.mainPanel.setLayout(mainLayout);
		this.getContentPane().add(mainPanel);
		this.mainPanel.add(stackerPanel, BorderLayout.NORTH);
		this.mainStacker = new Stacker(userName, 32, false);
		this.addStacker(mainStacker); //space
		this.chatPanel = new ChatPanel(mainStacker);
		this.mainPanel.add(chatPanel, BorderLayout.SOUTH);
		this.controlMenu = new ControlMenu(mainStacker);
		this.setJMenuBar(controlMenu);
		this.pack();
		this.setVisible(true);
		this.setFocusable(true);
		this.setFocusTraversalKeysEnabled(false);
		mainStacker.addKeyListener(this);
		mainStacker.requestFocus();
	}
	
	public void addStacker(Stacker stacker){
		this.stackers.add(stacker);
		stackerPanel.add(stacker);
		this.pack();
	}
	
	
	public void removeStacker(Stacker stacker){
		stackerPanel.remove(stacker);
		stackerPanel.revalidate();
		stackerPanel.repaint();
		this.pack();
	}


	@Override
	public void keyTyped(KeyEvent e) {
		
	}


	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println("Key inputed : " + e.getKeyCode());
		if (e.getKeyCode() == 9){
			chatPanel.startChat();
		}
		for(Stacker stacker : stackers){
			if(e.getKeyCode() == stacker.getNextLevelKey()){
				if(stacker.getStatus() == Status.RUNNING || stacker.getStatus() == Status.READY){
					stacker.moveToNextLevel();
				}
			}
		}
	}


	@Override
	public void keyReleased(KeyEvent e) {
	}
	
	

	public Stacker getMainStacker() {
		return mainStacker;
	}
	
	
	public ChatPanel getChatPanel() {
		return chatPanel;
	}

	public ControlMenu getControlPanel() {
		return controlMenu;
	}
	
	public GameClient getGameClient(){
		return this.mainStacker.getGameClient();
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Play as: ");
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String username = scanner.nextLine();
		System.out.println("You are playing as " + username);
		StackerGame.getInstance().initialize(username);
	}

}
