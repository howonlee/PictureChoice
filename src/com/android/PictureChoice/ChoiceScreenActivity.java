package com.android.PictureChoice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ChoiceScreenActivity extends Activity {
	int numTrials = 100;
	Button toBreak;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choice);
        toBreak = (Button) findViewById(R.id.button_to_break);
        toBreak.setOnClickListener(new OnClickListener(){
        	public void onClick(View view){
        		startActivity(new Intent("com.android.BREAKSHOW"));
        	}
        });
	}
}
