package com.game.stacker;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import javax.swing.JFrame;
import javax.swing.JPanel;


public class StackerGame extends javax.swing.JFrame implements KeyListener {

	private static final long serialVersionUID = 1L;
	
	private JPanel mainPanel = new JPanel();
	
	GridLayout layout = new GridLayout(1, 3);
	
	private List<Stacker> stackers = new ArrayList<Stacker>();
	
	private Stacker mainStacker;
	
	private ControlPanel controlPanel;
	

	private static StackerGame instance;
	
	
	private StackerGame(String name) {
		super(name);
		setResizable(false);
		mainPanel.setLayout(layout);
	}
	
	public synchronized static StackerGame getInstance(){
		if(instance == null){
			instance = new StackerGame("Stacker");
		}
		return instance;
	}
	

	private void createAndShowGUI(String userName) {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().add(mainPanel);
		
		this.mainStacker = new Stacker(userName, 32, false);
		this.addStacker(mainStacker); //space
		this.controlPanel = new ControlPanel(mainStacker);
		this.setJMenuBar(controlPanel);
		this.pack();
		this.setVisible(true);
		this.setFocusable(true);
		this.setFocusTraversalKeysEnabled(false);
		this.addKeyListener(this);
		this.requestFocus();
		new Thread(new MainWindowFocus(this)).start();
	}
	
	public void addStacker(Stacker stacker){
		this.stackers.add(stacker);
		mainPanel.add(stacker);
		this.pack();
	}
	
	
	public void removeStacker(Stacker stacker){
		mainPanel.remove(stacker);
		mainPanel.revalidate();
		mainPanel.repaint();
		this.pack();
	}


	@Override
	public void keyTyped(KeyEvent e) {
		
	}


	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println("Key inputed : " + e.getKeyCode());
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
	
	

	public ControlPanel getControlPanel() {
		return controlPanel;
	}



	private class MainWindowFocus implements Runnable {
		
		private StackerGame game;
		
		public MainWindowFocus(StackerGame game){
			this.game = game;
		}

		@Override
		public void run() {
			while(true){
				game.requestFocus();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					throw new RuntimeException("unable to sleep the thread for 100ms");
				}
			}
		}
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
		StackerGame.getInstance().createAndShowGUI(username);
		
	}



}
