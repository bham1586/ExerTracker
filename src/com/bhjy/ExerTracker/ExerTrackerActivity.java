package com.bhjy.ExerTracker;

import java.util.Date;
import java.util.List;

import com.bhjy.ExerTracker.R.id;
import com.bhjy.ExerTracker.Database.ExercisesDataSource;
import com.bhjy.ExerTracker.Database.MyDatabaseHelper;
import com.bhjy.ExerTracker.Database.SetsDataSource;
import com.bhjy.ExerTracker.Models.Exercise;
import com.bhjy.ExerTracker.Models.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

public class ExerTrackerActivity extends Activity {
    
	SharedPreferences mPrefs;
	final String showWelcomeScreenPref = "showWelcomeScreen";
	
    private ExercisesDataSource exercisesDB;
    private SetsDataSource setsDB;
    private List<Exercise> allExercises;
    private ListView exerciseList;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        exercisesDB = new ExercisesDataSource(this);
        setsDB = new SetsDataSource(this);
        exercisesDB.open();
        createButtonListeners();
        
		allExercises = exercisesDB.getAllExercises();
		        
		//find out if the welcome screen should be shown
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		Boolean showWelcomeScreen = mPrefs.getBoolean(showWelcomeScreenPref, true);
		if(showWelcomeScreen) {
			//welcome the user to the application
			new AlertDialog.Builder(this).setIcon(R.drawable.ic_launcher).setTitle(R.string.welcomeTitle).setMessage(R.string.welcomeMessage)
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				}).show();
			//set the screen to not show again
			SharedPreferences.Editor editor = mPrefs.edit();
			editor.putBoolean(showWelcomeScreenPref, false);
			editor.commit();
		}
		
        //this is for testing the progress functionality
        //if there is no history, it will create some
        createFakeHistory();
    }
    
    private void createButtonListeners(){

		int[] buttonList = {R.id.trackPullupsBtn, R.id.trackPushupsBtn, R.id.trackSitupsBtn, R.id.trackSquatsBtn, R.id.progressIcon, R.id.recordsIcon};

		ImageButton button;
		
		for(final int i:buttonList)
		{
			button = (ImageButton) findViewById(i);
			button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String actionName = "com.bhjy.ExerTracker.ShowMotionCountActivity";
					Bundle bundle = new Bundle();
					
					if(i == R.id.trackPullupsBtn) {
						bundle.putLong("exercise_id", allExercises.get(0).getId());					 
					}
					else if(i == R.id.trackPushupsBtn) {
						bundle.putLong("exercise_id", allExercises.get(1).getId());
					}
					else if(i == R.id.trackSitupsBtn) {
						bundle.putLong("exercise_id", allExercises.get(2).getId());
					}
					else if(i == R.id.trackSquatsBtn) {
						bundle.putLong("exercise_id", allExercises.get(3).getId());
					}
					else if(i == R.id.progressIcon) {
						actionName = "com.bhjy.ExerTracker.ShowProgressActivity";
					}
					else if(i == R.id.recordsIcon) {
						actionName = "com.bhjy.ExerTracker.ShowRecordsActivity";
					}
					
					Intent intent = new Intent(actionName);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			});			
		}		
	};
	
    @Override
	protected void onResume() {
		exercisesDB.open();
		//allExercises = exercisesDB.getAllExercises();
		//exerciseList.setAdapter(new ArrayAdapter<Exercise>(this, android.R.layout.simple_list_item_1, allExercises));
		super.onResume();
	}

	@Override
	protected void onPause() {
		exercisesDB.close();
		super.onPause();
	}
	
	private void createFakeHistory() {
		//This is a function that creates a bunch of fake records so that there is actually
		//some history to display on the progress page
		exercisesDB.open();
		setsDB.open();
		allExercises = exercisesDB.getAllExercises();
		for(Exercise e: allExercises) {
			//first make sure that there are no real values in the database
			List<Set> setList = setsDB.getAllSets(e.getId());
			if(setList.size() == 0) {
				
				Date startTime = new Date();
				startTime.setTime(startTime.getTime());
				int startNum = 40;
				//create a set every 12 hours for the past 7 days
				//make the reps somewhat random, but overall decreasing as it goes back in time
				//this gives the idea of progress over time
				for(int i = 0; i< 14; i++) {
					int randomNum = (int) Math.ceil(Math.random() * 8);
					int reps = startNum - i - randomNum;
					startTime.setTime(startTime.getTime() - 43200000);
					setsDB.createSet(e.getId(), reps, startTime, 0, 0, "");
				}				
			}
		}
		exercisesDB.close();
		setsDB.close();
	}
	
	
}