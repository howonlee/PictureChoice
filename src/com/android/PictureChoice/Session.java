package com.android.PictureChoice;

/**
 * For encapsulating the data of a session to send to the server.
 * @author Howon
 *
 */

public class Session {
	private final long beginTime;
	private final long endTime;
	private final String machineId;
	
	public Session(long beginTime, long endTime, String machine){
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.machineId = machine;
	}

	public long getBeginTime() {
		return beginTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public String getMachineId() {
		return machineId;
	}
	
}
