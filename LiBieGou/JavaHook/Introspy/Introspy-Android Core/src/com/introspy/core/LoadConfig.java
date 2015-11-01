package com.introspy.core;

import java.io.RandomAccessFile;
import android.text.format.Time;

public class LoadConfig {
	// set to true here makes it faster but won't
	// allows to update changes in the file a runtime
	private Boolean _onlyCheckOnce = false;	
	// prevents the app for constantly opening the config file
	private Boolean _alreadyChecked = false;
	private Boolean _previousCheckValue = false;
	
	private String _configFileName = "introspy.config";
	
	private Time _lastCheck = new Time();
	
	private String _hookTypes = "";
	
	private static LoadConfig _instance = null;
	
	protected LoadConfig() {
	}
	
	static public LoadConfig getInstance() {
		if (_instance == null)
			_instance = new LoadConfig();
		return _instance;
	}
	
	
	public String getHookTypes() {
		return _hookTypes;
	}
	
	// no config file means the app won't be hooked
	public Boolean initConfig(String dataDir) {
		if (_alreadyChecked)
			return _previousCheckValue;

		Time now = new Time();
		now.setToNow();
		
		// check for modifications only every X
		if (_lastCheck.toMillis(true) + 3000 >= now.toMillis(true)) {
			return _previousCheckValue;
		}
		
		String path = dataDir + "/" + _configFileName;
		_hookTypes = readFirstLineOfFile(path);
		if (_onlyCheckOnce)
			_alreadyChecked = true;
		_lastCheck.setToNow();
		
		if (_hookTypes.isEmpty()) {
			return (_previousCheckValue = false);
		}
		return (_previousCheckValue = true);
	}
	
	private String readFirstLineOfFile(String fn) {
	    String lineData = "";
	    try{
	        RandomAccessFile inFile = new RandomAccessFile(fn, "r");
	        lineData = inFile.readLine();
	        inFile.close();
	    }
	    // file not found
	    catch(Exception e){
	    	// Log.i("IntrospyLog", "--> "+ e);
	    	// app won't be hooked
	    }
	    return lineData;
	}
}
