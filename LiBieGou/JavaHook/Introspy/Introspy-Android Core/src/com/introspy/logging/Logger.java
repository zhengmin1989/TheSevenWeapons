package com.introspy.logging;

import com.introspy.core.*;

import android.util.Log;

// note: flush only when done with the logging 
// for a function as it dumps stack traces for the call

public class Logger extends LoggerDB {
	
	private void clean() {
		_out = "";
		_pListArgsBody = "";
		_pListRetBody = "";
	}

	protected void _log(String data) {
		_out += data;
	}
	
	// ####### public

	public void logInit(HookConfig config) {
		_config = config;
	}	
	
	public void logLine(String line) {
		_out += line + "\n";
	}
	
	public void logFlush_I(String notes) {
		_notes = notes;
		_out += notes;
		logFlush_I();
	}
	public void logFlush_W(String notes) {
		_notes = notes;
		_out += "-> !!! " + notes;
		logFlush_W();
	}
	
	// use a static ref to synchronize across threads
	public void logFlush_I() {
		_addTraces();
		synchronized (_TAG) {
			if (_enableDB)
				_logInDB("I");
			Log.i(_TAG, _out);
		}
		clean();
	}
	
	public void logFlush_W() {
		_addTraces();
		synchronized (_TAG) {
			if (_enableDB)
				_logInDB("W");
			Log.w(_TAG, _out);
		}
		clean();
	}
	
	public void logParameter(String name, String value) {
		if (_enableDB)
			_logDBParameter(name, value);
	}
	
	public void logParameter(String name, Object value) {
		if (_enableDB)
			_logDBParameter(name, "" + value);
	}
	
	public void logReturnValue(String name, String value) {
		if (_enableDB)
			_logDBReturnValue(name, value);
	}
	
	public void logReturnValue(String name, Object value) {
		if (_enableDB)
			_logDBReturnValue(name, "" + value);
	}
	
	public void logBasicInfo() {
		_log("### "+ _config.getCategory()+" ### " + 
				ApplicationConfig.getPackageName() + 
				" - " + _config.getClassName() + "->" 
				+ _config.getMethodName() + "()\n");
	}
}
