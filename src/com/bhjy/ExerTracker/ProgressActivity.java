package com.bhjy.ExerTracker;

import java.util.Date;
import java.util.List;

import com.bhjy.ExerTracker.Database.ExercisesDataSource;
import com.bhjy.ExerTracker.Database.MyDatabaseHelper;
import com.bhjy.ExerTracker.Database.SetsDataSource;
import com.bhjy.ExerTracker.Models.Exercise;
import com.bhjy.ExerTracker.Models.Set;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
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

public class ProgressActivity extends Activity {
    
    private ExercisesDataSource exercisesDB;
    private SetsDataSource setsDB;
    private List<Exercise> allExercises;
    private ListView exerciseList;
    private List<Set> allSets; 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the layout
        setContentView(R.layout.progress);
        
        //connect to the database
        exercisesDB = new ExercisesDataSource(this);
        setsDB = new SetsDataSource(this);
        
        //set up the buttons
        createButtonListeners();
        
        //this is for testing the progress functionality
        //if there is no history, it will create some
        createFakeHistory();
        
        //add functionality here
        //
        //
        
    }
    
    private void createButtonListeners(){

		ImageButton button;
		
		/* DON'T MAKE THIS BUTTON CLICKABLE WHEN ON THIS SCREEN
		//create the progress Icon
		button = (ImageButton) findViewById(R.id.progressIcon);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String actionName = "com.bhjy.ExerTracker.ShowProgressActivity";
				Intent intent = new Intent(actionName);
				startActivity(intent);
			}
		});
		*/
		
		//create the tracker icon that goes to the main screen
		button = (ImageButton) findViewById(R.id.trackerIcon);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(), ExerTrackerActivity.class);
				startActivity(intent);
			}
		});
		
		
		//create the records button
		button = (ImageButton) findViewById(R.id.recordsIcon);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String actionName = "com.bhjy.ExerTracker.ShowRecordsActivity";
				Intent intent = new Intent(actionName);
				startActivity(intent);
			}
		});
		

	};
	
    @Override
	protected void onResume() {
		//exercisesDB.open();
		//allExercises = exercisesDB.getAllExercises();
		//exerciseList.setAdapter(new ArrayAdapter<Exercise>(this, android.R.layout.simple_list_item_1, allExercises));
		super.onResume();
	}

	@Override
	protected void onPause() {
		//exercisesDB.close();
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
	}
}