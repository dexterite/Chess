/**
 * 
 */
package com.dexstudio.chess.algorithms;

import java.util.ArrayList;

import android.graphics.Point;

import com.dexstudio.chess.entity.ChessFigure;
import com.dexstudio.chess.entity.ChessMove;
import com.dexstudio.chess.entity.FigureColor;
import com.dexstudio.chess.helpers.MovesCalculator;

/**
 * @author Sergii
 *
 */
public class MiniMax2 extends ChessAlgorithm {
	
	private ChessFigure[][] originalBoard = null;
	private FigureColor playerMax = null;
	private FigureColor playerMin = null;
	//Depth
	private int initialDepth = 3;
	
	
	public MiniMax2(ChessFigure[][] board, int depth, FigureColor playerMax, FigureColor playerMin) {
		this.originalBoard = board;
		this.playerMax = playerMax;
		this.playerMin = playerMin;
		
		this.initialDepth = depth;
	}
	
	public void startEval() {
		maxi(this.initialDepth);
	}
	
	private int maxi(int depth) {
		
		if(depth == 0) {
			//Evaluation Function
			return Evaluation.evaluate(originalBoard, playerMax, playerMin);
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
				ArrayList<ChessMove> playerMaxMoves = MovesCalculator.getPossibleMoves(cf, this.originalBoard, this.playerMax, this.playerMin);
				if(playerMaxMoves.size() == 0) {
					continue;
				}
				
				for(ChessMove cm : playerMaxMoves) {
					Point oldPosition = new Point(cf.getX(), cf.getY());
					ChessFigure cfMovedTo = MovesCalculator.moveFigureTo(this.originalBoard, cf, cm.x, cm.y);
					
					int score = mini(depth - 1);
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
	
	private int mini(int depth) {
		
		if(depth == 0) {
			//Evaluation Function
			return Evaluation.evaluate(originalBoard, playerMin, playerMax);
		}
		int min = Integer.MAX_VALUE;
		
		//Evaluate all Figures
		for(int i=0; i<this.originalBoard.length; i++) {
			for(int j=0; j<this.originalBoard[i].length; j++) {
				ChessFigure cf = this.originalBoard[i][j];
				if(cf.getFigureColor() != this.playerMin || cf.isNotFigure()) {
					continue;
				}
				
				//Does this figure has possible moves?
				ArrayList<ChessMove> playerMinMoves = MovesCalculator.getPossibleMoves(cf, this.originalBoard, this.playerMin, this.playerMax);
				if(playerMinMoves.size() == 0) {
					continue;
				}
				
				for(ChessMove cm : playerMinMoves) {
					Point oldPosition = new Point(cf.getX(), cf.getY());
					ChessFigure cfMovedTo = MovesCalculator.moveFigureTo(this.originalBoard, cf, cm.x, cm.y);
					
					int score = maxi(depth - 1);
					if(score < min) {
						min = score;
						/*if(depth == this.initialDepth) {
							this.setSelectedMove(cm);
							this.setSelectedFigure(cf);
						}*/
						logMove(score, depth, false, true);
					} else {
						logMove(score, depth, false, false);
					}
					
					MovesCalculator.moveFigureTo(this.originalBoard, cf, oldPosition.x, oldPosition.y);
					MovesCalculator.bringBackTo(this.originalBoard, cfMovedTo, cm.x, cm.y);
				}
			}
		}
		
		return min;
	}

}
