package com.game.stacker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.game.stacker.network.GameClient;
import com.game.stacker.network.message.ActionMessage;
import com.game.stacker.state.BlockStatus;
import com.game.stacker.state.GameState;
import com.game.stacker.state.GameStateRecorder;
import com.game.stacker.state.StatsWriter;

public class Stacker extends javax.swing.JPanel {
	
	private static final long serialVersionUID = 1L;
	final static int PLATFORM_WIDTH = 7;
	final static int PLATFORM_LEVEL = 15;
	final static int BLOCK_SIZE = 30;
	final static int MOVING_BLOCK_WIDTH = 5;
	GridLayout layout = new GridLayout(PLATFORM_LEVEL, PLATFORM_WIDTH);
	final Block[][] blocks = new Block[PLATFORM_LEVEL][PLATFORM_WIDTH];
	final JPanel mainPanel = new JPanel();
	final StatsWriter stats = new StatsWriter(); 
	private MovingBlocks movingBlocks;
	private String gamer;
	private int nextLevelKey;
	private GameClient gameClient;
	private boolean networkGamer;
	private GameState gameState;
	
	private Thread movingBlockThread;


	public Stacker(String gamer, int nextLevelKey, boolean isNetworkGamer){
		this.gamer = gamer;
		this.nextLevelKey = nextLevelKey;
		this.networkGamer = isNetworkGamer;
		initialize();
	}

	private void initialize() {
		mainPanel.setLayout(layout);
		for (int i = 0; i < blocks.length; i++) {
			for (int j = 0; j < blocks[i].length; j++) {
				blocks[i][j] = new Block(i, j);
				blocks[i][j].unLight();
				blocks[i][j].setPreferredSize(new Dimension(BLOCK_SIZE, BLOCK_SIZE));
				blocks[i][j].setBorder(BorderFactory.createLineBorder(Color.GRAY));
				mainPanel.add(blocks[i][j]);
			}
		}
		this.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		JLabel gamerLabel = new JLabel(gamer);
		gamerLabel.setFont(new Font("Arial Black", Font.BOLD, 25));
		gamerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		this.setLayout(new BorderLayout());
		this.add(gamerLabel, BorderLayout.NORTH);
		this.add(mainPanel, BorderLayout.CENTER);
	}
	
	public void restoreGame(GameState gameState, boolean networkGamer){
		if(movingBlockThread != null) {
			movingBlockThread.interrupt();
		}
		this.gamer = gameState.getGamer();
		this.networkGamer = networkGamer;
		this.nextLevelKey = networkGamer?0:gameState.getNextLevelKey();
		this.unLightBoard();
		for(BlockStatus blockStatus : gameState.getBlockStatusList()){
			this.getBlocks()[blockStatus.getRow()][blockStatus.getCol()].setColor(blockStatus.getColor());
		}
		this.setGameState(gameState);
		this.start();
	}

	public void start() {
		if(this.getStatus()==Status.LOSE || this.getStatus() == Status.WIN) {
			this.unLightBoard();
		}
		if(gameState == null){
			movingBlocks = new MovingBlocks(MOVING_BLOCK_WIDTH, PLATFORM_WIDTH, PLATFORM_LEVEL, blocks);
		} else {
			movingBlocks = new MovingBlocks(gameState, PLATFORM_WIDTH, PLATFORM_LEVEL, blocks);
		}
		movingBlockThread = new Thread(new BlocksMovingThread());
		movingBlockThread.start();
	}
	
	private void endGame(Status status) {
		if (status.equals(Status.WIN)){
			stats.addWin();
		} else {
			stats.addLoss();
		}
		StackerGame.getInstance().getControlPanel().resetStartButton();
		
	}

	// This method will unlight all of the blocks on the board
	public void unLightBoard() {
		for (int i = 0; i < blocks.length; i++){
			for (int j = 0; j < blocks[0].length; j++){
				blocks[i][j].unLight();
			}
		}
	}
	
	
	private class BlocksMovingThread implements Runnable {
		
		@Override
		public void run() {
			movingBlocks.setStatus(Status.RUNNING);
			while(true){
				try {
					movingBlocks.move();
					Status status = movingBlocks.getStatus();
					if(status == Status.LOSE || status == Status.WIN || status == Status.ABORT){
						endGame(status);
						break;
					}
				} catch(Exception e){
					throw new RuntimeException("Unable to move the movingblock", e);
				}
			}
		}
	}

	public void setMovingBlocks(MovingBlocks movingBlocks) {
		this.movingBlocks = movingBlocks;
	}

	public int getNextLevelKey() {
		return nextLevelKey;
	}
	
	public void moveToNextLevel(){
		movingBlocks.moveToNextLevel();
		if(gameClient!=null){
			gameClient.sentMessage(new ActionMessage(gamer, gameClient.getClientIdentifier(), new GameStateRecorder().saveGame(this)));
		}
	}
	
	public Status getStatus(){
		if(movingBlocks == null){
			return null;
		}
		return movingBlocks.getStatus();
	}
	
	public void setStatus(Status status){
		if(movingBlocks != null){
			movingBlocks.setStatus(status);
		}
	}
	
	
	public void joinServer() {
		if(gameClient == null){
			gameClient = new GameClient(gamer);
			gameClient.startClient();
		}
	}
	
	public void pauseGame(){
		this.setStatus(Status.PAUSE);
	}
	
	public void playGame(){
		this.setStatus(Status.RUNNING);
	}

	public boolean isNetworkGamer() {
		return networkGamer;
	}

	public MovingBlocks getMovingBlocks() {
		return movingBlocks;
	}

	public String getGamer() {
		return gamer;
	}
	
	public Block[][] getBlocks(){
		return blocks;
	}

	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	public GameClient getGameClient() {
		return gameClient;
	}
	
	
}