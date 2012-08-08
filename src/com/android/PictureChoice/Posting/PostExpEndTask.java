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

public class PostExpEndTask extends AsyncTask<ExpEnd, Integer, Integer> {

	private String urlString = "http://www.stanford.edu/group/pdplab/cgi-bin/expend.php";
	@Override
	protected Integer doInBackground(ExpEnd... params) {
		ExpEnd id = params[0];
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(urlString);
		try {
			List<NameValuePair> nameValPairs = new ArrayList<NameValuePair>(1);
			nameValPairs.add(new BasicNameValuePair("exp_id", id.getExpId()));
			nameValPairs.add(new BasicNameValuePair("exp_code", id.getExpCode()));
			httppost.setEntity(new UrlEncodedFormEntity(nameValPairs));
			httpclient.execute(httppost);
			//Log.d("an http try", EntityUtils.toString(response.getEntity()));
				//this log consumes the content, hilariously
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
