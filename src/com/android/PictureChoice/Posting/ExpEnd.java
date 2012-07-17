package com.android.PictureChoice.Posting;

/**
 * For encapsulating the data of a session to send to the server.
 * @author Howon
 *
 */

public class ExpEnd {
	private final String exp_id;
	
	public ExpEnd(String exp_id){
		this.exp_id = exp_id;
	}
	
	public String getExpId(){
		return exp_id;
	}
	
}
