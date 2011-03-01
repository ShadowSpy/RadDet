package edu.ucla.raddet.collector;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;


//Android Service which collects radiation information, as well as GPS information
public class DataCollector extends Service{

	public static final int UPDATE_INTERVAL = 60000;	//Frequency of GPS location updates
		
	private TelephonyManager Tel;
	private LocationManager locManager;
	private FileOutputStream fOut;
	private OutputStreamWriter osw;

	private int gpsStatus;
	private int networkStatus;
	private int sig; // Valid values are (0-31, 99) as defined in TS 27.007 8.5
	private Location bestLocation;
	
	private static final String TAG = "RadDet";
	
	private PhoneStateListener signalListener = new PhoneStateListener() {
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        	sig = signalStrength.getGsmSignalStrength();
        	Log.d(TAG, "New Signal Strength: " +sig);
        }
    }; 

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
			Log.d(TAG, "Best location is currently: " + bestLocation.getLatitude() + ", " + bestLocation.getLongitude());
			}
		}
		public void onStatusChanged(String provider, int status, Bundle extras) {
			//super.onStatusChanged(provider, status, extras);
			if (provider.equals(LocationManager.GPS_PROVIDER))
				gpsStatus = status;
			else if (provider.equals(LocationManager.NETWORK_PROVIDER))
				networkStatus = status;
			
			Log.d(TAG, "GPS: " + gpsStatus + " Network: " + networkStatus);
			
			if (gpsStatus == LocationProvider.TEMPORARILY_UNAVAILABLE ||
			   (gpsStatus == LocationProvider.OUT_OF_SERVICE && 
				networkStatus == LocationProvider.TEMPORARILY_UNAVAILABLE)) {
				//Send best location update
				if (bestLocation != null) {
					try {
						String s = bestLocation.getLatitude() + "," + bestLocation.getLongitude();
						s += "," + bestLocation.getTime() + ",";
						s += sig + "\n";
						osw.write(s);
						osw.flush();
						Log.i(TAG, "(Latitude,Longitude,Time,Signal)" + s);
						
						Toast.makeText(getApplicationContext(), "(Latitude,Longitude,Time,Signal)" + s, Toast.LENGTH_LONG).show();
						
					} catch (IOException e) {
						Log.e(TAG, "Could not open output file");
						e.printStackTrace();
					}
					bestLocation = null;
				}
			}
		}

		// Functions declared for sake of interface satisfaction
		public void onProviderDisabled(String provider) {}
		public void onProviderEnabled(String provider) {}
	};
	
	public void onCreate() {
		
		//Set up GPS Signal Strength
        Tel = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        Tel.listen(signalListener ,PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
		
        //Set up location manager
		locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, UPDATE_INTERVAL, 0, locListener);
		locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, UPDATE_INTERVAL, 0, locListener);
		
		//Set up output file
		try {
			fOut = openFileOutput("current.txt", MODE_WORLD_READABLE | MODE_APPEND);
		} catch (FileNotFoundException e) {
			Log.e(TAG, "Could not open file output");
			e.printStackTrace();
		}
		osw = new OutputStreamWriter(fOut);
		
		Log.i(TAG, "DataCollector started");
		Menu.started = true;	//Notify Activity that service has started
	}
	
	public void onDestroy() {
		locManager.removeUpdates(locListener);
		try {
			osw.close();
		} catch (IOException e) {
			Log.e(TAG, "Could not close file writer");
			e.printStackTrace();
		}
		Log.i(TAG, "DataCollector stopped");
		Menu.started = false;	//Notify Activity that service has stopped
	}
	
	public IBinder onBind(Intent intent) {
		return null;
	}
}