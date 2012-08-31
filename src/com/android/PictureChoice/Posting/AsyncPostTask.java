package com.android.PictureChoice.Posting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.os.Environment;

public class AsyncPostTask extends AsyncTask<PostableData, Integer, Long> {

	@Override
	protected Long doInBackground(PostableData... params) {
		PostableData data = params[0];

		//store in android fs
		File sdCard = Environment.getExternalStorageDirectory();
		File dir = new File(sdCard.getAbsolutePath() + "/expdata");
		dir.mkdirs();
		String str = data.getNameValPairs().toString();
		String filename = data.getFileName().replaceAll("[^A-Za-z0-9 \t\n._]", ""); //regex away anything not a valid letter, num or underscore
		try {
			File file = new File(dir, filename);
			FileOutputStream fOut = new FileOutputStream(file, true);
			OutputStreamWriter osw = new OutputStreamWriter(fOut);
			osw.write(str);
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
