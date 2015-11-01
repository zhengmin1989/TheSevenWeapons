
package com.introspy.logging;

import java.util.ArrayList;
import java.util.List;

import com.introspy.core.ApplicationConfig;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteIntrospy {
	private static SQLiteIntrospy _instance = null;
	// Database fields
	  private SQLiteDatabase database;
	  private SQLiteIntrospyHelper dbHelper;
	  private String[] allColumns = { 
			  SQLiteIntrospyHelper.COLUMN_ID,
			  SQLiteIntrospyHelper.COLUMN_TYPE,
			  SQLiteIntrospyHelper.COLUMN_SUBTYPE,
			  SQLiteIntrospyHelper.COLUMN_CLASS,
			  SQLiteIntrospyHelper.COLUMN_METHOD,
			  SQLiteIntrospyHelper.COLUMN_DETAILS,
			  SQLiteIntrospyHelper.COLUMN_LOG_TYPE,
			  SQLiteIntrospyHelper.COLUMN_NOTES,
			  SQLiteIntrospyHelper.COLUMN_ST
			  };
	
	  public SQLiteIntrospy(Context context) {
	    dbHelper = new SQLiteIntrospyHelper(context);
	  }
	
	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }
	
	  public void close() {
	    dbHelper.close();
	  }
	
	  public SQLiteIntrospyLog createRow(String type, String subType,
			  String className, String methodName, 
			  String details, String logType) {
		  return createRow(type, subType, className, 
				  methodName, details, logType, "", "");
	  }
	  
	  public SQLiteIntrospyLog createRow(String type, String subType,
			  String className, String methodName, 
			  String details, String logType, String notes) {
		  return createRow(type, subType, className, 
				  methodName, details, logType, notes, "");
	  }
	  
	  public SQLiteIntrospyLog createRow(String type, String subType,
			  String className, String methodName, String details,
			  String logType, String notes, String st) {
	    ContentValues values = new ContentValues();
	    values.put(SQLiteIntrospyHelper.COLUMN_TYPE, type);
	    values.put(SQLiteIntrospyHelper.COLUMN_SUBTYPE, subType); 
	    values.put(SQLiteIntrospyHelper.COLUMN_CLASS, className);
	    values.put(SQLiteIntrospyHelper.COLUMN_METHOD, methodName);
	    values.put(SQLiteIntrospyHelper.COLUMN_DETAILS, details);
	    values.put(SQLiteIntrospyHelper.COLUMN_LOG_TYPE, logType);
	    values.put(SQLiteIntrospyHelper.COLUMN_NOTES, notes);
	    values.put(SQLiteIntrospyHelper.COLUMN_ST, st);
	    
	    long insertId = database.insert(
	    		SQLiteIntrospyHelper.TABLE_TRACES, 
	    		null, values);
	    
	    Cursor cursor = database.query(
	    		SQLiteIntrospyHelper.TABLE_TRACES,
	    		allColumns, SQLiteIntrospyHelper.COLUMN_ID + 
	    		" = " + insertId, null,
	    		null, null, null);
	    
	    cursor.moveToFirst();
	    SQLiteIntrospyLog 
	    	newRow = cursorToRow(cursor);
	    cursor.close();
	    return newRow;
	  }
	
	  private SQLiteIntrospyLog cursorToRow(Cursor cursor) {
		  SQLiteIntrospyLog Row = 
				  new SQLiteIntrospyLog();
	    Row.setId(cursor.getLong(0));
	    Row.setRow(cursor.getString(1));
	    return Row;
	}
	
	public void deleteRow(SQLiteIntrospyLog Row) {
	    long id = Row.getId();
	    System.out.println("Row deleted with id: " + id);
	    database.delete(SQLiteIntrospyHelper.TABLE_TRACES, 
	    		SQLiteIntrospyHelper.COLUMN_ID
	    		+ " = " + id, null);
	  }
	
	  public List<SQLiteIntrospyLog> getAllRows() {
		    List<SQLiteIntrospyLog> Rows = 
		    		new ArrayList<SQLiteIntrospyLog>();
	
		    Cursor cursor = database.query(
		    	SQLiteIntrospyHelper.TABLE_TRACES,
		        allColumns, null, null, null, null, null);
	
		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		    	SQLiteIntrospyLog Row = cursorToRow(cursor);
		      Rows.add(Row);
		      cursor.moveToNext();
		    }
		    // make sure to close the cursor
		    cursor.close();
		    return Rows;
		  }
	
	public static SQLiteIntrospy getInstance() {
		if (_instance  == null) {
			_instance = new 
					SQLiteIntrospy(ApplicationConfig.getContext());
		}
		return _instance;
	}
}
