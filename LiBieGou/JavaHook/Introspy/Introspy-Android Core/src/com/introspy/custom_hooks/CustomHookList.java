package com.introspy.custom_hooks;

import android.content.Intent;

import com.introspy.core.HookConfig;

public class CustomHookList {
	
	static public HookConfig[] getHookList() {
		return _hookList;
	}
	
	static private HookConfig[] _hookList = new HookConfig[] {	
		  
		new HookConfig(true, // set to true to enable the hook
		"java.lang.String", "equals", new Class<?>[]{Object.class},
			// class, 						   method name, 				 arguments
		new HookStrings(), 
			// instance of the implementation extending IntroHook (here in HookExampleInpl.java)
		"String Hook"),
		
		/*		new HookConfig(false, // set to true to enable the hook
		"android.content.ContextWrapper", "startService", new Class<?>[]{Intent.class},
		// class, 						   method name, 				 arguments
		new HookExampleImpl(), 
		// instance of the implementation extending IntroHook (here in HookExampleInpl.java)
		"StartService with an intent as argument"),
*/			// notes (optional)		
/*		new HookConfig(true, // set to true to enable the hook
			    "java.lang.StringBuilder", "toString", new Class<?>[]{},
				// class, 						   method name, 				 arguments
				new StringInit(), 
				// instance of the implementation extending IntroHook (here in HookExampleInpl.java)
				"String Hook"),			
*/			
//		    new HookConfig(true, // set to true to enable the hook
//		    "java.lang.StringBuilder", "append", new Class<?>[]{Object.class},
					// class, 						   method name, 				 arguments
//			new StringInit(), 
					// instance of the implementation extending IntroHook (here in HookExampleInpl.java)
//			"StringBuilder Hook"),

			// notes (optional)					
/*	    new HookConfig(true, // set to true to enable the hook
			    "java.lang.String", "contentEquals", new Class<?>[]{java.lang.StringBuffer.class},
				// class, 						   method name, 				 arguments
				new HookStrings(), 
				// instance of the implementation extending IntroHook (here in HookExampleInpl.java)
				"String Hook"),			
	    new HookConfig(true, // set to true to enable the hook
			    "java.lang.String", "matches", new Class<?>[]{java.lang.String.class},
				// class, 						   method name, 				 arguments
				new HookStrings(), 
				// instance of the implementation extending IntroHook (here in HookExampleInpl.java)
				"String Hook"),			
        new HookConfig(true, // set to true to enable the hook
		    "java.lang.String", "endsWith", new Class<?>[]{java.lang.String.class},
			// class, 						   method name, 				 arguments
			new HookStrings(), 
			// instance of the implementation extending IntroHook (here in HookExampleInpl.java)
			"String Hook"),	
		new HookConfig(true, // set to true to enable the hook
				    "java.lang.String", "startsWith", new Class<?>[]{java.lang.String.class},
					// class, 						   method name, 				 arguments
					new HookStrings(), 
					// instance of the implementation extending IntroHook (here in HookExampleInpl.java)
					"String Hook"),	
		new HookConfig(true, // set to true to enable the hook
			    "java.lang.StringBuilder", "toString", new Class<?>[]{java.lang.String.class},
				// class, 						   method name, 				 arguments
				new HookStrings(), 
				// instance of the implementation extending IntroHook (here in HookExampleInpl.java)
				"String Hook"),	
*/
//        new HookConfig(true, // set to true to enable the hook
//					    "java.lang.String", "<init>", new Class<?>[]{byte[].class},
//						// class, 						   method name, 				 arguments
//						new StringInit(), 
//						// instance of the implementation extending IntroHook (here in HookExampleInpl.java)
//						"String Hook"),			
//        new HookConfig(true, // set to true to enable the hook
//			    "android.util.Base64", "decode", new Class<?>[]{java.lang.String.class,Integer.TYPE},
				// class, 						   method name, 				 arguments
//				new HookStrings(), 
				// instance of the implementation extending IntroHook (here in HookExampleInpl.java)
//				"Base64 Hook"),					
//		        new HookConfig(true, // set to true to enable the hook
//			    "android.util.Base64", "decode", new Class<?>[]{byte[].class,Integer.TYPE},
				// class, 						   method name, 				 arguments
//				new StringInit(), 
				// instance of the implementation extending IntroHook (here in HookExampleInpl.java)
 //               "Base64 Hook"),	

	};
}

