package com.android.PictureChoice;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

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
	private final int numTrials = 5; //number of trials per block
	private int trialCount = 0;
	private int category = 0; //0 for category 1, 1 for cat 2
	//all time units in milliseconds
	private final int MIN_TIME = 50; //minimum picture-showing time
	private final int MAX_TIME = 500; //maximum picture-showing time
	private final int MASK_TIME = 500; //mask-showing time
	private String urlString = new String("http://www.google.com/");
	private Random generator = new Random();
	
	//state of the visibility state machine
	private int visState = 0;
	private ArrayList<Integer> cat1 = new ArrayList<Integer>();
	private ArrayList<Integer> cat2 = new ArrayList<Integer>();	
	private Handler mHandler = new Handler();
	private PostDataTask uploadTask = new PostDataTask();
	private URL url;

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
		try{
			url = new URL(urlString);
		} catch (MalformedURLException ex){
			throw new RuntimeException(ex);
		}

		initCategories();
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
		updatePic();
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
					int picLength = MIN_TIME + generator.nextInt((MAX_TIME - MIN_TIME));
					Thread.sleep(picLength);//astoundingly bad
				} catch (InterruptedException e){
					e.printStackTrace();
				}
				mHandler.post(cycleVis);
				try {
					Thread.sleep(MASK_TIME);
				} catch (InterruptedException e){
					e.printStackTrace();
				}
				mHandler.post(cycleVis);

			}
		};
		new Thread(threadRunnable).start();
	}

	/*
	 * cycleVisibility()
	 * This is basically a state machine to cycle through
	 * the possible visibilities
	 */
	private void cycleVisibility(){
		switch(visState){
		case 0: 
			pic.setVisibility(ImageView.VISIBLE);
			choice1.setVisibility(ImageView.INVISIBLE);
			choice2.setVisibility(ImageView.INVISIBLE);
			break;
		case 1:
			pic.setVisibility(ImageView.INVISIBLE);
			updatePic();
			uploadTask = new PostDataTask();
			uploadTask.execute(url);
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
		overridePendingTransition(0,0); //remove animation
	}
	
	private void initCategories(){
		//use ArrayList for categories since you can remove stuff
		cat1.clear();
		cat1.add(R.drawable.animal1);
		cat1.add(R.drawable.animal2);
		cat1.add(R.drawable.animal3);
		cat1.add(R.drawable.animal4);
		cat1.add(R.drawable.animal5);
		
		cat2.clear();
		cat2.add(R.drawable.noanimal1);
		cat2.add(R.drawable.noanimal2);
		cat2.add(R.drawable.noanimal3);
		cat2.add(R.drawable.noanimal4);
		cat2.add(R.drawable.noanimal5);
	}
	
	private void updatePic(){
		category = generator.nextInt(2);//range 0 to 1
		Integer resId = 0;
		if (category == 0){
			if (!cat1.isEmpty()){
				resId = cat1.remove(generator.nextInt(cat1.size()));
			} else { initCategories(); }
		} else if (category == 1){
			if (!cat2.isEmpty()){
				resId = cat2.remove(generator.nextInt(cat2.size()));
			} else { initCategories(); }
		}
		if (resId != 0){
			pic.setImageResource(resId);//lag problem
		}
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
