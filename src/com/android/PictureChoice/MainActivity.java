package com.android.PictureChoice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
	
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
    }
}