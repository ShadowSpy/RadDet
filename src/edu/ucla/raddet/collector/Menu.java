package edu.ucla.raddet.collector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Menu extends Activity {
	
	public static boolean started = false;
	
	//RadDet Features
	LinearLayout node;
    TextView tv;
    Button button;	//Turns the service on or off
    Intent dataCollectorIntent;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tv = new TextView(this);
        button = new Button(this);
        node = new LinearLayout(this);
        node.setOrientation(LinearLayout.VERTICAL);
        
        if (started) {
			tv.setText("Service started");
	        button.setText("Stop");
	    	button.setOnClickListener(stopListener);
        }
        else {
			tv.setText("Service stopped");
	        button.setText("Start");
	    	button.setOnClickListener(startListener);
        }
        
        node.addView(tv);
        node.addView(button);
        
        dataCollectorIntent = new Intent(this, DataCollector.class);

	    setContentView(node);
	    
    }
    
    private View.OnClickListener startListener = new View.OnClickListener() {
        public void onClick(View v) {
        	tv.setText("Please wait...");
        	startService(dataCollectorIntent);
        	button.setText("Stop");
        	tv.setText("Service started");
        	button.setOnClickListener(stopListener);
        }
    };
    
    private View.OnClickListener stopListener = new View.OnClickListener() {
        public void onClick(View v) {
        	tv.setText("Please wait...");
        	stopService(dataCollectorIntent);
        	button.setText("Start");
        	tv.setText("Service stopped");
        	button.setOnClickListener(startListener);
        }
    };    
}