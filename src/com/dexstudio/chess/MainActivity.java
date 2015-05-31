package com.dexstudio.chess;

import com.dexstudio.chess.entity.ChessBoard;
import com.dexstudio.chess.ui.ChessUiView;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends ActionBarActivity {
	
	//Consts
	private final int MAX_DEPTH = 5;
	//Variables
	private ChessBoard cb = null;
	private ChessUiView cuView = null;
	
	private Spinner sPlayerStrategy = null;
	private Spinner sComputerStrategy = null;
	private Spinner sDepth = null;
	
	private Button btnNext = null;
	private Button btnPrev = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		cuView = (ChessUiView)findViewById(R.id.chessui);
		
		this.initAlgorithms();
		this.initTreeDepth();
		this.initControls();
	}
	
	private void initControls() {
		btnNext = (Button) findViewById(R.id.btnNext);
		btnPrev = (Button) findViewById(R.id.btnPrev);
		
		btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		
		btnPrev.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MainActivity.this.cb.nextMove();
			}
		});
	}

	private void initAlgorithms() {
		//Init Algorithms
		sComputerStrategy = (Spinner) findViewById(R.id.sComputerStrategy);
		sPlayerStrategy = (Spinner) findViewById(R.id.sPlayerStrategy);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.algorithm_array, android.R.layout.simple_spinner_item);
		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sComputerStrategy.setAdapter(adapter);
		sPlayerStrategy.setAdapter(adapter);
	}
	
	private void initTreeDepth() {
		sDepth = (Spinner) findViewById(R.id.sDepth);
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
		for(int i=0; i<MAX_DEPTH+1; i++) {
			adapter.add("" + i);
		}
		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sDepth.setAdapter(adapter);
		sDepth.setSelection(3);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if (id == R.id.action_simulation) {
			cb = new ChessBoard(sPlayerStrategy.getSelectedItem().toString(), 
					sComputerStrategy.getSelectedItem().toString(), 
					Integer.parseInt(sDepth.getSelectedItem().toString()));
			cuView.setChessBoard(cb, true);
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
