package com.introspy.hooks;
import com.introspy.core.IntroHook;

//setPluginState(WebSettings.PluginState state) 
// with android.webkit.WebSettings.PluginState.ON
// setJavaScriptEnabled(boolean flag) with true
// setAllowFileAccess (boolean allow)
class Intro_WEBVIEW_SET extends IntroHook { 
	public void execute(Object... args) {
		
		// should only display data when first arg is set to true
		
		if ((Boolean)args[0] == true) {
			_logBasicInfo();
			_logFlush_W("-> !!! Set of a potentially dangerous " +
					"functionality to true for the webview using "+
					_config.getMethodName() + ", make sure this " +
					"functionality is necessary");
		}
	}
}

// addJavascriptInterface(Object object, String name)
class Intro_WEBVIEW_JS_BRIDGE extends IntroHook { 
	public void execute(Object... args) {
		
		// this only display data when there is a potential issue
		_logBasicInfo();
		_logFlush_W("-> !!! Javascript interface " +
				"added for the webview. Details: " +
				args[0] + ", name: " + args[1]);
	}
}
