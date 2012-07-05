package com.android.PictureChoice.Posting;

/**
 * For encapsulating the data of a session to send to the server.
 * @author Howon
 *
 */

public class Session {
	private final String machineId;
	private final String serial;
	
	public Session(String machine, String serial){
		this.machineId = machine;
		this.serial = serial;
	}

	public String getMachineId() {
		return machineId;
	}
	
	public String getSerial(){
		return serial;
	}
	
}
