package com.android.PictureChoice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
			}
		});

		blockNum = (TextView) findViewById(R.id.blockNumView);
		int temp = GlobalVar.getInstance().getBlockNum();
		String tempStr = "" + temp; //hacky
		blockNum.setText(tempStr);
		GlobalVar.getInstance().setBlockNum((temp + 1));
	}
}
