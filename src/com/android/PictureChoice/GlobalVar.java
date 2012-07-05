package com.android.PictureChoice;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.PictureChoice.Posting.Block;

/**
 * Singleton global variable holder
 * This holds global variables.
 * that is, it holds:
 * 1. block information
 * 2. a cache for images
 * I commit this sin to make counting this crud more bearable
 * @author Howon
 *
 */
class GlobalVar extends Application {
	private int exp_id = -1;
	private int blockNum = 0;
	private LruCache<String, Bitmap> imageCache;

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
			break_time_end, false, exp_id, blockNum);
	}
	
	public void initCache(){
		final int memClass = 16; //call it this for now
		final int cacheSize = 1024 * 1024 * memClass / 8;
		imageCache = new LruCache<String, Bitmap>(cacheSize){
			@Override
			protected int sizeOf(String key, Bitmap bitmap){
				return bitmap.getByteCount();
			}
		};
	}
	
	public void addBitmapToMemoryCache(String key, Bitmap bitmap){
		if (getBitmapFromMemCache(key) == null){
			imageCache.put(key, bitmap);
		}
	}
	
	public Bitmap getBitmapFromMemCache(String key){
		return imageCache.get(key);
	}
	
	
	private static GlobalVar instance;
	static {
		instance = new GlobalVar();
	}
	
	private GlobalVar(){
		//nada
	}
	
	public static GlobalVar getInstance(){
		return GlobalVar.instance;
	}
}
