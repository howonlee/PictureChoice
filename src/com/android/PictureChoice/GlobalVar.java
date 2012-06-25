package com.android.PictureChoice;

import android.app.Application;

/**
 * Singleton global variable holder
 * This holds a global variable.
 * I commit this sin to make counting the experiment blocks
 * more bearable
 * @author Howon
 *
 */
class GlobalVar extends Application {
	public int getBlockNum(){
		return blockNum;
	}
	
	public void setBlockNum(int blockNum){
		this.blockNum = blockNum;
	}
	
	private int blockNum = 0;
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
