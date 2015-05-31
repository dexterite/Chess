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
	//Current Evaluation Layout
	private ArrayList<ChessFigure> currentPlayerMax = null;
	private ArrayList<ChessFigure> currentPlayerMin = null;
	//Depth
	private int initialDepth = 3;
	
	public MiniMax(ArrayList<ChessFigure> playerMax, ArrayList<ChessFigure> playerMin, int depth) {
		this.originalPlayerMax = playerMax;
		this.originalPlayerMin = playerMin;
		
		this.initialDepth = depth;
	}
	
	public ChessMove startEval() throws CloneNotSupportedException {
		//Evaluation of all figures
		for(ChessFigure cf : this.originalPlayerMax) {
			//Does this figure has possible moves?
			ArrayList<ChessMove> maxPlayerMoves = this.getPossibleMovesMax(cf, originalPlayerMax, originalPlayerMin);
			if(maxPlayerMoves.size() == 0) {
				continue;
			}
			
			//Take next figure
			//Copy initial positions of player max
			this.currentPlayerMax = new ArrayList<ChessFigure>();
			for(int i=0; i<this.originalPlayerMax.size(); i++) {
				this.currentPlayerMax.add(this.originalPlayerMax.get(i).clone());
			}
			
			//Copy initial positions of player min
			this.currentPlayerMin = new ArrayList<ChessFigure>();
			for(int i=0; i<this.originalPlayerMin.size(); i++) {
				this.currentPlayerMin.add(this.originalPlayerMin.get(i).clone());
			}
			
			
		}
		
		return null;
	}
	
	private int maxi(int depth, ChessFigure moveCf) {
		if(depth == 0) {
			return evaluate();
		}
		int max = Integer.MIN_VALUE;
		for(ChessMove cm : this.getPossibleMovesMax(moveCf)) {
			int score = mini(depth - 1);
		}
	}

	private int mini(int depth, ChessFigure moveCf) {
		if(depth == 0) {
			return -evaluate();
		}
		int min = Integer.MAX_VALUE;
		for(ChessMove cm : this.getPossibleMovesMin(moveCf)) {
			int score = maxi(depth - 1);
			if(score < min) {
				min = score;
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

}
