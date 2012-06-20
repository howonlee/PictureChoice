package com.android.PictureChoice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BlockShowActivity extends Activity {
	Button toChoice;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.block);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		toChoice = (Button) findViewById(R.id.button_to_choice);
		toChoice.setOnClickListener(new OnClickListener(){
			public void onClick(View view){
				startActivity(new Intent("com.android.CHOICESHOW"));
			}
		});

	}
}
