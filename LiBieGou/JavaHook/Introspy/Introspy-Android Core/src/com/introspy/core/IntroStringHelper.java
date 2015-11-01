package com.introspy.core;

import android.util.Base64;

public class IntroStringHelper {
	protected String _byteArrayToHex(byte[] a) {
		   StringBuilder sb = new StringBuilder();
		   for(byte b: a)
		      sb.append(String.format("%02x-", b&0xff));
		   sb.deleteCharAt(sb.length()-1);
		   return sb.toString();
		}

	protected String _byteArrayToReadableStr(byte[] a) {
		   StringBuilder sb = new StringBuilder();
		   for(byte b: a) {
			   if (b >= 32 && b < 127)
				   sb.append(String.format("%c", b));
			   else
				   sb.append('.');
		   }
		   //sb.deleteCharAt(sb.length()-1);
		   return sb.toString();
		}
	
	protected String _byteArrayToB64(byte[] a) {
		return Base64.encodeToString(a, Base64.NO_WRAP);
	}
	
	protected Boolean _isItReadable(String input) {
	    int readableChar = 0;
	    for (int i = 0; i < input.length(); i++) {
	        int c = input.charAt(i);
//			Check if a string only contains US-ASCII code point
//	        if ((c >= 32 && c < 127) || c == 9 || 
//	        		c == 13 || c == 10 || c == 0) {
	        if (c >= 32 && c < 127) {
	        	readableChar++;
	        }
	    }
	    
	    // can be considered readable if X% characters are ascii
	    // (0 is considered a character here so that UTF16 
	    // can be considered readable too)
	    return (readableChar > (input.length() * 0.75) ? true : false);
	}
	
	protected String _escapeXMLChars(String s) {
		    return s.replaceAll("&",  "&amp;")
		         .replaceAll("'",  "&apos;")
		         .replaceAll("\"", "&quot;")
		         .replaceAll("<",  "&lt;")
		         .replaceAll(">",  "&gt;");
	 }
	
	protected String _getReadableByteArr(byte[] input) {
		String out = new String(input);
		if (!_isItReadable(out))
			out = _byteArrayToB64(input);
		else
			out = _byteArrayToReadableStr(input);
		return out;
	}
}
