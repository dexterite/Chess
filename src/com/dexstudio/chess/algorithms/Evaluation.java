package com.dexstudio.chess.algorithms;

import java.util.ArrayList;

import com.dexstudio.chess.entity.ChessFigure;
import com.dexstudio.chess.entity.ChessMove;
import com.dexstudio.chess.entity.ChessMoveType;
import com.dexstudio.chess.entity.FigureColor;
import com.dexstudio.chess.helpers.MovesCalculator;

public class Evaluation {
	
	public static int[] pawns = {
		 0,  0,  0,  0,  0,  0,  0,  0,
		 50, 50, 50, 50, 50, 50, 50, 50,
		 10, 10, 20, 30, 30, 20, 10, 10,
		  5,  5, 10, 25, 25, 10,  5,  5,
		  0,  0,  0, 20, 20,  0,  0,  0,
		  5, -5,-10,  0,  0,-10, -5,  5,
		  5, 10, 10,-20,-20, 10, 10,  5,
		  0,  0,  0,  0,  0,  0,  0,  0
	};
	
	public static int[] knights = {
		-50,-40,-30,-30,-30,-30,-40,-50,
		-40,-20,  0,  0,  0,  0,-20,-40,
		-30,  0, 10, 15, 15, 10,  0,-30,
		-30,  5, 15, 20, 20, 15,  5,-30,
		-30,  0, 15, 20, 20, 15,  0,-30,
		-30,  5, 10, 15, 15, 10,  5,-30,
		-40,-20,  0,  5,  5,  0,-20,-40,
		-50,-40,-30,-30,-30,-30,-40,-50,
	};
	
	public static int[] bishops = {
		-20,-10,-10,-10,-10,-10,-10,-20,
		-10,  0,  0,  0,  0,  0,  0,-10,
		-10,  0,  5, 10, 10,  5,  0,-10,
		-10,  5,  5, 10, 10,  5,  5,-10,
		-10,  0, 10, 10, 10, 10,  0,-10,
		-10, 10, 10, 10, 10, 10, 10,-10,
		-10,  5,  0,  0,  0,  0,  5,-10,
		-20,-10,-10,-10,-10,-10,-10,-20,
	};
	
	public static int[] rooks = {
		  0,  0,  0,  0,  0,  0,  0,  0,
		  5, 10, 10, 10, 10, 10, 10,  5,
		 -5,  0,  0,  0,  0,  0,  0, -5,
		 -5,  0,  0,  0,  0,  0,  0, -5,
		 -5,  0,  0,  0,  0,  0,  0, -5,
		 -5,  0,  0,  0,  0,  0,  0, -5,
		 -5,  0,  0,  0,  0,  0,  0, -5,
		  0,  0,  0,  5,  5,  0,  0,  0
	};
	
	public static int[] queens = {
		//queen
		-20,-10,-10, -5, -5,-10,-10,-20,
		-10,  0,  0,  0,  0,  0,  0,-10,
		-10,  0,  5,  5,  5,  5,  0,-10,
		 -5,  0,  5,  5,  5,  5,  0, -5,
		  0,  0,  5,  5,  5,  5,  0, -5,
		-10,  5,  5,  5,  5,  5,  0,-10,
		-10,  0,  5,  0,  0,  0,  0,-10,
		-20,-10,-10, -5, -5,-10,-10,-20
	};
	
	public static int[] kings = {
		-30,-40,-40,-50,-50,-40,-40,-30,
		-30,-40,-40,-50,-50,-40,-40,-30,
		-30,-40,-40,-50,-50,-40,-40,-30,
		-30,-40,-40,-50,-50,-40,-40,-30,
		-20,-30,-30,-40,-40,-30,-30,-20,
		-10,-20,-20,-20,-20,-20,-20,-10,
		 20, 20,  0,  0,  0,  0, 20, 20,
		 20, 30, 10,  0,  0, 10, 30, 20
	};
	
	public static int simpleEval(ChessFigure[][] board, FigureColor player, FigureColor opposite) {
		
		int maxPawns = 0;
		int maxBishops = 0;
		int maxRooks = 0;
		int maxQueens = 0;
		int maxKings = 0;
		
		for(int i=0; i<board.length; i++) {
			for(int j=0; j<board[i].length; j++) {
				ChessFigure cf = board[i][j];
				if(cf.isNotFigure()) {
					continue;
				}
				
				if(cf.isOfColor(player)) {
					if(cf.getFigureName() == "pawn") {
						maxPawns += pawns[(cf.getX()-1) * 8 + (cf.getY()-1)];
					}
					if(cf.getFigureName() == "bishop") {
						maxBishops += bishops[(cf.getX()-1) * 8 + (cf.getY()-1)];
					}
					if(cf.getFigureName() == "knight") {
						maxBishops += knights[(cf.getX()-1) * 8 + (cf.getY()-1)];
					}
					if(cf.getFigureName() == "queen") {
						maxQueens += queens[(cf.getX()-1) * 8 + (cf.getY()-1)];
					}
					if(cf.getFigureName() == "king") {
						maxKings += kings[(cf.getX()-1) * 8 + (cf.getY()-1)];
					}
				} else {
					if(cf.getFigureName() == "pawn") {
						maxPawns -= pawns[(cf.getX()-1) * 8 + (cf.getY()-1)];
					}
					if(cf.getFigureName() == "bishop") {
						maxBishops -= bishops[(cf.getX()-1) * 8 + (cf.getY()-1)];
					}
					if(cf.getFigureName() == "knight") {
						maxBishops -= knights[(cf.getX()-1) * 8 + (cf.getY()-1)];
					}
					if(cf.getFigureName() == "queen") {
						maxQueens -= queens[(cf.getX()-1) * 8 + (cf.getY()-1)];
					}
					if(cf.getFigureName() == "king") {
						maxKings -= kings[(cf.getX()-1) * 8 + (cf.getY()-1)];
					}
				}
			}
		}
		
		return 200*maxKings + 9*maxQueens + 5*maxRooks + 3*maxBishops + 1*maxPawns;
	}
	
	public static int evaluate(ChessFigure[][] board, FigureColor player, FigureColor opposite) {
		
		int maxPawns = 0;
		int maxBishops = 0;
		int maxRooks = 0;
		int maxQueens = 0;
		int maxKings = 0;
		
		for(int i=0; i<board.length; i++) {
			for(int j=0; j<board[i].length; j++) {
				ChessFigure cf = board[i][j];
				if(cf.isNotFigure()) {
					continue;
				}
				if(cf.isOfColor(player)) {
					if(cf.getFigureName() == "pawn") {
						maxPawns++;
					}
					if(cf.getFigureName() == "bishop") {
						maxBishops++;
					}
					if(cf.getFigureName() == "knight") {
						maxBishops++;
					}
					if(cf.getFigureName() == "queen") {
						maxQueens++;
					}
					if(cf.getFigureName() == "king") {
						maxKings++;
					}
				} else {
					if(cf.isCaptured()) {
						continue;
					}
					if(cf.getFigureName() == "pawn") {
						maxPawns--;
					}
					if(cf.getFigureName() == "bishop") {
						maxBishops--;
					}
					if(cf.getFigureName() == "knight") {
						maxBishops--;
					}
					if(cf.getFigureName() == "queen") {
						maxQueens--;
					}
					if(cf.getFigureName() == "king") {
						maxKings--;
					}
				}
			}
		}
		
		//Evaluating number of figures
		int p = 2000*maxKings + 90*maxQueens + 50*maxRooks + 30*maxBishops + 10*maxPawns;
		
		//Evaluating doubled, blocked and isolated pawns
		int diffDoubled = calcDoubledPawns(board, player) - calcDoubledPawns(board, opposite);
		int diffIsolated = calcIsolatedPawns(board, player) - calcIsolatedPawns(board, opposite);
		int diffBlocked = calcBlockedPawns(board, player, opposite) - calcBlockedPawns(board, opposite, player);
		p -= 5*(diffDoubled + diffIsolated + diffBlocked);
		
		//Evaluating mobility
		p += calcMobility(board, player, opposite) - calcMobility(board, opposite, player);
		
		return p;
	}
	
	public static int calcMobility(ChessFigure[][] board, FigureColor player, FigureColor opposite) {
		int mobility = 0;
		for(int i=0; i<board.length; i++) {
			for(int j=0; j<board[i].length; j++) {
				ChessFigure cf = board[i][j];
				if(cf.isNotFigure()) {
					continue;
				}
				
				if(cf.isOfColor(player)) {
					mobility += MovesCalculator.getPossibleMoves(cf, board, player, opposite).size();
				}
			}
		}
		return mobility;
	}
	
	public static int calcDoubledPawns(ChessFigure[][] board, FigureColor player) {
		int doubled = 0;
		
		for(int j=0; j<8; j++) {
			int localDoubled = 0;
			for(int i=0; i<8; i++) {
				if(board[i][j].isNotFigure() || !board[i][j].isOfColor(player)) {
					continue;
				}
				if(board[i][j].getFigureName().equals("pawn")) {
					localDoubled++;
				}
			}
			if(localDoubled > 1) {
				doubled++;
			}
		}
		
		return doubled;
	}
	
	public static int calcIsolatedPawns(ChessFigure[][] board, FigureColor player) {
		int isolated = 0;
		
		for(int j=0; j<8; j++) {
			int localIsolated = 0;
			for(int i=0; i<8; i++) {
				if(board[i][j].isNotFigure() || !board[i][j].isOfColor(player)) {
					continue;
				}
				if(board[i][j].getFigureName().equals("pawn")) {
					for(int k=0; k<8; k++) {
						//Check Left File
						if((j-1) >= 0) {
							if(board[k][j].getFigureName().equals("pawn")) {
								break;
							}
						}
						
						if((j+1) <= 7) {
							if(board[k][j].getFigureName().equals("pawn")) {
								break;
							}
						}
						
						localIsolated++;
					}
				}
			}
			if(localIsolated > 1) {
				isolated++;
			}
		}
		
		return isolated;
	}
	
	public static int calcBlockedPawns(ChessFigure[][] board, FigureColor player, FigureColor opposite) {
		int blocked = 0;
		
		for(int j=0; j<8; j++) {
			for(int i=0; i<8; i++) {
				if(board[i][j].isNotFigure() || !board[i][j].isOfColor(player) || !board[i][j].getFigureName().equals("pawn")) {
					continue;
				}
				
				int localCounter = 0;
				ArrayList<ChessMove> alCms = MovesCalculator.getPossibleMoves(board[i][j], board, player, opposite);
				for(ChessMove cm : alCms) {
					if(cm.getMoveType() == ChessMoveType.CMT_ALLOWED) {
						localCounter++;
						break;
					}
				}
				
				if(localCounter == 0) {
					blocked++;
				}
			}
		}
		
		return blocked;
	}
	
}
