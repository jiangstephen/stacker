package com.game.stacker;

import com.game.stacker.state.GameState;

public class MovingBlocks {

	private int numOfBlocks;

	private int currentLevel = 1;

	private final int platformWidth;

	private final int platformLevel;

	private int leftPosition = 0;

	private final int INIT_SPEED = 100;
	
	private final int FINAL_SPEED = 50;

	private Status status = Status.READY;

	private Direction direction = Direction.Right;

	private Integer leftBorder = 0;

	private Integer rightBorder;

	private final Block[][] blocks;

	public MovingBlocks(int numOfBlocks, int platformWidth, int platformLevel, Block[][] blocks) {
		this.numOfBlocks = numOfBlocks;
		this.platformLevel = platformLevel;
		this.platformWidth = platformWidth;
		this.rightBorder = platformWidth - 1;
		this.blocks = blocks;
	}
	
	public MovingBlocks(GameState gameState, int platformWidth, int platformLevel, Block[][] blocks){
		this(gameState.getNumOfBlocks(), platformWidth, platformLevel, blocks);
		this.currentLevel = gameState.getLevel();
		this.leftPosition = gameState.getLeftPoistion();
		this.leftBorder = gameState.getLeftBorder();
		this.rightBorder = gameState.getRightBorder();
		this.direction = gameState.getMovingDirection();
	}

	public void move() {
		try {
			Thread.sleep(INIT_SPEED - (INIT_SPEED - FINAL_SPEED)/platformLevel * currentLevel);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		if(status == Status.PAUSE){
			return;
		}
   		unLight();
		if (hitPlatformBooder()) {
			switchDirection();
		}
		if (direction == Direction.Right) {
			leftPosition++;
		} else {
			leftPosition--;
		}
		light();

	}

	public void moveToNextLevel() {
		unLight();
		if (currentLevel == 1) {
			leftBorder = leftPosition;
			rightBorder = leftPosition + numOfBlocks;
		}
		if (currentLevel > 1) {
			if (leftPosition < leftBorder) {
				int offset = leftBorder - leftPosition; 
				trimLeft(offset >= numOfBlocks ? numOfBlocks : offset);
			}
			if (leftPosition + numOfBlocks > rightBorder) {
				int offset = (leftPosition + numOfBlocks) - rightBorder;
				trimRight(offset >= numOfBlocks ? numOfBlocks : offset);
			}
			leftBorder = leftPosition;
			rightBorder = leftPosition + numOfBlocks;
		}
		light();
		if(currentLevel == platformLevel){
			this.status = Status.WIN;
			return;
		}
		currentLevel++;
		move();

	}

	private void trimLeft(int numOfBlocksOffSet) {
		numOfBlocks = numOfBlocks - numOfBlocksOffSet;
		for(int i=0; i<numOfBlocksOffSet; i++){
			blocks[platformLevel - currentLevel][leftPosition + i].missed();
		}
		if (numOfBlocks <= 0) {
			status = Status.LOSE;
			return;
		}
		leftPosition = leftPosition + numOfBlocksOffSet;
		
	}

	private void trimRight(int numOfBlocksOffSet) {
		for(int i=1; i<=numOfBlocksOffSet; i++){
			blocks[platformLevel - currentLevel][leftPosition + numOfBlocks - i].missed();
		}
		numOfBlocks = numOfBlocks - numOfBlocksOffSet;
		if (numOfBlocks <= 0) {
			status = Status.LOSE;
			return;
		}
		
	}

	private boolean hitPlatformBooder() {
		if (direction == Direction.Left && leftPosition == 0) {
			return true;
		}
		if (direction == Direction.Right && numOfBlocks + leftPosition == platformWidth) {
			return true;
		}
		return false;
	}

	private void switchDirection() {
		direction = direction.switchDirection();
	}

	public void light() {
		for (int i = 0; i < numOfBlocks && leftPosition + i < platformWidth; i++) {
			blocks[platformLevel - currentLevel][leftPosition + i].light();
		}
	}

	public void unLight() {
		for (int i = 0; i < numOfBlocks && leftPosition + i < platformWidth; i++) {
			blocks[platformLevel - currentLevel][leftPosition + i].unLight();
		}
	}

	public int getNumOfBlocks() {
		return numOfBlocks;
	}

	public void setNumOfBlocks(int numOfBlocks) {
		this.numOfBlocks = numOfBlocks;
	}

	public int getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}

	public int getLeftPosition() {
		return leftPosition;
	}

	public void setLeftPosition(int leftPosition) {
		this.leftPosition = leftPosition;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) { 
		this.status = status;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public int getPlatformWidth() {
		return platformWidth;
	}

	public int getPlatformLevel() {
		return platformLevel;
	}

	public Integer getLeftBorder() {
		return leftBorder;
	}

	public Integer getRightBorder() {
		return rightBorder;
	}

	public Block[][] getBlocks() {
		return blocks;
	}
	
	
	
}

