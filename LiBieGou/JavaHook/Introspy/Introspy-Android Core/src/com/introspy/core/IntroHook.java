package com.introspy.core;

import android.util.Log;

import com.introspy.logging.LoggerConfig;
import com.saurik.substrate.MS;

@SuppressWarnings("rawtypes")
public 
class IntroHook extends LoggerWrap {
	protected HookConfig _config = null;
	
	protected String _TAG = LoggerConfig.getTag();
	protected String _TAG_ERROR = LoggerConfig.getTagError();
	
	protected String _className, _methodName, _type;
	protected String _packageName, _dataDir;
	protected String _notes;
	protected Object _resources;
	protected Object _args;
	
	protected Class<?>[] _parameters;
	protected MS.MethodPointer _old = null;
	
	// Protected constructor prevents 
	// instantiation from other classes
	protected IntroHook() {

	}
	
	public void init(HookConfig config, Object resources, MS.MethodPointer old, Object... args) {
		if (_config == null) {
			_config = config;
			_className = _config.getClassName();
			_methodName = _config.getMethodName();
			_parameters = _config.getParameters();
			_type = _config.getCategory();
			_old = old;
			_notes = _config.getNotes();
			
			_resources = resources;
			
			_packageName = ApplicationConfig.getPackageName();
			_dataDir = ApplicationConfig.getDataDir();
			
			_logInit(config);
		}
	}
	
	public void execute(Object... args) {
		// display info on the app related to the hook
		_logBasicInfo();
		if (!_config.getNotes().isEmpty())
			_logLine("Notes: " + _notes);
		_logLine("-> Resources type: " + 
				(_resources !=  null ? _resources.getClass() : "None"));
		
		try {
			if (_config.getParameters() != null) {
				int argNb = 0;
				for (Class<?> elemParameter : _parameters) {
					_logLine("-> Argument " + (argNb + 1) +
							", Data: " + args[argNb].toString() +
							" (Type: " + elemParameter.getName() + ")"
							);
					argNb++;
				}
			}
		}
		catch (Exception e) {
			Log.w(_TAG, "-> Error getting arguments");
		}
		_logFlush_I();
	}
	
	@SuppressWarnings("unchecked")
	protected Object _hookInvoke(Object... args) throws Throwable {
		return _old.invoke(_resources, args);
	}
}
