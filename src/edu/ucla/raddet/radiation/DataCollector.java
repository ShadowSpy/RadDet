package edu.ucla.raddet.radiation;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


//Android Service which collects radiation information, as well as GPS information
public class DataCollector extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	//TODO: Austin--Write function and figure out what the return value should actually be
	private void getRadiation() {
	
	}
	
	//TODO: Roman--Write function and figure out what the return value should actually be
	private void getLocation() {
		
	}
}
