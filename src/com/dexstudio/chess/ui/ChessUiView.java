package com.dexstudio.chess.ui;

import java.util.ArrayList;

import com.dexstudio.chess.entity.ChessBoard;
import com.dexstudio.chess.entity.ChessFigure;
import com.dexstudio.chess.entity.ChessMove;
import com.dexstudio.chess.entity.ChessMoveType;
import com.dexstudio.chess.entity.MoveMessage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint("ClickableViewAccessibility")
public class ChessUiView extends View {
	
	private float squareSide = 0;
	
	private ChessBoard cb = null;
	
	private boolean toHighlight = false;
	private int highlightedX, highlightedY = 0;
	private ChessFigure cf = null;
	private ArrayList<ChessMove> moves = null;
	
	public ChessUiView(Context context) {
		super(context);
		
	}
	
	public ChessUiView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
	
	public void setChessBoard(ChessBoard cb) {
		this.cb = cb;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		drawBoard(canvas);
		
		super.onDraw(canvas);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch(event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			ChessUiView.this.highlightedX = (int)(event.getX() / squareSide);
			ChessUiView.this.highlightedY = (int)(event.getY() / squareSide);
			
			if(ChessUiView.this.toHighlight) {
				//Filter Moves outside the board
				if(ChessUiView.this.highlightedX >= 1 && ChessUiView.this.highlightedX <= 8 &&
						ChessUiView.this.highlightedY >= 1 && ChessUiView.this.highlightedY <= 8) {
					//Already selected, we can move
					ArrayList<ChessMove> moves = ChessUiView.this.cb.getPossibleMoves(ChessUiView.this.cf);
					MoveMessage mm = null;
					for(ChessMove cm : moves) {
						if(cm.x == (9-ChessUiView.this.highlightedY) && cm.y == ChessUiView.this.highlightedX) {
							mm = ChessUiView.this.cb.movePlayerFigure(ChessUiView.this.cf, 9-ChessUiView.this.highlightedY, ChessUiView.this.highlightedX);
							break;
						}
					}
					if(mm != null) {
						switch(mm) {
						case SUCCESS:
							//Sucessfully made move
							//TODO Check if computer's Figure is captured
							break;
							
						case ERROR_OVERLAP:
							
							break;
							
						case ERROR_WRONG_MOVE:
							
							break;
						}
					}
				}
				ChessUiView.this.toHighlight = false;
				ChessUiView.this.cf = null;
			} else {
				ChessUiView.this.cf = ChessUiView.this.cb.getPlayerFigure(9-ChessUiView.this.highlightedY, ChessUiView.this.highlightedX);
				
				if(ChessUiView.this.cf != null) {
					//Selected Figure
					ChessUiView.this.moves = ChessUiView.this.cb.getPossibleMoves(ChessUiView.this.cf);
					ChessUiView.this.toHighlight = true;
				}
			}
			
			ChessUiView.this.invalidate();
			
			break;
		case MotionEvent.ACTION_UP:
			
			break;
		}
		return super.onTouchEvent(event);
	}

	private void drawBoard(Canvas canvas) {
		Paint dark = new Paint();
		dark.setColor(Color.rgb(108, 47, 30));
		
		Paint light = new Paint();
		light.setColor(Color.rgb(219, 154, 64));
		light.setTextSize(42);
		
		float right = this.getWidth();
		squareSide = right / 10;
		canvas.drawRect(0, 0, right, right, dark);
		
		canvas.drawRect(squareSide-5, squareSide-5, right-squareSide+5, right-squareSide+5, light);
		
		for(int i=0; i<8; i++) {
			float btop = squareSide * (i+1);
			float bbottom = squareSide * (i+2);
			for(int j=0; j<8; j++) {
				float bleft = squareSide * (j+1);
				float bright = squareSide * (j+2);
				
				canvas.drawRect(bleft, btop, bright, bbottom, ((i+j) % 2 == 0 ? light : dark));
				
				if(i==0) {
					canvas.drawText("" + (char)(j+65), squareSide * (j+2) - squareSide/2 - 10, right-squareSide/2, light);
					canvas.drawText("" + (char)(j+65), squareSide * (j+2) - squareSide/2 - 10, squareSide/2, light);
				}
			}
			
			canvas.drawText("" + (8-i), squareSide/2, squareSide * (i+2) - squareSide/2, light);
			canvas.drawText("" + (i+1), right-squareSide/2, squareSide * (i+2) - squareSide/2, light);
		}
		
		if(this.cb == null) {
			return;
		}
		
		for(ChessFigure cf : this.cb.getPlayer()) {
			String uri = "drawable/white_" + cf.getFigureName();
			//String uri = "drawable/black_knight";
			
			try {
				int imageResource = getResources().getIdentifier(uri, null, this.getContext().getPackageName());
				
				BitmapDrawable bd = (BitmapDrawable) getResources().getDrawable(imageResource);
				bd.setBounds(0, 0, (int)squareSide, (int)squareSide);
				Bitmap bm = bd.getBitmap();
				Paint paint = new Paint();
				canvas.drawBitmap(bm, squareSide * (cf.getY()), squareSide * (9-cf.getX()), paint);
			} catch (Exception e) {
				
			}
		}
		
		for(ChessFigure cf : this.cb.getComputer()) {
			String uri = "drawable/black_" + cf.getFigureName();
			//String uri = "drawable/black_knight";
			
			try {
				int imageResource = getResources().getIdentifier(uri, null, this.getContext().getPackageName());
				
				BitmapDrawable bd = (BitmapDrawable) getResources().getDrawable(imageResource);
				bd.setBounds(0, 0, (int)squareSide, (int)squareSide);
				Bitmap bm = bd.getBitmap();
				Paint paint = new Paint();
				canvas.drawBitmap(bm, squareSide * (cf.getY()), squareSide * (cf.getX()), paint);
			} catch (Exception e) {
				
			}
		}
		
		//Highlighting
		if(toHighlight && 
				ChessUiView.this.highlightedX >= 1 && ChessUiView.this.highlightedX <= 8 &&
				ChessUiView.this.highlightedY >= 1 && ChessUiView.this.highlightedY <= 8) {
			Paint hgPaint = new Paint();
			hgPaint.setColor(Color.CYAN);
			hgPaint.setStyle(Style.STROKE);
			hgPaint.setStrokeWidth(3);
			
			canvas.drawRect(
					squareSide * this.highlightedX + 2, 
					squareSide * this.highlightedY + 2, 
					squareSide * (this.highlightedX + 1) - 2, 
					squareSide * (this.highlightedY + 1) - 2, 
					hgPaint);
			
			//Highlight Possible moves
			Paint hgmPaint = new Paint();
			hgmPaint.setColor(Color.GREEN);
			hgmPaint.setStyle(Style.STROKE);
			hgmPaint.setStrokeWidth(3);
			
			Paint hgmcPaint = new Paint();
			hgmcPaint.setColor(Color.rgb(252, 252, 238));
			hgmcPaint.setStyle(Style.STROKE);
			hgmcPaint.setStrokeWidth(4);
			
			if(this.moves != null) {
				for(ChessMove p : this.moves) {
					if(p.x > 8 || p.y > 8 || p.x < 1 || p.y < 1) {
						continue;
					}
					
					if(p.getMoveType() == ChessMoveType.CMT_ALLOWED) {
						canvas.drawRect(
								squareSide * p.y + 2, 
								squareSide * (9 - p.x) + 2, 
								squareSide * (p.y + 1) - 2, 
								squareSide * ((9 - p.x) + 1) - 2, 
								hgmPaint);
					}
					
					if(p.getMoveType() == ChessMoveType.CMT_CAPTURE) {
						canvas.drawRect(
								squareSide * p.y + 2, 
								squareSide * (9 - p.x) + 2, 
								squareSide * (p.y + 1) - 2, 
								squareSide * ((9 - p.x) + 1) - 2, 
								hgmcPaint);
						
						canvas.drawLine(
								squareSide * p.y + 20, 
								squareSide * (9 - p.x) + 20, 
								squareSide * (p.y + 1) - 20, 
								squareSide * ((9 - p.x) + 1) - 20, 
								hgmcPaint);
						
						canvas.drawLine(
								squareSide * p.y + 20, 
								squareSide * ((9 - p.x) + 1) - 20, 
								squareSide * (p.y + 1) - 20, 
								squareSide * (9 - p.x) + 20, 
								hgmcPaint);
					}
				}
			}
		}
	}
}
