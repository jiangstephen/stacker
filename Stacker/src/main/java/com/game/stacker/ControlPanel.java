package com.game.stacker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.game.stacker.state.GameState;
import com.game.stacker.state.GameStateRecorder;

public class ControlPanel extends JMenuBar {

	private static final long serialVersionUID = 1L;

	private JMenuItem buttonStart, buttonJoinServer, buttonPause, buttonResume, buttonSave, buttonRestore;
	
	JMenu actionMenu = new JMenu("Action");
	
	private Stacker  stacker;
	
	public ControlPanel(Stacker stacker){
		this.stacker = stacker;
		this.initialize();
	}

	public void initialize() {
		
		buttonStart = new JMenuItem("Start");
		buttonJoinServer = new JMenuItem("Join Server");
		buttonPause = new JMenuItem("Pause");
		buttonResume = new JMenuItem("Resume");
		buttonSave = new JMenuItem("Save");
		buttonRestore = new JMenuItem("Restore");
		this.add(actionMenu);
		actionMenu.add(buttonStart);
		actionMenu.add(buttonJoinServer);
		actionMenu.add(buttonPause);
		actionMenu.add(buttonResume);
		actionMenu.add(buttonSave);
		actionMenu.add(buttonRestore);
		buttonJoinServer.setEnabled(false);
		buttonPause.setEnabled(false);
		buttonResume.setEnabled(false);

		// run button
		buttonStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stacker.start();
				buttonStart.setEnabled(false);
				buttonJoinServer.setEnabled(true);
				buttonPause.setEnabled(true);
			}
		});
		// join server
		buttonJoinServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stacker.joinServer();
				buttonJoinServer.setEnabled(false);
			}
		});
		// pause the game
		buttonPause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(stacker.getStatus() == Status.RUNNING){
					stacker.setStatus(Status.PAUSE);
					buttonResume.setEnabled(true);
					buttonPause.setEnabled(false);
					stacker.pauseGame();
				}
			}
		});
		
		// resume the game
		buttonResume.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(stacker.getStatus() == Status.PAUSE){
					stacker.playGame();
					stacker.setStatus(Status.RUNNING);
					buttonResume.setEnabled(false);
					buttonPause.setEnabled(true);
				}
			}
		});
		
		// resume the game
		buttonSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try (FileOutputStream fos = new FileOutputStream("gameState.bin")) {
					GameStateRecorder gameStateRecorder = new GameStateRecorder();
					new ObjectOutputStream(fos).writeObject(gameStateRecorder.saveGame(stacker));
				} catch (Exception e1) {
					throw new RuntimeException("Unable to save the game", e1);
				}
			}
		});
		
		// resume the game
		buttonRestore.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try (FileInputStream fis = new FileInputStream("gameState.bin")) {
					GameState gameState = (GameState)new ObjectInputStream(fis).readObject();
					GameStateRecorder gameStateRecorder = new GameStateRecorder();
					Stacker restoredStacker = gameStateRecorder.restoreGame(gameState, false);
					StackerGame.getInstance().removeStacker(stacker);
					StackerGame.getInstance().addStacker(restoredStacker);
					restoredStacker.setGameState(gameState);
					stacker = restoredStacker;
				} catch (Exception e1) {
					throw new RuntimeException("Unable to save the game", e1);
				} 
			}
		});
	
	}
	
	public void resetStartButton(){
		buttonStart.requestFocusInWindow();
		buttonStart.setEnabled(true);
		buttonPause.setEnabled(false);
	}
}
