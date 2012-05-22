package com.bhjy.ExerTracker.Database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.bhjy.ExerTracker.Database.MyDatabaseHelper;
import com.bhjy.ExerTracker.Models.Exercise;
import com.bhjy.ExerTracker.Models.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class SetsDataSource {
	// Database fields
		private SQLiteDatabase database;
		private MyDatabaseHelper dbHelper;
		private String[] allColumns = { MyDatabaseHelper.SETS_ID, MyDatabaseHelper.SETS_EXERCISE_ID,
				MyDatabaseHelper.SETS_REPS, MyDatabaseHelper.SETS_START_TIME, MyDatabaseHelper.SETS_DURATION,
				MyDatabaseHelper.SETS_WEIGHT, MyDatabaseHelper.SETS_COMMENTS};
		private String[] allExerciseColumns = { MyDatabaseHelper.EXERCISES_ID,
				MyDatabaseHelper.EXERCISES_NAME, MyDatabaseHelper.EXERCISES_DESCRIPTION };

		SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		public SetsDataSource(Context context) {
			dbHelper = new MyDatabaseHelper(context);
		}

		public void open() throws SQLException {
			database = dbHelper.getWritableDatabase();
		}

		public void close() {
			dbHelper.close();
		}

		public Set createSet(long exerciseId, long reps, Date startTime, long duration, long weight, String comments) {
			ContentValues values = new ContentValues();
			values.put(MyDatabaseHelper.SETS_EXERCISE_ID, exerciseId);
			values.put(MyDatabaseHelper.SETS_REPS, reps);
			values.put(MyDatabaseHelper.SETS_START_TIME, convertDateToString(startTime) );
			values.put(MyDatabaseHelper.SETS_DURATION, duration);
			values.put(MyDatabaseHelper.SETS_WEIGHT, weight);
			values.put(MyDatabaseHelper.SETS_COMMENTS, comments);
			long insertId = database.insertOrThrow(MyDatabaseHelper.TABLE_SETS, null, values);
			Cursor cursor = database.query(MyDatabaseHelper.TABLE_SETS, allColumns, MyDatabaseHelper.SETS_ID + " = " + insertId, null, null, null, null);
			cursor.moveToFirst();
			Set newSet = cursorToSet(cursor);
			cursor.close();
			return newSet;
		}

		public void deleteSet(Set set) {
			long id = set.getId();
			System.out.println("Set deleted with id: " + id);
			database.delete(MyDatabaseHelper.TABLE_SETS, MyDatabaseHelper.SETS_ID
					+ " = " + id, null);
		}

		public List<Set> getAllSets(long exerciseId) {
			List<Set> sets = new ArrayList<Set>();

			Cursor cursor = database.query(MyDatabaseHelper.TABLE_SETS,
					allColumns, MyDatabaseHelper.SETS_EXERCISE_ID + " == " + String.valueOf(exerciseId), null, null, null, null);
			
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Set set = cursorToSet(cursor);
				sets.add(set);
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();
			return sets;
		}
		
		public List<Set> getTotalRepsByDate(long exerciseId) {
			List<Set> sets = new ArrayList<Set>();

			Cursor cursor = database.rawQuery("Select * from " + MyDatabaseHelper.TABLE_SETS + " where " + MyDatabaseHelper.SETS_EXERCISE_ID + " == " + String.valueOf(exerciseId) + " order by " + MyDatabaseHelper.SETS_START_TIME + " asc", null);
			
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Set set = cursorToSet(cursor);
				sets.add(set);
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();
			List<Set> repsByDay = new ArrayList<Set>();
			Date lastDate = new Date();
        	int repsInDay = 0;
        	int first = 1;
			for(final Set set:sets) {
				if(first > 0) {
					//set lastdate to this date
        			lastDate.setTime(set.getStartTime().getTime());
        			
        			first = 0;
				}
				else if(lastDate.getDate() != set.getStartTime().getDate()) {
        			Set newSet = new Set();
        			newSet.setReps(repsInDay);
        			newSet.setStartTime(lastDate);
        			Log.d("ExerTracker", set.getId() + " = " + lastDate.getTime() + " " + convertDateToString(newSet.getStartTime()));
        			repsByDay.add(newSet);
        			
        			//set lastdate to this date
        			lastDate.setTime(set.getStartTime().getTime());
        			
        			//reset repsInDay
        			repsInDay = 0;
        		}
        		//add reps to current day
        		repsInDay += set.getReps();
        	}

			Set newSet = new Set();
			newSet.setReps(repsInDay);
			newSet.setStartTime(lastDate);
			repsByDay.add(newSet);
			
			return repsByDay;
		}
		
		public List<Set> getAllSetsToday(long exerciseId) {
			List<Set> sets = new ArrayList<Set>();
			Date today = new Date();
			today.setHours(0);
			today.setMinutes(0);
			today.setSeconds(0);
			Cursor cursor = database.query(MyDatabaseHelper.TABLE_SETS,
					allColumns, MyDatabaseHelper.SETS_EXERCISE_ID + " == " + String.valueOf(exerciseId) 
					+ " and " + MyDatabaseHelper.SETS_START_TIME + " >= '" + convertDateToString(today) + "'"
					, null, null, null, null);
			
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Set set = cursorToSet(cursor);
				sets.add(set);
				cursor.moveToNext();
			}
			
			// Make sure to close the cursor
			cursor.close();
			
			return sets;
		}
		
		public Set getMaxRepsInSet(long exerciseId) {
			//get the maximum number of reps in a single set
			Cursor cursor = database.rawQuery("Select Max(" + MyDatabaseHelper.SETS_REPS + ") from " + MyDatabaseHelper.TABLE_SETS 
					+ " where " + MyDatabaseHelper.SETS_EXERCISE_ID + " == " + String.valueOf(exerciseId), null);
			cursor.moveToFirst();
			//select the first set with that many reps
			cursor = database.rawQuery("SELECT * from " + MyDatabaseHelper.TABLE_SETS + " where " + MyDatabaseHelper.SETS_REPS + " == " + cursor.getLong(0)
					+ " and " + MyDatabaseHelper.SETS_EXERCISE_ID + " == " + String.valueOf(exerciseId), null);
							
			cursor.moveToFirst();
			
			Set bestSet = new Set();
			if (!cursor.isAfterLast()) {
				bestSet = cursorToSet(cursor);
			}
			
			// Make sure to close the cursor
			cursor.close();
			
			return bestSet;
		}
		
		public Set getMaxRepsInDay(long exerciseId) {
			//first get all of the sets
			List<Set> allSets = getAllSets(exerciseId);
			Date lastDate = new Date();
        	Date dateOfMostReps = new Date();
        	int repsInDay = 0;
        	int maxRepsInDay = 0;
        	long timeInDay = 86400000;
        	for(final Set set:allSets) {
        		set.getStartTime().setHours(0);
        		set.getStartTime().setMinutes(0);
        		set.getStartTime().setSeconds(0);
        		if(Math.abs(lastDate.getTime() - set.getStartTime().getTime()) >= timeInDay) {
        			//check if last date was a record
        			if(repsInDay > maxRepsInDay) {
        				dateOfMostReps.setDate(lastDate.getDate());
        				maxRepsInDay = repsInDay;
        			}
        			//set lastdate to this date
        			lastDate.setDate(set.getStartTime().getDate());
        			
        			//reset repsInDay
        			repsInDay = 0;
        		}
        		//add reps to current day
        		repsInDay += set.getReps();
        	}
        	//check if the last day was the best
        	if(repsInDay > maxRepsInDay) {
				dateOfMostReps.setDate(lastDate.getDate());
				maxRepsInDay = repsInDay;
			}
        	
			//create a set that details the most reps in one day
        	Set bestSet = new Set();
        	bestSet.setStartTime(dateOfMostReps);
        	bestSet.setReps(maxRepsInDay);
        	
			return bestSet;
		}

		public Date convertToDate(String dateTime) {
			Date date = new Date();
			try {
				date = (Date) iso8601Format.parse(dateTime);
			} catch (ParseException e) {
				Log.e("ExerTracker", "Parsing ISO8601 datetime failed", e);
			} 
			return date;
		}
		
		public String convertDateToString(Date dateTime) {
			String date = iso8601Format.format(dateTime);
			return date;
		}
		
		private Set cursorToSet(Cursor cursor) {
			Set set = new Set();
			set.setId(cursor.getLong(0));
			//skip one for the exercise Id
			set.setReps(cursor.getLong(2));
			set.setStartTime(convertToDate(cursor.getString(3)));
			set.setDuration(cursor.getLong(4));
			set.setWeight(cursor.getLong(5));
			set.setComments(cursor.getString(6));
			
			//link the set to an exercise
			Exercise exercise = getExercise(cursor.getLong(1));
			set.setExercise(exercise);
			return set;
		}
		

		private Exercise getExercise(long id) {
			Cursor cursor = database.query(MyDatabaseHelper.TABLE_EXERCISES, allExerciseColumns, MyDatabaseHelper.EXERCISES_ID + " = " + id, null, null, null, null);
			cursor.moveToFirst();
			Exercise exercise = cursorToExercise(cursor);
			cursor.close();
			return exercise;
		}
		
		private Exercise cursorToExercise(Cursor cursor) {
			Exercise exercise = new Exercise();
			exercise.setId(cursor.getLong(0));
			exercise.setName(cursor.getString(1));
			exercise.setDescription(cursor.getString(2));
			return exercise;
		}
}
