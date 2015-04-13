package com.dexstudio.chess.entity;

import android.graphics.Point;

public class ChessMove extends Point {
	
	private ChessMoveType cmt = null;
	
	public ChessMove(int x, int y, ChessMoveType cmt) {
		super(x, y);
		this.cmt = cmt; 
	}
	
	public ChessMoveType getMoveType() {
		return this.cmt;
	}
	
	public void setMoveType(ChessMoveType cmt) {
		this.cmt = cmt;
	}
	
}
