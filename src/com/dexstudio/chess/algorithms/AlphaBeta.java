package com.dexstudio.chess.algorithms;

import java.util.ArrayList;

import android.graphics.Point;

import com.dexstudio.chess.entity.ChessFigure;
import com.dexstudio.chess.entity.ChessMove;
import com.dexstudio.chess.entity.FigureColor;
import com.dexstudio.chess.helpers.MovesCalculator;

public class AlphaBeta extends ChessAlgorithm {
	
	private ChessFigure[][] originalBoard = null;
	private FigureColor playerMax = null;
	private FigureColor playerMin = null;
	//Depth
	private int initialDepth = 3;
	
	public AlphaBeta(ChessFigure[][] board, int depth, FigureColor playerMax, FigureColor playerMin) {
		this.originalBoard = board;
		this.playerMax = playerMax;
		this.playerMin = playerMin;
		
		this.initialDepth = depth;
	}
	
	public void startEval() {
		alphaBetaMax(Integer.MIN_VALUE, Integer.MAX_VALUE, this.initialDepth);
	}
	
	public int alphaBetaMax(int alpha, int beta, int depth) {
		if(depth == 0) {
			return Evaluation.evaluate(originalBoard, playerMax, playerMin);
		}
		//Evaluate all Figures
		for(int i=0; i<this.originalBoard.length; i++) {
			for(int j=0; j<this.originalBoard[i].length; j++) {
				ChessFigure cf = this.originalBoard[i][j];
				if(cf.getFigureColor() != this.playerMax || cf.isNotFigure()) {
					continue;
				}
				
				//Does this figure has possible moves?
				ArrayList<ChessMove> playerMaxMoves = MovesCalculator.getPossibleMoves(cf, this.originalBoard, this.playerMax, this.playerMin);
				if(playerMaxMoves.size() == 0) {
					continue;
				}
				
				for(ChessMove cm : playerMaxMoves) {
					Point oldPosition = new Point(cf.getX(), cf.getY());
					ChessFigure cfMovedTo = MovesCalculator.moveFigureTo(this.originalBoard, cf, cm.x, cm.y);
					
					int score = alphaBetaMin(alpha, beta, depth - 1);
					if(score >= beta) {
						return beta;
					}
					if(score > alpha) {
						alpha = score;
						if(depth == this.initialDepth) {
							this.setSelectedMove(cm);
							this.setSelectedFigure(cf);
						}
						logMove(score, depth, true, true);
					} else {
						logMove(score, depth, true, false);
					}
					
					MovesCalculator.moveFigureTo(this.originalBoard, cf, oldPosition.x, oldPosition.y);
					MovesCalculator.bringBackTo(this.originalBoard, cfMovedTo, cm.x, cm.y);
				}
			}
		}
		return alpha;
	}

	public int alphaBetaMin(int alpha, int beta, int depth) {
		if(depth == 0) {
			return Evaluation.evaluate(originalBoard, playerMin, playerMax);
		}
		//Evaluate all Figures
		for(int i=0; i<this.originalBoard.length; i++) {
			for(int j=0; j<this.originalBoard[i].length; j++) {
				ChessFigure cf = this.originalBoard[i][j];
				if(cf.getFigureColor() != this.playerMax || cf.isNotFigure()) {
					continue;
				}
				
				//Does this figure has possible moves?
				ArrayList<ChessMove> playerMaxMoves = MovesCalculator.getPossibleMoves(cf, this.originalBoard, this.playerMin, this.playerMax);
				if(playerMaxMoves.size() == 0) {
					continue;
				}
				
				for(ChessMove cm : playerMaxMoves) {
					Point oldPosition = new Point(cf.getX(), cf.getY());
					ChessFigure cfMovedTo = MovesCalculator.moveFigureTo(this.originalBoard, cf, cm.x, cm.y);
					
					int score = alphaBetaMax(alpha, beta, depth - 1);
					if(score <= alpha) {
						return alpha;
					}
					if(score < beta) {
						beta = score;
						logMove(score, depth, true, true);
					} else {
						logMove(score, depth, true, false);
					}
					
					MovesCalculator.moveFigureTo(this.originalBoard, cf, oldPosition.x, oldPosition.y);
					MovesCalculator.bringBackTo(this.originalBoard, cfMovedTo, cm.x, cm.y);
				}
			}
		}
		return beta;
	}
}
