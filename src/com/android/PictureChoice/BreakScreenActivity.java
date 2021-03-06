package com.android.PictureChoice;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.android.PictureChoice.Posting.Block;
import com.android.PictureChoice.Posting.ExpEnd;
import com.android.PictureChoice.Posting.PostBlockTask;
import com.android.PictureChoice.Posting.PostExpEndTask;

public class BreakScreenActivity extends Activity {
	//maybe move this to the globals?
	static final int totalBlocks = 2;
	final int SANDSTONE = 0xffeee6cb;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.breakscreen);
		GlobalVar.getInstance().setBreakBeginTime(System.nanoTime());
		GlobalVar.getInstance().setAppFlag(false);
		
		Button toBlock = (Button) findViewById(R.id.button_to_block);
        toBlock.getBackground().setColorFilter(SANDSTONE, PorterDuff.Mode.MULTIPLY);
        TextView breakMsg = (TextView) findViewById(R.id.break_msg);
        toBlock.setOnClickListener(new OnClickListener(){
        	public void onClick(View view){
        		sendBlockPost();
        		GlobalVar.getInstance().setAppFlag(true);
        		startActivity(new Intent("com.android.BLOCKSHOW"));
        		overridePendingTransition(0,0); //remove animation
        	}
        });
        if (GlobalVar.getInstance().getBlockNum() == totalBlocks){
        	toBlock.setText("Press to exit the experiment");
        	breakMsg.setText("OK, you're done. \n\n The code for the Mechanical Turk HIT is 23FejJ4");
        	sendEndPost();
        	toBlock.setOnClickListener(new OnClickListener(){
        		public void onClick(View view){
        			sendBlockPost();
        			moveTaskToBack(true);
        			//you can't terminate an app in android
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
	
	private void sendEndPost(){
		String expId = Integer.toString(GlobalVar.getInstance().getExpId());
		ExpEnd thisExpEnd = new ExpEnd(expId);
		PostExpEndTask expEnd = new PostExpEndTask();
		expEnd.execute(thisExpEnd);
	}
	
	public static int getTotalBlocks(){
		return totalBlocks;
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
	
	@Override
	public void onPause(){
		super.onPause();
		if (!(GlobalVar.getInstance().getAppFlag())){
			GlobalVar.getInstance().setInterrupted();
		}
	}
}
