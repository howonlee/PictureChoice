package com.android.PictureChoice.Posting;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class PostableData {
		private final String urlString;
		private final String filename;
		private List<NameValuePair> nameValPairs;
		
		public PostableData(String urlString, String filename){
			this.urlString = urlString;
			this.filename = filename;
			nameValPairs = new ArrayList<NameValuePair>(1);
		}
		
		public String getFileName(){
			return filename;
		}
		
		public String getUrlString(){
			return urlString;
		}
		
		public List<NameValuePair> getNameValPairs(){
			return nameValPairs;
		}

		public void add(String key, String data){
			nameValPairs.add(new BasicNameValuePair(key, data));
		}
		
		public void add(String key, Long data){
			nameValPairs.add(new BasicNameValuePair(key, Long.toString(data)));	
		}
		
		public void add(String key, Integer data){
			nameValPairs.add(new BasicNameValuePair(key, Integer.toString(data)));	
		}
		
		public void add(String key, Boolean data){
			nameValPairs.add(new BasicNameValuePair(key, Boolean.toString(data)));	
		}

}
