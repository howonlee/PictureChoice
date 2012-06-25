package com.android.PictureChoice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class BlockShowActivity extends Activity {
	Button toChoice;
	TextView blockNum;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.block);
		toChoice = (Button) findViewById(R.id.button_to_choice);
		toChoice.setOnClickListener(new OnClickListener(){
			public void onClick(View view){
				startActivity(new Intent("com.android.CHOICESHOW"));
				overridePendingTransition(0,0); //remove animation
			}
		});

		blockNum = (TextView) findViewById(R.id.blockNumView);
		int temp = GlobalVar.getInstance().getBlockNum();
		String tempStr = "" + (temp + 1);
		blockNum.setText(tempStr);
		GlobalVar.getInstance().setBlockNum((temp + 1));
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
