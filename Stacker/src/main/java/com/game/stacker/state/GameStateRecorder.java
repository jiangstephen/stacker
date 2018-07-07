package com.game.stacker.state;

import java.awt.Color;

import com.game.stacker.Block;
import com.game.stacker.MovingBlocks;
import com.game.stacker.Stacker;

public class GameStateRecorder {
	
	public GameState saveGame(Stacker stacker){
		MovingBlocks movingBlocks = stacker.getMovingBlocks();
		GameState gameState = new GameState();
		gameState.setLeftBorder(movingBlocks.getLeftBorder());
		
		gameState.setLevel(movingBlocks.getCurrentLevel());
		gameState.setMovingDirection(movingBlocks.getDirection());
		gameState.setNumOfBlocks(movingBlocks.getNumOfBlocks());
		gameState.setRightBorder(movingBlocks.getRightBorder());
		gameState.setNextLevelKey(stacker.getNextLevelKey());
		gameState.setGamer(stacker.getGamer());
		gameState.setLeftPoistion(movingBlocks.getLeftPosition());
		for(int row = 0 ; row < stacker.getBlocks().length ; row ++){
			Block[] blocks = stacker.getBlocks()[row]; 
			for ( int col = 0 ; col < blocks.length ; col++ ){
				Color bgColor = blocks[col].getBackground(); 
				if(Color.YELLOW.equals(bgColor) || Color.RED.equals(bgColor)){
					gameState.addBlockStatus(new BlockStatus(row, col, bgColor));
				} 
			} 
		}
		return gameState;
	}
	
	public Stacker restoreGame(GameState gameState, boolean networkGamer){
		Stacker stacker = new Stacker(gameState.getGamer(), networkGamer?0:gameState.getNextLevelKey(), networkGamer);
		for(BlockStatus blockStatus : gameState.getBlockStatusList()){
			stacker.getBlocks()[blockStatus.getRow()][blockStatus.getCol()].setColor(blockStatus.getColor());
		}
		stacker.setGameState(gameState);
		return stacker;
	}
}
