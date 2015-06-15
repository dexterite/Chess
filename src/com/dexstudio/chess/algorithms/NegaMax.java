package com.dexstudio.chess.algorithms;

import java.util.ArrayList;

import android.graphics.Point;

import com.dexstudio.chess.entity.ChessFigure;
import com.dexstudio.chess.entity.ChessMove;
import com.dexstudio.chess.entity.FigureColor;
import com.dexstudio.chess.helpers.MovesCalculator;

public class NegaMax extends ChessAlgorithm {
	
	private ChessFigure[][] originalBoard = null;
	private FigureColor playerMax = null;
	private FigureColor playerMin = null;
	//Depth
	private int initialDepth = 3;
	private boolean maxSide = true;
	
	public NegaMax(ChessFigure[][] board, int depth, FigureColor playerMax, FigureColor playerMin) {
		this.originalBoard = board;
		this.playerMax = playerMax;
		this.playerMin = playerMin;
		
		this.initialDepth = depth;
	}
	
	public void startEval() {
		negaMax(this.initialDepth, maxSide);
	}
	
	public int negaMax(int depth, boolean maxSide) {
		if(depth == 0) {
			//Evaluation Function
			if(maxSide) {
				return Evaluation.evaluate(originalBoard, playerMax, playerMin);
			} else {
				return Evaluation.evaluate(originalBoard, playerMin, playerMax);
			}
		}
		
		int max = Integer.MIN_VALUE;
		
		//Evaluate all Figures
		for(int i=0; i<this.originalBoard.length; i++) {
			for(int j=0; j<this.originalBoard[i].length; j++) {
				ChessFigure cf = this.originalBoard[i][j];
				if(cf.getFigureColor() != this.playerMax || cf.isNotFigure()) {
					continue;
				}
				
				//Does this figure has possible moves?
				ArrayList<ChessMove> playerMaxMoves = null;
				if(maxSide) {
					playerMaxMoves = MovesCalculator.getPossibleMoves(cf, this.originalBoard, this.playerMax, this.playerMin);
				} else {
					playerMaxMoves = MovesCalculator.getPossibleMoves(cf, this.originalBoard, this.playerMin, this.playerMax);
				}
					
				if(playerMaxMoves.size() == 0 || playerMaxMoves == null) {
					continue;
				}
				
				for(ChessMove cm : playerMaxMoves) {
					Point oldPosition = new Point(cf.getX(), cf.getY());
					ChessFigure cfMovedTo = MovesCalculator.moveFigureTo(this.originalBoard, cf, cm.x, cm.y);
					
					int score = -negaMax(depth - 1, !maxSide);
					if(score > max) {
						max = score;
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
		
		return max;
	}
}
