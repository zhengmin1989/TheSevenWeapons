package com.introspy.custom_hooks;

import android.content.Intent;
import android.util.Log;

import com.introspy.core.IntroHook;

public class HookStrings extends IntroHook {
	@Override
	public void execute(Object... args) {
//		_logBasicInfo();
//		_logParameter("mzheng Intent details", (String)args[0]);
		_logFlush_I( "Method: " + _config.getMethodName());
		_logFlush_I("Equels String:" +  (String)args[0]);
	}
}

