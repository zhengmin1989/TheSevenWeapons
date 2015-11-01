package com.introspy.hooks;
import com.introspy.core.IntroHook;

import java.io.File;

import android.util.Log;


class Intro_FILE_PARENT extends IntroHook {
	// enherited by the FS hook classes
	protected boolean is_SD_card(String path) {
		if (path != null && 
				(path.contains("sdcard") || 
				path.contains("/storage/"))) {
			
			// crashes with system app:
			// path.contains(Environment.getExternalStorageDirectory().toString()
			
			//super.execute(config, resources, old, args);
			return true;
		}
		return false;
	}
}

class Intro_FILE_CHECK_DIR extends Intro_FILE_PARENT { 
	public void execute(Object... args) {
		// noisy so display data only when it reads the sdcard
		// arg0 is the path
		try {
			// String root = _dataDir + "/" + args[0];
			String path = "[" +  args[0] + "]";
			_logParameter("Path", path);
			if (is_SD_card(path)) {
				_logBasicInfo();
				_logFlush_W("Read/write on sdcard: " + path);
			} else {
				// one liner on this to avoid too much noise
				_logFlush_I("### FS:"+ _packageName + ":" + path);
			}
			
		} catch (Exception e) {
			Log.w("IntrospyLog", "Exception in Intro_FILE_CHECK_DIR: " + e);
			Log.w("IntrospyLog", "-> App path: " + _dataDir + 
					"\n" + e.fillInStackTrace());
		}
	}
}

class Intro_CHECK_FS_SET extends Intro_FILE_PARENT { 
	public void execute(Object... args) {
		// noisy
		// arg0 is the path
		if ((Boolean)(args[0]) == true && 
				(Boolean)args[1] == false) {
			//super.execute(config, resources, old, args);
			File f = (File) _resources;
			_logBasicInfo();
			_logParameter("Mode", "WORLD read/write");
			_logParameter("Path", f.getAbsolutePath());
			_logFlush_W("Writing file with WORLD read/write mode: " + 
						" in " + f.getAbsolutePath());
		}
	}
}

class Intro_FILE_CHECK_MODE extends Intro_FILE_PARENT { 
	@SuppressWarnings("deprecation")
	public void execute(Object... args) {
		// arg0 is the path
		String path = ": [" + _dataDir + "/" +  (String)args[0] + "]";
		if (is_SD_card(path)) {
			_logBasicInfo();
			_logParameter("Path", path);
			_logFlush_W("Read/write on sdcard: " + path);
		}
		else {
			// arg1 is the mode
			Integer mode = (Integer) args[1];
			
			String smode;
			switch (mode) {
				case android.content.Context.MODE_PRIVATE: 
					smode = "Private";
					break;
				case android.content.Context.MODE_WORLD_READABLE: 
					smode = "!!! World Readable !!!";
					break;
				case android.content.Context.MODE_WORLD_WRITEABLE: 
					smode = "!!! World Writable !!!";
					break;
				default: 
					smode = "???";
			}
			smode = "MODE: " + smode;
			
			if (mode == android.content.Context.MODE_WORLD_READABLE || 
					mode == android.content.Context.MODE_WORLD_WRITEABLE) {
				_logBasicInfo();
				_logParameter("Mode", smode);
				_logFlush_W("Writing file with dangerous mode: " + 
							smode + " in " + path);
			}
		}
	}
}
