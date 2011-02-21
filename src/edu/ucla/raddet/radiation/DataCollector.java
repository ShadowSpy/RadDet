package edu.ucla.raddet.radiation;

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


//Android Service which collects radiation information, as well as GPS information
public class DataCollector extends Service implements LocationListener{

	public LocationManager locManager;
	public static final int UPDATE_INTERVAL = 60000;	//Frequency of GPS location updates
	private File file = new File(getExternalFilesDir("raddet"), "output.txt");
	//private SignalStrength sigstrength = new SignalStrength();
	public SignalStrength sigstrength;
	
	//@Override
	public void onCreate() {
		locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
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

	//@Override
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

	//@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	//@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	//@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Returns the IDataCollector interface to calling process. Ignores Intent
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	/**
	 * The IDataCollector interface is defined through the resepective AIDL file
	 */
	private final IDataCollector.Stub mBinder = new IDataCollector.Stub() {
		public void startDataCollection() {
			try {
				locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, UPDATE_INTERVAL, 0, DataCollector.this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public void stopDataCollection() {
			locManager.removeUpdates(DataCollector.this);
		}
	};
}