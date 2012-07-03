package com.android.PictureChoice;

import java.net.URL;
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

public class PostTrialTask extends AsyncTask<URL, Integer, Long> {

	@Override
	protected Long doInBackground(URL... params) {
		java.sql.Time time = new java.sql.Time(500000);
		String name = "bob";
		Integer random = 42;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(params[0].toString());
		try {
			List<NameValuePair> nameValPairs = new ArrayList<NameValuePair>(1);
			nameValPairs.add(new BasicNameValuePair("time", time.toString()));
			nameValPairs.add(new BasicNameValuePair("name", name));
			nameValPairs.add(new BasicNameValuePair("random", random.toString()));
			httppost.setEntity(new UrlEncodedFormEntity(nameValPairs));
			HttpResponse response = httpclient.execute(httppost);
			Log.d("an http try", EntityUtils.toString(response.getEntity()));
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
