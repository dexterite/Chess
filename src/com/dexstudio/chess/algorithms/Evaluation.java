package com.dexstudio.chess.algorithms;

import java.util.ArrayList;

import com.dexstudio.chess.entity.ChessFigure;

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
	
	public static int simpleEval(ArrayList<ChessFigure> originalPlayerMax,
			ArrayList<ChessFigure> originalPlayerMin) {
		
		int maxPawns = 0;
		int maxBishops = 0;
		int maxRooks = 0;
		int maxQueens = 0;
		int maxKings = 0;
		
		for(ChessFigure cfMax : originalPlayerMax) {
			if(cfMax.isCaptured()) {
				continue;
			}
			if(cfMax.getFigureName() == "pawn") {
				maxPawns += pawns[(cfMax.getX()-1) * 8 + (cfMax.getY()-1)];
			}
			if(cfMax.getFigureName() == "bishop") {
				maxBishops += bishops[(cfMax.getX()-1) * 8 + (cfMax.getY()-1)];
			}
			if(cfMax.getFigureName() == "knight") {
				maxBishops += knights[(cfMax.getX()-1) * 8 + (cfMax.getY()-1)];
			}
			if(cfMax.getFigureName() == "queen") {
				maxQueens += queens[(cfMax.getX()-1) * 8 + (cfMax.getY()-1)];
			}
			if(cfMax.getFigureName() == "king") {
				maxKings += kings[(cfMax.getX()-1) * 8 + (cfMax.getY()-1)];
			}
		}
		
		for(ChessFigure cfMin : originalPlayerMin) {
			if(cfMin.isCaptured()) {
				continue;
			}
			if(cfMin.getFigureName() == "pawn") {
				maxPawns -= pawns[(cfMin.getX()-1) * 8 + (cfMin.getY()-1)];
			}
			if(cfMin.getFigureName() == "bishop") {
				maxBishops -= bishops[(cfMin.getX()-1) * 8 + (cfMin.getY()-1)];
			}
			if(cfMin.getFigureName() == "knight") {
				maxBishops -= knights[(cfMin.getX()-1) * 8 + (cfMin.getY()-1)];
			}
			if(cfMin.getFigureName() == "queen") {
				maxQueens -= queens[(cfMin.getX()-1) * 8 + (cfMin.getY()-1)];
			}
			if(cfMin.getFigureName() == "king") {
				maxKings -= kings[(cfMin.getX()-1) * 8 + (cfMin.getY()-1)];
			}
		}
		
		return 200*maxKings + 9*maxQueens + 5*maxRooks + 3*maxBishops + 1*maxPawns;
	}
	
	public static float evaluate(ArrayList<ChessFigure> originalPlayerMax,
			ArrayList<ChessFigure> originalPlayerMin) {
		
		int maxPawns = 0;
		int maxBishops = 0;
		int maxRooks = 0;
		int maxQueens = 0;
		int maxKings = 0;
		for(ChessFigure cfMax : originalPlayerMax) {
			if(cfMax.isCaptured()) {
				continue;
			}
			if(cfMax.getFigureName() == "pawn") {
				maxPawns++;
			}
			if(cfMax.getFigureName() == "bishop") {
				maxBishops++;
			}
			if(cfMax.getFigureName() == "knight") {
				maxBishops++;
			}
			if(cfMax.getFigureName() == "queen") {
				maxQueens++;
			}
			if(cfMax.getFigureName() == "king") {
				maxKings++;
			}
		}
		
		for(ChessFigure cfMin : originalPlayerMin) {
			if(cfMin.isCaptured()) {
				continue;
			}
			if(cfMin.getFigureName() == "pawn") {
				maxPawns--;
			}
			if(cfMin.getFigureName() == "bishop") {
				maxBishops--;
			}
			if(cfMin.getFigureName() == "knight") {
				maxBishops--;
			}
			if(cfMin.getFigureName() == "queen") {
				maxQueens--;
			}
			if(cfMin.getFigureName() == "king") {
				maxKings--;
			}
		}
		
		//Evaluating number of figures
		float p = 200*maxKings + 9*maxQueens + 5*maxRooks + 3*maxBishops + 1*maxPawns;
		//Evaluating doubled, blocked and isolated pawns
		
		
		//Evaluating mobility
		
		
		return p;
	}
	
	public static int calcDoubledPawns(ArrayList<ChessFigure> originalPlayer) {
		int doubled = 0;
		
		for(int i=0; i<8; i++) {
			int localDoubled = 0;
			for(int j=0; j<8; j++) {
				
			}
		}
		
		return doubled;
	}
	
}
