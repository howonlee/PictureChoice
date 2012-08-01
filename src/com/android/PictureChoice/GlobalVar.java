package com.android.PictureChoice;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.PictureChoice.libs.LruCache;

import com.android.PictureChoice.Posting.Block;

/**
 * Singleton global variable holder
 * This holds global variables.
 * that is, it holds:
 * 1. information about current block
 * 2. a cache for images
 * 3. the list of images which are to be used
 * I commit this sin to make counting this crud more bearable
 * @author Howon
 *
 */
class GlobalVar extends Application {
	private int exp_id = -1;
	private int blockNum = 0;
	private ArrayList<Integer> changeFirst = new ArrayList<Integer>();
	private ArrayList<Integer> changeSecond = new ArrayList<Integer>();
	private ArrayList<Integer> noChangeFirst = new ArrayList<Integer>();
	private ArrayList<Integer> noChangeSecond = new ArrayList<Integer>();
	private LruCache<String, Bitmap> imageCache;
	private boolean appFlag = true;
	private boolean interrupted = false;

	//block posting data
	private long time_begin;
	private long time_end;
	private long break_time_begin;
	private long break_time_end;
	
	public int getExpId(){
		return exp_id;
	}
	
	public void setExpId(int expId){
		exp_id = expId;
	}
	
	public int getBlockNum(){
		return blockNum;
	}
	
	public void setAppFlag(boolean app_flag){
		appFlag = app_flag;
	}
	
	public boolean getAppFlag(){
		return appFlag;
	}
	
	public void setInterrupted(){
		interrupted = true; //can't put in value: just say that it's interrupted
	}
	
	public boolean getInterrupted(){
		return interrupted;
	}
	
	public void setBlockNum(int blockNum){
		this.blockNum = blockNum;
	}
	//here's a lot of setters for the block data
	//and one getter, returning a Block object
	public void setBeginTime(long beginTime){
		time_begin = beginTime;
	}
	
	public void setEndTime(long endTime){
		time_end = endTime;
	}
	
	public void setBreakBeginTime(long breakBeginTime){
		break_time_begin = breakBeginTime;
	}
	
	public void setBreakEndTime(long breakEndTime){
		break_time_end = breakEndTime;
	}
	
	public Block getBlock(){
		return new Block(time_begin, time_end, break_time_begin,
			break_time_end, interrupted, exp_id, blockNum);
	}
	
	public void initCache(){
		final int memClass = 32; //call it this for now
		final int cacheSize = 1024 * 1024 * memClass / 8;
		imageCache = new LruCache<String, Bitmap>(cacheSize);
		//static LruCache, so we have normal sizes
	}
	
	public Bitmap getBitmapFromMemCache(String key){
		if (key.equals("0")) { return null; }
		return imageCache.get(key);
	}
	
	public void addBitmapToMemoryCache(String key, Bitmap bitmap){
		if (key.equals("0")) { return; }
		if (getBitmapFromMemCache(key) == null){
			imageCache.put(key, bitmap);
		}
	}
	
	public void initCategories(){
		changeFirst.clear();
		for (int i = R.drawable.acfig001_1; i <= R.drawable.acfig036_1; i++){
			changeFirst.add(i);
		}//change this for increases in the number of animals
		for (int i = R.drawable.ascfig001_301; i <= R.drawable.ascfig035_401; i++){
			changeFirst.add(i);
		}
		Collections.shuffle(changeFirst);
		int gap1 = (R.drawable.bcfig001_2 - R.drawable.acfig001_1); //the gap works because everything is evenly matched
		changeSecond.clear();
		for (int i = 0; i < changeFirst.size(); i++){
			changeSecond.add((changeFirst.get(i) + gap1));
		}
		
		noChangeFirst.clear();
		for (int i = R.drawable.afig001_1; i <= R.drawable.afig036_1; i++){
			noChangeFirst.add(i);
		}
		for (int i = R.drawable.asfig001_301; i <= R.drawable.asfig035_401; i++){
			noChangeFirst.add(i);
		}

		Collections.shuffle(noChangeFirst);
		int gap2 = (R.drawable.bfig001_2 - R.drawable.afig001_1);
		noChangeSecond.clear();
		for (int i = 0; i < noChangeFirst.size(); i++){
			noChangeSecond.add((noChangeFirst.get(i) + gap2));
		}
		Log.w("initCats", "start");
	}
	
	public Integer chooseResId(int category, ArrayList<Integer> catList){
		Integer resId = 0;
		if (!catList.isEmpty()){
			resId = catList.remove(0);
		} else {
			Log.d("initCategories", "repeating");
			initCategories();
			return chooseResId(category, catList);
		}
		return resId;
	}
	
	public ArrayList<Integer> getChangeFirst(){
		return changeFirst;
	}
	
	public ArrayList<Integer> getChangeSecond(){
		return changeSecond;
	}
	
	public ArrayList<Integer> getNoChangeFirst(){
		return noChangeFirst;
	}
	
	public ArrayList<Integer> getNoChangeSecond(){
		return noChangeSecond;
	}
	
	private static GlobalVar instance; //for singleton
	static {
		instance = new GlobalVar();
	}
	
	private GlobalVar(){
		//should be no code here
	}
	
	public static GlobalVar getInstance(){//for singleton
		return GlobalVar.instance;
	}
	
	public void clearMemory(){
		imageCache.evictAll();
		System.gc();
	}
	
}
