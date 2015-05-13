package com.mzheng;

import java.util.Arrays;

import android.util.Log;

public class MZLog {

	public static void Log(String tag, String msg)
	{
		Log.d(tag, msg);
	}
	
	public static void Log(Object someObj)
	{
		Log("mzheng", someObj.toString());
	}
	
	public static void Log(Object[] someObj)
	{
		Log("mzheng",Arrays.toString(someObj));
	}
	
}
