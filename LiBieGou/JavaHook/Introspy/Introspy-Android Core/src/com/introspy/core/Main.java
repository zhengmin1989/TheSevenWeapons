package com.introspy.core;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Member;
import android.util.Log;

import com.introspy.hooks.HookList;
import com.introspy.custom_hooks.CustomHookList;
import com.introspy.logging.LoggerConfig;
import com.saurik.substrate.*;

public class Main {	
//	static private String _TAG = LoggerConfig.getTag();
	static private String _TAG_ERROR = LoggerConfig.getTagError();
	static private String _TAG_LOG = LoggerConfig.getTagLog();
	static private boolean _debug = false;
	
	public static void initialize() {
		HookConfig[] _config = HookList.getHookList();
		HookConfig[] _custom_config = CustomHookList.getHookList();
		
		initializeConfig(_config);
		initializeConfig(_custom_config);
		
		MS.hookClassLoad("android.app.ContextImpl", new MS.ClassLoadHook() {
			public void classLoaded(Class<?> resources) { 
				ApplicationState.initApplicationState(resources);
			}
		});
	}
	
	protected static void initializeConfig(HookConfig[] config) {
		for (final HookConfig elemConfig : config) {
			if (!elemConfig.isActive())
				continue;
			
			MS.hookClassLoad(elemConfig.getClassName(),
				new MS.ClassLoadHook() {
					public void classLoaded(Class<?> resources) {
						_hookMethod(resources, elemConfig);
					}
				});
		}		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected static void _hookMethod(Class<?> resources, final HookConfig elemConfig) {
		
		GenericDeclaration pMethod = null;
		final String className = elemConfig.getClassName();
		final String methodName = elemConfig.getMethodName();
		final Class<?>[] parameters = elemConfig.getParameters();
		
		Log.i(_TAG_LOG, "### Hooking: " + className + "->" + 
				methodName + "() with " + 
        		parameters.length + " args");
		try {
			// check if the method is a constructor
			if (className.substring(className.lastIndexOf('.') + 1).equals(methodName))
				pMethod = resources.getDeclaredConstructor(parameters);
			else	
				pMethod = resources.getMethod(methodName, parameters); 
		}
		catch (NoSuchMethodException e) {
				Log.w(_TAG_ERROR, "Error - No such method: " + methodName + " with " + 
	            		parameters.length + " args");
		        for (int j = 0; j < parameters.length; j++) 
		        	Log.i(_TAG_ERROR, "Arg "+ (j+1) +" type: " + parameters[j]);
		        return;
		}
				
		final MS.MethodPointer old = new MS.MethodPointer();
		MS.hookMethod_(resources, (Member) pMethod,
				new MS.MethodHook() {
					public Object invoked(final Object resources,
							final Object... args) throws Throwable {
						if (ApplicationConfig.isEnabled())
							_hookMethodImpl(old, resources, elemConfig, args);
						return old.invoke(resources, args);
					}
				}, old);
	}
		
	@SuppressWarnings("rawtypes")
	protected static void _hookMethodImpl(final MS.MethodPointer old,
			Object resources, final HookConfig elemConfig,
			Object... args) {
		String packageName = ApplicationConfig.getPackageName();
		String dataDir = ApplicationConfig.getDataDir();
		String type = elemConfig.getSubType();
		
		if (packageName != null && dataDir != null && 
				LoadConfig.getInstance().initConfig(dataDir) && 
				LoadConfig.getInstance().getHookTypes().contains(type)) {
			try {
				elemConfig.getFunc().init(elemConfig, resources, old, args);

				if (LoadConfig.getInstance().getHookTypes().contains("STACK TRACES"))
					elemConfig.getFunc().enableTraces();
				else
					elemConfig.getFunc().disableTraces();
				
				if (LoadConfig.getInstance().getHookTypes().contains("NO DB") ||
						 LoadConfig.getInstance().getHookTypes().contains("SQLite"))
					elemConfig.getFunc().disableDBlogger();
				else
					elemConfig.getFunc().enableDBlogger();
				
				elemConfig.getFunc().init(elemConfig, resources, old, args);
				if (_debug)
					Log.i(_TAG_LOG, "=== Calling: " + elemConfig.getMethodName());
				
				elemConfig.getFunc().execute(args);
			} catch (Exception e) {
				Log.w(_TAG_ERROR, "-> Error in injected code: [" + e + "]" +
						"\nApp: " + ApplicationConfig.getPackageName() + 
						", method: " + elemConfig.getMethodName() + 
						", class: " + elemConfig.getClassName());
				// Log.w(_TAG_ERROR, LoggerErrorHandler._getStackTrace());
			}
		}
	}
}
