package com.dexstudio.chess.helpers;

import java.util.ArrayList;

import com.dexstudio.chess.entity.ChessFigure;
import com.dexstudio.chess.entity.ChessMove;
import com.dexstudio.chess.entity.ChessMoveType;
import com.dexstudio.chess.entity.FigureColor;
import com.dexstudio.chess.entity.FigureType;

public class MovesCalculator {

	public static ArrayList<ChessMove> getPossibleMoves(ChessFigure cf,
			ChessFigure[][] board,
			FigureColor playerColor, FigureColor opponentColor) {
		
		ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
		
		int figureMoveUpOrDown = (cf.getFigureColor() == FigureColor.WHITE ? -1 : 1);
		
		switch(cf.getFigureName()) {
		case "pawn":
			for(int i=1; i<3; i++) {
				if(cf.isBlack()) {
					if(cf.getX() > 2 && i >= 2) {
						continue;
					}
				} else if(cf.isWhite()) {
					if(cf.getX() < 7 && i >= 2) {
						continue;
					}
				}
				ChessFigure foundCf = getFigureAt(board, cf.getX() + (figureMoveUpOrDown * i), cf.getY());
				if(foundCf == null) {
					continue;
				}
				if(foundCf.isFigure()) {
					continue;
				}
				moves.add(new ChessMove(cf.getX() + (figureMoveUpOrDown * i), cf.getY(), ChessMoveType.CMT_ALLOWED));
			}
			for(int i=-1; i<2; i++) {
				if(i == 0) {
					continue;
				}
				ChessFigure foundCf = getFigureAt(board, cf.getX() + figureMoveUpOrDown, cf.getY() + i);
				if(foundCf == null) {
					continue;
				}
				if(foundCf.isOfColor(opponentColor)) {
					moves.add(new ChessMove(cf.getX() + figureMoveUpOrDown, cf.getY() + i, ChessMoveType.CMT_CAPTURE));
				}
			}
			break;
			
		case "king":
			for(int i=-1; i<2; i++) {
				for(int j=-1; j<2; j++) {
					if((i==0 && j==0)) {
						continue;
					}
					
					ChessFigure foundCf = getFigureAt(board, cf.getX() + i, cf.getY() + j);
					if(foundCf == null) {
						continue;
					}
					
					if(foundCf.isOfColor(playerColor)) {
						continue;
					}
					
					if(foundCf.isOfColor(opponentColor)) {
						moves.add(new ChessMove(cf.getX() + i, cf.getY() + j, ChessMoveType.CMT_CAPTURE));
					} else {
						moves.add(new ChessMove(cf.getX() + i, cf.getY() + j, ChessMoveType.CMT_ALLOWED));
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
						ChessFigure foundCf = getFigureAt(board, cf.getX() + (i*k), cf.getY() + (j*k));
						if(foundCf == null) {
							continue;
						}
						if(foundCf.isOfColor(playerColor)) {
							break;
						}
						
						if(foundCf.isOfColor(opponentColor)) {
							moves.add(new ChessMove(cf.getX() + (i*k), cf.getY() + (j*k), ChessMoveType.CMT_CAPTURE));
							break;
						}
						
						moves.add(new ChessMove(cf.getX() + (i*k), cf.getY() + (j*k), ChessMoveType.CMT_ALLOWED));
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
						ChessFigure foundCf = getFigureAt(board, cf.getX() + (i*k), cf.getY() + (j*k));
						if(foundCf == null) {
							continue;
						}
						if(foundCf.isOfColor(playerColor)) {
							break;
						}
						
						if(foundCf.isOfColor(opponentColor)) {
							moves.add(new ChessMove(cf.getX() + (i*k), cf.getY() + (j*k), ChessMoveType.CMT_CAPTURE));
							break;
						}
						
						moves.add(new ChessMove(cf.getX() + (i*k), cf.getY() + (j*k), ChessMoveType.CMT_ALLOWED));
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
						ChessFigure foundCf = getFigureAt(board, cf.getX() + (i*k), cf.getY() + (j*k));
						if(foundCf == null) {
							continue;
						}
						if(foundCf.isOfColor(playerColor)) {
							break;
						}
						if(foundCf.isOfColor(opponentColor)) {
							moves.add(new ChessMove(cf.getX() + (i*k), cf.getY() + (j*k), ChessMoveType.CMT_CAPTURE));
							break;
						}
						moves.add(new ChessMove(cf.getX() + (i*k), cf.getY() + (j*k), ChessMoveType.CMT_ALLOWED));
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
					
					ChessFigure foundCf = getFigureAt(board, cf.getX() + i, cf.getY() + j);
					if(foundCf == null) {
						continue;
					}
					
					if(foundCf.isOfColor(opponentColor)) {
						moves.add(new ChessMove(cf.getX() + i, cf.getY() + j, ChessMoveType.CMT_CAPTURE));
					} else if(!foundCf.isOfColor(playerColor)) {
						moves.add(new ChessMove(cf.getX() + (i), cf.getY() + (j), ChessMoveType.CMT_ALLOWED));
					}
				}
			}
			break;
		}
		
		return moves;
	}
	
	public static ChessFigure getFigureAt(ChessFigure[][] board, int x, int y) {
		if(x < 1 || x > 8 || y < 1 || y > 8) {
			return null;
		}
		return board[x-1][y-1];
	}
	
	public static ChessFigure moveFigureTo(ChessFigure[][] board, ChessFigure cf2Move, int newX, int newY) {
		ChessFigure target = board[newX - 1][newY - 1];
		if(target.isNotFigure()) {
			board[newX - 1][newY - 1] = cf2Move;
			board[cf2Move.getX() - 1][cf2Move.getY() - 1] = target;
			cf2Move.setXY(newX, newY);
		} else {
			board[newX - 1][newY - 1] = cf2Move;
			board[cf2Move.getX() - 1][cf2Move.getY() - 1] = new ChessFigure(FigureType.EMPTY);
			cf2Move.setXY(newX, newY);
		}
		return target;
	}
	
	public static void bringBackTo(ChessFigure[][] board, ChessFigure cf2Move, int newX, int newY) {
		board[newX - 1][newY - 1] = cf2Move;
	}
}
