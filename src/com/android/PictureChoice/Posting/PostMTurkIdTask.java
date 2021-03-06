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

public class PostMTurkIdTask extends AsyncTask<MTurkId, Integer, Integer> {

	private String urlString = "http://www.stanford.edu/group/pdplab/cgi-bin/mturkid.php";
	@Override
	protected Integer doInBackground(MTurkId... params) {
		MTurkId id = params[0];
		Integer toReturn = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(urlString);
		try {
			List<NameValuePair> nameValPairs = new ArrayList<NameValuePair>(1);
			nameValPairs.add(new BasicNameValuePair("mturk_id", id.getMTurkId()));
			nameValPairs.add(new BasicNameValuePair("exp_id", id.getExpId()));
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
