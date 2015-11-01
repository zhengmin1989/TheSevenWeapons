package com.introspy.custom_hooks;

import android.util.Log;

import com.introspy.core.IntroHook;

public class StringInit extends IntroHook {
	@Override
	public void execute(Object... args) {
		_logBasicInfo();
//		_logParameter("mzheng Intent details", (String)args[0]);

///*	 
			try {
		Log.d("mzhengToString", (String)_hookInvoke(args));
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//*/
	}
}

