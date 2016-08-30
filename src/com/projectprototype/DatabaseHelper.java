package com.projectprototype;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper{
	// database version
	private static final int database_VERSION = 1;
	// database name
	private static final String database_NAME = "LeaveDB";
	private static final String table_LEAVES = "Leaves";
	private static final String leave_ID = "id";
	private static final String leave_NAME = "name";
	private static final String leave_DATE = "date";
	private static final String leave_TYPE = "type";
	private static final String leave_MONTHYEAR = "MMYYYY";
	
	public DatabaseHelper(Context context) {
		super(context, database_NAME, null, database_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String CREATE_BOOK_TABLE = "CREATE TABLE Leaves ( " + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "name TEXT, " + "date TEXT, " + "type TEXT, " + "MMYYYY TEXT )";
		db.execSQL(CREATE_BOOK_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS Leaves");
		this.onCreate(db);
	}
	
	public boolean createLog(String name, String date, String type) {
		// get reference of the BookDB database
		
		String[] dateArr = date.split("-");
		String monthyear = dateArr[0] + dateArr[2];
		//date = dateArr[0] + "-" + dateArr[1] + "-" + dateArr[2];
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		// make values to be inserted
		ContentValues values = new ContentValues();
		values.put(leave_NAME, name);
		values.put(leave_DATE, date);
		values.put(leave_TYPE, type);		
		values.put(leave_MONTHYEAR, monthyear);

		// insert book
		long result = db.insert(table_LEAVES, null, values);
		
		// close database transaction
		db.close();
		
		if (result == -1){
			return false;
		}
		else{
			return true;
		}		
	}
	
	public void deleteAll() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(table_LEAVES,null,null);
		db.close();
	}
	
	public List<String> getAllInDate(String date) {
		List<String> output = new ArrayList<String>();
		Integer id;
		String name;
		String type;
		
		date = date.replace("/","-");
		
		//Log.i("myApp", date);
		// select book query
		//String query = "SELECT  * FROM " + table_LEAVES;
		String query = "SELECT  * FROM Leaves WHERE date = '" + date + "'";

		// get reference of the BookDB database
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		// parse all results
		if (cursor.moveToFirst()) {
			do {
				id = Integer.parseInt(cursor.getString(0));
				name = cursor.getString(1);
				type = cursor.getString(3);

				// Add book to books
				//Log.i("myApp", Integer.toString(id));
				//Log.i("myApp", name + " ( " + type + " ) ");
				output.add(name + " ( " + type + " ) ");
				//Log.i("myApp", cursor.getString(2));
				//Log.i("myapp", output.get(id-1));
			} while (cursor.moveToNext());
		}
		return output;
	}
	
	public boolean dateHit(String day, String monthyear){
		String date;
		String query = "SELECT  * FROM Leaves WHERE MMYYYY = '" + monthyear + "'";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		// parse all results
		if (cursor.moveToFirst()) {
			do {
				date = cursor.getString(2);
				String[] dateArr = date.split("-");

				if (day.equals(dateArr[1])){
					return true;
				}
			} while (cursor.moveToNext());
		}
		return false;
	}
}
