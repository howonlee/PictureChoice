package com.android.PictureChoice;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.android.PictureChoice.Posting.PostTrialTask;
import com.android.PictureChoice.Posting.TrialChoice;

public class ChoiceScreenActivity extends Activity {
	private final int numTrials = 25; //number of trials per block
	private int trialCount = 0;
	private int category = 0; //0 for category 1, 1 for cat 2
	//all time units in milliseconds
	private final int MIN_TIME = 50; //minimum picture-showing time
	private final int MAX_TIME = 300; //maximum picture-showing time
	private final int MASK_TIME = 500; //mask-showing time
	private Random generator = new Random();
	public int currPicLength;
	
	//data on blocks, for posting
	private int currPicId = -1;
	private long currBeginTime;
	private long currEndTime;
	private long currMaskBeginTime;
	private long currMaskEndTime;
	//no current block number, no currChoice: that's in GlobalVar

	//state of the visibility state machine
	private int visState = 0;
	//resource id's for both categories of pictures
	//thread handler and asynctask
	private Handler mHandler = new Handler();
	private PostTrialTask uploadTask = new PostTrialTask();

	//views
	ImageView pic, mask;
	Button choice1, choice2;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choice);
		findViews();
		GlobalVar.getInstance().setBeginTime(System.nanoTime());
		GlobalVar.getInstance().setAppFlag(false);
		choice1.setOnClickListener(new OnClickListener(){
			public void onClick(View view){
				TrialChoice choice = new TrialChoice(currPicId, currBeginTime, currEndTime, 
						GlobalVar.getInstance().getBlockNum(), 0,
						currMaskBeginTime, currMaskEndTime, currPicLength);
				uploadTask = new PostTrialTask();//one of two main inefficiencies
				uploadTask.execute(choice);
				System.gc();
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
				TrialChoice choice = new TrialChoice(currPicId, currBeginTime, currEndTime, 
						GlobalVar.getInstance().getBlockNum(), 1,
						currMaskBeginTime, currMaskEndTime, currPicLength);
				uploadTask = new PostTrialTask();//one of two main inefficiencies
				uploadTask.execute(choice);
				System.gc();
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

	/**
	 * find all the views and set options for them
	 */
	private void findViews(){
		pic = (ImageView) findViewById(R.id.picture);
		mask = (ImageView) findViewById(R.id.mask);
		choice1 = (Button) findViewById(R.id.choice1);
		choice2 = (Button) findViewById(R.id.choice2);
		pic.setAnimation(null);
		mask.setAnimation(null);
		choice1.setAnimation(null);
		choice2.setAnimation(null);
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
					currPicLength = MIN_TIME + generator.nextInt((MAX_TIME - MIN_TIME));
					Log.d("picLength", Integer.toString(currPicLength));
					Thread.sleep(currPicLength);//astoundingly bad
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
			mask.setVisibility(ImageView.INVISIBLE);
			pic.setVisibility(ImageView.VISIBLE);
			choice1.setVisibility(ImageView.INVISIBLE);
			choice2.setVisibility(ImageView.INVISIBLE);
			currBeginTime = System.nanoTime();
			break;
		case 1:
			pic.setVisibility(ImageView.INVISIBLE);
			mask.setVisibility(ImageView.VISIBLE);
			currEndTime = System.nanoTime();
			currMaskBeginTime = System.nanoTime(); //redundant, yes
			break;
		case 2: 
			mask.setVisibility(ImageView.INVISIBLE);
			currMaskEndTime = System.nanoTime();
			updatePic();
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
		GlobalVar.getInstance().setAppFlag(true);
		GlobalVar.getInstance().setEndTime(System.nanoTime());
		startActivity(new Intent("com.android.BREAKSHOW"));
		overridePendingTransition(0,0); //remove animation
	}

	private void updatePic(){
		ArrayList<Integer> cat1 = GlobalVar.getInstance().getCat1();
		ArrayList<Integer> cat2 = GlobalVar.getInstance().getCat2();
		
		category = generator.nextInt(2);//range 0 to 1
		Integer resId = 0;
		if (category == 0){
			resId = GlobalVar.getInstance().chooseResId(0, cat1);
			currPicId = ((resId - R.drawable.animal01) * 4) + 1;
		} else if (category == 1){
			resId = GlobalVar.getInstance().chooseResId(1, cat2);
			currPicId = ((resId - R.drawable.noanimal01)* 4) + 3;
		}
		//now that we've chosen resId...
		if (resId != 0){
			updateView(resId);
			storeInCache(cat1);
			storeInCache(cat2);
		}
	}
	
	private void updateView(int resId){
		final String imageKey = String.valueOf(resId);
		final Bitmap bitmap = GlobalVar.getInstance().getBitmapFromMemCache(imageKey);
		if (bitmap != null){
			pic.setImageBitmap(bitmap);
		} else {
			pic.setImageResource(resId);
		}
	}
	
	private void storeInCache(ArrayList<Integer> category){
		if (!category.isEmpty()){
			storeInCache(category.get(0));
		} else {
			GlobalVar.getInstance().initCategories();
		}
	}
	
	private void storeInCache(int resId){
		String id = String.valueOf(resId);
		GlobalVar.getInstance().addBitmapToMemoryCache(id, BitmapFactory.decodeResource(getResources(), resId));
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
