package com.game.stacker;

public enum Direction {
	
	Left {
		@Override
		public Direction switchDirection() {
			return Right;
		}
	}, Right {
		@Override
		public Direction switchDirection() {
			return Left;
		}
	};
	
	public abstract Direction switchDirection(); 

}
