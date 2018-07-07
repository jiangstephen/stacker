package com.game.stacker;

import java.awt.Color;
import javax.swing.JPanel;

public class Block extends JPanel {

	private static final long serialVersionUID = 1L;
	final int row, column;
	private boolean lit = false;

	public Block(int column, int row) {
		this.column = column;
		this.row = row;
	}

	public void light() {
		lit = true;
		this.setBackground(Color.YELLOW);
	}

	public void unLight() {
		lit = false;
		this.setBackground(Color.BLACK);
	}
	
	public void missed(){
		this.setBackground(Color.RED);
	}
	
	public void setColor(Color color){
		this.setBackground(color);
	}

	public boolean isLit() {
		return lit;
	}
}