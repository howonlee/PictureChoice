package com.android.PictureChoice.Posting;

/**
 * For encapsulating the data of a block to send to the server.
 * @author Howon
 *
 */

public class Block {
	private final long beginTime;
	private final long endTime;
	private final long breakBeginTime;
	private final long breakEndTime;
	private final boolean interrupted;
	private final int expId;
	private final int blockNum;//redundant, but sent anyways
	
	public Block(long beginTime, long endTime, long breakBeginTime,
			long breakEndTime, boolean interrupted, int expId, int blockNum){
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.breakBeginTime = breakBeginTime;
		this.breakEndTime = breakEndTime;
		this.interrupted = interrupted;
		this.expId = expId;
		this.blockNum = blockNum;
	}

	public long getBeginTime() {
		return beginTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public long getBreakBeginTime() {
		return breakBeginTime;
	}

	public long getBreakEndTime() {
		return breakEndTime;
	}

	public boolean getInterrupted() {
		return interrupted;
	}

	public int getExpId() {
		return expId;
	}
	
	public int getBlockNum(){
		return blockNum;
	}
	
}
