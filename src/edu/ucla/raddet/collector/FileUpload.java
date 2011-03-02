package edu.ucla.raddet.collector;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

import android.app.Activity;
import android.os.Bundle;
//import android.widget.TextView;

public class FileUpload extends Activity {
	URL connectURL;
    String params;
    String responseString;
    String fileName;
    byte[] dataToServer;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TextView tv = new TextView(this);
        
		String pathToOurFile = "file_to_send.txt";
		String urlServer = "http://192.168.0.197/handle_upload.php";

		FileOutputStream fOut = null;
		OutputStreamWriter osw = null;

		try {
			fOut = openFileOutput(pathToOurFile, MODE_PRIVATE);      
			osw = new OutputStreamWriter(fOut);
			osw.write("abcdefg\n");
			osw.flush();
		}
		catch (Exception e) { 
			e.printStackTrace();
		}
		
		uploadFile(pathToOurFile, urlServer);
        
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