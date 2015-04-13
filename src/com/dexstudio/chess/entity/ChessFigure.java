package com.dexstudio.chess.entity;

public class ChessFigure {
	
	private FigureType ft = null;
	private int x = 0;
	private int y = 0;
	
	public ChessFigure(FigureType ft) {
		this.ft = ft;
		this.x = ft.x;
		this.y = ft.y;
	}
	
	public String getFigureName() {
		return this.ft.name;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	enum FigureType {
		
		PAWN_1		(2, 1, "pawn"),
		PAWN_2		(2, 2, "pawn"),
		PAWN_3		(2, 3, "pawn"),
		PAWN_4		(2, 4, "pawn"),
		PAWN_5		(2, 5, "pawn"),
		PAWN_6		(2, 6, "pawn"),
		PAWN_7		(2, 7, "pawn"),
		PAWN_8		(2, 8, "pawn"),
		ROOK_LEFT	(1, 1, "rook"),
		ROOK_RIGHT	(1, 8, "rook"),
		KNIGHT_LEFT	(1, 2, "knight"),
		KNIGHT_RIGHT(1, 7, "knight"),
		BISHOP_LEFT	(1, 3, "bishop"),
		BISHOP_RIGHT(1, 6, "bishop"),
		QUEEN		(1, 4, "queen"),
		KING		(1, 5, "king");
		
		private int x;
		private int y;
		private String name;
		
		FigureType(int x, int y, String name) {
			this.x = x;
			this.y = y;
			this.name = name;
		}
	}
	
}
