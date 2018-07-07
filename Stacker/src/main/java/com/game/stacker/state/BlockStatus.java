package com.game.stacker.state;

import java.awt.Color;
import java.io.Serializable;

public class BlockStatus implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private final int row;
	
	private final int col;
	
	private final Color color;
	
	public BlockStatus(int row, int col, Color color){
		this.row = row;
		this.col = col;
		this.color = color;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public Color getColor() {
		return color;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + col;
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlockStatus other = (BlockStatus) obj;
		if (col != other.col)
			return false;
		if (row != other.row)
			return false;
		return true;
	}
	
	

}
