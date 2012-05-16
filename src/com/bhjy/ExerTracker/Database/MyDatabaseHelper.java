package com.bhjy.ExerTracker.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDatabaseHelper extends SQLiteOpenHelper {


	private static final String DATABASE_NAME = "ExerTracker.db";
	private static final int DATABASE_VERSION = 2;

	// Database creation sql statements
	// Exercises table
	public static final String TABLE_EXERCISES = "exercises";
	public static final String EXERCISES_ID = "_id";
	public static final String EXERCISES_NAME = "name";
	public static final String EXERCISES_DESCRIPTION = "description";
	private static final String TABLE_EXERCISES_CREATE = "create table "
			+ TABLE_EXERCISES + "( " 
			+ EXERCISES_ID + " integer primary key autoincrement, " 
			+ EXERCISES_NAME + " text not null, "
			+ EXERCISES_DESCRIPTION + " text);";
	
	// Sets Table
	public static final String TABLE_SETS = "sets";
	public static final String SETS_ID = "_id";
	public static final String SETS_EXERCISE_ID = "exercise_id";
	public static final String SETS_REPS = "reps";
	public static final String SETS_START_TIME = "startTime";
	public static final String SETS_DURATION = "duration";
	public static final String SETS_WEIGHT = "weight";
	public static final String SETS_COMMENTS = "comment";
	private static final String TABLE_SETS_CREATE = "create table "
			+ TABLE_SETS + "( " 
			+ SETS_ID + " integer primary key autoincrement, " 
			+ SETS_EXERCISE_ID + "integer, "
			+ SETS_REPS + "integer, "
			+ SETS_START_TIME + "datetime, "
			+ SETS_DURATION + "integer, "
			+ SETS_WEIGHT + "integer, "
			+ SETS_COMMENTS + "text, " 
			+ "FOREIGN KEY(" + SETS_EXERCISE_ID + ") REFERENCES " + TABLE_EXERCISES + "(" + EXERCISES_ID + "));";
	
	public MyDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.w(MyDatabaseHelper.class.getName(),
				"Creating Database.");
		db.execSQL(TABLE_EXERCISES_CREATE);
		db.execSQL(TABLE_SETS_CREATE);
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
