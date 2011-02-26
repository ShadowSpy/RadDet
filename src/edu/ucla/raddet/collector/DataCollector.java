package edu.ucla.raddet.collector;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SignalStrength;
import android.util.Log;


//Android Service which collects radiation information, as well as GPS information
public class DataCollector extends Service{

	public static final int UPDATE_INTERVAL = 60000;	//Frequency of GPS location updates
	
	private LocationManager locManager;
	private FileOutputStream fOut;
	private OutputStreamWriter osw;
	private SignalStrength sigstrength;
	
	private int gpsStatus;
	private int networkStatus;
	private Location bestLocation;
	
	private static final String TAG = "DataCollector";

	private LocationListener locListener = new LocationListener() {
		public void onLocationChanged(Location loc) {
			//Keep track of best location
			//Having a location > no location
			if (bestLocation == null)
				bestLocation = loc;
			//GPS Location > Network Location
			else if (bestLocation.getProvider().equals(LocationManager.NETWORK_PROVIDER) &&
					 loc.getProvider().equals(LocationManager.GPS_PROVIDER))
				bestLocation = loc;
			//More accuracy > Less accuracy
			else if (bestLocation.getProvider().equals(loc.getProvider())) {
				if (loc.getAccuracy() >= bestLocation.getAccuracy())
					bestLocation = loc;
			}
		}
		public void onProviderDisabled(String provider) {
		}
		public void onProviderEnabled(String provider) {
		}
		public void onStatusChanged(String provider, int status, Bundle extras) {
			if (provider.equals(LocationManager.GPS_PROVIDER))
				gpsStatus = status;
			else if (provider.equals(LocationManager.NETWORK_PROVIDER))
				networkStatus = status;
			
			if (gpsStatus == LocationProvider.TEMPORARILY_UNAVAILABLE ||
			   (gpsStatus == LocationProvider.OUT_OF_SERVICE && 
				networkStatus == LocationProvider.TEMPORARILY_UNAVAILABLE)) {
				//Send best location update
				if (bestLocation != null) {
					try {
						String s = "Coordinates: " + bestLocation.getLatitude() + ", " + bestLocation.getLongitude();
						s += " at timestamp " + bestLocation.getTime();
						osw.write(s);
						Log.d(TAG, s);
					} catch (IOException e) {
						Log.e(TAG, "Could not open output file");
						e.printStackTrace();
					}
				}
				bestLocation = null;
			}
		}
	};
	
	public void onCreate() {
		locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, UPDATE_INTERVAL, 0, locListener);
		locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, UPDATE_INTERVAL, 0, locListener);
		try {
			fOut = openFileOutput("samplefile.txt", MODE_WORLD_READABLE);
		} catch (FileNotFoundException e) {
			Log.e(TAG, "Could not open file output");
			e.printStackTrace();
		}
		osw = new OutputStreamWriter(fOut);
		Log.i(TAG, "DataCollector started");
		Menu.started = true;
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
		Menu.started = false;
	}

	//TODO: Austin--Write function and figure out what the return value should actually be
	public int getRadiation(SignalStrength sig) {
			int strength = sig.getGsmSignalStrength();
			String s = "Signal Strength:" + strength + " dB";
			return strength;
	}
	
	public IBinder onBind(Intent intent) {
		return null;
	}
}