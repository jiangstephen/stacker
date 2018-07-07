package com.game.stacker.state;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import com.game.stacker.Direction;

public class GameState implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Set<BlockStatus> blockStatusList = new LinkedHashSet<>();
	
	private int level;
	
	private Direction movingDirection;
	
	private int leftBorder;
	
	private int rightBorder;
	
	private int numOfBlocks;
	
	private String gamer;
	
	private int nextLevelKey;
	
	private int leftPoistion;

	public Set<BlockStatus> getBlockStatusList() {
		return blockStatusList;
	}
	
	public void addBlockStatus(BlockStatus blockStatus){
		this.blockStatusList.add(blockStatus);
	}
	
	public void removeBlockStatus(BlockStatus blockStatus){
		this.blockStatusList.remove(blockStatus);
	}
	

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Direction getMovingDirection() {
		return movingDirection;
	}

	public void setMovingDirection(Direction movingDirection) {
		this.movingDirection = movingDirection;
	}

	public int getLeftBorder() {
		return leftBorder;
	}

	public void setLeftBorder(int leftBorder) {
		this.leftBorder = leftBorder;
	}

	public int getRightBorder() {
		return rightBorder;
	}

	public void setRightBorder(int rightBorder) {
		this.rightBorder = rightBorder;
	}

	public int getNumOfBlocks() {
		return numOfBlocks;
	}

	public void setNumOfBlocks(int numOfBlocks) {
		this.numOfBlocks = numOfBlocks;
	}

	public String getGamer() {
		return gamer;
	}

	public void setGamer(String gamer) {
		this.gamer = gamer;
	}

	public int getNextLevelKey() {
		return nextLevelKey;
	}

	public void setNextLevelKey(int nextLevelKey) {
		this.nextLevelKey = nextLevelKey;
	}

	public int getLeftPoistion() {
		return leftPoistion;
	}

	public void setLeftPoistion(int leftPoistion) {
		this.leftPoistion = leftPoistion;
	}

}
