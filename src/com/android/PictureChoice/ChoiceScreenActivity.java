package com.android.PictureChoice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class ChoiceScreenActivity extends Activity {
	final int numTrials = 5;
	int trialCount = 0;
	//all time units in milliseconds
	final int minTime = 50;
	final int maxTime = 200;
	final int maskTime = 50;
	
	//used for cycling visibility
	private int visState = 0;

	private Handler mHandler = new Handler();

	ImageView pic, mask;
	Button choice1, choice2;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choice);
		//find all the views
		pic = (ImageView) findViewById(R.id.picture);
		mask = (ImageView) findViewById(R.id.mask);
		choice1 = (Button) findViewById(R.id.choice1);
		choice2 = (Button) findViewById(R.id.choice2);

		pic.setAnimation(null);
		mask.setAnimation(null);
		choice1.setOnClickListener(new OnClickListener(){
			public void onClick(View view){
				trialCount++;
				if (trialCount == numTrials){
					goToBreak();
				} else {
					doTrial();
				}
			}
		});
		choice2.setOnClickListener(new OnClickListener(){
			public void onClick(View view){
				trialCount++;
				if (trialCount == numTrials){
					goToBreak();
				} else {
					doTrial();
				}
			}
		});
		doTrial();
	}

	private void doTrial(){
		final Runnable cycleVis = new Runnable(){
			public void run(){
				cycleVisibility();
			}
		};
		Runnable threadRunnable = new Runnable(){
			public void run(){
				//mask waits
				mHandler.post(cycleVis);
				try {
					Thread.sleep(1200);//astoundingly bad
				} catch (InterruptedException e){
					e.printStackTrace();
				}
				mHandler.post(cycleVis);
				try {
					Thread.sleep(1200);
				} catch (InterruptedException e){
					e.printStackTrace();
				}
				mHandler.post(cycleVis);

			}
		};
		new Thread(threadRunnable).start();
	}

	private void cycleVisibility(){
		switch(visState){
		case 0: 
			pic.setVisibility(ImageView.VISIBLE);
			choice1.setVisibility(ImageView.INVISIBLE);
			choice2.setVisibility(ImageView.INVISIBLE);
			break;
		case 1:
			pic.setVisibility(ImageView.INVISIBLE);
			mask.setVisibility(ImageView.VISIBLE);
			break;
		case 2: 
			mask.setVisibility(ImageView.INVISIBLE);
			choice1.setVisibility(ImageView.VISIBLE);
			choice2.setVisibility(ImageView.VISIBLE);
			break;
		}
		visState++;
		if (visState >= 3) {
			visState = 0;
		}
	}
	
	private void goToBreak(){
		choice1.setVisibility(View.INVISIBLE);
		choice2.setVisibility(View.INVISIBLE);
		startActivity(new Intent("com.android.BREAKSHOW"));
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
