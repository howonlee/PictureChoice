package com.android.PictureChoice.Posting;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;

public class PostTrialTask extends AsyncTask<TrialChoice, Integer, Long> {

	private String urlString = "http://www.stanford.edu/group/pdplab/cgi-bin/mobiletrialscript.php";
	@Override
	protected Long doInBackground(TrialChoice... params) {
		TrialChoice choice = params[0];
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(urlString);
		try {
			List<NameValuePair> nameValPairs = new ArrayList<NameValuePair>(1);
			nameValPairs.add(new BasicNameValuePair("pic_id", choice.getPicId()));
			nameValPairs.add(new BasicNameValuePair("time_begin", Long.toString(choice.getBeginTime())));
			nameValPairs.add(new BasicNameValuePair("time_end", Long.toString(choice.getEndTime())));
			nameValPairs.add(new BasicNameValuePair("time2_begin", Long.toString(choice.getBeginTime2())));
			nameValPairs.add(new BasicNameValuePair("time2_end", Long.toString(choice.getEndTime2())));
			nameValPairs.add(new BasicNameValuePair("block_num", Integer.toString(choice.getBlockNum())));
			nameValPairs.add(new BasicNameValuePair("choice_made", Integer.toString(choice.getChoiceMade())));
			nameValPairs.add(new BasicNameValuePair("mask_begin", Long.toString(choice.getMaskBeginTime())));
			nameValPairs.add(new BasicNameValuePair("mask_end", Long.toString(choice.getMaskEndTime())));
			nameValPairs.add(new BasicNameValuePair("time_pic_click", Long.toString(choice.getPicClickTime())));
			nameValPairs.add(new BasicNameValuePair("time_click", Long.toString(choice.getClickTime())));
			nameValPairs.add(new BasicNameValuePair("pic_length", Integer.toString(choice.getPicLength())));
			nameValPairs.add(new BasicNameValuePair("exp_id", Integer.toString(choice.getExpId())));
			httppost.setEntity(new UrlEncodedFormEntity(nameValPairs));
			httpclient.execute(httppost);
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
