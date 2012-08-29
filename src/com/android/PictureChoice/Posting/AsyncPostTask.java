package com.android.PictureChoice.Posting;

import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

public class AsyncPostTask extends AsyncTask<PostableData, Integer, Long> {
	private String urlString = "http://www.stanford.edu/group/pdplab/cgi-bin/mobileblockscript.php";
	@Override
	protected Long doInBackground(PostableData... params) {
		PostableData data = params[0];
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(data.getUrlString());
		try {
			httppost.setEntity(new UrlEncodedFormEntity(data.getNameValPairs()));
			httpclient.execute(httppost);
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
