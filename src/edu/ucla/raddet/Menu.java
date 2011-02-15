package edu.ucla.raddet;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.TextView;
import edu.ucla.raddet.radiation.IDataCollector;

public class Menu extends Activity {
	
	private IDataCollector dataCollector = null;
	
	/** Saves the connection to the data collector service */
	private ServiceConnection conn = new ServiceConnection() {

		//@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			dataCollector = IDataCollector.Stub.asInterface(service);
			try {
				dataCollector.startDataCollection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName className) {
			dataCollector = null;
		}
	};
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	String str;
    	if (!bindService(new Intent(IDataCollector.class.getName()), conn, Context.BIND_AUTO_CREATE)) {
    		System.out.println("Could not bind to data collector service");
    		str = "hello, fail";
    	}
    	else {
    		str = "Hello, Android";
    	}
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText(str);
        setContentView(tv);
    }
}