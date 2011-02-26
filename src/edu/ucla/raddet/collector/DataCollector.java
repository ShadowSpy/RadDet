package edu.ucla.raddet.collector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;

import android.R.string;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.PhoneStateListener;
import android.util.Log;


//Android Service which collects radiation information, as well as GPS information
public class DataCollector extends Service{

	public static final int UPDATE_INTERVAL = 60000;	//Frequency of GPS location updates
	
	private LocationManager locManager;
	private FileOutputStream fOut;
	private OutputStreamWriter osw;
	private Location loc;
	private TelephonyManager Tel;
	private SignalStrength sigstrength;
	private static final String TAG = "DataCollector";

	
	private LocationListener locListener = new LocationListener() {
		public void onLocationChanged(Location loc) {
			
			try {
				//Get Location
				String s = "Coordinates: " + loc.getLatitude() + ", " + loc.getLongitude();
				s += " at timestamp " + loc.getTime();
				
				//Get Signal Strength
				/*int strength = sigstrength.getGsmSignalStrength();
				String sstrength = "SIGNAL STRENGTH!!!:" + String.valueOf(strength);
				Log.i(TAG, sstrength);*/
										
				osw.write(s);
				Log.d(TAG, s);
			} catch (IOException e) {				
				Log.e(TAG, "Could not open output file");
				e.printStackTrace();
			}
		}
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
		}
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
		}
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}
	};
	
	public void onCreate() {	
		
		//GPS Signal Strength
        //Tel = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        //Tel.listen(MyListener ,PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
		
		//Location
		locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, UPDATE_INTERVAL, 10, locListener);

		try {
			fOut = openFileOutput("samplefile.txt", MODE_WORLD_READABLE);
		} catch (FileNotFoundException e) {
			Log.e(TAG, "Could not open file output");
			e.printStackTrace();
		}
		osw = new OutputStreamWriter(fOut);
		Log.i(TAG, "DataCollector started");
	}
	
	public void onDestroy() {
		locManager.removeUpdates(locListener);
		try {
			osw.flush();
			osw.close();
		} catch (IOException e) {
			Log.e(TAG, "Could not close file writer");
			e.printStackTrace();
		}
		Log.i(TAG, "DataCollector stopped");
	}

	//TODO: Austin--Write function and figure out what the return value should actually be
	public int getRadiation(SignalStrength sig) {
			int strength = sig.getGsmSignalStrength();
			return strength;
	}
	
	public String getSig(SignalStrength sig){
		String s = sig.getClass().getName() + '@' + Integer.toHexString(hashCode());
		return s;
	}
	
	public IBinder onBind(Intent intent) {
		return null;
	}
}