package com.dexstudio.chess.entity;

import java.util.ArrayList;
import java.util.Locale;

import com.dexstudio.chess.entity.ChessFigure.FigureType;

public class ChessBoard {
	
	private ArrayList<ChessFigure> player = new ArrayList<ChessFigure>();
	private ArrayList<ChessFigure> computer = new ArrayList<ChessFigure>();
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
		for(FigureType ft : FigureType.values()) {
			player.add(new ChessFigure(ft));
			computer.add(new ChessFigure(ft));
		}
	}
	
	public ArrayList<ChessFigure> getPlayer() {
		return this.player;
	}
	
	public ArrayList<ChessFigure> getComputer() {
		return this.computer;
	}
	
	public void movePlayerFigure(int x, int y, int moveX, int moveY) {
		for(ChessFigure cf : player) {
			if(cf.getX() == x && cf.getY() == y) {
				cf.setXY(moveX, moveY);
			}
		}
	}
	
	public void moveComputerFigure(int x, int y, int moveX, int moveY) {
		for(ChessFigure cf : computer) {
			if(cf.getX() == x && cf.getY() == y) {
				cf.setXY(moveX, moveY);
			}
		}
	}
	
	public ChessFigure getPlayerFigure(int x, int y) {
		for(ChessFigure cf : player) {
			if(cf.getX() == x && cf.getY() == y) {
				return cf;
			}
		}
		return null;
	}
	
	public ChessFigure getComputerFigure(int x, int y) {
		for(ChessFigure cf : computer) {
			if(cf.getX() == x && cf.getY() == y && !cf.isCaptured()) {
				return cf;
			}
		}
		return null;
	}
	
	public ArrayList<ChessMove> getPossibleMoves(ChessFigure moveCf) {
		ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
		switch(moveCf.getFigureName()) {
		case "pawn":
			for(int i=1; i<3; i++) {
				if(moveCf.getX() > 2 && i >= 2) {
					continue;
				}
				if(this.getPlayerFigure(moveCf.getX() + i, moveCf.getY()) == null && 
						this.getComputerFigure(9 - (moveCf.getX() + i), moveCf.getY()) == null) {
					moves.add(new ChessMove(moveCf.getX() + i, moveCf.getY(), ChessMoveType.CMT_ALLOWED));
				}
			}
			
			for(int i=-1; i<2; i++) {
				if(i == 0) {
					continue;
				}
				if(this.getPlayerFigure(moveCf.getX() + 1, moveCf.getY() + i) == null && 
						this.getComputerFigure(9 - (moveCf.getX() + 1), moveCf.getY() + i) != null) {
					moves.add(new ChessMove(moveCf.getX() + 1, moveCf.getY() + i, ChessMoveType.CMT_CAPTURE));
				}
			}
			
			break;
			
		case "king":
			for(int i=-1; i<2; i++) {
				for(int j=-1; j<2; j++) {
					if((i==0 && j==0) || (this.getPlayerFigure(moveCf.getX() + i, moveCf.getY() + j) != null)) {
						continue;
					}
					
					if(this.getComputerFigure(9 - (moveCf.getX() + i), moveCf.getY() + j) != null) {
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
						if(this.getPlayerFigure(moveCf.getX() + (i*k), moveCf.getY() + (j*k)) != null) {
							break;
						}
						
						if(this.getComputerFigure(9 - (moveCf.getX() + i*k), moveCf.getY() + (j*k)) != null) {
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
						if(this.getPlayerFigure(moveCf.getX() + (i*k), moveCf.getY() + (j*k)) != null) {
							break;
						}
						
						if(this.getComputerFigure(9 - (moveCf.getX() + i*k), moveCf.getY() + (j*k)) != null) {
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
						if(this.getPlayerFigure(moveCf.getX() + (i*k), moveCf.getY() + (j*k)) != null) {
							break;
						}
						if(this.getComputerFigure(9 - (moveCf.getX() + i*k), moveCf.getY() + (j*k)) != null) {
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
					if(this.getComputerFigure(9 - (moveCf.getX() + i), moveCf.getY() + j) != null) {
						moves.add(new ChessMove(moveCf.getX() + i, moveCf.getY() + j, ChessMoveType.CMT_CAPTURE));
					} else if(this.getPlayerFigure(moveCf.getX() + (i), moveCf.getY() + (j)) == null) {
						moves.add(new ChessMove(moveCf.getX() + (i), moveCf.getY() + (j), ChessMoveType.CMT_ALLOWED));
					}
				}
			}
			break;
		}
		
		return moves;
	}
	
	public ArrayList<ChessMove> getPossibleComputerMoves(ChessFigure moveCf) {
		ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
		switch(moveCf.getFigureName()) {
		case "pawn":
			for(int i=1; i<3; i++) {
				if(moveCf.getX() > 2 && i >= 2) {
					continue;
				}
				if(this.getComputerFigure(moveCf.getX() + i, moveCf.getY()) == null && 
						this.getPlayerFigure(9 - (moveCf.getX() + i), moveCf.getY()) == null) {
					moves.add(new ChessMove(moveCf.getX() + i, moveCf.getY(), ChessMoveType.CMT_ALLOWED));
				}
			}
			
			for(int i=-1; i<2; i++) {
				if(i == 0) {
					continue;
				}
				if(this.getComputerFigure(moveCf.getX() + 1, moveCf.getY() + i) == null && 
						this.getPlayerFigure(9 - (moveCf.getX() + 1), moveCf.getY() + i) != null) {
					moves.add(new ChessMove(moveCf.getX() + 1, moveCf.getY() + i, ChessMoveType.CMT_CAPTURE));
				}
			}
			
			break;
			
		case "king":
			for(int i=-1; i<2; i++) {
				for(int j=-1; j<2; j++) {
					if((i==0 && j==0) || (this.getComputerFigure(moveCf.getX() + i, moveCf.getY() + j) != null)) {
						continue;
					}
					
					if(this.getPlayerFigure(9 - (moveCf.getX() + i), moveCf.getY() + j) != null) {
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
						if(this.getComputerFigure(moveCf.getX() + (i*k), moveCf.getY() + (j*k)) != null) {
							break;
						}
						
						if(this.getPlayerFigure(9 - (moveCf.getX() + i*k), moveCf.getY() + (j*k)) != null) {
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
						if(this.getComputerFigure(moveCf.getX() + (i*k), moveCf.getY() + (j*k)) != null) {
							break;
						}
						
						if(this.getPlayerFigure(9 - (moveCf.getX() + i*k), moveCf.getY() + (j*k)) != null) {
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
						if(this.getComputerFigure(moveCf.getX() + (i*k), moveCf.getY() + (j*k)) != null) {
							break;
						}
						if(this.getPlayerFigure(9 - (moveCf.getX() + i*k), moveCf.getY() + (j*k)) != null) {
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
					if(this.getPlayerFigure(9 - (moveCf.getX() + i), moveCf.getY() + j) != null) {
						moves.add(new ChessMove(moveCf.getX() + i, moveCf.getY() + j, ChessMoveType.CMT_CAPTURE));
					} else if(this.getComputerFigure(moveCf.getX() + (i), moveCf.getY() + (j)) == null) {
						moves.add(new ChessMove(moveCf.getX() + (i), moveCf.getY() + (j), ChessMoveType.CMT_ALLOWED));
					}
				}
			}
			break;
		}
		
		return moves;
	}
	
	public void captureComputer(int x, int y) {
		for(int i=0; i<this.computer.size(); i++) {
			ChessFigure cf = this.computer.get(i);
			if(cf.getX() == x && cf.getY() == y) {
				cf.setCaptured();
				break;
			}
		}
	}

	public void makeComputerMove() {
		
	}
	
	public void nextMove() {
		moveNumber++;
		
		if(moveNumber % 2 == 0) {
			//TODO Black Move
			
		} else if(moveNumber % 2 != 0) {
			//TODO White Move
			
		}
	}
	
	public ChessBoard selfCopy() throws CloneNotSupportedException {
		ChessBoard cb = new ChessBoard(this.playerAlgorithm, this.computerAlgorithm, this.depth);
		cb.computer = new ArrayList<ChessFigure>();
		for(int i=0; i<this.computer.size(); i++) {
			this.computer.get(i).clone();
		}
		return cb;
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
