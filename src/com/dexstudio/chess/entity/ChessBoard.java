package com.dexstudio.chess.entity;

import java.util.Locale;

import com.dexstudio.chess.algorithms.MiniMax2;
import com.dexstudio.chess.helpers.MovesCalculator;

public class ChessBoard {
	
	private ChessFigure[][] board = null;
	private Algorithm playerAlgorithm = Algorithm.MINI_MAX;
	private Algorithm computerAlgorithm = Algorithm.MINI_MAX;
	private int depth = 3;
	private long moveNumber = 0;
	
	public ChessBoard() {
		initGame();
	}
	
	public ChessBoard(String playerAlgo, String compAlgo, int depth) {
		this();
		this.playerAlgorithm = Algorithm.algoFromString(playerAlgo);
		this.computerAlgorithm = Algorithm.algoFromString(compAlgo);
		this.depth = depth;
	}
	
	public ChessBoard(Algorithm playerAlgo, Algorithm compAlgo, int depth) {
		this();
		this.playerAlgorithm = playerAlgo;
		this.computerAlgorithm = compAlgo;
		this.depth = depth;
	}


	private void initGame() {
		this.board = new ChessFigure[8][];
		for(int i=0; i<8; i++) {
			this.board[i] = new ChessFigure[8];
			for(int j=0; j<8; j++) {
				FigureType ft = FigureType.findFigureTypeByCoord(i+1, j+1);
				this.board[i][j] = new ChessFigure(ft);
			}
		}
	}
	
	public ChessFigure[][] getFigures() {
		return this.board;
	}
	
	public void nextMove() {
		moveNumber++;
		
		if(moveNumber % 2 == 0) {
			MiniMax2 mm = new MiniMax2(this.board, this.depth, FigureColor.WHITE, FigureColor.BLACK);
			mm.startEval();
			//Move the figure
			MovesCalculator.moveFigureTo(this.board, mm.getSelectedFigure(), mm.getSelectedMove().x, mm.getSelectedMove().y);
		} else if(moveNumber % 2 != 0) {
			MiniMax2 mm = new MiniMax2(this.board, this.depth, FigureColor.BLACK, FigureColor.WHITE);
			mm.startEval();
			//Move the figure
			MovesCalculator.moveFigureTo(this.board, mm.getSelectedFigure(), mm.getSelectedMove().x, mm.getSelectedMove().y);
		}
	}
	
	public enum Algorithm {
		MINI_MAX,
		ALPHA_BETA,
		NEGA_MAX;
		
		public static Algorithm algoFromString(String algoStr) {
			if(algoStr.toLowerCase(Locale.ENGLISH).equals("MiniMax")) {
				return Algorithm.MINI_MAX;
			} else if(algoStr.toLowerCase(Locale.ENGLISH).equals("Alpha-Beta Prunning")) {
				return Algorithm.ALPHA_BETA;
			} else if(algoStr.toLowerCase(Locale.ENGLISH).equals("Nega-Max")) {
				return Algorithm.NEGA_MAX;
			} else {
				return Algorithm.MINI_MAX;
			}
		}
	}
}
