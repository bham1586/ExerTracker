package com.bhjy.ExerTracker.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDatabaseHelper extends SQLiteOpenHelper {

	public static final String TABLE_EXERCISES = "exercises";
	public static final String EXERCISES_ID = "_id";
	public static final String EXERCISES_NAME = "name";
	public static final String EXERCISES_DESCRIPTION = "description";

	private static final String DATABASE_NAME = "ExerTracker.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String TABLE_EXERCISES_CREATE = "create table "
			+ TABLE_EXERCISES + "( " 
			+ EXERCISES_ID + " integer primary key autoincrement, " 
			+ EXERCISES_NAME + " text not null, "
			+ EXERCISES_DESCRIPTION + " text);";
	
	public MyDatabaseHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_EXERCISES_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MyDatabaseHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISES);
		onCreate(db);
	}

}
