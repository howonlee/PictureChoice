package com.android.PictureChoice;

/**
 * Gives the choice of a trial made by somebody
 * @author Howon
 */
public class TrialChoice {
	private final int picId;
	private final long beginTime;
	private final long endTime;
	private final int blockId;
	private final int choiceMade;
	
	public TrialChoice(int picId, long beginTime, long endTime, int blockId, int choiceMade){
		this.picId = picId;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.blockId = blockId;
		this.choiceMade = choiceMade;
	}
	
	public int getPicId(){
		return picId;
	}
	public long getBeginTime(){
		return beginTime;
	}
	public long getEndTime(){
		return endTime;
	}
	public int getBlockId(){
		return blockId;
	}
	public int getChoiceMade(){
		return choiceMade;
	}

}
