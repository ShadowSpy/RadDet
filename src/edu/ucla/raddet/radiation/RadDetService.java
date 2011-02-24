package edu.ucla.raddet.radiation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

import edu.ucla.raddet.Menu;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.IBinder;
import android.telephony.SignalStrength;
import android.util.Log;
import android.widget.Toast;

public class RadDetService extends Service 
{
	public static Menu MAIN_ACTIVITY;
	
	private Timer timer=new Timer(); 
	 
    private static long UPDATE_INTERVAL = 1*60*1000;  //default

    private static long DELAY_INTERVAL = 0;  
    
    // hooks main activity here    
    public static void setMainActivity(Menu activity) 
    {
      MAIN_ACTIVITY = activity;      
    }
    
    /* 
     * not using ipc...but if we use in future
     */
    public IBinder onBind(Intent intent) {
      return null;
    }
    
    @Override 
    public void onCreate() 
    {
      super.onCreate();     

      _startService();

      if (MAIN_ACTIVITY != null)  Log.d(getClass().getSimpleName(), "RadDetService started");
    }
    
    @Override 
    public void onDestroy() 
    {
      super.onDestroy();

      _shutdownService();

      if (MAIN_ACTIVITY != null)  Log.d(getClass().getSimpleName(), "FileScannerService stopped");
    }
    
    /*
     * starting the service
     */
    private void _startService()
    {      
      timer.scheduleAtFixedRate(    
          
              new TimerTask() {

                    public void run() {

                        try{

                        doServiceWork();

                        Thread.sleep(UPDATE_INTERVAL);

                        }catch(InterruptedException ie){

                            Log.e(getClass().getSimpleName(), "RadDetService InterruptedException"+ie.toString());
                        }
                        
                    }
                  },
                  DELAY_INTERVAL,
                  UPDATE_INTERVAL);

      Log.i(getClass().getSimpleName(), "RadDetService Timer started....");
    }
    
    
    //start the processing, the actual work, getting config params, get data from network etc
    private void doServiceWork()
    {
        //do something whatever you want
        //TODO: we implement our stuff here    
    	SignalStrength sigstrength;
    	
    }
    
    
    //shutting down the service
    
    private void _shutdownService()
    {
      if (timer != null) timer.cancel();
      Log.i(getClass().getSimpleName(), "Timer stopped...");
    }
}
