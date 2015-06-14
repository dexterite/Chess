/**
 * 
 */
package com.dexstudio.chess.algorithms;

import java.util.ArrayList;

import android.graphics.Point;
import android.util.Log;

import com.dexstudio.chess.entity.ChessFigure;
import com.dexstudio.chess.entity.ChessMove;
import com.dexstudio.chess.entity.ChessMoveType;

/**
 * @author Sergii
 *
 */
public class MiniMax2 {
	
	private ArrayList<ChessFigure> originalPlayerMax = null;
	private ArrayList<ChessFigure> originalPlayerMin = null;
	//Depth
	private int initialDepth = 3;
	//Selected ChessMove
	private ChessMove selectedMove = null;
	private ChessFigure selectedFigure = null;
	
	public MiniMax2(ArrayList<ChessFigure> playerMax, ArrayList<ChessFigure> playerMin, int depth) {
		this.originalPlayerMax = playerMax;
		this.originalPlayerMin = playerMin;
		
		this.initialDepth = depth;
	}
	
	public void startEval() throws CloneNotSupportedException {
		maxi(this.initialDepth);
	}
	
	private int maxi(int depth) {
		
		if(depth == 0) {
			//Evaluation Function
			return Evaluation.simpleEval(originalPlayerMax, originalPlayerMin);
		}
		int max = Integer.MIN_VALUE;
		
		//Evaluate all Figures
		for(ChessFigure cf : originalPlayerMax) {
			if(cf.isCaptured()) {
				continue;
			}
			//Does this figure has possible moves?
			ArrayList<ChessMove> playerMaxMoves = this.getPossibleMovesMax(cf, originalPlayerMax, originalPlayerMin);
			if(playerMaxMoves.size() == 0) {
				continue;
			}
			
			this.logSelectedFigure(cf.getFigureName(), cf.getX(), cf.getY(), this.initialDepth - depth);
			//For each found move
			for(ChessMove cm : playerMaxMoves) {
				//Do the move on nextMaxFigures
				Point oldPosition = new Point(cf.getX(), cf.getY());
				this.logMove(cf.getX(), cf.getY(), cm.x, cm.y, this.initialDepth - depth);
				this.moveFigure(cf.getX(), cf.getY(), cm.x, cm.y, originalPlayerMax);
				ChessFigure capturedFigure = this.checkIfCanCapture(cm.x, cm.y, originalPlayerMin);
				if(capturedFigure != null) {
					capturedFigure.setCaptured();
					this.logCapture(capturedFigure.getFigureName(), capturedFigure.getX(), capturedFigure.getY(), this.initialDepth - depth);
				}
				 
				//Then evaluate min
				int score = mini(depth - 1);
				if(score > max) {
					max = score;
					if(depth == this.initialDepth) {
						this.setSelectedMove(cm);
						this.setSelectedFigure(cf);
					}
				}
				
				this.logMove(cm.x, cm.y, oldPosition.x, oldPosition.y, this.initialDepth - depth);
				this.moveFigure(cm.x, cm.y, oldPosition.x, oldPosition.y, originalPlayerMax);
				if(capturedFigure != null) {
					capturedFigure.unsetCaptured();
					this.logCapture(capturedFigure.getFigureName(), capturedFigure.getX(), capturedFigure.getY(), this.initialDepth - depth);
				}
			}
		}
		
		return max;
	}

	private int mini(int depth) {
		
		if(depth == 0) {
			//Evaluation Function
			return -(Evaluation.simpleEval(originalPlayerMin, originalPlayerMax));
		}
		int min = Integer.MAX_VALUE;
		
		//Evaluate all Figures
		for(ChessFigure cf : originalPlayerMin) {
			//Does this figure has possible moves?
			ArrayList<ChessMove> playerMinMoves = this.getPossibleMovesMin(cf, originalPlayerMax, originalPlayerMin);
			if(playerMinMoves.size() == 0) {
				continue;
			}
			
			//For each found move
			for(ChessMove cm : playerMinMoves) {
				//Do the move on nextMinFigures
				Point oldPosition = new Point(cf.getX(), cf.getY());
				this.logMove(cf.getX(), cf.getY(), cm.x, cm.y, this.initialDepth - depth);
				this.moveFigure(cf.getX(), cf.getY(), cm.x, cm.y, originalPlayerMin);
				ChessFigure capturedFigure = this.checkIfCanCapture(cm.x, cm.y, originalPlayerMax);
				if(capturedFigure != null) {
					capturedFigure.setCaptured();
					this.logCapture(capturedFigure.getFigureName(), capturedFigure.getX(), capturedFigure.getY(), this.initialDepth - depth);
				}
				
				//Then evaluate min
				int score = maxi(depth - 1);
				if(score > min) {
					min = score;
				}
				
				this.logMove(cm.x, cm.y, oldPosition.x, oldPosition.y, this.initialDepth - depth);
				this.moveFigure(cm.x, cm.y, oldPosition.x, oldPosition.y, originalPlayerMin);
				if(capturedFigure != null) {
					capturedFigure.unsetCaptured();
					this.logCapture(capturedFigure.getFigureName(), capturedFigure.getX(), capturedFigure.getY(), this.initialDepth - depth);
				}
			}
		}
		
		return min;
	}
	
	private ChessFigure checkIfCanCapture(int x, int y,
			ArrayList<ChessFigure> originalPlayer) {
		
		for(ChessFigure cf : originalPlayer) {
			if(cf.getX() == x && cf.getY() == y && !cf.isCaptured()) {
				return cf;
			}
		}
		
		return null;
		
	}
	
	public void moveFigure(int x, int y, int moveX, int moveY, ArrayList<ChessFigure> alCf) {
		for(ChessFigure cf : alCf) {
			if(cf.getX() == x && cf.getY() == y) {
				cf.setXY(moveX, moveY);
			}
		}
	}
	
	public ChessFigure getFigure(int x, int y, ArrayList<ChessFigure> alCf) {
		for(ChessFigure cf : alCf) {
			if(cf.getX() == x && cf.getY() == y && !cf.isCaptured()) {
				return cf;
			}
		}
		return null;
	}
	
	public ArrayList<ChessMove> getPossibleMoves(ChessFigure cf, ArrayList<ChessFigure> playerFigures) {
		ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
		
		switch(cf.getFigureName()) {
		case "pawn":
			
		}
		
		return moves;
	}
	
	public ArrayList<ChessMove> getPossibleMovesMax(ChessFigure moveCf, 
			ArrayList<ChessFigure> playerMaxFigures,
			ArrayList<ChessFigure> playerMinFigures) {
		
		ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
		switch(moveCf.getFigureName()) {
		case "pawn":
			for(int i=1; i<3; i++) {
				if(moveCf.getX() > 2 && i >= 2) {
					continue;
				}
				if(this.getFigure(moveCf.getX() + i, moveCf.getY(), playerMaxFigures) == null && 
						this.getFigure(9 - (moveCf.getX() + i), moveCf.getY(), playerMinFigures) == null) {
					moves.add(new ChessMove(moveCf.getX() + i, moveCf.getY(), ChessMoveType.CMT_ALLOWED));
				}
			}
			
			for(int i=-1; i<2; i++) {
				if(i == 0) {
					continue;
				}
				if(this.getFigure(moveCf.getX() + 1, moveCf.getY() + i, playerMaxFigures) == null && 
						this.getFigure(9 - (moveCf.getX() + 1), moveCf.getY() + i, playerMinFigures) != null) {
					moves.add(new ChessMove(moveCf.getX() + 1, moveCf.getY() + i, ChessMoveType.CMT_CAPTURE));
				}
			}
			
			break;
			
		case "king":
			for(int i=-1; i<2; i++) {
				for(int j=-1; j<2; j++) {
					if((i==0 && j==0) || (this.getFigure(moveCf.getX() + i, moveCf.getY() + j, playerMaxFigures) != null)) {
						continue;
					}
					
					if(this.getFigure(9 - (moveCf.getX() + i), moveCf.getY() + j, playerMinFigures) != null) {
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
						if(this.getFigure(moveCf.getX() + (i*k), moveCf.getY() + (j*k), playerMaxFigures) != null) {
							break;
						}
						
						if(this.getFigure(9 - (moveCf.getX() + i*k), moveCf.getY() + (j*k), playerMinFigures) != null) {
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
						if(this.getFigure(moveCf.getX() + (i*k), moveCf.getY() + (j*k), playerMaxFigures) != null) {
							break;
						}
						
						if(this.getFigure(9 - (moveCf.getX() + i*k), moveCf.getY() + (j*k), playerMinFigures) != null) {
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
						if(this.getFigure(moveCf.getX() + (i*k), moveCf.getY() + (j*k), playerMaxFigures) != null) {
							break;
						}
						if(this.getFigure(9 - (moveCf.getX() + i*k), moveCf.getY() + (j*k), playerMinFigures) != null) {
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
					if(this.getFigure(9 - (moveCf.getX() + i), moveCf.getY() + j, playerMinFigures) != null) {
						moves.add(new ChessMove(moveCf.getX() + i, moveCf.getY() + j, ChessMoveType.CMT_CAPTURE));
					} else if(this.getFigure(moveCf.getX() + (i), moveCf.getY() + (j), playerMaxFigures) == null) {
						moves.add(new ChessMove(moveCf.getX() + (i), moveCf.getY() + (j), ChessMoveType.CMT_ALLOWED));
					}
				}
			}
			break;
		}
		
		ArrayList<ChessMove> finalMoves = new ArrayList<ChessMove>();
		for(ChessMove cm : moves) {
			if(!(cm.x < 1 || cm.x > 8 || cm.y < 1 || cm.y > 8)) {
				finalMoves.add(cm);
			}
		}
		
		return finalMoves;
	}
	
	public ArrayList<ChessMove> getPossibleMovesMin(ChessFigure moveCf,
			ArrayList<ChessFigure> playerMaxFigures,
			ArrayList<ChessFigure> playerMinFigures) {
		ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
		switch(moveCf.getFigureName()) {
		case "pawn":
			for(int i=1; i<3; i++) {
				if(moveCf.getX() > 2 && i >= 2) {
					continue;
				}
				if(this.getFigure(moveCf.getX() + i, moveCf.getY(), playerMinFigures) == null && 
						this.getFigure(9 - (moveCf.getX() + i), moveCf.getY(), playerMaxFigures) == null) {
					moves.add(new ChessMove(moveCf.getX() + i, moveCf.getY(), ChessMoveType.CMT_ALLOWED));
				}
			}
			
			for(int i=-1; i<2; i++) {
				if(i == 0) {
					continue;
				}
				if(this.getFigure(moveCf.getX() + 1, moveCf.getY() + i, playerMinFigures) == null && 
						this.getFigure(9 - (moveCf.getX() + 1), moveCf.getY() + i, playerMaxFigures) != null) {
					moves.add(new ChessMove(moveCf.getX() + 1, moveCf.getY() + i, ChessMoveType.CMT_CAPTURE));
				}
			}
			
			break;
			
		case "king":
			for(int i=-1; i<2; i++) {
				for(int j=-1; j<2; j++) {
					if((i==0 && j==0) || (this.getFigure(moveCf.getX() + i, moveCf.getY() + j, playerMinFigures) != null)) {
						continue;
					}
					
					if(this.getFigure(9 - (moveCf.getX() + i), moveCf.getY() + j, playerMaxFigures) != null) {
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
						if(this.getFigure(moveCf.getX() + (i*k), moveCf.getY() + (j*k), playerMinFigures) != null) {
							break;
						}
						
						if(this.getFigure(9 - (moveCf.getX() + i*k), moveCf.getY() + (j*k), playerMaxFigures) != null) {
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
						if(this.getFigure(moveCf.getX() + (i*k), moveCf.getY() + (j*k), playerMinFigures) != null) {
							break;
						}
						
						if(this.getFigure(9 - (moveCf.getX() + i*k), moveCf.getY() + (j*k), playerMaxFigures) != null) {
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
						if(this.getFigure(moveCf.getX() + (i*k), moveCf.getY() + (j*k), playerMinFigures) != null) {
							break;
						}
						if(this.getFigure(9 - (moveCf.getX() + i*k), moveCf.getY() + (j*k), playerMaxFigures) != null) {
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
					if(this.getFigure(9 - (moveCf.getX() + i), moveCf.getY() + j, playerMaxFigures) != null) {
						moves.add(new ChessMove(moveCf.getX() + i, moveCf.getY() + j, ChessMoveType.CMT_CAPTURE));
					} else if(this.getFigure(moveCf.getX() + (i), moveCf.getY() + (j), playerMinFigures) == null) {
						moves.add(new ChessMove(moveCf.getX() + (i), moveCf.getY() + (j), ChessMoveType.CMT_ALLOWED));
					}
				}
			}
			break;
		}
		
		ArrayList<ChessMove> finalMoves = new ArrayList<ChessMove>();
		for(ChessMove cm : moves) {
			if(!(cm.x < 1 || cm.x > 8 || cm.y < 1 || cm.y > 8)) {
				finalMoves.add(cm);
			}
		}
		
		return finalMoves;
	}
	
	private ArrayList<ChessFigure> cloneChessFigures(ArrayList<ChessFigure> original) throws CloneNotSupportedException {
		ArrayList<ChessFigure> cloned = new ArrayList<ChessFigure>();
		
		for(int i=0; i<original.size(); i++) {
			cloned.add(original.get(i).clone());
		}
		
		return cloned;
	}

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
	
	private void logMove(int fromX, int fromY, int toX, int toY, int depth) {
		Log.i("MOVE", String.format("%sFrom %d,%d to %d,%d", prepend(depth), fromX, fromY, toX, toY));
	}
	
	private void logCapture(String name, int atX, int atY, int depth) {
		Log.i("CAPTURE", String.format("%s%s at %d,%d", prepend(depth), name, atX, atY));
	}
	
	private void logSelectedFigure(String name, int atX, int atY, int depth) {
		Log.i("SELECT", String.format("%s%s at %d,%d", prepend(depth), name, atX, atY));
	}

}
