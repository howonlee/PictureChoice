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

public class PostSessionTask extends AsyncTask<Session, Integer, Integer> {

	private String urlString = "http://www.stanford.edu/group/pdplab/cgi-bin/mobileexpscript.php";
	@Override
	protected Integer doInBackground(Session... params) {
		Session session = params[0];
		Integer toReturn = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(urlString);
		try {
			List<NameValuePair> nameValPairs = new ArrayList<NameValuePair>(1);
			nameValPairs.add(new BasicNameValuePair("android_machine", session.getMachineId()));
			nameValPairs.add(new BasicNameValuePair("serial", session.getSerial()));
			httppost.setEntity(new UrlEncodedFormEntity(nameValPairs));
			HttpResponse response = httpclient.execute(httppost);
			//Log.d("an http try", EntityUtils.toString(response.getEntity()));
				//this log consumes the content, hilariously
			toReturn = Integer.valueOf(EntityUtils.toString(response.getEntity()));
		} catch (Exception e){
			e.printStackTrace();
		}
		return toReturn;
	}
}
