package com.android.PictureChoice;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.PictureChoice.Posting.PostSessionTask;
import com.android.PictureChoice.Posting.Session;
import com.android.PictureChoice.Posting.VersionTask;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
	final int SANDSTONE = 0xffeee6cb;
	final int APP_VERSION = 1;
	EditText mTurkText;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button toBlock = (Button) findViewById(R.id.button_to_block);
        mTurkText = (EditText) findViewById(R.id.mTurkText);
        //setting background color...
        toBlock.getBackground().setColorFilter(SANDSTONE, PorterDuff.Mode.MULTIPLY);
        toBlock.setOnClickListener(new OnClickListener(){
        	public void onClick(View view){        		   
                //how to include?
        		setName();
        		GlobalVar.getInstance().setAppFlag(true);
        		startActivity(new Intent("com.android.BLOCKSHOW"));
        	}
        });
        
        doSessionTask();
        doVersionTask();     
        GlobalVar.getInstance().initCache();
		GlobalVar.getInstance().initCategories();
        GlobalVar.getInstance().setAppFlag(false);
    }
    
    private void setName(){
    	String name = mTurkText.getText().toString();
    	GlobalVar.getInstance().setName(name);
    }
    
    private void doSessionTask(){
    	Session session = new Session(Build.MODEL, Build.SERIAL);
        PostSessionTask postTask = new PostSessionTask();
        postTask.execute(session);
        try {
        	GlobalVar.getInstance().setExpId(postTask.get());
        } catch (Exception e){ //for the postTask
        	e.printStackTrace(); 
        	//should probably explode and cry like a baby here
        }
    }
    
    private void doVersionTask(){
        VersionTask versionTask = new VersionTask();
        versionTask.execute(APP_VERSION);
        try {
        	if (!versionTask.get()){
        		//wrong app version
    			Toast toast = Toast.makeText(getApplicationContext(), "Wrong app version", Toast.LENGTH_LONG);
    			toast.show();
    			moveTaskToBack(true);
        	}
        } catch (Exception e){
        	e.printStackTrace();
        	//again, cry and let loose the dogs of war
        }
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
	
	//don't need this functionality right now
	/*
	@Override
	public void onPause(){
		super.onPause();
		if (!(GlobalVar.getInstance().getAppFlag())){
			GlobalVar.getInstance().setInterrupted();
		}
	}*/
}