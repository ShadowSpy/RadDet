package edu.ucla.raddet.collector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Menu extends Activity {
    TextView tv;
    Intent dataCollectorIntent;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv = new TextView(this);
        dataCollectorIntent = new Intent(this, DataCollector.class);
        startService(dataCollectorIntent);
        
		tv.setText("Service started!");
	    setContentView(tv);
    }
}