package com.android.PictureChoice;

/**
 * Is the choice of a trial.
 * (for encapsulating data to be sent to the server)
 * @author Howon
 */
public class TrialChoice {
	private final int picId;
	private final long beginTime;
	private final long endTime;
	private final int blockId;
	private final int choiceMade;
	private final long maskBeginTime;
	private final long maskEndTime;
	
	public TrialChoice(int picId, long beginTime, long endTime, int blockId, int choiceMade,
			long maskBeginTime, long maskEndTime){
		this.picId = picId;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.blockId = blockId;
		this.choiceMade = choiceMade;
		this.maskBeginTime = maskBeginTime;
		this.maskEndTime = maskEndTime;
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
	public long getMaskBeginTime(){
		return maskBeginTime;
	}
	public long getMaskEndTime(){
		return maskEndTime;
	}

}