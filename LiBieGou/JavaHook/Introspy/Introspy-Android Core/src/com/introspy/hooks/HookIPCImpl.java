package com.introspy.hooks;
import com.introspy.core.IntroHook;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

class FuncIPC extends IntroHook {
	protected String getExtras(Intent intent) {
		String out = "";
		try {
			Bundle bundle = intent.getExtras();
		    if (bundle != null) {
				for (String key : bundle.keySet()) {
				    Object value = bundle.get(key);
				    out += String.format("--> [%s %s (%s)]\n", key,  
				        value.toString(), value.getClass().getName());
				}
				out = out.substring(0, out.length() - 1);
		    }
		}
		catch (Exception e) {
			out = "Cannot get intent extra";
		}
		return out;
	}
}

class Intro_DUMP_INTENT extends FuncIPC { 
	public void execute(Object... args) {		
		_logBasicInfo();
		
		// arg0 is an Intent
		Intent intent = (Intent) args[0];
		String out = "-> " + intent;
		_logParameter("Intent", intent);
		String extra = getExtras(intent);
		if (!extra.isEmpty()) {
			_logParameter("Extra", extra);
			out += "\n-> Extra: \n" + extra + "";
		}
		_logFlush_I(out);
	}
}

// Hook:
// Intent registerReceiver (BroadcastReceiver receiver, IntentFilter filter)
// Intent registerReceiver (BroadcastReceiver receiver, IntentFilter filter, 
//							String broadcastPermission, Handler scheduler)
class Intro_IPC_RECEIVER extends FuncIPC { 
	public void execute(Object... args) {
		_logBasicInfo();
		String out = "";
		
		// arg1 is an intent filter
		IntentFilter intentFilter = (IntentFilter) args[1];
		if (intentFilter != null) {
			out = "-> Intent Filter: \n";
			for (int i = 0; i < intentFilter.countActions(); i++)
				out += "--> [Action "+ i +":"+intentFilter.getAction(i)+"]\n";
			out = out.substring(0, out.length() - 1);
			_logParameter("Intent Filter", out);
		}
		
		// args[2] is the permissions
		if (args.length > 2 && args[2] != null) {
			out += ", permissions: " + args[2];
			_logParameter("Permissions", args[2]);
		}
		_logLine(out);
		
		if (args.length == 2 || (args.length > 2 && args[2] == null))			
			_logFlush_I("-> No permissions explicitely defined for the Receiver");
		else
			_logFlush_I();
	}
}

class Intro_URI_REGISTER extends FuncIPC { 
	public void execute(Object... args) {
		String uriPath = (String)args[1];
		_logParameter("URI Path", uriPath);
		
		String data = "URI:"+_config.getMethodName()+":"
						+_packageName+uriPath;
		_logBasicInfo();
		_logFlush_I(data);
	}
}
	
// IPCs disabled in the manifest can be enabled dynamically
class Intro_IPC_MODIFIED extends FuncIPC { 
	public void execute(Object... args) {
		
		// arg1: newState
		int newState = (Integer)args[1];
		if (newState == 
				android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
			_logBasicInfo();
			_logParameter("New State", "COMPONENT_ENABLED_STATE_ENABLED");
			_logFlush_W("-> !!! Component ["+ args[0] +
					"] is ENABLED dynamically");
		}
	}
}

