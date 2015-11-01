package com.introspy.logging;

import com.introspy.core.HookConfig;
import com.introspy.core.IntroStringHelper;

public class LoggerConfig extends IntroStringHelper {
	protected LoggerConfig() {
	}
	
	protected HookConfig _config;
	
	public static String _TAG = "Introspy";
	public static String _TAG_ERROR = "IntrospyError";
	public static String _TAG_LOG = "IntrospyLog";
	
	public static String getTag() {
		return _TAG;
	}
	
	public static String getTagError() {
		return _TAG_ERROR;
	}
	
	public static String getTagLog() {
		return _TAG_LOG;
	}

	protected String _out = "";
	protected String _notes = "";
	protected String _traces = "";
	
	protected boolean _enableDB = true;
	
	// this can be enabled via the _config file
	protected boolean _stackTraces = false;
	
	// change this value to get full traces
	protected boolean _fullTraces = false;
	
}
