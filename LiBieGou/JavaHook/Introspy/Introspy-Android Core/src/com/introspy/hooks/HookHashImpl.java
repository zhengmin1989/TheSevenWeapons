package com.introspy.hooks;
import com.introspy.core.IntroHook;

import java.lang.reflect.Method;
import java.security.MessageDigest;

import android.util.Log;


class Intro_HASH extends IntroHook { 
	public void execute(Object... args) {
		
	    StackTraceElement[] ste = Thread.currentThread().getStackTrace();
	    // this is called within apps and is super noisy so not displaying it
	    if (ste[7].toString().contains("com.crashlytics."))
	    	return;
	    // the above code may only work on android 4.2.2
	    // replace with the code below if so
	    /* for (int i = 0; i < ste.length; i++)
	        if (ste[i].toString().contains("com.crashlytics."))
	        	return; */
	    
		if (args[0] != null) {
            _logBasicInfo();
            String input = _getReadableByteArr((byte[])args[0]);            
            
            byte[] output = null;
            String s_output = "";
            try {
                    // execution the method to calculate the digest
                    // using reflection to call digest from the object's instance
                    try {
                            Class<?> cls = Class.forName("java.security.MessageDigest");
                            Object obj =_resources;
                            Class<?> noparams[] = {};
                            Method xmethod = cls.getDeclaredMethod("digest", noparams);
                            output = (byte[]) xmethod.invoke(obj);
                            s_output = _getReadableByteArr(output);
                    }
                    catch (Exception e) {
                    	Log.w(_TAG_ERROR, "Error in Hash func: " + e);
                    }
            }
            catch (Throwable e) {
            	Log.w(_TAG_ERROR, "Error in Hash func: " + e);
            }

            // use reflection to call a method from this instance
            String algoName = null;
            try {
                    Class<?> cls = Class.forName("java.security.MessageDigest");
                    Object obj =_resources;
                    Class<?> noparams[] = {};
                    Method xmethod = cls.getDeclaredMethod("getAlgorithm", noparams);
                    algoName = (String) xmethod.invoke(obj);
            }
            catch (Exception e) {
                    algoName = "error: " + e;        
            }
            
            _logLine("-> Hash of : [" + input + "] is: [" + 
                            s_output +"] , Algo: [" + algoName + "]");
            
            _logParameter("Input", input);
            _logParameter("Algo", algoName);
            _logReturnValue("Output", s_output);
            
            if (algoName.contains("MD5")) {
                    _logFlush_W("MD5 used, this hashing algo " +
                    		"is broken and should not be used");
            }
            else
                    _logFlush_I();
		}
	}
}

class Intro_GET_HASH extends Intro_CRYPTO { 
	public void execute(Object... args) {		
		try {
			byte[] data  = (byte[]) _hookInvoke(args);
			MessageDigest dg = (MessageDigest) _resources;
			_logBasicInfo();
			
			String sdata = _getReadableByteArr(data);
			
			_logReturnValue("Data", sdata);
			_logParameter("Algo", dg);
			
			_logFlush_I("-> Algo: " + dg + ", Data: " + sdata);
		} catch (Throwable e) {
			Log.i(_TAG_ERROR, "Error in Fun_GET_HASH" + e);
		}
	}
}
