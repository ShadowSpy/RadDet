package edu.ucla.raddet.collector;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
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

	//Service Features
	public static final long UPDATE_INTERVAL = 30000L;	//Frequency of GPS location updates
		
	private TelephonyManager Tel;
	private LocationManager locManager;
	private AlarmManager alarmManager;
	private FileOutputStream fOut;
	private OutputStreamWriter osw;

	private int gpsStatus;
	private int networkStatus;
	private double sig; // Valid values are 0-10
	private Location bestLocation;
	
	private static final String TAG = "RadDet";
	
    //Upload Features
	URL connectURL;
    String params;
    String responseString;
    String fileName;
    byte[] dataToServer;  
	
    String pathToOurFile = "file_to_send.txt";
    //Current IP Address
    //String urlServer = "http://192.168.110.121/handle_upload.php";
    
    //Peter's Home IP Address
    String urlServer = "http://76.89.156.78/handle_upload.php";
    //String urlServer = "http://192.168.0.197/handle_upload.php";
    
	private PhoneStateListener signalListener = new PhoneStateListener() {
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        	double p = signalStrength.getCdmaDbm();
        	
        	if(p > -80)
        		sig = 10;
        	else if(p < -90)
        		sig = 0;
        	else
        	{
        		double power = java.lang.Math.pow(10,((p-30)/10));
        		sig = ((power-(java.lang.Math.pow(10,-12)))/(9*java.lang.Math.pow(10,-12)))*10;
        	}
        	Log.d(TAG, "New Signal Strength: " + p + " dBm" + "& " + sig + " ratio");
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
				if (loc.getAccuracy() <= bestLocation.getAccuracy())
					bestLocation = loc;
			}
			Log.d(TAG, "Best location is currently: " + bestLocation.getLatitude() + ", " + bestLocation.getLongitude() + " Type of network: " + bestLocation.getProvider());
			Calendar cal = Calendar.getInstance();
			//if(cal.get(Calendar.HOUR)== 5 && cal.get(Calendar.MINUTE)== 0 && cal.get(Calendar.SECOND)== 0 && cal.get(Calendar.DAY_OF_MONTH)== 2)
			if((cal.get(Calendar.SECOND)>= 0 && cal.get(Calendar.SECOND)<= 10) || (cal.get(Calendar.SECOND)>= 20 && cal.get(Calendar.SECOND)<= 30) || (cal.get(Calendar.SECOND)>= 40 && cal.get(Calendar.SECOND)<= 50))
			{	
				try {
					String s = bestLocation.getLatitude() + "," + bestLocation.getLongitude();
					s += "," + sig + ",";
					s += bestLocation.getTime() + "\n";
					osw.write(s);
					osw.flush();
					Log.i(TAG, "(Latitude,Longitude,Signal,Time)" + s);
					
					//Toast.makeText(getApplicationContext(), "(Latitude,Longitude,Signal,Time)" + s, Toast.LENGTH_LONG).show();
					
					//Calendar cal = Calendar.getInstance();
					//if(cal.get(Calendar.HOUR)== 5 && cal.get(Calendar.MINUTE)== 0 && cal.get(Calendar.SECOND)== 0 && cal.get(Calendar.DAY_OF_MONTH)== 2)
					//if(cal.get(Calendar.HOUR)== 5)
					{						
						Log.i(TAG, "Sending data to server...");
						Toast.makeText(getApplicationContext(), "Sending data to server...", Toast.LENGTH_LONG).show();
						uploadFile("current.txt", urlServer);
					}
				} catch (IOException e) {
					Log.e(TAG, "Could not open output file");
					e.printStackTrace();
				}
				//bestLocation = null;
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
						s += "," + sig + ",";
						s += bestLocation.getTime() + "\n";
						osw.write(s);
						osw.flush();
						Log.i(TAG, "(Latitude,Longitude,Signal,Time)" + s);
						
						Toast.makeText(getApplicationContext(), "(Latitude,Longitude,Signal,Time)" + s, Toast.LENGTH_LONG).show();
						
						//Calendar cal = Calendar.getInstance();
						//if(cal.get(Calendar.HOUR)== 5 && cal.get(Calendar.MINUTE)== 0 && cal.get(Calendar.SECOND)== 0 && cal.get(Calendar.DAY_OF_MONTH)== 2)
						//if(cal.get(Calendar.HOUR)== 5)
						{						
							Log.i(TAG, "Sending data to server...");
							Toast.makeText(getApplicationContext(), "Sending data to server...", Toast.LENGTH_LONG).show();
							uploadFile("current.txt", urlServer);
						}
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
		public void onProviderEnabled(String provider) {
			/*try {
				String s = bestLocation.getLatitude() + "," + bestLocation.getLongitude();
				s += "," + sig + ",";
				s += bestLocation.getTime() + "\n";
				osw.write(s);
				osw.flush();
				Log.i(TAG, "(Latitude,Longitude,Signal,Time)" + s);
				
				Toast.makeText(getApplicationContext(), "(Latitude,Longitude,Signal,Time)" + s, Toast.LENGTH_LONG).show();
				
				//Calendar cal = Calendar.getInstance();
				//if(cal.get(Calendar.HOUR)== 5 && cal.get(Calendar.MINUTE)== 0 && cal.get(Calendar.SECOND)== 0 && cal.get(Calendar.DAY_OF_MONTH)== 2)
				//if(cal.get(Calendar.HOUR)== 5)
				{						
					Log.i(TAG, "Sending data to server...");
					Toast.makeText(getApplicationContext(), "Sending data to server...", Toast.LENGTH_LONG).show();
					uploadFile("current.txt", urlServer);
				}
			} catch (IOException e) {
				Log.e(TAG, "Could not open output file");
				e.printStackTrace();
			}*/
		}
	};
	
	public void onCreate() {
		
		//Set up GPS Signal Strength
        Tel = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        Tel.listen(signalListener ,PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
		
        //Set up location manager
		locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, UPDATE_INTERVAL, 0, locListener);
		locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, UPDATE_INTERVAL, 0, locListener);
		
		//Set up alarm manager
		alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
			
		//Set up output file
		boolean fileExists = false;;
		for (String name : fileList()) {
			if (name.equals("current.txt")) {
				fileExists = true;
				break;
			}
		}
		
		try {
			fOut = openFileOutput("current.txt", MODE_WORLD_READABLE | MODE_APPEND);
		} catch (FileNotFoundException e) {
			Log.e(TAG, "Could not open file output");
			e.printStackTrace();
		}
		osw = new OutputStreamWriter(fOut);
		
		if (!fileExists) {
			//Insert row header for CSV file
			try {
				osw.write("Lat,Long,Signal,Time\n");
				osw.flush();
				
				//Set an alarm to send out the file
				Calendar cal = Calendar.getInstance();	//Get current time
				cal.set(Calendar.HOUR, 0);				//We only care about the actual date
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.add(Calendar.DAY_OF_MONTH, 1);		//Set calendar to midnight of tomorrow
				
				// Call the DataPrep receiver to send file at midnight of tomorrow
				PendingIntent pi = PendingIntent.getBroadcast(this, 0, new Intent(this, DataPrep.class), 0);
				alarmManager.set(AlarmManager.RTC, cal.getTimeInMillis(), pi);
				Log.i(TAG, "Alarm set to send file on " + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.DAY_OF_MONTH));
			} catch (IOException e) {
				Log.e(TAG, "Could not open output file");
				e.printStackTrace();
			}
		}
				
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
	
	public void uploadFile(String filename, String url){
		try {
			FileInputStream fis = openFileInput(filename);
			HttpFileUploader htfu = new HttpFileUploader(url,"noparamshere", filename);
			htfu.doStart(fis, filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
