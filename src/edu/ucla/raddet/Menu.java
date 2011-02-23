package edu.ucla.raddet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import edu.ucla.raddet.radiation.RadDetService;

public class Menu extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);                      
        
        //just pass the reference to the service
        RadDetService.setMainActivity(this);
    	
        //creating an intent for the service
        final Intent RadDetService = new Intent(this, RadDetService.class);
        
        //start button
        Button btnStart = (Button) findViewById(android.R.id.button1);
        //start button on click
        btnStart.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {     
                //start the service
                startService(RadDetService);
                
                //remove or hide the app
                finish();                
            }
        });
        
        //stop button
        Button btnStop = (Button) findViewById(android.R.id.closeButton);

        //on click of stop button
        btnStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {             
                //stop the service
                stopService(RadDetService);  

                //remove or hide the app
                finish(); 
            }
        });
        
        /*@Override 
        protected void onDestroy() {
              super.onDestroy();
        }*/
        
    }
	

/*	//LocationService class// 
	public class LocalService extends Service {
	
		private NotificationManager mNM;
		
		public class LocalBinder extends Binder {
			LocalService getService(){
				return LocalService.this;
			}
		}
		
		@Override
		public void onCreate(){
			mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
			showNotification();
		}
		
	    @Override
	    public int onStartCommand(Intent intent, int flags, int startId) {
	        Log.i("LocalService", "Received start id " + startId + ": " + intent);
	        // We want this service to continue running until it is explicitly
	        // stopped, so return sticky.
	        return START_STICKY;
	    }
	    
	    //TODO: Check cancel(1) and the 0 in toast
	    @Override
	    public void onDestroy() {
	        // Cancel the persistent notification.
	        mNM.cancel(1);
	        
	        // Tell the user we stopped.
	        Toast.makeText(this, 0, Toast.LENGTH_SHORT).show();
	    }
		
	    @Override
	    public IBinder onBind(Intent intent) {
	        return mBinder;
	    }
	    
	    private final IBinder mBinder = new LocalBinder();
	    
		private void showNotification(){
			System.out.println("Notification here:");
		}
	}
	//End of LocationService Class
	
	
	//Start Service
	private LocalService mBoundService;
	
	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service){
		
	        // This is called when the connection with the service has been
	        // established, giving us the service object we can use to
	        // interact with the service.  Because we have bound to a explicit
	        // service that we know is running in our own process, we can
	        // cast its IBinder to a concrete class and directly access it.
			mBoundService = ((LocalService.LocalBinder)service).getService();
			
			//Text to user
			Toast.makeText(Binding.this, R.string.local_service_connected,
					Toast.LENGTH_SHORT).show();
		}
	};*/
	
	

/*    @Override
    public void onCreate(Bundle savedInstanceState) {
    	String str;
 
    	str = "Hello, Android";

        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText(str);
        setContentView(tv);
    }*/

}











/*private IDataCollector dataCollector = null;

*//** Saves the connection to the data collector service *//*
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

	//@Override
	public void onServiceDisconnected(ComponentName className) {
		dataCollector = null;
	}
};

*//** Called when the activity is first created. *//*
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
}*/