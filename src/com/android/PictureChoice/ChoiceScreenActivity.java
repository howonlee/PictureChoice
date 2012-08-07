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
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.PictureChoice.Posting.PostTrialTask;
import com.android.PictureChoice.Posting.TrialChoice;

public class ChoiceScreenActivity extends Activity {
	private final int numTrials = 50;
	private final int numSixes = 18;
	private final int numOthers = 7;
	private int trialCount = 0; 
	//private final int MIN_TIME = 50; //minimum picture-showing time
	//private final int MAX_TIME = 300; //maximum picture-showing time
	//private ArrayList<Integer> possibleTimes = new ArrayList<Integer>();
	private ArrayList<Pair<Integer, Integer>> picQueue = new ArrayList<Pair<Integer, Integer>>();
	//private final int FIXATION_TIME = 500;
	private final int FIRST_TIME = 20000;
	private final int SECOND_TIME = 20000;
	private final int MASK_TIME = 1000; //mask-showing time
	//private final int BUTTON_TIME = Integer.MAX_VALUE; //amount of time for button pressing
	private final int FEEDBACK_TIME = 500; //this doesn't seem to be actual time
	
	//data on blocks, for posting
	public int currPicLength;
	private String currPicId = "invalid";
	private int currChoiceMade = 0;
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
	private Thread mThread;
	private PostTrialTask uploadTask = new PostTrialTask();

	//views
	ImageView pic, mask, pic2; //fixation;
	Button choice1, choice2, picButton1, picButton2;
	TextView feedback;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choice);
		findViews();
		GlobalVar.getInstance().setBeginTime(System.nanoTime());
		GlobalVar.getInstance().setAppFlag(false);
		initPicQueue();
		setButtonOnClickListener(choice1, 1);
		setButtonOnClickListener(choice2, -1);
		updatePic();
		doTrial();
	}
	
	private void setButtonOnClickListener(Button button, final int choiceMade){
		button.setOnClickListener(new OnClickListener(){
			public void onClick(View view){
				currChoiceMade = choiceMade;
				currClickTime = System.nanoTime();
				System.gc();
				mThread.interrupt();
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
		//fixation = (ImageView) findViewById(R.id.fixation);
		feedback = (TextView) findViewById(R.id.feedbackView);
		choice1 = (Button) findViewById(R.id.choice1);
		choice2 = (Button) findViewById(R.id.choice2);
		picButton1 = (Button) findViewById(R.id.pic_button1);
		picButton2 = (Button) findViewById(R.id.pic_button2);
		OnClickListener picClickListener = new OnClickListener(){
			public void onClick(View v) {
				mThread.interrupt(); //thread interruption: is very hacky, yes
				currPicClickTime = System.nanoTime();
			}
		};
		picButton1.setOnClickListener(picClickListener);
		picButton2.setOnClickListener(picClickListener);
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
				mHandler.post(cycleVis);
				try {
					currPicLength = FIRST_TIME;
					Log.d("picLength", Integer.toString(currPicLength));
					Thread.sleep(currPicLength);
					mHandler.post(cycleVis);
				} catch (InterruptedException e){
					mHandler.post(cycleVis);
				}
				try {
					Thread.sleep(MASK_TIME);
					mHandler.post(cycleVis);
				} catch (InterruptedException e){
					mHandler.post(cycleVis);
				}
				try {
					Thread.sleep(SECOND_TIME);
					mHandler.post(cycleVis);
				} catch (InterruptedException e){
					mHandler.post(cycleVis);
				}
				try {
					Thread.sleep(FEEDBACK_TIME);
					mHandler.post(cycleVis);
				} catch (InterruptedException e){
					mHandler.post(cycleVis);
				}
			}
		};
		mThread = new Thread(threadRunnable);
		mThread.start();
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
			currChoiceMade = 0;
			feedback.setVisibility(ImageView.INVISIBLE);
			mask.setVisibility(ImageView.INVISIBLE);
			pic.setVisibility(ImageView.VISIBLE);
			picButton1.setVisibility(ImageView.VISIBLE);
			picButton2.setVisibility(ImageView.VISIBLE);
			currBeginTime = System.nanoTime();
			break;
		case 1:
			pic.setVisibility(ImageView.INVISIBLE);
			picButton1.setVisibility(ImageView.INVISIBLE);
			picButton2.setVisibility(ImageView.INVISIBLE);
			mask.setVisibility(ImageView.VISIBLE);
			currEndTime = System.nanoTime();
			currMaskBeginTime = System.nanoTime(); //redundant, yes
			break;
		case 2: 
			mask.setVisibility(ImageView.INVISIBLE);
			currMaskEndTime = System.nanoTime();
			pic2.setVisibility(ImageView.VISIBLE);
			currBeginTime2 = System.nanoTime();
			choice1.setVisibility(ImageView.VISIBLE);
			choice2.setVisibility(ImageView.VISIBLE);
			GlobalVar.getInstance().clearMemory();
			break;
		case 3:
			currEndTime2 = System.nanoTime();
			setFeedback(currChoiceMade, currPicId);
			feedback.setVisibility(ImageView.VISIBLE);
			pic2.setVisibility(ImageView.INVISIBLE);
			choice1.setVisibility(ImageView.INVISIBLE);
			choice2.setVisibility(ImageView.INVISIBLE);
			postData();
			break;
		case 4:
			trialCount++;
			if (trialCount == numTrials){
				goToBreak();
			} else {
				updatePic();
				doTrial();
			}
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
		ArrayList<ArrayList<Pair<Integer, Integer>>> picIds = GlobalVar.getInstance().getPicIds();
		Integer resId = 0, resId2 = 0;
		Pair<Integer, Integer> picPair = new Pair<Integer, Integer>(0,0);
		logCategory(picQueue);
		if (!picQueue.isEmpty()){
			picPair = picQueue.remove(0); 
		}
		resId = picPair.first;
		resId2 = picPair.second;
		if (resId != 0 && resId2 != 0){
			currPicId = getResources().getResourceEntryName(resId);
			updateView(resId);
			updateView2(resId2);
			Log.d("pic1", Integer.toString(resId));
			Log.d("pic2", Integer.toString(resId2));
			for (int i = 0; i < picIds.size(); i++){
				storeInCache(picIds.get(i));
			}
		}
	}
	
	private void initPicQueue(){
		ArrayList<ArrayList<Pair<Integer, Integer>>> picIds = GlobalVar.getInstance().getPicIds();
		for (int i = 0; i < numSixes; i++){
			picQueue.add(picIds.get(0).remove(0));
			picQueue.add(picIds.get(2).remove(0));
		}
		for (int i = 0; i < numOthers; i++){
			picQueue.add(picIds.get(1).remove(0));
			picQueue.add(picIds.get(3).remove(0));
		}
		Collections.shuffle(picQueue);
	}
	
	private void logCategory(ArrayList<Pair<Integer, Integer>> category){
		ArrayList<String> resources = new ArrayList<String>();
		for (int i = 0; i < category.size(); i++){
			resources.add(getResources().getResourceEntryName(category.get(i).first));
			resources.add(getResources().getResourceEntryName(category.get(i).second));
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
	
	private void storeInCache(ArrayList<Pair<Integer, Integer>> category){
		if (!category.isEmpty()){
			storeInCache(category.get(0).first);
		} else {
			GlobalVar.getInstance().initCategories();
		}
	}
	
	private void storeInCache(int resId){
		String id = String.valueOf(resId);
		GlobalVar.getInstance().addBitmapToMemoryCache(id, BitmapFactory.decodeResource(getResources(), resId));
	}
	
	private void postData(){
		int expId = GlobalVar.getInstance().getExpId();
		TrialChoice choice = new TrialChoice(currPicId, currBeginTime,
				currEndTime, currBeginTime2, currEndTime2, 
				GlobalVar.getInstance().getBlockNum(),
				currChoiceMade, currMaskBeginTime, currMaskEndTime,
				currPicClickTime, currClickTime, currPicLength,
				expId);
		uploadTask = new PostTrialTask();//one of two main inefficiencies
		uploadTask.execute(choice);
	}
	
	private void setFeedback(int currChoice, String currPicId){
		//right
		String feedbackString = "";
		Log.w("setFeedback", Integer.toString(currChoice));
		Log.w("setFeedback", currPicId);
		if (currChoice == 1 && currPicId.contains("c")){
			feedbackString = "Correct!";
		} else if (currChoice == -1 && !(currPicId.contains("c"))){
			feedbackString = "Correct!";
		} else if (currChoice == 0){
			feedbackString = "Too late!";
		} else {
			feedbackString = "Incorrect!";
		}
		feedback.setText(feedbackString);
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
