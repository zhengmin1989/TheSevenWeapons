package com.introspy.core;

import android.content.Context;
 
public class ApplicationConfig {
	
   private static String 	_packageName = null;
   private static String 	_dataDir = null;
   private static LoadConfig _loadConfig = null;
   private static Boolean 	_enabled = false;
   private static Context 	_context = null;
   
   public static boolean g_verbose_errors = false;
   public static boolean g_debug = false;
   public static boolean g_hook_em_all = true;

   // Private constructor prevents instantiation from other classes
   private ApplicationConfig() {}

   // get
   public static String getPackageName() {
      return _packageName;
   }
   public static String getDataDir() {
	  return _dataDir;
   }
   public static LoadConfig getLoadConfig() {
	   if (_loadConfig == null)
		   _loadConfig = new LoadConfig();
		  return _loadConfig;
   }
   public static Context getContext() {
		return _context;
   }
   
   // set
   public static void setPackageName(String packageName) {
	   _packageName = packageName;
   }
   public static void setDataDir(String dataDir) {
	   _dataDir = dataDir;
   }
   public static void setContext(Context context) {
	   _context = context;
   }
   
   // ####
   public static void disable() {
	   _enabled = false;
   }
   public static void enable() {
	   _enabled = true;
   }
   
   public static boolean isEnabled() {
		return _enabled;
   }
   
}
