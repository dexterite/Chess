package com.dexstudio.chess.entity;

import java.util.ArrayList;

import android.util.Log;

import com.dexstudio.chess.entity.ChessFigure.FigureType;

public class ChessBoard {
	
	private ArrayList<ChessFigure> player = new ArrayList<ChessFigure>();
	private ArrayList<ChessFigure> computer = new ArrayList<ChessFigure>();
	
	public ChessBoard() {
		initGame();
	}

	private void initGame() {
		for(FigureType ft : FigureType.values()) {
			player.add(new ChessFigure(ft));
			computer.add(new ChessFigure(ft));
		}
	}
	
	public ArrayList<ChessFigure> getPlayer() {
		return this.player;
	}
	
	public ArrayList<ChessFigure> getComputer() {
		return this.computer;
	}
	
	public void movePlayerFigure(int x, int y, int moveX, int moveY) {
		for(ChessFigure cf : player) {
			if(cf.getX() == x && cf.getY() == y) {
				cf.setXY(moveX, moveY);
			}
		}
	}
	
	public void moveComputerFigure(int x, int y, int moveX, int moveY) {
		for(ChessFigure cf : computer) {
			if(cf.getX() == x && cf.getY() == y) {
				cf.setXY(moveX, moveY);
			}
		}
	}
	
	public ChessFigure getPlayerFigure(int x, int y) {
		for(ChessFigure cf : player) {
			if(cf.getX() == x && cf.getY() == y) {
				return cf;
			}
		}
		return null;
	}
	
	public ChessFigure getComputerFigure(int x, int y) {
		for(ChessFigure cf : computer) {
			if(cf.getX() == x && cf.getY() == y) {
				return cf;
			}
		}
		return null;
	}
	
	public ArrayList<ChessMove> getPossibleMoves(ChessFigure moveCf) {
		ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
		switch(moveCf.getFigureName()) {
		case "pawn":
			for(int i=1; i<3; i++) {
				if(moveCf.getX() > 2 && i >= 2) {
					continue;
				}
				if(this.getPlayerFigure(moveCf.getX() + i, moveCf.getY()) == null && 
						this.getComputerFigure(9 - (moveCf.getX() + i), moveCf.getY()) == null) {
					moves.add(new ChessMove(moveCf.getX() + i, moveCf.getY(), ChessMoveType.CMT_ALLOWED));
				}
			}
			
			for(int i=-1; i<2; i++) {
				if(i == 0) {
					continue;
				}
				if(this.getPlayerFigure(moveCf.getX() + 1, moveCf.getY() + i) == null && 
						this.getComputerFigure(9 - (moveCf.getX() + 1), moveCf.getY() + i) != null) {
					moves.add(new ChessMove(moveCf.getX() + 1, moveCf.getY() + i, ChessMoveType.CMT_CAPTURE));
				}
			}
			
			break;
			
		case "king":
			for(int i=-1; i<2; i++) {
				for(int j=-1; j<2; j++) {
					if((i==0 && j==0) || (this.getPlayerFigure(moveCf.getX() + i, moveCf.getY() + j) != null)) {
						continue;
					}
					
					if(this.getComputerFigure(9 - (moveCf.getX() + i), moveCf.getY() + j) != null) {
						moves.add(new ChessMove(moveCf.getX() + i, moveCf.getY() + j, ChessMoveType.CMT_CAPTURE));
					} else {
						moves.add(new ChessMove(moveCf.getX() + i, moveCf.getY() + j, ChessMoveType.CMT_ALLOWED));
					}
				}
			}
			break;
		case "queen":
			
			for(int i=-1; i<2; i++) {
				for(int j=-1; j<2; j++) {
					for(int k=1; k<8; k++) {
						if(i==0 && j==0) {
							continue;
						}
						if(this.getPlayerFigure(moveCf.getX() + (i*k), moveCf.getY() + (j*k)) != null) {
							break;
						}
						
						if(this.getComputerFigure(9 - (moveCf.getX() + i*k), moveCf.getY() + (j*k)) != null) {
							moves.add(new ChessMove(moveCf.getX() + (i*k), moveCf.getY() + (j*k), ChessMoveType.CMT_CAPTURE));
							break;
						}
						
						moves.add(new ChessMove(moveCf.getX() + (i*k), moveCf.getY() + (j*k), ChessMoveType.CMT_ALLOWED));
					}
				}
			}
			break;
		case "bishop":
			for(int i=-1; i<2; i++) {
				for(int j=-1; j<2; j++) {
					for(int k=1; k<8; k++) {
						if(i==0 || j==0) {
							continue;
						}
						if(this.getPlayerFigure(moveCf.getX() + (i*k), moveCf.getY() + (j*k)) != null) {
							break;
						}
						
						if(this.getComputerFigure(9 - (moveCf.getX() + i*k), moveCf.getY() + (j*k)) != null) {
							moves.add(new ChessMove(moveCf.getX() + (i*k), moveCf.getY() + (j*k), ChessMoveType.CMT_CAPTURE));
							break;
						}
						
						moves.add(new ChessMove(moveCf.getX() + (i*k), moveCf.getY() + (j*k), ChessMoveType.CMT_ALLOWED));
					}
				}
			}
			break;
		case "rook":
			for(int i=-1; i<2; i++) {
				for(int j=-1; j<2; j++) {
					for(int k=1; k<8; k++) {
						if((!(i==0 || j==0)) || (i==0 && j==0)) {
							continue;
						}
						if(this.getPlayerFigure(moveCf.getX() + (i*k), moveCf.getY() + (j*k)) != null) {
							break;
						}
						if(this.getComputerFigure(9 - (moveCf.getX() + i*k), moveCf.getY() + (j*k)) != null) {
							moves.add(new ChessMove(moveCf.getX() + (i*k), moveCf.getY() + (j*k), ChessMoveType.CMT_CAPTURE));
							break;
						}
						moves.add(new ChessMove(moveCf.getX() + (i*k), moveCf.getY() + (j*k), ChessMoveType.CMT_ALLOWED));
					}
				}
			}
			break;
		case "knight":
			for(int i=-2; i<3; i++) {
				for(int j=-2; j<3; j++) {
					if(Math.abs(Math.abs(i) - Math.abs(j)) != 1 || (i==0 || j==0)) {
						continue;
					}
					if(this.getComputerFigure(9 - (moveCf.getX() + i), moveCf.getY() + j) != null) {
						moves.add(new ChessMove(moveCf.getX() + i, moveCf.getY() + j, ChessMoveType.CMT_CAPTURE));
					} else if(this.getPlayerFigure(moveCf.getX() + (i), moveCf.getY() + (j)) == null) {
						moves.add(new ChessMove(moveCf.getX() + (i), moveCf.getY() + (j), ChessMoveType.CMT_ALLOWED));
					}
				}
			}
			break;
		}
		
		//Filter Moves
		/*ArrayList<ChessMove> filteredMoves = new ArrayList<ChessMove>();
		for(ChessMove cm : moves) {
			//Check to not step onto own figure
			for(ChessFigure cf : player) {
				if(cf.getX() == cm.x && cf.getY() == cm.y) {
					cm.setMoveType(ChessMoveType.CMT_FORBIDDEN);
					filteredMoves.add(cm);
				}
			}
			
			//Check if can be captured
			for(ChessFigure cf : computer) {
				if(cf.getX() == cm.x && cf.getY() == cm.y && cm.getMoveType() == ChessMoveType.CMT_CAPTURE) {
					filteredMoves.add(cm);
				} else {
					cm.setMoveType(ChessMoveType.CMT_ALLOWED);
					filteredMoves.add(cm);
				}
			}
		}*/
		
		return moves;
	}
	
	public MoveMessage movePlayerFigure(ChessFigure moveCf, int x, int y) {
		//TODO Write Check to Move Figure
		//Check if there is not other players figure
		for(ChessFigure cf : player) {
			if(cf.getX() == x && cf.getY() == y) {
				return MoveMessage.ERROR_OVERLAP;
			}
		}
		
		moveCf.setXY(x, y);
		return MoveMessage.SUCCESS;
	}
}
