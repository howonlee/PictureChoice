package com.android.PictureChoice.Posting;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


import android.os.AsyncTask;
import android.util.Log;

public class PostSessionTask extends AsyncTask<Session, Integer, Long> {

	private String urlString = "http://www.stanford.edu/group/pdplab/cgi-bin/mobileexpscript.php";
	@Override
	protected Long doInBackground(Session... params) {
		Session session = params[0];
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(urlString);
		try {
			List<NameValuePair> nameValPairs = new ArrayList<NameValuePair>(1);
			nameValPairs.add(new BasicNameValuePair("time_begin", Long.toString(block.getBeginTime())));
			nameValPairs.add(new BasicNameValuePair("time_end", Long.toString(block.getEndTime())));
			nameValPairs.add(new BasicNameValuePair("break_time_begin", Long.toString(block.getBreakBeginTime())));
			nameValPairs.add(new BasicNameValuePair("break_time_end", Long.toString(block.getBreakEndTime())));
			nameValPairs.add(new BasicNameValuePair("interruption", Boolean.toString(block.getInterrupted())));
			nameValPairs.add(new BasicNameValuePair("exp_id", Integer.toString(block.getExpId())));
			httppost.setEntity(new UrlEncodedFormEntity(nameValPairs));
			HttpResponse response = httpclient.execute(httppost);
			Log.d("an http try", EntityUtils.toString(response.getEntity()));
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
