package com.introspy.hooks;
import com.introspy.core.IntroHook;

class Intro_ExecSQL extends IntroHook { 
	public void execute(Object... args) {
//		SQLiteDatabase db = (SQLiteDatabase) _resources;
		_logBasicInfo();
		_logFlush_I("-> [" + args[0] + "]");
	}
}

class Intro_SQLite_UPDATE extends IntroHook { 
	public void execute(Object... args) {
//		SQLiteDatabase db = (SQLiteDatabase) _resources;
		_logBasicInfo();
		
		_logParameter("Table", args[0]);
		_logParameter("Content Values", args[1]);
		_logParameter("Where", args[2]);
		_logParameter("Where Args", args[3]);
		
		_logFlush_I("-> " + "Table: " + args[0] + ", " +
				"ContentValues: " + args[1] + ", " +
				"Where: " + args[2] + ", " +
				"WhereArgs: " + args[3]);
		// TODO: dump 'where' args (array of strings for args[3])
	}
}

class Intro_SQLite_INSERT extends IntroHook { 
	public void execute(Object... args) {
//		SQLiteDatabase db = (SQLiteDatabase) _resources;
		_logBasicInfo();
	
		_logParameter("Table", args[0]);
		_logParameter("Content Values", args[1]);
		
		_logFlush_I("-> " + "Table: " + args[0] + ", " +
						"ContentValues: " + args[2]);
		// TODO: dump this other array of strings
	}
}
