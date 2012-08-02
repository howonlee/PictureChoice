package com.android.PictureChoice;

import java.util.ArrayList;
import java.util.Collections;

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
	private final int numTrials = 50;
	private final int numSixes = 18;
	private final int numOthers = 7;
	private int trialCount = 0;
	private int changeIndicator = 0; //change or no change
	//all time units in milliseconds
	//private final int MIN_TIME = 50; //minimum picture-showing time
	//private final int MAX_TIME = 300; //maximum picture-showing time
	//private ArrayList<Integer> possibleTimes = new ArrayList<Integer>();
	private ArrayList<Integer> changeNoChange = new ArrayList<Integer>();
	private final int FIXATION_TIME = 500;
	private final int FIRST_TIME = 500;
	private final int SECOND_TIME = 1000;
	private final int MASK_TIME = 1000; //mask-showing time
	
	//data on blocks, for posting
	public int currPicLength;
	private String currPicId = "invalid";
	private long currBeginTime;
	private long currEndTime;
	private long currBeginTime2;
	private long currEndTime2;
	private long currMaskBeginTime;
	private long currMaskEndTime;
	private long currPicClickTime = 0;
	private long currClickTime;
	//no current block number, no currChoice: that's in GlobalVar

	//state of the visibility state machine
	private int visState = 0;
	//thread handler and asynctask
	private Handler mHandler = new Handler();
	private PostTrialTask uploadTask = new PostTrialTask();

	//views
	ImageView pic, mask, pic2, fixation;
	Button choice1, choice2;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choice);
		findViews();
		GlobalVar.getInstance().setBeginTime(System.nanoTime());
		GlobalVar.getInstance().setAppFlag(false);
		setButtonOnClickListener(choice1, 1);
		setButtonOnClickListener(choice2, -1);
		updatePic();
		doTrial();
	}
	
	private void setButtonOnClickListener(Button button, final int choiceMade){
		button.setOnClickListener(new OnClickListener(){
			public void onClick(View view){
				int expId = GlobalVar.getInstance().getExpId();
				currClickTime = System.nanoTime();
				TrialChoice choice = new TrialChoice(currPicId, currBeginTime,
						currEndTime, currBeginTime2, currEndTime2, 
						GlobalVar.getInstance().getBlockNum(),
						choiceMade, currMaskBeginTime, currMaskEndTime,
						currPicClickTime, currClickTime, currPicLength,
						expId);
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
	}

	/**
	 * find all the views and set options for them
	 */
	private void findViews(){
		pic = (ImageView) findViewById(R.id.picture);
		mask = (ImageView) findViewById(R.id.mask);
		pic2 = (ImageView) findViewById(R.id.picture2);
		fixation = (ImageView) findViewById(R.id.fixation);
		choice1 = (Button) findViewById(R.id.choice1);
		choice2 = (Button) findViewById(R.id.choice2);
		pic.setAnimation(null);
		mask.setAnimation(null);
		pic2.setAnimation(null);
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
					Thread.sleep(FIXATION_TIME);
				} catch (InterruptedException e){
					e.printStackTrace();
				}
				mHandler.post(cycleVis);
				try {
					currPicLength = FIRST_TIME;
					Log.d("picLength", Integer.toString(currPicLength));
					Thread.sleep(currPicLength);
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
				try {
					currPicLength = SECOND_TIME;
					Thread.sleep(currPicLength);
				} catch (InterruptedException e){
					e.printStackTrace();
				}
				mHandler.post(cycleVis);

			}
		};
		new Thread(threadRunnable).start();
	}
	
	/*private int getNextPicLength(){
		if (possibleTimes.isEmpty()){
			for (int i = 50; i < 300; i += 50){
				possibleTimes.add(i);
			}
			Collections.shuffle(possibleTimes);
		}
		return possibleTimes.remove(0);
	}*/

	/*
	 * cycleVisibility()
	 * This is basically a state machine to cycle through
	 * the possible visibilities
	 */
	private void cycleVisibility(){
		switch(visState){
		case 0:
			fixation.setVisibility(ImageView.VISIBLE);
			GlobalVar.getInstance().clearMemory();
			choice1.setVisibility(ImageView.INVISIBLE);
			choice2.setVisibility(ImageView.INVISIBLE);
			break;
		case 1: 
			fixation.setVisibility(ImageView.INVISIBLE);
			mask.setVisibility(ImageView.INVISIBLE);
			pic.setVisibility(ImageView.VISIBLE);
			currBeginTime = System.nanoTime();
			break;
		case 2:
			pic.setVisibility(ImageView.INVISIBLE);
			mask.setVisibility(ImageView.VISIBLE);
			currEndTime = System.nanoTime();
			currMaskBeginTime = System.nanoTime(); //redundant, yes
			break;
		case 3: 
			mask.setVisibility(ImageView.INVISIBLE);
			currMaskEndTime = System.nanoTime();
			pic2.setVisibility(ImageView.VISIBLE);
			currBeginTime2 = System.nanoTime();
			break;
		case 4:
			pic2.setVisibility(ImageView.INVISIBLE);
			currEndTime2 = System.nanoTime();
			updatePic();
			choice1.setVisibility(ImageView.VISIBLE);
			choice2.setVisibility(ImageView.VISIBLE);
			break;
		}
		visState++;
		if (visState >= 5) {
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
		ArrayList<Integer> catChangeFirstSixes = GlobalVar.getInstance().getChangeFirstSixes();
		ArrayList<Integer> catChangeSecondSixes = GlobalVar.getInstance().getChangeSecondSixes();
		ArrayList<Integer> catNoChangeFirstSixes = GlobalVar.getInstance().getNoChangeFirstSixes();
		ArrayList<Integer> catNoChangeSecondSixes = GlobalVar.getInstance().getNoChangeSecondSixes();
		ArrayList<Integer> catChangeFirstOthers = GlobalVar.getInstance().getChangeFirstOthers();
		ArrayList<Integer> catChangeSecondOthers = GlobalVar.getInstance().getChangeSecondOthers();
		ArrayList<Integer> catNoChangeFirstOthers = GlobalVar.getInstance().getNoChangeFirstOthers();
		ArrayList<Integer> catNoChangeSecondOthers = GlobalVar.getInstance().getNoChangeSecondOthers();
		
		changeIndicator = getChangeNoChange();
		Integer resId = 0, resId2 = 0;
		logCategory(catChangeFirstSixes);
		logCategory(catNoChangeFirstSixes);
		logCategory(catChangeFirstOthers);
		logCategory(catNoChangeFirstOthers);
		switch (changeIndicator){
		case 0:
			resId = GlobalVar.getInstance().chooseResId(0, catChangeFirstSixes);
			resId2 = GlobalVar.getInstance().chooseResId(0, catChangeSecondSixes);
			break;
		case 1:
			resId = GlobalVar.getInstance().chooseResId(1, catNoChangeFirstSixes);
			resId2 = GlobalVar.getInstance().chooseResId(1, catNoChangeSecondSixes);
			break;
		case 2:
			resId = GlobalVar.getInstance().chooseResId(0, catChangeFirstOthers);
			resId2 = GlobalVar.getInstance().chooseResId(0, catChangeSecondOthers);
			break;
		case 3:
			resId = GlobalVar.getInstance().chooseResId(1, catNoChangeFirstOthers);
			resId2 = GlobalVar.getInstance().chooseResId(1, catNoChangeSecondOthers);
			break;
		}
		currPicId = getResources().getResourceEntryName(resId);
		if (resId != 0 && resId2 != 0){
			updateView(resId);
			updateView2(resId2);
			Log.d("pic1", Integer.toString(resId));
			Log.d("pic2", Integer.toString(resId2));
			storeInCache(catChangeFirstSixes);
			storeInCache(catChangeFirstOthers);
			storeInCache(catNoChangeFirstSixes);
			storeInCache(catNoChangeFirstOthers);
		}
	}
	
	private void logCategory(ArrayList<Integer> category){
		ArrayList<String> resources = new ArrayList<String>();
		for (int i = 0; i < category.size(); i++){
			resources.add(getResources().getResourceEntryName(category.get(i)));
		}
		Log.w("logCategory", resources.toString());
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

	private void updateView2(int resId2){
		final String imageKey = String.valueOf(resId2);
		final Bitmap bitmap = GlobalVar.getInstance().getBitmapFromMemCache(imageKey);
		if (bitmap != null){
			pic2.setImageBitmap(bitmap);
		} else {
			pic2.setImageResource(resId2);
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
	
	private int getChangeNoChange(){
		if (changeNoChange.isEmpty()){
			for (int i = 0; i < numSixes; i++){
				changeNoChange.add(0);
				changeNoChange.add(1);
			}
			for (int i = 0; i < numOthers; i++){
				changeNoChange.add(2);
				changeNoChange.add(3);
			}
			Collections.shuffle(changeNoChange);
			Log.w("changeNoChange", "initialized");
		}
		return changeNoChange.remove(0);
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
