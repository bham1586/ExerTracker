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

public class RecordsActivity extends Activity {
    
    private ExercisesDataSource exercisesDB;
    private SetsDataSource setsDB;
    private List<Exercise> allExercises;
    private List<Set> allSets; 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the layout
        setContentView(R.layout.records);
        
        //connect to the database
        exercisesDB = new ExercisesDataSource(this);
        setsDB = new SetsDataSource(this);
        exercisesDB.open();
        setsDB.open();
        allExercises = exercisesDB.getAllExercises();
        
        //set up the buttons
        createButtonListeners();
        
        TextView tv;
        for(final Exercise e:allExercises)
		{
        	allSets = setsDB.getAllSets(e.getId());
        	Date lastDate = new Date();
        	Date dateOfMostRepsInDay = new Date();
        	Date dateOfMostRepsInSet = new Date();
        	int repsInDay = 0;
        	int maxRepsInDay = 0;
        	int maxRepsInSet = 0;
        	for(final Set set:allSets) {
        		if(lastDate.getDate() != set.getStartTime().getDate()) {
        			//check if last date was a record
        			
        			//set lastdate to this date
        			
        			//reset repsInDay
        			
        		}
        		//check if reps in set is greater than record
        		
        		//add reps to current day
        		
        	}
        	//display results in the text views
            tv = (TextView) this.findViewById(R.id.MostPullupsInDay);
            tv.setText(maxReps "");
		}
    }
    
    private void createButtonListeners(){

		ImageButton button;
		
		
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
		
		//create the tracker icon that goes to the main screen
		button = (ImageButton) findViewById(R.id.trackerIcon);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(), ExerTrackerActivity.class);
				startActivity(intent);
			}
		});
		
		/*
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
		*/

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
	
}