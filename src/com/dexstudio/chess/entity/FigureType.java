package com.dexstudio.chess.entity;

public enum FigureType {
	
	PAWN_B_8		(2, 1, "pawn", FigureColor.BLACK),
	PAWN_B_7		(2, 2, "pawn", FigureColor.BLACK),
	PAWN_B_6		(2, 3, "pawn", FigureColor.BLACK),
	PAWN_B_5		(2, 4, "pawn", FigureColor.BLACK),
	PAWN_B_4		(2, 5, "pawn", FigureColor.BLACK),
	PAWN_B_3		(2, 6, "pawn", FigureColor.BLACK),
	PAWN_B_2		(2, 7, "pawn", FigureColor.BLACK),
	PAWN_B_1		(2, 8, "pawn", FigureColor.BLACK),
	ROOK_B_LEFT	(1, 1, "rook", FigureColor.BLACK),
	ROOK_B_RIGHT	(1, 8, "rook", FigureColor.BLACK),
	KNIGHT_B_LEFT	(1, 2, "knight", FigureColor.BLACK),
	KNIGHT_B_RIGHT(1, 7, "knight", FigureColor.BLACK),
	BISHOP_B_LEFT	(1, 3, "bishop", FigureColor.BLACK),
	BISHOP_B_RIGHT(1, 6, "bishop", FigureColor.BLACK),
	QUEEN_B		(1, 4, "queen", FigureColor.BLACK),
	KING_B		(1, 5, "king", FigureColor.BLACK),
	//White Figures
	PAWN_W_1		(7, 1, "pawn", FigureColor.WHITE),
	PAWN_W_2		(7, 2, "pawn", FigureColor.WHITE),
	PAWN_W_3		(7, 3, "pawn", FigureColor.WHITE),
	PAWN_W_4		(7, 4, "pawn", FigureColor.WHITE),
	PAWN_W_5		(7, 5, "pawn", FigureColor.WHITE),
	PAWN_W_6		(7, 6, "pawn", FigureColor.WHITE),
	PAWN_W_7		(7, 7, "pawn", FigureColor.WHITE),
	PAWN_W_8		(7, 8, "pawn", FigureColor.WHITE),
	ROOK_W_LEFT		(8, 1, "rook", FigureColor.WHITE),
	ROOK_W_RIGHT	(8, 8, "rook", FigureColor.WHITE),
	KNIGHT_W_LEFT	(8, 2, "knight", FigureColor.WHITE),
	KNIGHT_W_RIGHT	(8, 7, "knight", FigureColor.WHITE),
	BISHOP_W_LEFT	(8, 3, "bishop", FigureColor.WHITE),
	BISHOP_W_RIGHT	(8, 6, "bishop", FigureColor.WHITE),
	QUEEN_W			(8, 4, "queen", FigureColor.WHITE),
	KING_W			(8, 5, "king", FigureColor.WHITE),
	
	EMPTY			(-1, -1, "empty", FigureColor.NONE)
	;
	
	private int x;
	private int y;
	private String name;
	private FigureColor fc;
	
	FigureType(int x, int y, String name, FigureColor fc) {
		this.setX(x);
		this.setY(y);
		this.setName(name);
		this.setFc(fc);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FigureColor getFc() {
		return fc;
	}

	public void setFc(FigureColor fc) {
		this.fc = fc;
	}
	
	public static FigureType findFigureTypeByCoord(int x, int y) {
		for(FigureType ft : FigureType.values()) {
			if(ft.x == x && ft.y == y) {
				return ft;
			}
		}
		
		return FigureType.EMPTY;
	}
}
