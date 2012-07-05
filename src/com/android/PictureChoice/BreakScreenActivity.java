package com.android.PictureChoice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.android.PictureChoice.Posting.Block;
import com.android.PictureChoice.Posting.PostBlockTask;

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
		GlobalVar.getInstance().setBreakBeginTime(System.nanoTime());
        toBlock.setOnClickListener(new OnClickListener(){
        	public void onClick(View view){
        		sendBlockPost();
        		startActivity(new Intent("com.android.BLOCKSHOW"));
        		overridePendingTransition(0,0); //remove animation
        	}
        });
        currentBlock = GlobalVar.getInstance().getBlockNum();
        if (currentBlock == totalBlocks){
        	breakMsg.setText("OK, you're done");
        	toBlock.setOnClickListener(new OnClickListener(){
        		public void onClick(View view){
        			sendBlockPost();
        			moveTaskToBack(true);
        			//you can't terminate an app in android; this is sad
        		}
        	});
        }
	}
	
	private void sendBlockPost(){
		GlobalVar.getInstance().setBreakEndTime(System.nanoTime());
		//hopefully, this is a valid block object
		Block block = GlobalVar.getInstance().getBlock();
		PostBlockTask blockTask = new PostBlockTask();
		blockTask.execute(block);
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
