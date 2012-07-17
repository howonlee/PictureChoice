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

import com.android.PictureChoice.Posting.MTurkId;
import com.android.PictureChoice.Posting.PostMTurkIdTask;
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
                doMTurkTask();
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
    
    private void doMTurkTask(){
    	MTurkId turk = new MTurkId(mTurkText.getText().toString(), Integer.toString(GlobalVar.getInstance().getExpId()));
    	PostMTurkIdTask mturkId = new PostMTurkIdTask();
    	mturkId.execute(turk);
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
	
	@Override
	public void onPause(){
		super.onPause();
		if (!(GlobalVar.getInstance().getAppFlag())){
			GlobalVar.getInstance().setInterrupted();
		}
	}
}