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

public class BlockShowActivity extends Activity {
	final int SANDSTONE = 0xffeee6cb;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.block);
		Button toChoice = (Button) findViewById(R.id.button_to_choice);
        toChoice.getBackground().setColorFilter(SANDSTONE, PorterDuff.Mode.MULTIPLY);
		toChoice.setOnClickListener(new OnClickListener(){
			public void onClick(View view){
				GlobalVar.getInstance().setAppFlag(true);
				startActivity(new Intent("com.android.CHOICESHOW"));
				overridePendingTransition(0,0); //remove animation
			}
		});

		TextView blockNum = (TextView) findViewById(R.id.blockNumView);
		int temp = GlobalVar.getInstance().getBlockNum();
		String tempStr = "" + (temp + 1);
		blockNum.setText(tempStr + " of " + BreakScreenActivity.getTotalBlocks() + " blocks");
		GlobalVar.getInstance().setBlockNum((temp + 1));
		GlobalVar.getInstance().setAppFlag(false);
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
