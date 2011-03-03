package edu.ucla.raddet.collector;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * A receiver which renames current.txt (so that it can
 * be stored) and then sends that file to an external server.
 * 
 * This class assumes that current.txt has already been written to
 * and has been written in correct CSV syntax
 */
public class DataPrep extends BroadcastReceiver {
	public static final String TAG = "RadDet";
	private SimpleDateFormat format = new SimpleDateFormat("ddMMMyy");
	
	public void onReceive(Context context, Intent intent) {
		// TODO 
		File currentFile = new File(context.getFilesDir(),"current.txt");
		File newPath = new File(context.getFilesDir(), "raddet" + format.format(new Date()) + ".csv");
		
		// Rename current.txt file to a new file
		if(!currentFile.renameTo(newPath)) {
			//Reaching this code means the renaming failed
			Log.e(TAG, "DataPrep error. File could not be renamed");
			return;
		}
		
		Log.i(TAG, "Sending file " + newPath.getName() + " to server");
		// TODO Send new file to server
	}
}
