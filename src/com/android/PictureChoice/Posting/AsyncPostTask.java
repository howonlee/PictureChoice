package com.android.PictureChoice.Posting;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

public class AsyncPostTask extends AsyncTask<PostableData, Integer, Long> {

	@Override
	protected Long doInBackground(PostableData... params) {
		PostableData data = params[0];

		//store in android fs
		String str = data.getNameValPairs().toString();
		String filename = "/pdpdata/".concat(data.getFileName()).replaceAll("[^A-Za-z0-9 \t\n.]", ""); //regex away anything not a valid letter, num or underscore
		try {
			FileOutputStream fOut = new FileOutputStream(filename, true);
			OutputStreamWriter osw = new OutputStreamWriter(fOut);
			osw.write(str);
			osw.flush();
			osw.close();
		} catch (IOException ioe){
			ioe.printStackTrace();
		}
		
		//post to web
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
