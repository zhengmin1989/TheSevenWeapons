package com.introspy.hooks;

import java.io.File;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.nio.ByteBuffer;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.TrustManager;

import org.apache.http.conn.scheme.HostNameResolver;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.params.HttpParams;

import com.introspy.core.HookConfig;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

public class HookList {
	
	static public HookConfig[] getHookList() {
		return _hookList;
	}
	
	static private HookConfig[] _hookList = new HookConfig[] {
		
		/*  ############################################
		 *  ############### Crypto Methods
		 */ 
			new HookConfig(false, "CRYPTO", "GENERAL CRYPTO", "javax.crypto.Cipher", "getInstance", 
				new Intro_CRYPTO(), new Class<?>[]{String.class}, ""),
							
			new HookConfig(false, "CRYPTO", "GENERAL CRYPTO", "javax.crypto.Cipher", "update", 
				new Intro_CRYPTO(), new Class<?>[]{byte[].class},
				"Continues a multi-part transformation (encryption or decryption)"),
				
			new HookConfig(false, "CRYPTO", "GENERAL CRYPTO", "javax.crypto.Cipher", "update", 
				new Intro_CRYPTO(), new Class<?>[]{byte[].class},
				"Continues a multi-part transformation (encryption or decryption)"),
			
			new HookConfig(false, "CRYPTO", "GENERAL CRYPTO", "java.util.Random", "Random", 
				new Intro_SHOULD_NOT_BE_USED(), new Class<?>[]{}, "Weak RNG"),
				
			// doesn't need to hook that if you hook doFinal as the hook calls it already
			new HookConfig(false, "CRYPTO", "GENERAL CRYPTO", "javax.crypto.Cipher", "getIV", 
				new Intro_CRYPTO(), new Class<?>[]{}, ""),
			
			// this gives the hash algo + hash
			// need to hook "update" to get what is hashed
			// we can also retrieve this info by hooking 'update'
			// so I'm disabling it
			new HookConfig(false, "CRYPTO", "HASH", "java.security.MessageDigest", "digest", 
					new Intro_GET_HASH(), new Class<?>[]{}, ""),
				
			// ############ doFinal
			new HookConfig(true, "CRYPTO", "GENERAL CRYPTO", "javax.crypto.Cipher", "doFinal", 
				new Intro_CRYPTO_FINAL(), new Class<?>[]{},
				"Finishes a multi-part transformation (encryption or decryption)"),
			
			// method doesn't seem to exist in Android < 4
			new HookConfig(false, "CRYPTO", "GENERAL CRYPTO", "javax.crypto.Cipher", "doFinal", 
				new Intro_CRYPTO_FINAL(), new Class<?>[]{byte[].class, Integer.TYPE},
				"Finishes a multi-part transformation (encryption or decryption)"),
			
			new HookConfig(true, "CRYPTO", "GENERAL CRYPTO", "javax.crypto.Cipher", "doFinal", 
				new Intro_CRYPTO_FINAL(), new Class<?>[]{byte[].class, Integer.TYPE, Integer.TYPE},
				"Finishes a multi-part transformation (encryption or decryption)"),
				
			new HookConfig(true, "CRYPTO", "GENERAL CRYPTO", "javax.crypto.Cipher", "doFinal", 
				new Intro_CRYPTO_FINAL(), new Class<?>[]{byte[].class, Integer.TYPE, Integer.TYPE, 
															byte[].class, Integer.TYPE},
						"Finishes a multi-part transformation (encryption or decryption)"),
			
			new HookConfig(true, "CRYPTO", "GENERAL CRYPTO", "javax.crypto.Cipher", "doFinal", 
				new Intro_CRYPTO_FINAL(), new Class<?>[]{byte[].class, Integer.TYPE, 
																	Integer.TYPE, byte[].class},
								"Finishes a multi-part transformation (encryption or decryption)"),				
			
			new HookConfig(true, "CRYPTO", "GENERAL CRYPTO", "javax.crypto.Cipher", "doFinal", 
				new Intro_CRYPTO_FINAL(), new Class<?>[]{ByteBuffer.class, ByteBuffer.class},
				"Finishes a multi-part transformation (encryption or decryption)"),
				
			new HookConfig(true, "CRYPTO", "GENERAL CRYPTO", "javax.crypto.Cipher", "doFinal", 
				new Intro_CRYPTO_FINAL(), new Class<?>[]{byte[].class},
				"Finishes a multi-part transformation (encryption or decryption)"),
				
			// ########### Key related functions
	
			new HookConfig(true, "CRYPTO", "KEY", "javax.crypto.spec.PBEKeySpec", "PBEKeySpec", 
					new Intro_CRYPTO_PBEKEY(), new Class<?>[]{char[].class, byte[].class, Integer.TYPE, Integer.TYPE}, 
					"Create a key with: pwd, salt, iterations, keylength"),
					
			new HookConfig(true, "CRYPTO", "KEY", "javax.crypto.spec.PBEKeySpec", "PBEKeySpec", 
					new Intro_CRYPTO_PBEKEY(), new Class<?>[]{char[].class, byte[].class, Integer.TYPE}, 
					"Create a key with: pwd, salt, iterations"),

			new HookConfig(true, "CRYPTO", "KEY", "javax.crypto.spec.PBEKeySpec", "PBEKeySpec", 
					new Intro_CRYPTO_PBEKEY(), new Class<?>[]{char[].class}, 
					"Create a key with: pwd"),
				
			new HookConfig(true, "CRYPTO", "KEY", "javax.crypto.spec.SecretKeySpec", "SecretKeySpec", 
					new Intro_GET_KEY(), new Class<?>[]{byte[].class, String.class}, ""),
					
			// keystore - get the cert and the .p12 passcode
			new HookConfig(true, "CRYPTO", "KEY", "org.apache.http.conn.ssl.SSLSocketFactory", "SSLSocketFactory", 
				new Intro_CRYPTO_KEYSTORE_HOSTNAME(), new Class<?>[]{String.class, KeyStore.class, String.class, KeyStore.class,
															SecureRandom.class, HostNameResolver.class}, ""),
			new HookConfig(true, "CRYPTO", "KEY", "org.apache.http.conn.ssl.SSLSocketFactory", "SSLSocketFactory", 
				new Intro_CRYPTO_KEYSTORE(), new Class<?>[]{KeyStore.class, String.class, KeyStore.class}, ""),
			new HookConfig(true, "CRYPTO", "KEY", "org.apache.http.conn.ssl.SSLSocketFactory", "SSLSocketFactory", 
				new Intro_CRYPTO_KEYSTORE(), new Class<?>[]{KeyStore.class, String.class}, ""),
			//new HookConfig(true, "CRYPTO", "GENERAL CRYPTO", "org.apache.http.conn.ssl.SSLSocketFactory", "SSLSocketFactory", 
			//		new Intro_CRYPTO_KEYSTORE(), new Class<?>[]{KeyStore.class}, ""),
					
			// ############ digest (hash function)
				
			// may not need to call the digest methods when update is hooked (?)
			new HookConfig(false, "CRYPTO", "HASH", "java.security.MessageDigest", "digest", 
					new Intro_HASH(), new Class<?>[]{byte[].class, Integer.TYPE, Integer.TYPE},
					"Performs the final update and then computes and returns the final hash value"),
			new HookConfig(false, "CRYPTO", "HASH", "java.security.MessageDigest", "digest", 
					new Intro_HASH(), new Class<?>[]{byte[].class},
					"Performs the final update and then computes and returns the final hash value"),
			new HookConfig(true, "CRYPTO", "HASH", "java.security.MessageDigest", "update", 
					new Intro_HASH(), new Class<?>[]{byte[].class},
					"Uses a one-way hash function to turn an arbitrary number of " +
					"bytes into a fixed-length byte sequence."),	
			new HookConfig(true, "CRYPTO", "HASH", "java.security.MessageDigest", "update", 
					new Intro_HASH(), new Class<?>[]{byte[].class, Integer.TYPE, Integer.TYPE},
					"Uses a one-way hash function to turn an arbitrary number of " +
					"bytes into a fixed-length byte sequence."),
			new HookConfig(true, "CRYPTO", "HASH", "java.security.MessageDigest", "update", 
					new Intro_HASH(), new Class<?>[]{ByteBuffer.class},
					"Uses a one-way hash function to turn an arbitrary number of " +
					"bytes into a fixed-length byte sequence."),
					
			// ############ init
			new HookConfig(true, "CRYPTO", "GENERAL CRYPTO", "javax.crypto.Cipher", "init", 
				new Intro_CRYPTO_INIT(), new Class<?>[]{Integer.TYPE, Key.class},
				"Initializes this cipher instance with the specified key"),
			new HookConfig(true, "CRYPTO", "GENERAL CRYPTO", "javax.crypto.Cipher", "init", 
				new Intro_CRYPTO_INIT(), new Class<?>[]{Integer.TYPE, Key.class, SecureRandom.class},
				"Initializes this cipher instance with the specified key"),
			new HookConfig(true, "CRYPTO", "GENERAL CRYPTO", "javax.crypto.Cipher", "init", 
				new Intro_CRYPTO_INIT(), new Class<?>[]{Integer.TYPE, Key.class, AlgorithmParameterSpec.class, SecureRandom.class},
				"Initializes this cipher instance with the specified key"),	
			new HookConfig(true, "CRYPTO", "GENERAL CRYPTO", "javax.crypto.Cipher", "init", 
				new Intro_CRYPTO_INIT(), new Class<?>[]{Integer.TYPE, Key.class, AlgorithmParameters.class, SecureRandom.class},
				"Initializes this cipher instance with the specified key"),
			new HookConfig(true, "CRYPTO", "GENERAL CRYPTO", "javax.crypto.Cipher", "init", 
				new Intro_CRYPTO_INIT(), new Class<?>[]{Integer.TYPE, Key.class, AlgorithmParameters.class},
				"Initializes this cipher instance with the specified key"),	
			new HookConfig(true, "CRYPTO", "GENERAL CRYPTO", "javax.crypto.Cipher", "init", 
				new Intro_CRYPTO_INIT(), new Class<?>[]{Integer.TYPE, Key.class, AlgorithmParameterSpec.class},
				"Initializes this cipher instance with the specified key"),	
			
			// TODO: 3 init calls missing (with certificates)
				
		/*  ############################################
		 *  ############### File System Methods
		 */ 
			new HookConfig(true, "STORAGE", "FS", "java.io.FileOutputStream", "FileOutputStream", 
					new Intro_FILE_CHECK_DIR(), new Class<?>[]{File.class}, ""),	
			// crashes
			new HookConfig(false, "STORAGE", "FS", "java.io.File", "File", 
					new Intro_FILE_CHECK_DIR(), new Class<?>[]{String.class}, ""),	
			// does not always work when other FS APIs are hooked (crashes)
			new HookConfig(false, "STORAGE", "FS", "java.io.File", "File", 
					new Intro_FILE_CHECK_DIR(), new Class<?>[]{String.class, String.class}, ""),
			// URI
			new HookConfig(true, "STORAGE", "FS", "java.io.File", "File", 
					new Intro_DUMP(), new Class<?>[]{URI.class}, ""),

			// bad: true, false: (boolean readable, boolean ownerOnly)
			new HookConfig(true, "STORAGE", "FS", "java.io.File", "setReadable", 
					new Intro_CHECK_FS_SET(), new Class<?>[]{Boolean.TYPE, Boolean.TYPE}, 
					"Manipulates the read permissions for the abstract path designated by this file."),	
			new HookConfig(true, "STORAGE", "FS", "java.io.File", "setWritable", 
					new Intro_CHECK_FS_SET(), new Class<?>[]{Boolean.TYPE, Boolean.TYPE},
					"Manipulates the read permissions for the abstract path designated by this file."),
			new HookConfig(true, "STORAGE", "FS", "java.io.File", "setExecutable", 
					new Intro_CHECK_FS_SET(), new Class<?>[]{Boolean.TYPE, Boolean.TYPE},
					"Manipulates the read permissions for the abstract path designated by this file."),
							
			// secu, world/readable
			new HookConfig(true, "STORAGE", "FS", "android.content.ContextWrapper", "openFileOutput", 
					new Intro_FILE_CHECK_MODE(), new Class<?>[]{String.class, Integer.TYPE}, ""),
					
		/*  ############################################
		 *  ############### IPC Methods
		 */ 					
			
			new HookConfig(true, "IPC", "IPC", "android.content.ContextWrapper", "startService", 
					new Intro_DUMP_INTENT(), new Class<?>[]{Intent.class}, ""),
			new HookConfig(false, "IPC", "IPC", "android.content.ContextWrapper", "startActivities", 
					new Intro_DUMP_INTENT(), new Class<?>[]{Intent[].class}, ""),
				
			// Android > 4.1
			new HookConfig(false, "IPC", "IPC", "android.content.ContextWrapper", "startActivity", 
					new Intro_DUMP_INTENT(), new Class<?>[]{Intent.class, Bundle.class}, ""),

			new HookConfig(true, "IPC", "IPC", "android.content.ContextWrapper", "startActivity", 
					new Intro_DUMP_INTENT(), new Class<?>[]{Intent.class, Bundle.class}, ""),
			new HookConfig(true, "IPC", "IPC", "android.content.ContextWrapper", "startActivity", 
					new Intro_DUMP_INTENT(), new Class<?>[]{Intent.class}, ""),	
					
			new HookConfig(true, "IPC", "IPC", "android.content.ContextWrapper", "startActivity", 
					new Intro_DUMP_INTENT(), new Class<?>[]{Intent.class}, ""),		

			new HookConfig(true, "IPC", "IPC", "android.content.ContextWrapper", "sendBroadcast", 
					new Intro_DUMP_INTENT(), new Class<?>[]{Intent.class}, ""),
			new HookConfig(true, "IPC", "IPC", "android.content.ContextWrapper", "sendBroadcast", 
					new Intro_DUMP(), new Class<?>[]{Intent.class, String.class}, ""),
					
			new HookConfig(true, "IPC", "IPC", "android.content.ContextWrapper", "registerReceiver", 
					new Intro_IPC_RECEIVER(), new Class<?>[]{BroadcastReceiver.class, IntentFilter.class}, ""),
			new HookConfig(true, "IPC", "IPC", "android.content.ContextWrapper", "registerReceiver", 
					new Intro_IPC_RECEIVER(), new Class<?>[]{BroadcastReceiver.class, IntentFilter.class, 
															String.class, Handler.class}, ""),
			// TODO: more IPCs to hook here, sendBroadcast(s) for instance
															
															
			// useful to find dynamically enabled IPCs
			// crashes the zygote process on 4.2.2 (?)
			new HookConfig(false, "IPC", "IPC", "android.content.pm.PackageManager", "setComponentEnabledSetting", 
					new Intro_IPC_MODIFIED(), new Class<?>[]{ComponentName.class, Integer.TYPE, Integer.TYPE}, ""),
					
		/*  ############################################
		 *  ############### Shared Preference Methods
		 */ 
			new HookConfig(true, "STORAGE", "PREF", "android.content.ContextWrapper", "getSharedPreferences", 
					new Intro_CHECK_SHARED_PREF(), new Class<?>[]{String.class, Integer.TYPE},
					"Used to get shared preferences"),
				
			// Get shared preference methods
			new HookConfig(true, "STORAGE", "PREF", "android.app.SharedPreferencesImpl", "getString", 
					new Intro_GET_SHARED_PREF(), new Class<?>[]{String.class, String.class},
					"Checks whether the preferences contains a preference"),
			new HookConfig(true, "STORAGE", "PREF", "android.app.SharedPreferencesImpl", "getStringSet", 
					new Intro_GET_SHARED_PREF(), new Class<?>[]{String.class, Set.class},
					"Checks whether the preferences contains a preference"),
			new HookConfig(true, "STORAGE", "PREF", "android.app.SharedPreferencesImpl", "getAll", 
					new Intro_GET_ALL_SHARED_PREF(), new Class<?>[]{},
					"Checks whether the preferences contains a preference"),
			new HookConfig(true, "STORAGE", "PREF", "android.app.SharedPreferencesImpl", "getBoolean", 
					new Intro_GET_SHARED_PREF(), new Class<?>[]{String.class, Boolean.TYPE},
					"Checks whether the preferences contains a preference"),
			new HookConfig(true, "STORAGE", "PREF", "android.app.SharedPreferencesImpl", "getFloat", 
					new Intro_GET_SHARED_PREF(), new Class<?>[]{String.class, Float.TYPE},
					"Checks whether the preferences contains a preference"),
			new HookConfig(true, "STORAGE", "PREF", "android.app.SharedPreferencesImpl", "getInt", 
					new Intro_GET_SHARED_PREF(), new Class<?>[]{String.class, Integer.TYPE},
					"Checks whether the preferences contains a preference"),
			new HookConfig(true, "STORAGE", "PREF", "android.app.SharedPreferencesImpl", "getLong", 
					new Intro_GET_SHARED_PREF(), new Class<?>[]{String.class, Long.TYPE},
					"Checks whether the preferences contains a preference"),
			new HookConfig(true, "STORAGE", "PREF", "android.app.SharedPreferencesImpl", "contains", 
					new Intro_CONTAINS_SHARED_PREF(), new Class<?>[]{String.class},
					"Checks whether the preferences contains a preference"),	
					
			new HookConfig(true, "STORAGE", "PREF", "android.content.SharedPreferences.Editor", "putString", 
					new Intro_PUT_SHARED_PREF(), new Class<?>[]{String.class, String.class},
					"Set a String value in the preferences editor, to be written back once"),	
			new HookConfig(true, "STORAGE", "PREF", "android.content.SharedPreferences.Editor", "putBoolean", 
					new Intro_PUT_SHARED_PREF(), new Class<?>[]{String.class, Boolean.TYPE},
					"Set a bool value in the preferences editor, to be written back once"),
			new HookConfig(true, "STORAGE", "PREF", "android.content.SharedPreferences.Editor", "putInt", 
					new Intro_PUT_SHARED_PREF(), new Class<?>[]{String.class, Integer.TYPE},
					"Set an Int value in the preferences editor, to be written back once"),	
					
			new HookConfig(false, "STORAGE", "PREF", "android.content.SharedPreferences.Editor", "commit", 
					new Intro_DUMP(), new Class<?>[]{}, 
					"Used to commit shared preferences"),
					
		/*  ############################################
		 *  ############### URI Methods
		 */ 
			new HookConfig(true, "IPC", "URI", "android.content.ContextWrapper", "grantUriPermission", 
					new Intro_DUMP(), new Class<?>[]{String.class, Uri.class, Integer.TYPE}, 
					"Grant permission to access a specific Uri to another package"),
			
			// used to register URI for a content provider
			new HookConfig(false, "IPC", "URI", "android.content.UriMatcher", "addURI", 
					new Intro_URI_REGISTER(), new Class<?>[]{String.class, String.class, Integer.TYPE}, 
					"Add a URI to match, and the code to return when this URI is matched."),			
			
		/*  ############################################
		 *  ############### SSL Methods
		 */ 
			new HookConfig(true, "CRYPTO", "SSL", "org.apache.http.conn.ssl.SSLSocketFactory", "connectSocket", 
					new Intro_DUMP(), new Class<?>[]{Socket.class, String.class, Integer.TYPE, 
										InetAddress.class, Integer.TYPE, HttpParams.class},
										"Connects a socket to the given host (uses SSL)"),
					
			new HookConfig(true, "CRYPTO", "SSL", "org.apache.http.conn.ssl.SocketFactory", "connectSocket", 
					new Intro_DUMP(), new Class<?>[]{Socket.class, String.class, Integer.TYPE, 
										InetAddress.class, Integer.TYPE, HttpParams.class},
										"Connects a socket to the given host"),

			new HookConfig(true, "CRYPTO", "SSL", "org.apache.http.client.methods.HttpGet", "HttpGet", 
					new Intro_CHECK_URI(), new Class<?>[]{String.class}, "HTTP GET"),
			//new HookConfig(false, "CRYPTO", "SSL", "org.apache.http.client.methods.HttpGet", "HttpGet", 
			//		new Intro_DUMP(), new Class<?>[]{URI.class}, "HTTP GET"),
			//new HookConfig(false, "CRYPTO", "SSL", "org.apache.http.client.methods.HttpPost", "HttpPost", 
			//		new Intro_DUMP(), new Class<?>[]{URI.class}, "HTTP POST"),
			new HookConfig(true, "CRYPTO", "SSL", "org.apache.http.client.methods.HttpPost", "HttpPost", 
					new Intro_CHECK_URI(), new Class<?>[]{String.class}, "HTTP POST"),

			// Hooks methods used to do cert pinning or remove cert validation
			new HookConfig(true, "CRYPTO", "SSL", "javax.net.ssl.SSLContext", "init", 
					new Intro_SSL_CHECK_TRUST_MANAGER(), new Class<?>[]{KeyManager[].class, 
											TrustManager[].class, SecureRandom.class}, ""),		
	
			new HookConfig(true, "CRYPTO", "SSL", "javax.net.ssl.HttpsURLConnection", "setSSLSocketFactory", 
					new Intro_SSL_CHECK_TRUST_SOCKETFACTORY(), new Class<?>[]{javax.net.ssl.SSLSocketFactory.class}, ""),
					
			// these methods can be used to allow all hostnames to be used
			new HookConfig(true, "CRYPTO", "SSL", "org.apache.http.conn.ssl.SSLSocketFactory", "setHostnameVerifier", 
					new Intro_CHECK_HOSTNAME_VERIFIER(), new Class<?>[]{X509HostnameVerifier.class}, ""),
			
			new HookConfig(true, "CRYPTO", "SSL", "javax.net.ssl.HttpsURLConnection", "setDefaultHostnameVerifier", 
					new Intro_CHECK_HOSTNAME_VERIFIER(), new Class<?>[]{HostnameVerifier.class}, ""),			
					
		/*  ############################################
		 *  ############### WEBVIEW Methods
		 */ 
			new HookConfig(true, "MISC", "WEBVIEW", "android.webkit.WebSettings", "setJavaScriptEnabled", 
					new Intro_WEBVIEW_SET(), new Class<?>[]{Boolean.TYPE}, 
					"Tells the WebView to enable JavaScript execution"),
			// deprecated
			new HookConfig(false, "MISC", "WEBVIEW", "android.webkit.WebSettings", "setPluginState", 
					new Intro_WEBVIEW_SET(), new Class<?>[]{Boolean.TYPE}, 
					"Tells the WebView to enable Plugin execution (deprecated in API 18)"),
			new HookConfig(true, "MISC", "WEBVIEW", "android.webkit.WebSettings", "setAllowFileAccess", 
					new Intro_WEBVIEW_SET(), new Class<?>[]{Boolean.TYPE}, 
					"Tells the WebView to enable FileSystem access"),
					
			// can lead to RCE if Android <= JELLY_BEAN (v17?)
			new HookConfig(true, "MISC", "WEBVIEW", "android.webkit.WebView", "addJavascriptInterface", 
					new Intro_WEBVIEW_JS_BRIDGE(), new Class<?>[]{Object.class, String.class}, 
					"Injects the supplied Java object into this WebView."),
			//  This is a powerful feature, but also presents a security risk for applications 
			//		targeted to API level JELLY_BEAN or below, because JavaScript could use 
			//		reflection to access an injected object's public fields.
					
		/*  ############################################
		 *  ############### SQLite Methods
		 */ 
			// prone to SQLi
			new HookConfig(true, "STORAGE", "SQLite", "android.database.sqlite.SQLiteDatabase", "execSQL", 
					new Intro_ExecSQL(), new Class<?>[]{String.class}, 
					"Execute a single SQL statement that is NOT a SELECT or any other SQL statement that returns data."),
			new HookConfig(true, "STORAGE", "SQLite", "android.database.sqlite.SQLiteDatabase", "execSQL", 
					new Intro_ExecSQL(), new Class<?>[]{String.class, Object[].class}, 
					"Execute a single SQL statement that is NOT a SELECT or any other SQL statement that returns data."),
		
			new HookConfig(true, "STORAGE", "SQLite", "android.database.sqlite.SQLiteDatabase", "update", 
					new Intro_SQLite_UPDATE(), new Class<?>[]{String.class, ContentValues.class, String.class, String[].class}, 
					"Convenience method for updating rows in the database."),
			new HookConfig(true, "STORAGE", "SQLite", "android.database.sqlite.SQLiteDatabase", "updateWithOnConflict", 
					new Intro_SQLite_UPDATE(), new Class<?>[]{String.class, ContentValues.class, String.class, String[].class, Integer.TYPE}, 
					"Convenience method for updating rows in the database."),

			new HookConfig(true, "STORAGE", "SQLite", "android.database.sqlite.SQLiteDatabase", "insert", 
					new Intro_SQLite_INSERT(), new Class<?>[]{String.class, String.class, ContentValues.class}, 
					"Convenience method for inserting a row into the database."),
			new HookConfig(true, "STORAGE", "SQLite", "android.database.sqlite.SQLiteDatabase", "insertOrThrow", 
					new Intro_SQLite_INSERT(), new Class<?>[]{String.class, String.class, ContentValues.class}, 
					"Convenience method for inserting a row into the database."),
			new HookConfig(true, "STORAGE", "SQLite", "android.database.sqlite.SQLiteDatabase", "insertWithOnConflict", 
					new Intro_SQLite_INSERT(), new Class<?>[]{String.class, String.class, ContentValues.class, Integer.TYPE}, 
					"Convenience method for inserting a row into the database."),
					
			new HookConfig(true, "STORAGE", "SQLite", "android.database.sqlite.SQLiteDatabase", "replace", 
					new Intro_SQLite_INSERT(), new Class<?>[]{String.class, String.class, ContentValues.class}, 
					"Convenience method for inserting a row into the database."),
			new HookConfig(true, "STORAGE", "SQLite", "android.database.sqlite.SQLiteDatabase", "replace", 
					new Intro_SQLite_INSERT(), new Class<?>[]{String.class, String.class, ContentValues.class}, 
					"Convenience method for inserting a row into the database."),
	};
}
