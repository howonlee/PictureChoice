package com.android.PictureChoice;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.PictureChoice.Posting.PostSessionTask;
import com.android.PictureChoice.Posting.Session;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
	
	PostSessionTask postTask;
	Session session;
	Button toBlock;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        toBlock = (Button) findViewById(R.id.button_to_block);
        toBlock.setOnClickListener(new OnClickListener(){
        	public void onClick(View view){
        		startActivity(new Intent("com.android.BLOCKSHOW"));
        	}
        });
        session = new Session(Build.MODEL, Build.SERIAL);
        postTask = new PostSessionTask();
        postTask.execute(session);
        try {
        	GlobalVar.getInstance().setExpId(postTask.get());
        } catch (Exception e){ //for the postTask
        	e.printStackTrace();
        }
        GlobalVar.getInstance().initCache();
    }
    
	@Override
	public void onBackPressed(){
		//do nothing
	}
	
	@Override
	public boolean onSearchRequested(){
		return false;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		return true; //so no keys will work
	}
}