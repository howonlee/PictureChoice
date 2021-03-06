package com.android.PictureChoice;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.Pair;

import com.android.PictureChoice.Posting.Block;
import com.android.PictureChoice.libs.LruCache;

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
	private ArrayList<ArrayList<Pair<Integer, Integer>>> picIds = new ArrayList<ArrayList<Pair<Integer, Integer>>>();
	ArrayList<Pair<Integer, Integer>> changeSixes = new ArrayList<Pair<Integer, Integer>>();
	ArrayList<Pair<Integer, Integer>> changeOthers = new ArrayList<Pair<Integer, Integer>>();
	ArrayList<Pair<Integer, Integer>> noChangeSixes = new ArrayList<Pair<Integer, Integer>>();
	ArrayList<Pair<Integer, Integer>> noChangeOthers = new ArrayList<Pair<Integer, Integer>>();
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
		int gap1 = (R.drawable.bcfig001_2 - R.drawable.acfig001_1);
		int gap2 = (R.drawable.bfig001_2 - R.drawable.afig001_1);
		setCats(changeSixes, R.drawable.acfig001_1, R.drawable.acfig036_1, gap1);
		setCats(changeOthers, R.drawable.ascfig001_301, R.drawable.ascfig035_401, gap1);
		setCats(noChangeSixes, R.drawable.afig001_1, R.drawable.afig036_1, gap2);
		setCats(noChangeOthers, R.drawable.asfig001_301, R.drawable.asfig035_401, gap2);
		picIds.add(changeSixes);
		picIds.add(changeOthers);
		picIds.add(noChangeSixes);
		picIds.add(noChangeOthers);
		Log.w("initCats", "start");
	}
	
	private void setCats(ArrayList<Pair<Integer, Integer>> cat, int first, int last, int gap){
		for (int i = first; i <= last; i++){
			Pair<Integer, Integer> picPair = new Pair<Integer, Integer>(i, i+gap);
			cat.add(picPair);
		}
		Collections.shuffle(cat);
	}
	
	public Integer chooseResId(int category, ArrayList<Integer> catList){
		Integer resId = 0;
		if (!catList.isEmpty()){ 
			resId = catList.remove(0);
		}
		return resId;
	}
	
	public ArrayList<ArrayList<Pair<Integer, Integer>>> getPicIds(){
		return picIds;
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
