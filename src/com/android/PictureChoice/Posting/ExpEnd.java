package com.android.PictureChoice.Posting;

/**
 * For encapsulating the data of a session to send to the server.
 * @author Howon
 *
 */

public class ExpEnd {
	private final String exp_id;
	private final String exp_code;
	
	public ExpEnd(String exp_id, String exp_code){
		this.exp_id = exp_id;
		this.exp_code = exp_code;
	}
	
	public String getExpId(){
		return exp_id;
	}
	
	public String getExpCode(){
		return exp_code;
	}
	
}
