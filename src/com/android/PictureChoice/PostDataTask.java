package com.android.PictureChoice;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;

public class PostDataTask extends AsyncTask<URL, Integer, Long> {

	@Override
	protected Long doInBackground(URL... params) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://localhost:8888/show");
		try {
			List<NameValuePair> nameValPairs = new ArrayList<NameValuePair>(1);
			nameValPairs.add(new BasicNameValuePair("time", Long.toString(System.currentTimeMillis())));
			httppost.setEntity(new UrlEncodedFormEntity(nameValPairs));
			httpclient.execute(httppost);
		} catch (ClientProtocolException e){
		} catch (IOException e){
		}
		return null;
	}

}
