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

import com.android.PictureChoice.Posting.AsyncPostTask;
import com.android.PictureChoice.Posting.PostableData;

public class BreakScreenActivity extends Activity {
	//maybe move this to the globals?
	static final int totalBlocks = 2;
	final int SANDSTONE = 0xffeee6cb;
	//String expCode = "";
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
        	//expCode = getExpCode();
        	breakMsg.setText("OK, you're done.\n\nYour score is:"
        			.concat(Integer.toString(GlobalVar.getInstance().getNumCorrect()))
        			.concat("\n\nPlease press the exit button and inform the experimenter."));
        	//you got my javascript dot notation into my java!
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
		PostableData block = GlobalVar.getInstance().getBlock();
		AsyncPostTask blockTask = new AsyncPostTask();
		blockTask.execute(block);
	}
	
	private void sendEndPost(){
		String expId = Integer.toString(GlobalVar.getInstance().getExpId());
		PostableData endTask = new PostableData("http://www.stanford.edu/group/pdplab/cgi-bin/expend.php",
												GlobalVar.getInstance().getName().concat("_expend.txt"));
		endTask.add("exp_id", expId);
		//endTask.add("exp_code", data)
		//no expcode; this post will not work currently
		AsyncPostTask expEnd = new AsyncPostTask();
		expEnd.execute(endTask);
	}
	
	//unused
	private String getExpCode(){
		return Long.toHexString(Double.doubleToLongBits(Math.random())).substring(0, 6);
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
