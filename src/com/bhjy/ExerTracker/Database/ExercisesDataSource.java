package com.bhjy.ExerTracker.Database;

import java.util.ArrayList;
import java.util.List;

import com.bhjy.ExerTracker.Database.MyDatabaseHelper;
import com.bhjy.ExerTracker.Models.Exercise;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ExercisesDataSource {
	// Database fields
		private SQLiteDatabase database;
		private MyDatabaseHelper dbHelper;
		private String[] allColumns = { MyDatabaseHelper.EXERCISES_ID,
				MyDatabaseHelper.EXERCISES_NAME, MyDatabaseHelper.EXERCISES_DESCRIPTION };

		public ExercisesDataSource(Context context) {
			dbHelper = new MyDatabaseHelper(context, null, null, 1);
		}

		public void open() throws SQLException {
			database = dbHelper.getWritableDatabase();
		}

		public void close() {
			dbHelper.close();
		}

		public Exercise createExercise(String name, String description) {
			ContentValues values = new ContentValues();
			values.put(MyDatabaseHelper.EXERCISES_NAME, name);
			values.put(MyDatabaseHelper.EXERCISES_DESCRIPTION, description);
			long insertId = database.insert(MyDatabaseHelper.TABLE_EXERCISES, null,	values);
			Cursor cursor = database.query(MyDatabaseHelper.TABLE_EXERCISES, allColumns, MyDatabaseHelper.EXERCISES_ID + " = " + insertId, null, null, null, null);
			cursor.moveToFirst();
			Exercise newExercise = cursorToExercise(cursor);
			cursor.close();
			return newExercise;
		}

		public void deleteExercise(Exercise exercise) {
			long id = exercise.getId();
			System.out.println("Exercise deleted with id: " + id);
			database.delete(MyDatabaseHelper.TABLE_EXERCISES, MyDatabaseHelper.EXERCISES_ID
					+ " = " + id, null);
		}

		public List<Exercise> getAllExercises() {
			List<Exercise> exercises = new ArrayList<Exercise>();

			Cursor cursor = database.query(MyDatabaseHelper.TABLE_EXERCISES,
					allColumns, null, null, null, null, null);

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Exercise exercise = cursorToExercise(cursor);
				exercises.add(exercise);
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();
			return exercises;
		}

		private Exercise cursorToExercise(Cursor cursor) {
			Exercise exercise = new Exercise();
			exercise.setId(cursor.getLong(0));
			exercise.setName(cursor.getString(1));
			exercise.setDescription(cursor.getString(2));
			return exercise;
		}
}
