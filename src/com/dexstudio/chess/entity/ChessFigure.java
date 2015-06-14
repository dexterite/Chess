package com.dexstudio.chess.entity;

import java.util.Locale;

public class ChessFigure implements Cloneable {
	
	private FigureType ft = null;
	private int x = 0;
	private int y = 0;
	private FigureStatus fs = null;
	
	public ChessFigure(FigureType ft) {
		this.ft = ft;
		this.x = ft.getX();
		this.y = ft.getY();
		this.fs = FigureStatus.FS_ALIVE;
	}
	
	public void setCaptured() {
		this.fs = FigureStatus.FS_CAPTURED;
	}
	
	public void unsetCaptured() {
		this.fs = FigureStatus.FS_ALIVE;
	}
	
	public boolean isCaptured() {
		return this.fs == FigureStatus.FS_CAPTURED ? true : false;
	}
	
	public String getFigureName() {
		return this.ft.getName();
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public String getFigureColorName() {
		return this.ft.getFc().toString().toLowerCase(Locale.ENGLISH);
	}
	
	public FigureColor getFigureColor() {
		return this.ft.getFc();
	}
	
	public boolean isFigure() {
		return this.ft.getFc() != FigureColor.NONE ? true : false;
	}
	
	public boolean isNotFigure() {
		return this.ft.getFc() == FigureColor.NONE ? true : false;
	}
	
	public boolean isWhite() {
		return this.ft.getFc() == FigureColor.WHITE ? true : false;
	}
	
	public boolean isBlack() {
		return this.ft.getFc() == FigureColor.BLACK ? true : false;
	}
	
	public boolean isOfColor(FigureColor fc) {
		return this.ft.getFc() == fc ? true : false;
	}
	
	@Override
	public ChessFigure clone() throws CloneNotSupportedException {
		ChessFigure cf = (ChessFigure)super.clone();
		cf.fs = this.fs;
		cf.ft = this.ft;
		cf.x = this.x;
		cf.y = this.y;
		return cf;
	}
	
}
