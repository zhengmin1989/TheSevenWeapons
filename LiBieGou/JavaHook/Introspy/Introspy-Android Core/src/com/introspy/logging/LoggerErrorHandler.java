package com.introspy.logging;

public class LoggerErrorHandler {
	static public  String _getStackTrace() {
		String out = "";
		StackTraceElement[] ste = 
				Thread.currentThread().getStackTrace();
		for (int i = 0; i < ste.length; i++)
			out += ste[i].toString() + "\n";
		return out.substring(0, out.length() - 1);
	}
}
