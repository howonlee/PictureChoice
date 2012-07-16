package com.android.PictureChoice.Posting;

/**
 * For encapsulating the data of a session to send to the server.
 * @author Howon
 *
 */

public class MTurkId {
	private final String mturk_id;
	private final String exp_id;
	
	public MTurkId(String mturk_id, String exp_id){
		this.mturk_id = mturk_id;
		this.exp_id = exp_id;
	}

	public String getMTurkId() {
		return mturk_id;
	}
	
	public String getExpId(){
		return exp_id;
	}
	
}
