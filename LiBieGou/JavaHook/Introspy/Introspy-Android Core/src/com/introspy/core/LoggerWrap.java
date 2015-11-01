package com.introspy.core;

import com.introspy.logging.Logger;

public class LoggerWrap extends IntroStringHelper {
	private Logger _l = new Logger();
	
	protected void _logInit(HookConfig config) {
		_l.logInit(config);
	}	
	
	protected void _logLine(String line) {
		_l.logLine(line);
	}
	
	protected void _logFlush_I(String notes) {
		_l.logFlush_I(notes);
	}
	protected void _logFlush_W(String notes) {
		_l.logFlush_W(notes);
	}
	
	protected void _logFlush_I() {
		_l.logFlush_I();
	}
	
	protected void _logFlush_W() {
		_l.logFlush_W();
	}
	
	protected void _logParameter(String name, String value) {
		_l.logParameter(name, value);
	}
	
	protected void _logParameter(String name, Object value) {
		_l.logParameter(name, "" + value);
	}
	
	protected void _logReturnValue(String name, String value) {
		_l.logReturnValue(name, value);
	}
	
	protected void _logReturnValue(String name, Object value) {
		_l.logReturnValue(name, "" + value);
	}
	
	protected void _logBasicInfo() {
		_l.logBasicInfo();
	}
	
	protected String _getFullTraces() {
		return _l.getFullTraces();
	}

	protected String _getLightTraces() {
		return _l.getLightTraces();		
	}
	
	public void disableDBlogger() {
		_l.disableDBlogger();
	}
	
	public void enableDBlogger() {
		_l.enableDBlogger();
	}
	
	public void enableTraces() {
		_l.enableTraces();
	}
	
	public void disableTraces() {
		_l.disableTraces();
	}
	
}
