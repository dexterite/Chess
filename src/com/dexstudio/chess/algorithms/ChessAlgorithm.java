package com.dexstudio.chess.algorithms;

import com.dexstudio.chess.entity.ChessFigure;
import com.dexstudio.chess.entity.ChessMove;

import android.util.Log;

public class ChessAlgorithm {
	
	//Selected ChessMove
	private ChessMove selectedMove = null;
	private ChessFigure selectedFigure = null;
	
	public ChessMove getSelectedMove() {
		return selectedMove;
	}

	public void setSelectedMove(ChessMove selectedMove) {
		this.selectedMove = selectedMove;
	}

	public ChessFigure getSelectedFigure() {
		return selectedFigure;
	}

	public void setSelectedFigure(ChessFigure selectedFigure) {
		this.selectedFigure = selectedFigure;
	}

	private static String prepend(int depth) {
		return depth == 0 ? "" :String.format("%"+depth+"s", "    ");
	}
	
	protected void logMove(int score, int depth, boolean max, boolean selected) {
		if(max) {
			Log.i("MOVE", String.format("%s ["+(selected?"*":"")+"%d]", prepend(depth), score));
		} else {
			Log.i("MOVE", String.format("%s ("+(selected?"*":"")+"%d)", prepend(depth), score));
		}
	}
	
	protected void logMove(int fromX, int fromY, int toX, int toY, int depth) {
		Log.i("MOVE", String.format("%sFrom %d,%d to %d,%d", prepend(depth), fromX, fromY, toX, toY));
	}
	
	protected void logCapture(String name, int atX, int atY, int depth) {
		Log.i("CAPTURE", String.format("%s%s at %d,%d", prepend(depth), name, atX, atY));
	}
	
	protected void logSelectedFigure(String name, int atX, int atY, int depth) {
		Log.i("SELECT", String.format("%s%s at %d,%d", prepend(depth), name, atX, atY));
	}
	
}
