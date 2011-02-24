package edu.ucla.raddet.collector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SignalStrength;
import android.util.Log;


//Android Service which collects radiation information, as well as GPS information
public class DataCollector extends Service{

	public static final int UPDATE_INTERVAL = 60000;	//Frequency of GPS location updates
	
	private LocationManager locManager;
	private File file;
	private SignalStrength sigstrength;
	private static final String TAG = "DataCollector";

	private LocationListener locListener = new LocationListener() {
		public void onLocationChanged(Location loc) {
			try {
				PrintStream out = new PrintStream(new FileOutputStream(file));
				
				String s = "Coordinates:" + loc.getLatitude() + ", " + loc.getLongitude();
				s += "at timestamp" + loc.getTime();
				out.println(s);
			} catch (FileNotFoundException e) {
				System.out.println("Could not open output file");
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
		locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
		file = new File(getExternalFilesDir("raddet"), "output.txt");
		Log.i(TAG, "Service started");
	}
	
	public void onDestroy() {
		locManager.removeUpdates(locListener);
		Log.i(TAG, "Service stopped");
	}

	//TODO: Austin--Write function and figure out what the return value should actually be
	public int getRadiation(SignalStrength sig) {
		try{
			PrintStream out = new PrintStream(new FileOutputStream(file));
			
			int strength = sig.getGsmSignalStrength();
			String s = "Signal Strength:" + strength + " dB";
			out.println(s);
			return strength;
		}
		catch (FileNotFoundException e){
			System.out.println("Could not open output file");
			e.printStackTrace();
		}
		return 0;
	}
	
	public IBinder onBind(Intent intent) {
		return null;
	}
}