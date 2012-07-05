package com.android.PictureChoice.Posting;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

/**
 * For checking version number and Internet connection.
 * @author Howon Lee
 *
 */
public class VersionTask extends AsyncTask<Integer, Integer, Boolean> {

	private String urlString = "http://www.stanford.edu/group/pdplab/cgi-bin/mobileversion.php";
	@Override
	protected Boolean doInBackground(Integer... params) {
		Integer appVersion = params[0];
		Integer serverVersion = -1;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(urlString); //don't really need to be a httpPost, but everything else is, anyways
		try {
			List<NameValuePair> nameValPairs = new ArrayList<NameValuePair>(1);
			httppost.setEntity(new UrlEncodedFormEntity(nameValPairs));
			HttpResponse response = httpclient.execute(httppost);
			//Log.d("an http try", EntityUtils.toString(response.getEntity()));
				//this log consumes the content, hilariously
			serverVersion = Integer.valueOf(EntityUtils.toString(response.getEntity()));
		} catch (Exception e){
			e.printStackTrace();
		}
		return (appVersion == serverVersion);
	}
}

