package com.android.PictureChoice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class ChoiceScreenActivity extends Activity {
	int numTrials = 100;
	//all time units in milliseconds
	int minTime = 50;
	int maxTime = 200;
	int maskTime = 50; 
	
	private Handler mHandler = new Handler();
	
	ImageView pic;
	ImageView mask;
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
        
        pic = (ImageView) findViewById(R.id.picture);
        pic.setAnimation(null);
        mask = (ImageView) findViewById(R.id.mask);
        mask.setAnimation(null);
        doTrial();
	}
	
	private void doTrial(){
		final Runnable postRunnable = new Runnable(){
			public void run(){
				pic.setVisibility(ImageView.INVISIBLE);
				mask.setVisibility(ImageView.VISIBLE);
			}
		};
		Runnable threadRunnable = new Runnable(){
			public void run(){
				int i = 0;
				while (i <= 1) {
					try {
						Thread.sleep(1200);
						/*
						 * This is astoundingly bad
						 * I mean, incredibly crappy
						 */
					} catch (InterruptedException e){
						e.printStackTrace();
					}
					i++;
				}
				mHandler.post(postRunnable);
			}
		};
		new Thread(threadRunnable).start();
	}
}
