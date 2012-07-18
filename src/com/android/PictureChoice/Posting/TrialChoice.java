package com.android.PictureChoice.Posting;

/**
 * Is the choice of a trial.
 * (for encapsulating data to be sent to the server)
 * @author Howon
 */
public class TrialChoice {
	private final int picId;
	private final long beginTime;
	private final long endTime;
	private final long beginTime2;
	private final long endTime2;
	private final int blockNum;
	private final int choiceMade;
	private final long maskBeginTime;
	private final long maskEndTime;
	private final long picClickTime;
	private final long clickTime;
	private final int picLength;
	private final int expId;
	
	public TrialChoice(int picId, long beginTime, long endTime,
			long beginTime2, long endTime2,
			int blockNumber, int choiceMade,
			long maskBeginTime, long maskEndTime,
			long picClickTime, long clickTime, 
			int picLength, int expId){
		this.picId = picId;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.beginTime2 = beginTime2;
		this.endTime2 = endTime2;
		this.blockNum = blockNumber;
		this.choiceMade = choiceMade;
		this.maskBeginTime = maskBeginTime;
		this.maskEndTime = maskEndTime;
		this.picClickTime = picClickTime;
		this.clickTime = clickTime;
		this.picLength = picLength;
		this.expId = expId;
	}
	
	public int getPicLength(){
		return picLength;
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
	public long getBeginTime2(){
		return beginTime2;
	}
	public long getEndTime2(){
		return endTime2;
	}
	public int getBlockNum(){
		return blockNum;
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
	public long getPicClickTime(){
		return picClickTime;
	}
	public long getClickTime(){
		return clickTime;
	}
	public int getExpId(){
		return expId;
	}

}
