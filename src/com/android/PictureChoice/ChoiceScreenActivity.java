package com.android.PictureChoice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import com.android.PictureChoice.Posting.PostTrialTask;
import com.android.PictureChoice.Posting.TrialChoice;

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

public class ChoiceScreenActivity extends Activity {
	private final int numTrials = 5; //number of trials per block
	private int trialCount = 0;
	private int category = 0; //0 for category 1, 1 for cat 2
	//all time units in milliseconds
	private final int MIN_TIME = 499; //minimum picture-showing time
	private final int MAX_TIME = 500; //maximum picture-showing time
	private final int MASK_TIME = 500; //mask-showing time
	private Random generator = new Random();
	
	//data for posting
	private int currPicId = -1;
	private long currBeginTime;
	private long currEndTime;
	private long currMaskBeginTime;
	private long currMaskEndTime;
	//no current block number, no currChoice

	//state of the visibility state machine
	private int visState = 0;
	private ArrayList<Integer> cat1 = new ArrayList<Integer>();
	private ArrayList<Integer> cat2 = new ArrayList<Integer>();
	private Handler mHandler = new Handler();
	private PostTrialTask uploadTask = new PostTrialTask();

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

		initCategories();
		pic.setAnimation(null);
		mask.setAnimation(null);
		choice1.setAnimation(null);
		choice2.setAnimation(null);
		GlobalVar.getInstance().setBeginTime(System.nanoTime());
		choice1.setOnClickListener(new OnClickListener(){
			public void onClick(View view){
				TrialChoice choice = new TrialChoice(currPicId, currBeginTime, currEndTime, 
						GlobalVar.getInstance().getBlockNum(), 0,
						currMaskBeginTime, currMaskEndTime);
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
						currMaskBeginTime, currMaskEndTime);
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
		GlobalVar.getInstance().setEndTime(System.nanoTime());
		startActivity(new Intent("com.android.BREAKSHOW"));
		overridePendingTransition(0,0); //remove animation
	}

	private void initCategories(){
		//use ArrayList for categories since you can remove stuff
		cat1.clear();
		//perhaps
		cat1.add(R.drawable.animal01);
		cat1.add(R.drawable.animal02);
		cat1.add(R.drawable.animal03);
		cat1.add(R.drawable.animal04);
		cat1.add(R.drawable.animal05);
		cat1.add(R.drawable.animal06);

		cat2.clear();
		cat2.add(R.drawable.noanimal01);
		cat2.add(R.drawable.noanimal02);
		cat2.add(R.drawable.noanimal03);
		cat2.add(R.drawable.noanimal04);
		cat2.add(R.drawable.noanimal05);
		
		Collections.shuffle(cat1);
		Collections.shuffle(cat2);
	}

	private void updatePic(){
		category = generator.nextInt(2);//range 0 to 1
		Integer resId = 0;
		//category arraylists are shuffled, so just take the next
		if (category == 0){
			resId = chooseResId(0, cat1);
			currPicId = ((resId - R.drawable.animal01) * 4) + 1;
			Log.d("currPicId is", Integer.toString(currPicId));
		} else if (category == 1){
			resId = chooseResId(1, cat2);
			currPicId = ((resId - R.drawable.noanimal01)* 4) + 3;
			Log.d("currPicId is", Integer.toString(currPicId));
		}
		Log.d("length of arraylists 1 and 2", cat1.size() + " " + cat2.size());
		//now that we've chosen resId...
		if (resId != 0){
			final String imageKey = String.valueOf(resId);
			final Bitmap bitmap = GlobalVar.getInstance().getBitmapFromMemCache(imageKey);
			if (bitmap != null){
				pic.setImageBitmap(bitmap);
			} else {
				pic.setImageResource(resId);
				Log.d("We set this wrong!", "");
			}
			if (!cat1.isEmpty()){
				storeInCache(cat1.get(0));
			} else {
				initCategories();
			}
			if (!cat2.isEmpty()){
				storeInCache(cat2.get(0));
			} else {
				initCategories();
			}
		}
	}
	
	private void storeInCache(int resId){
		String id = String.valueOf(resId);
		GlobalVar.getInstance().addBitmapToMemoryCache(id, BitmapFactory.decodeResource(getResources(), resId));
	}
	
	private Integer chooseResId(int category, ArrayList<Integer> catList){
		Integer resId = 0;
		if (!catList.isEmpty()){
			resId = catList.remove(0);
		} else {
			initCategories();
			return chooseResId(category, catList);
		}
		return resId;
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
