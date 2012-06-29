package com.android.PictureChoice;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class BitmapWorkerTask extends AsyncTask<Integer, Integer, Bitmap> {
	private final WeakReference<ImageView> imageViewReference;
	private Context c;
	private int data = 0;
	
	public BitmapWorkerTask(ImageView imageView, Context context){
		imageViewReference = new WeakReference<ImageView>(imageView);
		c = context;
	}
	
	@Override
	protected Bitmap doInBackground(Integer... params) {
		data = params[0];
		return BitmapFactory.decodeResource(c.getResources(), data);
	}
	
	protected void onPostExecute(Bitmap bitmap){
		if (imageViewReference != null && bitmap != null){
			final ImageView imageView = imageViewReference.get();
			//no need to add the bitmap to cache because of
			//the way we're using this thing
			//that is, we won't get any cache hits for anything we add here
			if (imageView != null){
				imageView.setImageBitmap(bitmap);
			}
		}
	}
}
