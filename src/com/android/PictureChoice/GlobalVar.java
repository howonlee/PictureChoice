package com.android.PictureChoice;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Singleton global variable holder
 * This holds global variables.
 * that is, it holds:
 * 1. block number
 * 2. a cache for images
 * I commit this sin to make counting this crud more bearable
 * @author Howon
 *
 */
class GlobalVar extends Application {
	private boolean interrupted = false;
	private int exp_id = -1;
	private int blockNum = 0;
	private LruCache<String, Bitmap> imageCache;
	
	public void wasInterrupted(){
		interrupted = true;
	}
	
	public void resetInterruption(){
		interrupted = false;
	}
	
	public boolean getInterrupted(){
		return interrupted;
	}
	
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
