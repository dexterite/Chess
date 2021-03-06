/**
 * 
 */
package com.dexstudio.chess.algorithms;

import java.util.ArrayList;

import com.dexstudio.chess.entity.ChessFigure;
import com.dexstudio.chess.entity.ChessMove;
import com.dexstudio.chess.entity.ChessMoveType;

/**
 * @author Sergii
 *
 */
public class MiniMax {
	
	private ArrayList<ChessFigure> originalPlayerMax = null;
	private ArrayList<ChessFigure> originalPlayerMin = null;
	//Depth
	private int initialDepth = 3;
	//Selected ChessMove
	private ChessMove selectedMove = null;
	private ChessFigure selectedFigure = null;
	
	public MiniMax(ArrayList<ChessFigure> playerMax, ArrayList<ChessFigure> playerMin, int depth) {
		this.originalPlayerMax = playerMax;
		this.originalPlayerMin = playerMin;
		
		this.initialDepth = depth;
	}
	
	public void startEval() throws CloneNotSupportedException {
		maxi(this.initialDepth, originalPlayerMax, originalPlayerMin);
	}
	
	private int maxi(int depth, 
			ArrayList<ChessFigure> localMaxFigures, 
			ArrayList<ChessFigure> localMinFigures) throws CloneNotSupportedException {
		
		if(depth == 0) {
			//Evaluation Function
			return 1;
		}
		int max = Integer.MIN_VALUE;
		
		//Evaluate all Figures
		for(ChessFigure cf : localMaxFigures) {
			//Does this figure has possible moves?
			ArrayList<ChessMove> playerMaxMoves = this.getPossibleMovesMax(cf, localMaxFigures, localMinFigures);
			if(playerMaxMoves.size() == 0) {
				continue;
			}
			
			//For each found move
			for(ChessMove cm : playerMaxMoves) {
				ArrayList<ChessFigure> nextMaxFigures = this.cloneChessFigures(localMaxFigures);
				ArrayList<ChessFigure> nextMinFigures = this.cloneChessFigures(localMinFigures);
				
				//Do the move on nextMaxFigures
				this.moveFigure(cf.getX(), cf.getY(), cm.x, cm.y, localMaxFigures);
				
				//Then evaluate min
				int score = mini(depth - 1, nextMaxFigures, nextMinFigures);
				if(score > max) {
					max = score;
					if(depth == this.initialDepth) {
						this.setSelectedMove(cm);
						this.setSelectedFigure(cf);
					}
				}
			}
		}
		
		return max;
	}

	private int mini(int depth, 
			ArrayList<ChessFigure> localMaxFigures, 
			ArrayList<ChessFigure> localMinFigures) throws CloneNotSupportedException {
		
		if(depth == 0) {
			//Evaluation Function
			return -1;
		}
		int min = Integer.MAX_VALUE;
		
		//Evaluate all Figures
		for(ChessFigure cf : localMinFigures) {
			//Does this figure has possible moves?
			ArrayList<ChessMove> playerMinMoves = this.getPossibleMovesMin(cf, localMaxFigures, localMinFigures);
			if(playerMinMoves.size() == 0) {
				continue;
			}
			
			//For each found move
			for(ChessMove cm : playerMinMoves) {
				ArrayList<ChessFigure> nextMaxFigures = this.cloneChessFigures(localMaxFigures);
				ArrayList<ChessFigure> nextMinFigures = this.cloneChessFigures(localMinFigures);
				
				//Do the move on nextMinFigures
				this.moveFigure(cf.getX(), cf.getY(), cm.x, cm.y, nextMinFigures);
				
				//Then evaluate min
				int score = maxi(depth - 1, nextMaxFigures, nextMinFigures);
				if(score > min) {
					min = score;
				}
			}
		}
		
		return min;
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
		
		return moves;
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
		
		return moves;
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

}
