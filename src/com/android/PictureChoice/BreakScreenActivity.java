package com.android.PictureChoice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class BreakScreenActivity extends Activity {
	//maybe move this to the globals?
	final int totalBlocks = 2;
	int currentBlock;
	Button toBlock;
	TextView breakMsg;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.breakscreen);
        toBlock = (Button) findViewById(R.id.button_to_block);
        breakMsg = (TextView) findViewById(R.id.break_msg);
        toBlock.setOnClickListener(new OnClickListener(){
        	public void onClick(View view){
        		startActivity(new Intent("com.android.BLOCKSHOW"));
        	}
        });
        currentBlock = GlobalVar.getInstance().getBlockNum();
        if (currentBlock == totalBlocks){
        	breakMsg.setText("OK, you're done");
        	toBlock.setOnClickListener(new OnClickListener(){
        		public void onClick(View view){
        			moveTaskToBack(true);
        			//you can't terminate an app in android; this is sad
        		}
        	});
        }
	}
	

	@Override
	public void onBackPressed(){
		//do nothing
	}

	@Override
	public boolean onSearchRequested(){
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		return true; //so no keys will work
	}
}
