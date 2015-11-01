package com.introspy.logging;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteIntrospyHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "introspy.db";
	// increment this number each time the DB structure changes
    private static final int DATABASE_VERSION = 8;
    
    public static final String TABLE_TRACES = "tracedCalls";
    
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_SUBTYPE = "subType";
    public static final String COLUMN_CLASS = "className";
    public static final String COLUMN_METHOD = "methodName";
    public static final String COLUMN_DETAILS = 
    							"argumentsAndReturnValueDict";
    public static final String COLUMN_LOG_TYPE = "logType";
    public static final String COLUMN_NOTES = "notes";
    public static final String COLUMN_ST = "callStack";
    
    private static final String DATABASE_CREATE = 
		"create table "
	      + TABLE_TRACES + "(" + COLUMN_ID
		      + " integer primary key autoincrement, " + 
		      COLUMN_TYPE 		+ " text not null, " +
		      COLUMN_SUBTYPE 	+ " text not null, " +
		      COLUMN_CLASS 		+ " text not null, " +
		      COLUMN_METHOD 	+ " text not null, " +
		      COLUMN_DETAILS 	+ " text not null, " +
		      COLUMN_LOG_TYPE 	+ " text not null, " +
		      COLUMN_NOTES 		+ " text not null, " +
		      COLUMN_ST 		+ " text not null " +
	      ");";
    
    SQLiteIntrospyHelper(Context context) {
        super(context, 
        		DATABASE_NAME, null, DATABASE_VERSION);    	
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }
    
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRACES);
	    onCreate(db);
	}
}
